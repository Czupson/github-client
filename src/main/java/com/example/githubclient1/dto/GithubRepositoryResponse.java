package com.example.githubclient1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepositoryResponse(
        @JsonProperty("full_name")
        String fullName,
        String description,
        @JsonProperty("clone_url")
        String cloneUrl,
        @JsonProperty("stargazers_count")
        Integer stars,
        @JsonProperty("created_at")
        String createdAt
){
}