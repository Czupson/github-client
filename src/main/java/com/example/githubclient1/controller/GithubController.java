package com.example.githubclient1.controller;

import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.service.GithubRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/repositories")
@RequiredArgsConstructor
public class GithubController {
    private final GithubRepositoryService githubRepositoryService;

    @GetMapping("/{owner}/{repo}")
    public RepositoryResponse getRepositories(
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo) {
        log.info("Received request for repository: {}/{}", owner, repo);
        RepositoryResponse response = githubRepositoryService.getRepository(owner, repo);
        log.info("Repository returned successfully: {}/{}", owner, repo);
        return response;
    }
}