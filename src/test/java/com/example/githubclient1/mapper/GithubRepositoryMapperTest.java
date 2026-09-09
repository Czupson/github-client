package com.example.githubclient1.mapper;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.example.githubclient1.dto.RepositoryResponse;
import com.example.githubclient1.entity.RepositoryEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void mapToEntity_GithubRepositoryResponseProvided_RepositoryEntityReturned() {
        GithubRepositoryResponse source = new GithubRepositoryResponse("octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        RepositoryEntity result = mapper.mapToEntity(source);
        assertAll(
                () -> assertEquals("octocat/Hello-World", result.getFullName()),
                () -> assertEquals("This your first repo!", result.getDescription()),
                () -> assertEquals("https://github.com/octocat/Hello-World.git", result.getCloneUrl()),
                () -> assertEquals(100, result.getStars()),
                () -> assertEquals("2011-01-26T19:01:12Z", result.getCreatedAt()));
    }

    @Test
    void mapToResponse_RepositoryEntityProvided_RepositoryResponseReturned() {
        RepositoryEntity entity = new RepositoryEntity(1L, "octocat/Hello-World", "This your first repo!",
                "https://github.com/octocat/Hello-World.git", 100, "2011-01-26T19:01:12Z");
        RepositoryResponse result = mapper.mapToResponse(entity);
        assertAll(
                () -> assertEquals("octocat/Hello-World", result.fullName()),
                () -> assertEquals("This your first repo!", result.description()),
                () -> assertEquals("https://github.com/octocat/Hello-World.git", result.cloneUrl()),
                () -> assertEquals(100, result.stars()),
                () -> assertEquals("2011-01-26T19:01:12Z", result.createdAt()));
    }
}
