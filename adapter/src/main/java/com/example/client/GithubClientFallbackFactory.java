package com.example.client;

import com.example.dto.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class GithubClientFallbackFactory implements FallbackFactory<GithubClient> {

    @Override
    public GithubClient create(Throwable cause) {
        return (traceId, owner, repo) ->
                new GithubRepositoryResponse(
                        owner + "/" + repo,
                        "Fallback response",
                        "",
                        0,
                        ""
                );
    }
}