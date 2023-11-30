package com.rayhan.readcache.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * GitHubRepoData is a generic class representing data for a GitHub repository.
 * We need to sort repo data based on different data fields like- forks, stars, lastUpdated.
 * That's why we extened Comparator and utilize the generic type T.
 *
 * @param <T> The type of the order property, must implement Comparable.
 */
@AllArgsConstructor
@Getter
public class GitHubRepoData<T extends Comparable<T>> {

    // Name of the GitHub repository
    private final String name;

    // Order property: (forks, stars, last_updated, open_issues)
    private final T order;

    /**
     * Overrides the default toString method to provide a custom string representation of the object.
     * This is needed to return the output in the expected format and pass our Test Suite.
     *
     * @return A formatted string containing the name and order of the GitHub repository.
     */
    @Override
    public String toString() {
        return "[\"" + name + "\", " + (order instanceof String ? "\"" + order + "\"" : order) + "]";
    }
}
