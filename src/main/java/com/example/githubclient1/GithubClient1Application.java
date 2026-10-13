package com.example.githubclient1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GithubClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(GithubClient1Application.class, args);
	}

}
