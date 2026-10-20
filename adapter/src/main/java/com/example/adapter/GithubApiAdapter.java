package com.example.adapter;

import com.example.port.GithubApiPort;
import com.example.dto.GithubRepositoryResponse;
import com.example.Repository;
import com.example.client.GithubClient;
import com.example.mapper.GithubRepositoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubApiAdapter implements GithubApiPort {

    private final GithubClient githubClient;
    private final GithubRepositoryMapper githubRepositoryMapper;

    @Override
    public Repository getRepository(String owner, String repo) {
        GithubRepositoryResponse response =
                githubClient.getRepository(null, owner, repo);

        return githubRepositoryMapper.mapToDomain(response);
    }
}
