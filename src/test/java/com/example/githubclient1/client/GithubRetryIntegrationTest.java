package com.example.githubclient1.client;

import com.example.githubclient1.dto.GithubRepositoryResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import org.wiremock.spring.InjectWireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(properties = "github.api.url=http://localhost:8181")
@EnableWireMock(@ConfigureWireMock(port = 8181))
class GithubRetryIntegrationTest {

    @Autowired
    private GithubClient githubClient;

    @InjectWireMock
    private WireMockServer wireMockServer;

    @Test
    void getRepository_WhenGitHubReturns503Then200_RetriesRequest() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        GithubRepositoryResponse repositoryResponse = new GithubRepositoryResponse("octocat/Hello-World",
                        "Test repository", "https://github.com/octocat/Hello-World.git", 100,
                        "2020-01-01T00:00:00Z");
        String responseBody = objectMapper.writeValueAsString(repositoryResponse);
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                        .inScenario("GitHub retry")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(aResponse().withStatus(503))
                        .willSetStateTo("Second attempt"));
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                        .inScenario("GitHub retry")
                        .whenScenarioStateIs("Second attempt")
                        .willReturn(aResponse().withStatus(503))
                        .willSetStateTo("Success"));
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/Hello-World"))
                        .inScenario("GitHub retry")
                        .whenScenarioStateIs("Success")
                        .willReturn(aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json").withBody(responseBody)));
        // When
        GithubRepositoryResponse response = githubClient.getRepository("octocat", "Hello-World");
        // Then
        assertEquals("octocat/Hello-World", response.fullName());
        verify(3, getRequestedFor(urlEqualTo("/repos/octocat/Hello-World"))
        );
    }

    @Test
    void getRepository_WhenGitHubAlwaysReturns503_StopsAfterThreeAttempts() {
        // Given
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/Hello-World")).willReturn(aResponse().withStatus(503)));
        // When
        Exception exception = assertThrows(RetryableException.class,
                () -> githubClient.getRepository("octocat", "Hello-World"));
        // Then
        assertEquals(503, ((RetryableException) exception).status());
        verify(3, getRequestedFor(urlEqualTo("/repos/octocat/Hello-World")));
    }

    @Test
    void getRepository_WhenGitHubReturns404_DoesNotRetry() {
        // Given
        wireMockServer.stubFor(get(urlEqualTo("/repos/octocat/NotExistingRepository")).willReturn(aResponse().withStatus(404)));
        // When
        Exception exception = assertThrows(FeignException.NotFound.class,
                () -> githubClient.getRepository("octocat", "NotExistingRepository"));
        // Then
        verify(1, getRequestedFor(urlEqualTo("/repos/octocat/NotExistingRepository")));
    }
}