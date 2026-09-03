package com.example.githubclient1.mapper;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class GithubRepositoryMapperTest {
    private final GithubRepositoryMapper mapper = Mappers.getMapper(GithubRepositoryMapper.class);

    @Test
    void mapGithubRepository_GithubRepositoryResponseProvided_RepositoryResponseReturned() {
        GithubRepositoryResponse source = new GithubRepositoryResponse(
                "octocat/Hello-World",
                "This your first repo!",
                "https://github.com/octocat/Hello-World.git",
                100,
                "2011-01-26T19:01:12Z"
        );
        RepositoryResponse result = mapper.map(source);
        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo("octocat/Hello-World");
        assertThat(result.description()).isEqualTo("This your first repo!");
        assertThat(result.cloneUrl()).isEqualTo("https://github.com/octocat/Hello-World.git");
        assertThat(result.stars()).isEqualTo(100);
        assertThat(result.createdAt()).isEqualTo("2011-01-26T19:01:12Z");
    }
}
