package com.example.githubclient1.client;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class GithubClientFallbackFactory implements FallbackFactory<GithubClient> {

    @Override
    public GithubClient create(Throwable cause) {
        return (owner, repo) ->
                new GithubRepositoryResponse(owner + "/" + repo, "Fallback response", "", 0, "");
    }
}