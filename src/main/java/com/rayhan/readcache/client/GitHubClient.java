package com.rayhan.readcache.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.rayhan.readcache.constants.ApplicationConstant.ACCEPT_HEADER;
import static com.rayhan.readcache.constants.ApplicationConstant.TOKEN_PREFIX;

/**
 * GitHubClient is a utility class for making paginated requests to the GitHub API.
 * It's mainly a wrapper on RestTemplate.
 */
@Builder
@Slf4j
@Getter
public class GitHubClient {

    // Base URL for GitHub API
    private final String baseUrl;

    // Page size for paginated requests
    private final int pageSize;

    // API token for authentication
    private final String apiToken;

    // RestTemplate for making HTTP requests
    private final RestTemplate restTemplate;

    // ObjectMapper for JSON processing
    private final ObjectMapper mapper;

    /**
     * Reads paginated data from the specified path in the GitHub API.
     *
     * @param path The path to the GitHub API resource.
     * @return A JSON string containing paginated data.
     */
    public String readPaginatedData(String path) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl + path)
                .queryParam("per_page", pageSize)
                .build()
                .toUri();

        var result = mapper.createArrayNode();

        while (uri != null) {
            ResponseEntity<String> response = send(uri);
            try {
                // If the response body is null or not an array, we don't need pagination check. Just return.
                if (response.getBody() == null || !isArrayResponse(response)) {
                    return response.getBody();
                }
            } catch (JsonProcessingException e) {
                log.error("Malformatted JSON response: {}", response);
                throw new RuntimeException(e);
            }
            uri = getNextPageLinkFromResponseHeader(response);
            try {
                result.addAll(mapper.readValue(response.getBody(), ArrayNode.class));
            } catch (JsonProcessingException e) {
                log.error("Failed to process JSON response");
                throw new RuntimeException(e);
            }
        }

        return result.toString();
    }

    /**
     * Checks if the response body is an array.
     *
     * @param response The HTTP response.
     * @return True if the response body is an array, false otherwise.
     * @throws JsonProcessingException If there is an issue processing the JSON response.
     */
    private boolean isArrayResponse(ResponseEntity<String> response) throws JsonProcessingException {
        JsonNode jsonNode = mapper.readTree(response.getBody());
        return jsonNode != null && jsonNode.isArray();
    }

    /**
     * Sends an HTTP GET request to the specified URI.
     *
     * @param uri The URI of the request.
     * @return The HTTP response.
     */
    ResponseEntity<String> send(URI uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, ACCEPT_HEADER);

        if (apiToken != null) {
            headers.add(HttpHeaders.AUTHORIZATION, TOKEN_PREFIX + " " + apiToken);
        }

        RequestEntity<Void> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, uri);

        return restTemplate.exchange(requestEntity, String.class);
    }

    /**
     * Extracts the next page link from the 'Link' header in the HTTP response.
     * Official Reference: https://docs.github.com/en/rest/guides/using-pagination-in-the-rest-api?apiVersion=2022-11-28
     *
     * @param response The HTTP response.
     * @return The URI of the next page, or null if there is no next page.
     */
    URI getNextPageLinkFromResponseHeader(ResponseEntity<String> response) {
        final String LINK_SEPARATOR = ",";
        final String PARAM_SEPARATOR = ";";

        HttpHeaders headers = response.getHeaders();
        List<String> headerValues = headers.get(HttpHeaders.LINK);

        if (headerValues == null || headerValues.isEmpty()) {
            return null;
        }

        String header = headerValues.get(0);
        String[] links = header.split(LINK_SEPARATOR);

        for (String link : links) {
            String trimmedLink = link.trim();
            if (trimmedLink.endsWith("rel=\"next\"")) {
                String[] parts = trimmedLink.split(PARAM_SEPARATOR);
                String firstPart = parts[0].trim();

                if (firstPart.startsWith("<") && firstPart.endsWith(">")) {
                    return URI.create(firstPart.substring(1, firstPart.length() - 1));
                }
            }
        }

        return null;
    }
}
