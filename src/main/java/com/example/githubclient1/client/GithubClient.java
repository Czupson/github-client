package com.example.githubclient1.client;

import com.example.githubclient1.config.GithubFeignConfig;
import com.example.githubclient1.dto.GithubRepositoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "githubClient",
        url = "${github.api.url:https://api.github.com}",
        configuration = GithubFeignConfig.class)
public interface GithubClient {
    @GetMapping("/repos/{owner}/{repo}")
    GithubRepositoryResponse getRepository(
            @PathVariable String owner,
            @PathVariable String repo);
}
