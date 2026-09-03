package com.example.githubclient1.service;

import com.example.githubclient1.client.GithubClient;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.exception.RepositoryNotFoundException;
import com.example.githubclient1.mapper.GithubRepositoryMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubRepositoryService {
    private final GithubClient githubClient;
    private final GithubRepositoryMapper githubRepositoryMapper;
    public RepositoryResponse getRepository(String owner, String repo) {
        log.info("Fetching GitHub repository: {}/{}", owner, repo);
        try {
            GithubRepositoryResponse githubRepository =
                    githubClient.getRepository(owner, repo);
            log.info("GitHub repository fetched successfully: {}/{}", owner, repo);
            return githubRepositoryMapper.map(githubRepository);
        } catch (FeignException.NotFound exception) {
            log.warn("GitHub repository not found: {}/{}", owner, repo);
            throw new RepositoryNotFoundException("Repository " + owner + "/" + repo + " not found");
        }
    }
}
