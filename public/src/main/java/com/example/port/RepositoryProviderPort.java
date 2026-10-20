package com.example.port;

import com.example.Repository;

import java.util.Optional;

public interface RepositoryProviderPort {

    Repository save(Repository repository);

    Optional<Repository> findByFullName(String fullName);

    void delete(Repository repository);
}