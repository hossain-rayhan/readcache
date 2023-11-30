package com.rayhan.readcache.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.benmanes.caffeine.cache.Cache;
import com.rayhan.readcache.client.GitHubClient;
import com.rayhan.readcache.model.GitHubRepoViewData;
import com.rayhan.readcache.utils.GitHubHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import static com.rayhan.readcache.constants.ApplicationConstant.GITHUB_CACHEABLE_ENDPOINTS;
import static com.rayhan.readcache.constants.ApplicationConstant.GITHUB_REPOS_PATH;
import static com.rayhan.readcache.constants.ApplicationConstant.ORDER_BY_FORKS;
import static com.rayhan.readcache.constants.ApplicationConstant.ORDER_BY_LAST_UPDATED;
import static com.rayhan.readcache.constants.ApplicationConstant.ORDER_BY_OPEN_ISSUES;
import static com.rayhan.readcache.constants.ApplicationConstant.ORDER_BY_STARS;
import static com.rayhan.readcache.constants.ApplicationConstant.REPO_VIEW_CACHE_KEY;

/**
 * GitHubProxyService is a service class responsible for proxying and caching GitHub API requests.
 */
@Slf4j
@Service
public class GitHubProxyService {

    // RestTemplate for making HTTP requests
    @Autowired
    RestTemplate restTemplate;

    // GitHubClient for interacting with the GitHub API
    @Autowired
    GitHubClient gitHubClient;

    // Cache for storing common shared read data where we don't need any customization
    @Autowired
    Cache<String, String> sharedReadCache;

    // Cache for storing custom GitHubRepoViewData
    @Autowired
    Cache<String, GitHubRepoViewData> gitHubRepoViewCache;

    /**
     * Proxies GET requests to GitHub API, caching results for cacheable endpoints.
     *
     * @param path The path of the API request.
     * @return The response from GitHub API or the cached response.
     */
    public String proxyGet(String path) {
        // Normalize path to handle trailing slash
        String normalizedPath = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;

        if (GITHUB_CACHEABLE_ENDPOINTS.contains(normalizedPath)) {
            if (sharedReadCache.getIfPresent(path) == null) {
                try {
                    if (GITHUB_REPOS_PATH.equals(normalizedPath)) {
                        // We need custom processing for GitHub repositories data to serve vew APIs.
                        cacheGitHubRepoAndViewData();
                    } else {
                        sharedReadCache.put(normalizedPath, gitHubClient.readPaginatedData(normalizedPath));
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Caught exception while making GET call to:" + path, e);
                }
            }

            // Return the cached response for cacheable endpoints
            return sharedReadCache.getIfPresent(normalizedPath);
        }

        // Proxy to GitHub directly for non-cacheable endpoints
        return gitHubClient.readPaginatedData(normalizedPath);
    }

    /**
     * Retrieves and returns a limited number of repositories based on the specified ordering criteria.
     *
     * @param limit     The number of repositories to retrieve.
     * @param orderedBy The criteria by which the repositories are ordered.
     * @return The response containing the bottom N repositories.
     */
    public String getBottomNRepos(int limit, String orderedBy) {
        if (gitHubRepoViewCache.getIfPresent(REPO_VIEW_CACHE_KEY) == null) {
            cacheGitHubRepoAndViewData();
        }

        GitHubRepoViewData viewData = gitHubRepoViewCache.getIfPresent(REPO_VIEW_CACHE_KEY);

        if (viewData == null) {
            log.debug("Received null GitHubRepoViewData for: {}", orderedBy);
            return Collections.emptyList().toString();
        }

        return switch (orderedBy) {
            case ORDER_BY_FORKS -> viewData.getOrderedByForks().stream().skip(Math.max(0, viewData.getOrderedByForks().size() - limit)).limit(limit).toList().toString();
            case ORDER_BY_STARS -> viewData.getOrderedByStars().stream().skip(Math.max(0, viewData.getOrderedByStars().size() - limit)).limit(limit).toList().toString();
            case ORDER_BY_OPEN_ISSUES -> viewData.getOrderedByOpenIssues().stream().skip(Math.max(0, viewData.getOrderedByOpenIssues().size() - limit)).limit(limit).toList().toString();
            case ORDER_BY_LAST_UPDATED -> viewData.getOrderedByLastUpdated().stream().skip(Math.max(0, viewData.getOrderedByLastUpdated().size() - limit)).limit(limit).toList().toString();
            default -> Collections.emptyList().toString();
        };
    }

    /**
     * Caches GitHub repository data and GitHubRepoViewData in shared read cache and gitHubRepoViewCache, respectively.
     */
    private void cacheGitHubRepoAndViewData() {
        String repoData = gitHubClient.readPaginatedData(GITHUB_REPOS_PATH);
        sharedReadCache.put(GITHUB_REPOS_PATH, repoData);

        GitHubRepoViewData repoViewData;
        try {
            // Generate GitHubRepoViewData from the retrieved GitHubRepo data
            repoViewData = GitHubHelper.generateViewsFromRepositoryData(repoData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        gitHubRepoViewCache.put(REPO_VIEW_CACHE_KEY, repoViewData);
    }
}
