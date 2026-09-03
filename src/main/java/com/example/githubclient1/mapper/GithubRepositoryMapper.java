package com.example.githubclient1.mapper;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GithubRepositoryMapper {
    RepositoryResponse map(GithubRepositoryResponse source);
}
