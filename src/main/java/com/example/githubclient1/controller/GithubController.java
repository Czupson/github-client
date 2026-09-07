package com.example.githubclient1.controller;

import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.service.GithubRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GithubController {
    private final GithubRepositoryService githubRepositoryService;

    @GetMapping("/repositories/{owner}/{repo}")
    public RepositoryResponse getRepositories(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo) {
        log.info("Received request for repository: {}/{}", owner, repo);
        RepositoryResponse response = githubRepositoryService.getRepository(owner, repo);
        log.info("Repository returned successfully: {}/{}", owner, repo);
        return response;
    }

    @PostMapping("/repositories/{owner}/{repo}")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveRepository(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo) {
        log.info("Received request to save repository: {}/{}", owner, repo);
        githubRepositoryService.saveRepository(owner, repo);
        log.info("Repository saved successfully: {}/{}", owner, repo);
    }

    @GetMapping("/local/repositories/{owner}/{repo}")
    public RepositoryResponse getLocalRepository(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo) {
        log.info("Received request for local repository: {}/{}", owner, repo);
        RepositoryResponse response = githubRepositoryService.getLocalRepository(owner, repo);
        log.info("Local repository returned successfully: {}/{}", owner, repo);
        return response;
    }
}