package com.example.githubclient1.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GithubRetryer extends Retryer.Default {
    public GithubRetryer() {
        super(100, 1000, 3);
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        log.warn("Retrying GitHub request after error: {}", e.status());
        super.continueOrPropagate(e);
    }

    @Override
    public Retryer clone() {
        return new GithubRetryer();
    }
}