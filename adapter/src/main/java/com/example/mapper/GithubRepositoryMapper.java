package com.example.mapper;

import com.example.Repository;
import com.example.dto.GithubRepositoryResponse;
import com.example.dto.RepositoryResponse;
import com.example.entity.RepositoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GithubRepositoryMapper {
    Repository mapToDomain(GithubRepositoryResponse source);
    RepositoryResponse map(GithubRepositoryResponse source);
    RepositoryEntity mapToEntity(GithubRepositoryResponse source);
    RepositoryResponse mapToResponse(RepositoryEntity source);
}
