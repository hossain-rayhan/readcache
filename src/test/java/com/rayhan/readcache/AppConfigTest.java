package com.rayhan.readcache;

import com.github.benmanes.caffeine.cache.Cache;
import com.rayhan.readcache.client.GitHubClient;
import com.rayhan.readcache.model.GitHubRepoViewData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static com.rayhan.readcache.constants.ApplicationConstant.GITHUB_BASE_URL;
import static com.rayhan.readcache.constants.ApplicationConstant.PAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class AppConfigTest {

    @Test
    void provideRestTemplate() {
        AppConfig appConfig = new AppConfig();
        RestTemplate restTemplate = appConfig.provideRestTemplate();

        assertNotNull(restTemplate);
    }

    @Test
    void provideGitHubClient() {
        AppConfig appConfig = new AppConfig();
        GitHubClient gitHubClient = appConfig.provideGitHubClient();

        assertNotNull(gitHubClient);
        assertEquals(GITHUB_BASE_URL, gitHubClient.getBaseUrl());
        assertEquals(PAGE_SIZE, gitHubClient.getPageSize());
    }

    @Test
    void provideSharedReadCache() {
        AppConfig appConfig = new AppConfig();
        Cache<String, String> sharedReadCache = appConfig.provideSharedReadCache();

        assertNotNull(sharedReadCache);
    }

    @Test
    void provideGitHubRepoViewCache() {
        AppConfig appConfig = new AppConfig();
        Cache<String, GitHubRepoViewData> gitHubRepoViewCache = appConfig.provideGitHubRepoViewCache();

        assertNotNull(gitHubRepoViewCache);
    }
}
