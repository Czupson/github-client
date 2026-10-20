package com.example.port;

import com.example.Repository;

public interface GithubApiPort {

    Repository getRepository(String owner, String repo);
}
