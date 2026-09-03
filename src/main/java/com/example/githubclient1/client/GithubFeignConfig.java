package com.example.githubclient1.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;

public class GithubFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}