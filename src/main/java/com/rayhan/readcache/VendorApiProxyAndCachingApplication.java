package com.rayhan.readcache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

import static com.rayhan.readcache.constants.ApplicationConstant.ENV_SERVER_PORT;

@Slf4j
@SpringBootApplication
public class VendorApiProxyAndCachingApplication implements CommandLineRunner {
	public static void main(String[] args) {
		String serverPort = System.getenv(ENV_SERVER_PORT);
		SpringApplication application = new SpringApplication(VendorApiProxyAndCachingApplication.class);

		if (serverPort != null) {
			log.info("Environment variable found for SERVER_PORT: {}", serverPort);
			application.setDefaultProperties(Collections.singletonMap("server.port", serverPort));
		} else {
			log.info("No environment variable found for SERVER_PORT. Starting on default port: 8080");
		}

		application.run(args);
	}

	@Override
	public void run(String... args) {
	}
}
