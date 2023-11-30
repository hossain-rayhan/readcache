package com.rayhan.readcache.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GitHubRepoDataTest {

    @Test
    public void toString_withIntegerOrder() {
        GitHubRepoData<Integer> repoData = new GitHubRepoData<>("repoName", 100);

        String result = repoData.toString();

        assertEquals("[\"repoName\", 100]", result);
    }

    @Test
    public void toString_withStringOrder() {
        GitHubRepoData<String> repoData = new GitHubRepoData<>("repoName", "last_updated_time");

        String result = repoData.toString();

        assertEquals("[\"repoName\", \"last_updated_time\"]", result);
    }
}
