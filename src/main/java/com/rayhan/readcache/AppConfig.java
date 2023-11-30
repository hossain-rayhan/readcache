package com.rayhan.readcache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.rayhan.readcache.client.GitHubClient;
import com.rayhan.readcache.model.GitHubRepoViewData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static com.rayhan.readcache.constants.ApplicationConstant.CACHE_TTL_IN_SECONDS;
import static com.rayhan.readcache.constants.ApplicationConstant.ENV_GITHUB_API_TOKEN;
import static com.rayhan.readcache.constants.ApplicationConstant.GITHUB_BASE_URL;
import static com.rayhan.readcache.constants.ApplicationConstant.PAGE_SIZE;

/**
 * AppConfig is a configuration class for creating and configuring beans used in the application.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and provides a RestTemplate bean for making HTTP requests.
     *
     * @return RestTemplate instance.
     */
    @Bean
    public RestTemplate provideRestTemplate() {
        return new RestTemplate();
    }

    /**
     * Creates and provides a shared read cache bean using Caffeine.
     *
     * @return Cache instance for common key-value data with a specified time-to-live (TTL) for entries.
     */
    @Bean
    public Cache<String, String> provideSharedReadCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_TTL_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Creates and provides a cache bean for GitHubRepoViewData using Caffeine.
     *
     * @return Cache instance for specific GitHub RepoView data with a specified time-to-live (TTL) for entries.
     */
    @Bean
    public Cache<String, GitHubRepoViewData> provideGitHubRepoViewCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(CACHE_TTL_IN_SECONDS, TimeUnit.SECONDS)
                .build();
    }

    /**
     * Creates and provides a GitHubClient bean with configured properties.
     *
     * @return GitHubClient instance for interacting with the GitHub API.
     */
    @Bean
    public GitHubClient provideGitHubClient() {
        GitHubClient.GitHubClientBuilder builder = GitHubClient.builder()
                .baseUrl(GITHUB_BASE_URL)
                .mapper(new ObjectMapper())
                .pageSize(PAGE_SIZE)
                .restTemplate(provideRestTemplate());

        // Retrieve GitHub API token from environment variables
        String gitHubApiToken = System.getenv(ENV_GITHUB_API_TOKEN);

        // Set API token if available
        if (gitHubApiToken != null) {
            builder.apiToken(gitHubApiToken);
        }

        return builder.build();
    }
}
