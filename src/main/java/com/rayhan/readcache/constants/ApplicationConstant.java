package com.rayhan.readcache.constants;

import java.util.Set;

public class ApplicationConstant {

    /**
     * GitHub Endpoint specific constants.
     * FIXME: Move to a framework specific application config file in yaml or xml format.
     */
    public static final String ENV_SERVER_PORT = "SERVER_PORT";
    public static final String ENV_GITHUB_API_TOKEN = "GITHUB_API_TOKEN";
    public static final String GITHUB_BASE_URL = "https://api.github.com";
    public static final String HEALTH_CHECK_URL = "https://api.github.com";
    public static final String GITHUB_NETFLIX_ORG = "/orgs/Netflix";
    public static final String GITHUB_REPOS_PATH = "/orgs/Netflix/repos";
    public static final Set<String> GITHUB_CACHEABLE_ENDPOINTS = Set.of(
            "/",
            GITHUB_NETFLIX_ORG,
            GITHUB_NETFLIX_ORG + "/members",
            GITHUB_NETFLIX_ORG + "/repos");


    /**
     * HTTP Client configuration specific constants.
     */
    public static final String ACCEPT_HEADER = "application/json";
    public static final String TOKEN_PREFIX = "token";
    public static final int PAGE_SIZE = 150;

    /**
     * In-Memory Cache specific constants.
     */
    public static final int CACHE_TTL_IN_SECONDS = 60;
    public static final String REPO_VIEW_CACHE_KEY = "GitHubRepoViewCacheKey";


    /**
     * GitHub Repository specific constants.
     */
    public static final String ORDER_BY_FORKS = "forks";
    public static final String ORDER_BY_STARS = "stars";
    public static final String ORDER_BY_OPEN_ISSUES = "open_issues";
    public static final String ORDER_BY_LAST_UPDATED = "last_updated";
    public static final String REPO_FULL_NAME = "full_name";
    public static final String REPO_FORK_COUNT = "forks_count";
    public static final String REPO_STAR_COUNT = "stargazers_count";
    public static final String REPO_ISSUE_COUNT = "open_issues_count";
    public static final String REPO_LAST_UPDATED = "updated_at";


}
