package com.rayhan.readcache.controller;

import com.rayhan.readcache.service.GitHubProxyService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GitHubProxyControllerTest {

    @Mock
    private GitHubProxyService service;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private GitHubProxyController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getData_ReturnsData() {
        when(service.proxyGet(any())).thenReturn("Mocked result");

        String result = controller.getData(request);

        assertEquals("Mocked result", result);
    }

    @Test
    void getData_ThrowsRuntimeException_WhenServiceThrowsException() {
        when(service.proxyGet(any())).thenThrow(new RuntimeException("Mocked exception"));

        assertThrows(RuntimeException.class, () -> controller.getData(request));
    }

    @Test
    void getData_ReturnsEmptyString() {
        when(service.proxyGet(any())).thenReturn("");

        String result = controller.getData(request);

        assertEquals("", result);
    }

    @Test
    void getBottomNRepos_ReturnsValidResult() {
        when(service.getBottomNRepos(5, "stars")).thenReturn("Mocked result");

        String result = controller.getBottomNRepos(5, "stars");

        assertEquals("Mocked result", result);
    }

    @Test
    void getBottomNRepos_ThrowsException_WhenServiceThrowsException() {
        when(service.getBottomNRepos(5, "stars")).thenThrow(new RuntimeException("Mocked exception"));

        assertThrows(RuntimeException.class, () -> controller.getBottomNRepos(5, "stars"));
    }

    @Test
    void getBottomNRepos_ReturnsEmptyString() {
        when(service.getBottomNRepos(5, "stars")).thenReturn("");

        String result = controller.getBottomNRepos(5, "stars");

        assertEquals("", result);
    }
}
