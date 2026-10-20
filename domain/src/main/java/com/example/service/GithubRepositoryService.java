package com.example.service;

import com.example.port.GithubRepositoryPort;
import com.example.port.RepositoryProviderPort;
import com.example.dto.RepositoryResponse;
import com.example.exception.RepositoryNotFoundException;
import com.example.port.GithubApiPort;
import com.example.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GithubRepositoryService implements GithubRepositoryPort {
    private final GithubApiPort githubApiPort;
    private final RepositoryProviderPort repositoryProviderPort;

    @Override
    public RepositoryResponse getRepository(String owner, String repo) {
        log.info("Fetching GitHub repository: {}/{}", owner, repo);
        Repository repository = githubApiPort.getRepository(owner, repo);
        log.info("GitHub repository fetched successfully: {}/{}", owner, repo);
        return mapToResponse(repository);
    }

    @Override
    public void saveRepository(String owner, String repo) {
        log.info("Fetching repository from GitHub for local save: {}/{}", owner, repo);
        Repository repository = githubApiPort.getRepository(owner, repo);
        repository.setFullName(repository.getFullName().toLowerCase());
        repositoryProviderPort.save(repository);
        log.info("Repository saved locally: {}/{}", owner, repo);
    }

    @Override
    public RepositoryResponse getLocalRepository(String owner, String repo) {
        String fullName = owner.toLowerCase() + "/" + repo.toLowerCase();
        log.info("Fetching repository from local database: {}", fullName);
        Repository repository = repositoryProviderPort.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException(
                        "Repository " + fullName + " not found locally"
                ));
        return mapToResponse(repository);
    }

    public void updateRepository(String owner, String repo) {
        String fullName = owner.toLowerCase() + "/" + repo.toLowerCase();
        log.info("Updating local repository: {}", fullName);
        Repository existingRepository = repositoryProviderPort.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException(
                        "Repository " + fullName + " not found locally"
                ));
        Repository githubRepository = githubApiPort.getRepository(owner, repo);
        existingRepository.setDescription(githubRepository.getDescription());
        existingRepository.setCloneUrl(githubRepository.getCloneUrl());
        existingRepository.setStars(githubRepository.getStars());
        existingRepository.setCreatedAt(githubRepository.getCreatedAt());
        repositoryProviderPort.save(existingRepository);
        log.info("Repository updated successfully: {}", fullName);
    }

    @Override
    public void deleteRepository(String owner, String repo) {
        String fullName = owner.toLowerCase() + "/" + repo.toLowerCase();
        log.info("Deleting local repository: {}", fullName);
        Repository repository = repositoryProviderPort.findByFullName(fullName)
                .orElseThrow(() -> new RepositoryNotFoundException(
                        "Repository " + fullName + " not found locally"
                ));
        repositoryProviderPort.delete(repository);
        log.info("Repository deleted successfully: {}", fullName);
    }

    private RepositoryResponse mapToResponse(Repository repository) {
        return new RepositoryResponse(
                repository.getFullName(),
                repository.getDescription(),
                repository.getCloneUrl(),
                repository.getStars(),
                repository.getCreatedAt()
        );
    }
}
