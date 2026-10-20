package com.example.adapter;

import com.example.Repository;
import com.example.port.RepositoryProviderPort;
import com.example.entity.RepositoryEntity;
import com.example.repository.RepositoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RepositoryProviderAdapter implements RepositoryProviderPort {

    private final RepositoryRepository repositoryRepository;

    @Override
    public Repository save(Repository repository) {
        RepositoryEntity entity = new RepositoryEntity();
        entity.setFullName(repository.getFullName());
        entity.setDescription(repository.getDescription());
        entity.setCloneUrl(repository.getCloneUrl());
        entity.setStars(repository.getStars());
        entity.setCreatedAt(repository.getCreatedAt());
        RepositoryEntity savedEntity = repositoryRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Repository> findByFullName(String fullName) {
        return repositoryRepository.findByFullName(fullName)
                .map(this::mapToDomain);
    }

    @Override
    public void delete(Repository repository) {
        repositoryRepository.findByFullName(repository.getFullName())
                .ifPresent(repositoryRepository::delete);
    }

    private com.example.Repository mapToDomain(RepositoryEntity entity) {
        return new com.example.Repository(
                entity.getFullName(),
                entity.getDescription(),
                entity.getCloneUrl(),
                entity.getStars(),
                entity.getCreatedAt()
        );
    }
}