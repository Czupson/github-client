package com.example.githubclient1.config;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class GithubFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder githubErrorDecoder() {
        return new GithubErrorDecoder();
    }

    @Bean
    public Retryer githubRetryer() {
        return new GithubRetryer();
    }
}