package com.rayhan.readcache.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static com.rayhan.readcache.constants.ApplicationConstant.HEALTH_CHECK_URL;

/**
 * HealthCheckController is a Spring MVC controller responsible for handling health check requests.
 */
@Slf4j
@Controller
@RequestMapping("/")
public class HealthCheckController {
    @Autowired
    private RestTemplate restTemplate;

    /**
     * Handles GET requests to "/healthcheck" and performs a health check by pinging an upstream endpoint.
     * FIXME: Make the health check logic more robust.
     *
     * @return The HTTP status code indicating the health status - OK if the upstream endpoint is reachable, SERVICE_UNAVAILABLE otherwise.
     */
    @GetMapping("/healthcheck")
    @ResponseBody
    public int healthCheck() {
        log.trace("Pinging upstream endpoint for HealthCheck: {}", HEALTH_CHECK_URL);

        try {
            int statusCode = restTemplate.getForEntity(URI.create(HEALTH_CHECK_URL), String.class).getStatusCodeValue();

            if (statusCode == HttpStatus.OK.value()) {
                return HttpStatus.OK.value();
            }
        } catch (Exception e) {
            log.error("Health Check failed");
        }

        return HttpStatus.SERVICE_UNAVAILABLE.value();
    }
}
