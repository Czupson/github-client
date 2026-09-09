package com.example.githubclient1.config;

import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GithubErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 503) {
            log.warn("GitHub returned 503 Service Unavailable for method {}. Retrying request.", methodKey);
            return new RetryableException(
                    response.status(),
                    "GitHub returned 503 Service Unavailable",
                    response.request().httpMethod(),
                    (Throwable) null,
                    (Long) null,
                    response.request()
            );
        }
        log.debug("GitHub returned status {} for method {}. Using default error decoder.", response.status(), methodKey);
        return defaultErrorDecoder.decode(methodKey, response);
    }
}