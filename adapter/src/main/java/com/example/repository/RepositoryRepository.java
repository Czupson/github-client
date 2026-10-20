package com.example.repository;

import com.example.entity.RepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositoryRepository extends JpaRepository<RepositoryEntity, Long> {

    Optional<RepositoryEntity> findByFullName(String fullName);
}