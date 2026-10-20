package com.example.dto;

public record RepositoryResponse(
        String fullName,
        String description,
        String cloneUrl,
        Integer stars,
        String createdAt
) {
}
