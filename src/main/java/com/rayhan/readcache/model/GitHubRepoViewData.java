package com.rayhan.readcache.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * GitHubRepoViewData stores the cleaned and formatted data for GitHub repositories.
 * We utilize this class to cache and return various views based on request.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GitHubRepoViewData {
    private List<GitHubRepoData<Integer>> orderedByForks;
    private List<GitHubRepoData<Integer>> orderedByStars;
    private List<GitHubRepoData<Integer>> orderedByOpenIssues;
    private List<GitHubRepoData<String>> orderedByLastUpdated;
}
