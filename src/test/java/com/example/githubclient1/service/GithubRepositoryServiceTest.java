package com.example.githubclient1.service;

import com.example.githubclient1.client.GithubClient;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.entity.RepositoryEntity;
import com.example.githubclient1.exception.RepositoryNotFoundException;
import com.example.githubclient1.mapper.GithubRepositoryMapper;
import com.example.githubclient1.repository.RepositoryRepository;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {
    private GithubClient githubClient;
    private GithubRepositoryMapper githubRepositoryMapper;
    private GithubRepositoryService service;

    @Mock
    private RepositoryRepository repositoryRepository;

    @BeforeEach
    void setUp() {
        this.githubClient = mock(GithubClient.class);
        this.githubRepositoryMapper = Mappers.getMapper(GithubRepositoryMapper.class);
        this.service = new GithubRepositoryService(githubClient, githubRepositoryMapper, repositoryRepository);
    }

    @Test
    void getRepository_WhenRepositoryExists_ShouldReturnMatchingDto() {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        GithubRepositoryResponse response = new GithubRepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(githubClient.getRepository(owner, repositoryName)).thenReturn(response);
        // When
        RepositoryResponse result = service.getRepository(owner, repositoryName);
        // Then
        assertAll(
                () -> assertEquals("octocat/Hello-World", result.fullName()),
                () -> assertEquals("This your first repo!", result.description()),
                () -> assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl()),
                () -> assertEquals(100, result.stars()),
                () -> assertEquals("2011-01-26T19:01:12Z", result.createdAt()));
        verify(githubClient).getRepository(owner, repositoryName);
    }

    @Test
    void getRepository_RepositoryDoesNotExist_RepositoryNotFoundExceptionThrown() {
        // Given
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(githubClient.getRepository(owner, repositoryName)).thenThrow(FeignException.NotFound.class);
        // When and Then
        assertThrows(RepositoryNotFoundException.class,
                () -> service.getRepository(owner, repositoryName));
        verify(githubClient).getRepository(owner, repositoryName);
    }

    @Test
    void saveRepository_RepositoryExists_ShouldSaveRepository() {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        GithubRepositoryResponse githubResponse = new GithubRepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git",
                100, "2011-01-26T19:01:12Z");
        when(githubClient.getRepository(owner, repositoryName)).thenReturn(githubResponse);
        // When
        service.saveRepository(owner, repositoryName);
        // Then
        verify(githubClient).getRepository(owner, repositoryName);
        verify(repositoryRepository).save(any());
    }

    @Test
    void getLocalRepository_RepositoryExists_ShouldReturnRepository() {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        RepositoryEntity entity = new RepositoryEntity(1L, "octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(repositoryRepository.findByFullName("octocat/Hello-World")).thenReturn(Optional.of(entity));
        // When
        RepositoryResponse result = service.getLocalRepository(owner, repositoryName);
        // Then
        assertAll(
                () -> assertEquals("octocat/Hello-World", result.fullName()),
                () -> assertEquals("This your first repo!", result.description()),
                () -> assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl()),
                () -> assertEquals(100, result.stars()),
                () -> assertEquals("2011-01-26T19:01:12Z", result.createdAt()));
        verify(repositoryRepository).findByFullName("octocat/Hello-World");
    }

    @Test
    void getLocalRepository_RepositoryDoesNotExist_RepositoryNotFoundExceptionThrown() {
        // Given
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(repositoryRepository.findByFullName("octocat/NotExistingRepository")).thenReturn(Optional.empty());
        // When + Then
        assertThrows(RepositoryNotFoundException.class,
                () -> service.getLocalRepository(owner, repositoryName));
        verify(repositoryRepository).findByFullName("octocat/NotExistingRepository");
    }

    @Test
    void saveRepository_RepositoryDoesNotExist_RepositoryNotFoundExceptionThrown() {
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(githubClient.getRepository(owner, repositoryName)).thenThrow(FeignException.NotFound.class);
        assertThrows(RepositoryNotFoundException.class,
                () -> service.saveRepository(owner, repositoryName));
        verify(githubClient).getRepository(owner, repositoryName);
        verifyNoInteractions(repositoryRepository);
    }

    @Test
    void updateRepository_RepositoryExists_RepositoryUpdated() {
        String owner = "octocat";
        String repositoryName = "Hello-World";
        RepositoryEntity entity = new RepositoryEntity(1L, "octocat/Hello-World", "Old description",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        GithubRepositoryResponse githubResponse = new GithubRepositoryResponse("octocat/Hello-World",
                "Updated description", "https://github.com/octocat/Hello-World.git", 200,
                "2011-01-26T19:01:12Z");
        when(repositoryRepository.findByFullName("octocat/Hello-World")).thenReturn(Optional.of(entity));
        when(githubClient.getRepository(owner, repositoryName)).thenReturn(githubResponse);
        service.updateRepository(owner, repositoryName);
        assertEquals("octocat/Hello-World", entity.getFullName());
        assertEquals("Updated description", entity.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", entity.getCloneUrl());
        assertEquals(200, entity.getStars());
        assertEquals("2011-01-26T19:01:12Z", entity.getCreatedAt());
        verify(repositoryRepository).save(entity);
        verify(githubClient).getRepository(owner, repositoryName);
    }

    @Test
    void updateRepository_RepositoryDoesNotExist_RepositoryNotFoundExceptionThrown() {
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(repositoryRepository.findByFullName("octocat/NotExistingRepository")).thenReturn(Optional.empty());
        assertThrows(RepositoryNotFoundException.class,
                () -> service.updateRepository(owner, repositoryName));
        verify(repositoryRepository).findByFullName("octocat/NotExistingRepository");
        verifyNoInteractions(githubClient);
    }

    @Test
    void updateRepository_RepositoryNotFoundOnGithub_RepositoryNotFoundExceptionThrown() {
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        RepositoryEntity entity = new RepositoryEntity(1L, "octocat/NotExistingRepository", "Old description",
                "https://github.com/octocat/NotExistingRepository.git", 100, "2011-01-26T19:01:12Z");
        when(repositoryRepository.findByFullName("octocat/NotExistingRepository")).thenReturn(Optional.of(entity));
        when(githubClient.getRepository(owner, repositoryName)).thenThrow(FeignException.NotFound.class);
        assertThrows(RepositoryNotFoundException.class,
                () -> service.updateRepository(owner, repositoryName));
        verify(repositoryRepository).findByFullName("octocat/NotExistingRepository");
        verify(githubClient).getRepository(owner, repositoryName);
        verify(repositoryRepository, never()).save(entity);
    }

    @Test
    void deleteRepository_RepositoryExists_RepositoryDeleted() {
        String owner = "octocat";
        String repositoryName = "Hello-World";
        RepositoryEntity entity = new RepositoryEntity(1L, "octocat/Hello-World", "Description",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(repositoryRepository.findByFullName("octocat/Hello-World")).thenReturn(Optional.of(entity));
        service.deleteRepository(owner, repositoryName);
        verify(repositoryRepository).findByFullName("octocat/Hello-World");
        verify(repositoryRepository).delete(entity);
    }

    @Test
    void deleteRepository_RepositoryDoesNotExist_RepositoryNotFoundExceptionThrown() {
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(repositoryRepository.findByFullName("octocat/NotExistingRepository")).thenReturn(Optional.empty());
        assertThrows(RepositoryNotFoundException.class,
                () -> service.deleteRepository(owner, repositoryName));
        verify(repositoryRepository).findByFullName("octocat/NotExistingRepository");
        verify(repositoryRepository, never()).delete(any());
    }
}
