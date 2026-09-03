package com.example.githubclient1.service;

import com.example.githubclient1.client.GithubClient;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.exception.RepositoryNotFoundException;
import com.example.githubclient1.mapper.GithubRepositoryMapper;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GithubRepositoryServiceTest {
    private GithubClient githubClient;
    private GithubRepositoryMapper githubRepositoryMapper;
    private GithubRepositoryService service;

    @BeforeEach
    void setUp() {
        this.githubClient = mock(GithubClient.class);
        this.githubRepositoryMapper = Mappers.getMapper(GithubRepositoryMapper.class);
        this.service = new GithubRepositoryService(githubClient, githubRepositoryMapper);
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
}
