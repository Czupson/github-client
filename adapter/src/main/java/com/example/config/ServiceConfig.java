package com.example.config;

import com.example.port.GithubRepositoryPort;
import com.example.port.GithubApiPort;
import com.example.port.RepositoryProviderPort;
import com.example.service.GithubRepositoryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public GithubRepositoryPort githubRepositoryPort(
            GithubApiPort githubApiPort,
            RepositoryProviderPort repositoryProviderPort) {

        return new GithubRepositoryService(
                githubApiPort,
                repositoryProviderPort
        );
    }
}