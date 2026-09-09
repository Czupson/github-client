package com.example.githubclient1.config;

import feign.Request;
import feign.RetryableException;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GithubRetryerTest {
    @Test
    void continueOrPropagate_ThreeAttempts_ThrowsException() {
        //Given
        GithubRetryer retryer = new GithubRetryer();
        RetryableException exception = new RetryableException(503, "Service Unavailable", Request.HttpMethod.GET,
                (Throwable) null, (Long) null, Request.create(Request.HttpMethod.GET, "https://api.github.com/repos/octocat/Hello-World",
                        Collections.emptyMap(), null, null, null));
        //When + Then
        assertThrows(RetryableException.class,
                () -> {
                    retryer.continueOrPropagate(exception);
                    retryer.continueOrPropagate(exception);
                    retryer.continueOrPropagate(exception);
                    retryer.continueOrPropagate(exception);
                }
        );
    }
}