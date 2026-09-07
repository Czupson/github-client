package com.example.githubclient1.controller;

import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.exception.RepositoryNotFoundException;
import com.example.githubclient1.service.GithubRepositoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GithubController.class)
class GithubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GithubRepositoryService githubRepositoryService;

    @Test
    void getRepositories_RepositoryExists_RepositoryResponseReturned() throws Exception {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        RepositoryResponse response = new RepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(githubRepositoryService.getRepository(owner, repositoryName)).thenReturn(response);
        // When and Then
        mockMvc.perform(get("/repositories/{owner}/{repo}", owner, repositoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("This your first repo!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.stars").value(100))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12Z"));
        verify(githubRepositoryService).getRepository(owner, repositoryName);
    }

    @Test
    void getRepositories_RepositoryDoesNotExist_NotFoundReturned() throws Exception {
        // Given
        String owner = "Owner";
        String repo = "NotExistingOne";
        when(githubRepositoryService.getRepository(owner, repo)).thenThrow(new RepositoryNotFoundException("Repository not found"));
        // When + Then
        mockMvc.perform(get("/repositories/{owner}/{repo}", owner, repo)).andExpect(status().isNotFound());
        verify(githubRepositoryService).getRepository(owner, repo);
    }

    @Test
    void saveRepository_RepositoryExists_CreatedReturned() throws Exception {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        // When + Then
        mockMvc.perform(post("/repositories/{owner}/{repo}", owner, repositoryName))
                .andExpect(status().isCreated());
        verify(githubRepositoryService).saveRepository(owner, repositoryName);
    }

    @Test
    void getLocalRepository_RepositoryExists_RepositoryResponseReturned() throws Exception {
        // Given
        String owner = "octocat";
        String repositoryName = "Hello-World";
        RepositoryResponse response = new RepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        when(githubRepositoryService.getLocalRepository(owner, repositoryName)).thenReturn(response);
        // When + Then
        mockMvc.perform(get("/local/repositories/{owner}/{repo}", owner, repositoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("octocat/Hello-World"))
                .andExpect(jsonPath("$.description").value("This your first repo!"))
                .andExpect(jsonPath("$.cloneUrl").value("https://github.com/octocat/Hello-World.git"))
                .andExpect(jsonPath("$.stars").value(100))
                .andExpect(jsonPath("$.createdAt").value("2011-01-26T19:01:12Z"));
        verify(githubRepositoryService).getLocalRepository(owner, repositoryName);
    }

    @Test
    void getLocalRepository_RepositoryDoesNotExist_NotFoundReturned() throws Exception {
        // Given
        String owner = "octocat";
        String repositoryName = "NotExistingRepository";
        when(githubRepositoryService.getLocalRepository(owner, repositoryName)).thenThrow(new RepositoryNotFoundException("Repository not found"));
        // When + Then
        mockMvc.perform(get("/local/repositories/{owner}/{repo}", owner, repositoryName))
                .andExpect(status().isNotFound());
        verify(githubRepositoryService).getLocalRepository(owner, repositoryName);
    }
}