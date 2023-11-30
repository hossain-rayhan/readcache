package com.rayhan.readcache.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rayhan.readcache.model.GitHubRepoData;
import com.rayhan.readcache.model.GitHubRepoViewData;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static com.rayhan.readcache.constants.ApplicationConstant.REPO_FORK_COUNT;
import static com.rayhan.readcache.constants.ApplicationConstant.REPO_FULL_NAME;
import static com.rayhan.readcache.constants.ApplicationConstant.REPO_ISSUE_COUNT;
import static com.rayhan.readcache.constants.ApplicationConstant.REPO_LAST_UPDATED;
import static com.rayhan.readcache.constants.ApplicationConstant.REPO_STAR_COUNT;

/**
 * GitHubHelper is a utility class providing methods for processing GitHub repository data and generate RepoView.
 */
public class GitHubHelper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static class GitHubRepoDataComparator<T extends Comparable<T>> implements Comparator<GitHubRepoData<T>> {
        @Override
        public int compare(GitHubRepoData<T> o1, GitHubRepoData<T> o2) {
            // First, compare by order field in descending order
            int dataComparison = o2.getOrder().compareTo(o1.getOrder());

            // If order is equal, compare by name
            if (dataComparison == 0) {
                return o1.getName().compareTo(o2.getName());
            }

            return dataComparison;
        }
    }

    /**
     * Generates a GitHubRepoViewData object from the provided JSON string representing GitHub repository data.
     *
     * @param json The JSON string containing GitHub repository data.
     * @return GitHubRepoViewData object with ordered lists based on different criteria.
     * @throws JsonProcessingException If there is an issue processing the JSON data.
     */
    public static GitHubRepoViewData generateViewsFromRepositoryData(String json) throws JsonProcessingException {
        GitHubRepoViewData gitHubRepoViewData = new GitHubRepoViewData();
        List<ObjectNode> repositories;
        GitHubRepoDataComparator<Integer> intOrderComparator = new GitHubRepoDataComparator<>();
        GitHubRepoDataComparator<String> stringOrderComparator = new GitHubRepoDataComparator<>();

        try {
            // Parse JSON string into a list of ObjectNode
            repositories = objectMapper.readValue(json, new TypeReference<List<ObjectNode>>() {});
        } catch (IOException e) {
            throw new RuntimeException("Failed to map JSON response to ObjectNode", e);
        }

        // Populate GitHubRepoViewData with ordered lists based on different criteria
        gitHubRepoViewData.setOrderedByForks(repositories.stream()
                .map(repository -> new GitHubRepoData<>(repository.get(REPO_FULL_NAME).asText(), repository.get(REPO_FORK_COUNT).asInt()))
                .sorted(intOrderComparator)
                .toList());

        gitHubRepoViewData.setOrderedByStars(repositories.stream()
                .map(repository -> new GitHubRepoData<>(repository.get(REPO_FULL_NAME).asText(), repository.get(REPO_STAR_COUNT).asInt()))
                .sorted(intOrderComparator)
                .toList());

        gitHubRepoViewData.setOrderedByOpenIssues(repositories.stream()
                .map(repository -> new GitHubRepoData<>(repository.get(REPO_FULL_NAME).asText(), repository.get(REPO_ISSUE_COUNT).asInt()))
                .sorted(intOrderComparator)
                .toList());

        gitHubRepoViewData.setOrderedByLastUpdated(repositories.stream()
                .map(repository -> new GitHubRepoData<>(repository.get(REPO_FULL_NAME).asText(), repository.get(REPO_LAST_UPDATED).asText()))
                .sorted(stringOrderComparator)
                .toList());

        return gitHubRepoViewData;
    }
}
