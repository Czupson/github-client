package com.example.githubclient1.mapper;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.entity.RepositoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GithubRepositoryMapper {
    RepositoryResponse map(GithubRepositoryResponse source);
    RepositoryEntity mapToEntity(GithubRepositoryResponse source);
    RepositoryResponse mapToResponse(RepositoryEntity source);
}
