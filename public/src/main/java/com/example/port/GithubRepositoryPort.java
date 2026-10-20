package com.example.port;

import com.example.dto.RepositoryResponse;

public interface GithubRepositoryPort {

    RepositoryResponse getRepository(String owner, String repo);

    void saveRepository(String owner, String repo);

    RepositoryResponse getLocalRepository(String owner, String repo);

    void updateRepository(String owner, String repo);

    void deleteRepository(String owner, String repo);
}