package com.rayhan.readcache.controller;

import com.rayhan.readcache.service.GitHubProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * GitHubProxyController is a Spring MVC controller responsible for handling requests related to GitHub data.
 */
@Controller
@RequestMapping("/")
public class GitHubProxyController {

    @Autowired
    private GitHubProxyService service;

    /**
     * Handles GET requests to any path (matching "**") and proxies the request to the GitHub API.
     *
     * @param request The HttpServletRequest object representing the incoming HTTP request.
     * @return The response received from our GitHubProxyService.
     */
    @GetMapping("**")
    @ResponseBody
    public String getData(HttpServletRequest request) {
        return service.proxyGet(request.getRequestURI());
    }

    /**
     * Handles GET requests to "/view/bottom/{n}/{orderedBy}" to retrieve the bottom N repositories sorted by a specified criteria.
     *
     * @param limit     The number of repositories to retrieve.
     * @param orderedBy The criteria by which the repositories are ordered.
     * @return The response containing the bottom N repositories.
     */
    @GetMapping("/view/bottom/{n}/{orderedBy}")
    @ResponseBody
    public String getBottomNRepos(@PathVariable("n") int limit, @PathVariable("orderedBy") String orderedBy) {
        return service.getBottomNRepos(limit, orderedBy);
    }
}
