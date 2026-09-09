package com.example.githubclient1.service;

import com.example.githubclient1.client.GithubClient;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.entity.RepositoryEntity;
import com.example.githubclient1.exception.RepositoryNotFoundException;
import com.example.githubclient1.mapper.GithubRepositoryMapper;
import com.example.githubclient1.repository.RepositoryRepository;
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
    private final RepositoryRepository repositoryRepository;

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

    public void saveRepository(String owner, String repo) {
        log.info("Fetching repository from GitHub for local save: {}/{}", owner, repo);
        try {
            GithubRepositoryResponse githubRepository = githubClient.getRepository(owner, repo);
            RepositoryEntity entity = githubRepositoryMapper.mapToEntity(githubRepository);
            repositoryRepository.save(entity);
            log.info("Repository saved locally: {}/{}", owner, repo);
        } catch (FeignException.NotFound exception) {
            log.warn("GitHub repository not found: {}/{}", owner, repo);
            throw new RepositoryNotFoundException("Repository " + owner + "/" + repo + " not found");
        }
    }

    public RepositoryResponse getLocalRepository(String owner, String repo) {
        String fullName = owner + "/" + repo;
        log.info("Fetching repository from local database: {}", fullName);
        RepositoryEntity entity = repositoryRepository.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository " + fullName + " not found locally"));
        return githubRepositoryMapper.mapToResponse(entity);
    }

    public void updateRepository(String owner, String repo) {
        String fullName = owner + "/" + repo;
        log.info("Updating local repository: {}", fullName);
        RepositoryEntity entity = repositoryRepository.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository " + fullName + " not found locally"));
        try {
            GithubRepositoryResponse githubRepository = githubClient.getRepository(owner, repo);
            entity.setFullName(githubRepository.fullName());
            entity.setDescription(githubRepository.description());
            entity.setCloneUrl(githubRepository.cloneUrl());
            entity.setStars(githubRepository.stars());
            entity.setCreatedAt(githubRepository.createdAt());
            repositoryRepository.save(entity);
            log.info("Repository updated successfully: {}", fullName);
        } catch (FeignException.NotFound exception) {
            log.warn("GitHub repository not found: {}", fullName);
            throw new RepositoryNotFoundException("Repository " + fullName + " not found on GitHub");
        }
    }

    public void deleteRepository(String owner, String repo) {
        String fullName = owner + "/" + repo;
        log.info("Deleting local repository: {}", fullName);
        RepositoryEntity entity = repositoryRepository.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException("Repository " + fullName + " not found locally"));
        repositoryRepository.delete(entity);
        log.info("Repository deleted successfully: {}", fullName);
    }
}
