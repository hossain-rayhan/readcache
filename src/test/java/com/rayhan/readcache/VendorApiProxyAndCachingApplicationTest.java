package com.rayhan.readcache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class VendorApiProxyAndCachingApplicationTest {

    @Autowired
    private VendorApiProxyAndCachingApplication application;

    @Test
    void contextLoads() {
        assertTrue(application != null);
    }
}

