package com.rayhan.readcache.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rayhan.readcache.model.GitHubRepoData;
import com.rayhan.readcache.model.GitHubRepoViewData;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GitHubHelperTest {

    @Test
    public void generateViewsFromRepositoryData_WithValidJSON() throws JsonProcessingException {
        String validJSON = "[{\"full_name\":\"repo1\",\"forks_count\":11,\"stargazers_count\":22,\"open_issues_count\":33,\"updated_at\":\"2023-11-29\"}," +
                "{\"full_name\":\"repo2\",\"forks_count\":22,\"stargazers_count\":33,\"open_issues_count\":44,\"updated_at\":\"2023-11-28\"}]";

        GitHubRepoViewData result = GitHubHelper.generateViewsFromRepositoryData(validJSON);

        List<GitHubRepoData<Integer>> orderedByForks = result.getOrderedByForks();
        assertEquals(2, orderedByForks.size());
        assertEquals("repo2", orderedByForks.get(0).getName());
        assertEquals(22, orderedByForks.get(0).getOrder());
        assertEquals("repo1", orderedByForks.get(1).getName());
        assertEquals(11, orderedByForks.get(1).getOrder());

        List<GitHubRepoData<Integer>> orderedByStars = result.getOrderedByStars();
        assertEquals(2, orderedByStars.size());
        assertEquals("repo2", orderedByStars.get(0).getName());
        assertEquals(33, orderedByStars.get(0).getOrder());
        assertEquals("repo1", orderedByStars.get(1).getName());
        assertEquals(22, orderedByStars.get(1).getOrder());
    }

    @Test
    public void generateViewsFromRepositoryData_WithInvalidJSON() {
        String invalidJSON = "[{\"full_name\":\"repo1\",\"forks_count\":10,\"stargazers_count\":20,\"open_issues_count\":5,\"updated_at\":\"2021-12-01\"}," +
                "{\"full_name\":\"repo2\",\"forks_count\":15,\"stargazers_count\":25,\"open_issues_count\":8,\"updated_at\":\"2021-11-01\","; // Malformed JSON

        assertThrows(RuntimeException.class, () -> GitHubHelper.generateViewsFromRepositoryData(invalidJSON));
    }
}
