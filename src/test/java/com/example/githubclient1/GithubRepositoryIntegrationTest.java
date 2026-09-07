package com.example.githubclient1;

import com.example.githubclient1.client.GithubClient;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.entity.RepositoryEntity;
import com.example.githubclient1.repository.RepositoryRepository;
import com.example.githubclient1.service.GithubRepositoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
class GithubRepositoryIntegrationTest {

    @Autowired
    private GithubRepositoryService githubRepositoryService;

    @Autowired
    private RepositoryRepository repositoryRepository;

    @MockitoBean
    private GithubClient githubClient;

    @BeforeEach
    void cleanDatabase() {
        repositoryRepository.deleteAll();
    }

    @Test
    void saveRepository_RepositoryExists_RepositorySavedInDatabase() {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        GithubRepositoryResponse githubResponse = new GithubRepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(githubClient.getRepository(owner, repositoryName)).thenReturn(githubResponse);
        // When
        githubRepositoryService.saveRepository(owner, repositoryName);
        // Then
        RepositoryEntity savedRepository = repositoryRepository.findByFullName("octocat/Hello-World").orElseThrow();
        assertNotNull(savedRepository.getId());
        assertEquals("octocat/Hello-World", savedRepository.getFullName());
        assertEquals("This your first repo!", savedRepository.getDescription());
        assertEquals("https://github.com/octocat/Hello-World.git", savedRepository.getCloneUrl());
        assertEquals(100, savedRepository.getStars());
        assertEquals("2011-01-26T19:01:12Z", savedRepository.getCreatedAt());
    }

    @Test
    void getLocalRepository_RepositoryExists_RepositoryResponseReturned() {
        // Given
        RepositoryEntity entity = new RepositoryEntity(null, "octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        repositoryRepository.save(entity);
        // When
        var result = githubRepositoryService.getLocalRepository("octocat", "Hello-World");
        // Then
        assertEquals("octocat/Hello-World", result.fullName());
        assertEquals("This your first repo!", result.description());
        assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl());
        assertEquals(100, result.stars());
        assertEquals("2011-01-26T19:01:12Z", result.createdAt());
    }
}