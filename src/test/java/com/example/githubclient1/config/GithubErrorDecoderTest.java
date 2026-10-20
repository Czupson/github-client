package com.example.githubclient1.config;

import feign.FeignException;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class GithubErrorDecoderTest {
    private final GithubErrorDecoder errorDecoder = new GithubErrorDecoder();

    @Test
    void decode_ServiceUnavailable_ReturnsRetryableException() {
        //Given
        Response response = Response.builder().status(503).reason("Service Unavailable")
                .request(Request.create(Request.HttpMethod.GET, "https://api.github.com/repos/octocat/Hello-World",
                        Collections.emptyMap(), null, null, null)).build();
        //When
        Exception result = errorDecoder.decode("GithubClient#getRepository", response);
        //Then
        assertInstanceOf(RetryableException.class, result);
    }

    @Test
    void decode_NotFound_ReturnsFeignExceptionNotFound() {
        //Given
        Response response = Response.builder().status(404).reason("Not Found")
                .request(Request.create(Request.HttpMethod.GET, "https://api.github.com/repos/octocat/NotExistingRepository",
                        Collections.emptyMap(), null, null, null)).build();
        //When
        Exception result = errorDecoder.decode("GithubClient#getRepository", response);
        //Then
        assertInstanceOf(FeignException.NotFound.class, result);
    }
}