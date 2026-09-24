package com.example.githubclient1.repository;

import com.example.githubclient1.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long> {

    Optional<RepositoryEntity> findByFullName(String fullName);
}