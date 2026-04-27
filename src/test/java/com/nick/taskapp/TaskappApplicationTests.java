package com.nick.taskapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
/*
 * Basic application context test.
 *
 * Verifies that the Spring Boot application can start
 * and load all required beans successfully.
 */
@SpringBootTest
class TaskappApplicationTests {
	/*
     * Smoke test for application startup.
     */
	@Test
	void contextLoads() {
	}

}


/*

This is a basic Spring Boot startup test.
It checks:
“Can the application context load successfully?”
In simple terms:
“Can Spring start the app without crashing?”

1. Context load test
@SpringBootTest starts the full Spring application context.
2. Smoke test
A smoke test is a basic “does the app start?” test.
It does not test business logic. It just confirms the app wiring is not broken.
*/