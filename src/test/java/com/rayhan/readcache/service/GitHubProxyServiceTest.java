package com.rayhan.readcache.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.rayhan.readcache.client.GitHubClient;
import com.rayhan.readcache.model.GitHubRepoData;
import com.rayhan.readcache.model.GitHubRepoViewData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.rayhan.readcache.constants.ApplicationConstant.REPO_VIEW_CACHE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GitHubProxyServiceTest {
    private final String CACHEABLE_ENDPOINT = "/orgs/Netflix";
    private final String NON_CACHEABLE_ENDPOINT = "/orgs/Netflix/non-cacheable";
    private final int LIMIT = 10;
    private final String ORDERED_BY = "forks";


    // Create dummy GitHubRepoData objects
    GitHubRepoData<Integer> repoData1 = new GitHubRepoData<>("Repo1", 100);
    GitHubRepoData<Integer> repoData2 = new GitHubRepoData<>("Repo2", 200);
    GitHubRepoData<Integer> repoData3 = new GitHubRepoData<>("Repo3", 150);

    // Dummy data for GitHubRepoViewData
    List<GitHubRepoData<Integer>> orderedByForks = Arrays.asList(repoData1, repoData2, repoData3);
    List<GitHubRepoData<Integer>> orderedByStars = Arrays.asList(repoData3, repoData1, repoData2);
    List<GitHubRepoData<Integer>> orderedByOpenIssues = Arrays.asList(repoData2, repoData3, repoData1);
    List<GitHubRepoData<String>> orderedByLastUpdated = Arrays.asList(
            new GitHubRepoData<>("Repo4", "2022-01-01"),
            new GitHubRepoData<>("Repo5", "2022-02-15"),
            new GitHubRepoData<>("Repo6", "2022-03-20")
    );

    // Create GitHubRepoViewData with dummy data
    GitHubRepoViewData dummyGitHubRepoViewData = new GitHubRepoViewData(
            orderedByForks,
            orderedByStars,
            orderedByOpenIssues,
            orderedByLastUpdated
    );

    @Mock
    private Cache<String, String> sharedReadCache;

    @Mock
    private Cache<String, GitHubRepoViewData> gitHubRepoViewCache;

    @InjectMocks
    private GitHubProxyService gitHubProxyService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        gitHubProxyService = new GitHubProxyService();
        sharedReadCache = mock(Cache.class);
        gitHubRepoViewCache = mock(Cache.class);
        gitHubProxyService.restTemplate = mock(RestTemplate.class);
        gitHubProxyService.gitHubClient = mock(GitHubClient.class);
        gitHubProxyService.sharedReadCache = sharedReadCache;
        gitHubProxyService.gitHubRepoViewCache = gitHubRepoViewCache;
    }

    @Test
    public void proxyGet_ReturnsFromCacheWhenAvailable() {
        when(sharedReadCache.getIfPresent(CACHEABLE_ENDPOINT)).thenReturn("cache-data");

        String result = gitHubProxyService.proxyGet(CACHEABLE_ENDPOINT);

        assertEquals("cache-data", result);
        verify(gitHubProxyService.gitHubClient, never()).readPaginatedData(anyString());
    }

    @Test
    void proxyGet_SuccessForNonCacheableEndpoint() {
        String directGitHubCallResponse = "Direct GitHubCall Response";

        when(sharedReadCache.getIfPresent(NON_CACHEABLE_ENDPOINT)).thenReturn(null);
        when(gitHubProxyService.gitHubClient.readPaginatedData(NON_CACHEABLE_ENDPOINT)).thenReturn(directGitHubCallResponse);

        String result = gitHubProxyService.proxyGet(NON_CACHEABLE_ENDPOINT);

        assertEquals(directGitHubCallResponse, result);
    }

    @Test
    void getBottomNReposWithCachedData_SuccessWithValidData() {
        when(gitHubRepoViewCache.getIfPresent(REPO_VIEW_CACHE_KEY)).thenReturn(dummyGitHubRepoViewData);

        String result = gitHubProxyService.getBottomNRepos(LIMIT, ORDERED_BY);

        verify(gitHubRepoViewCache, times(2)).getIfPresent(REPO_VIEW_CACHE_KEY);
        assertEquals(result, orderedByForks.stream().toList().toString());
    }
}
