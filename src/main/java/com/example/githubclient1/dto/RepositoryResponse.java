package com.example.githubclient1.dto;

public record RepositoryResponse(
        String fullName,
        String description,
        String cloneUrl,
        Integer stars,
        String createdAt
) {
}
