package com.rayhan.readcache.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class HealthCheckControllerTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HealthCheckController healthCheckController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void healthCheck_ReturnHttpStatusOK() {
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        int result = healthCheckController.healthCheck();

        assertEquals(HttpStatus.OK.value(), result);
        verify(restTemplate, times(1)).getForEntity(any(URI.class), eq(String.class));
    }

    @Test
    void healthCheck_ReturnsServiceUnavailable() {
        when(restTemplate.getForEntity(any(URI.class), eq(String.class)))
                .thenThrow(new RuntimeException("Service Unavailable"));

        int result = healthCheckController.healthCheck();

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), result);
        verify(restTemplate, times(1)).getForEntity(any(URI.class), eq(String.class));
    }
}
