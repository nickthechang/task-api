package com.nick.taskapp.integration;

import com.nick.taskapp.auth.AuthRequestDto;
import com.nick.taskapp.dto.ApiResponse;
import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.repository.TaskRepository;
import com.nick.taskapp.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Integration tests for the task API.
 *
 * Starts the full Spring Boot application and verifies that
 * authentication, controllers, services, repositories, and
 * persistence work together.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    /*
     * Random port assigned to the test server.
     */
    @LocalServerPort
    private int port;

    /*
     * Repository used to clean task data before each test.
     */
    @Autowired
    private TaskRepository taskRepository;

    /*
     * Repository used to clean user data before each test.
     */
    @Autowired
    private UserRepository userRepository;

    /*
     * Simple HTTP client used to call the running test server.
     */
    private final RestTemplate restTemplate = new RestTemplate();

    private String baseUrl;
    private String authUrl;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        baseUrl = "http://localhost:" + port + "/tasks";
        authUrl = "http://localhost:" + port + "/auth";
    }

    /*
     * Verifies that an authenticated user can create and retrieve tasks.
     */
    @Test
    void shouldCreateAndGetTaskWithAuthentication() {
        String token = registerAndLogin();

        TaskRequestDto request = new TaskRequestDto(
                "Integration Test Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<TaskRequestDto> createEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse =
                restTemplate.postForEntity(baseUrl, createEntity, String.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertTrue(createResponse.getBody().contains("Integration Test Task"));

        HttpEntity<Void> getEntity = new HttpEntity<>(headers);

        ResponseEntity<String> getResponse =
                restTemplate.exchange(baseUrl, HttpMethod.GET, getEntity, String.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertTrue(getResponse.getBody().contains("Integration Test Task"));
    }

    /*
     * Verifies that protected task endpoints reject unauthenticated requests.
     */
    @Test
    void shouldRejectUnauthenticatedTaskRequest() {
        ResponseEntity<String> response =
                restTemplate.getForEntity(baseUrl, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    /*
     * Registers and logs in a test user, then returns the JWT token.
     */
    private String registerAndLogin() {
        AuthRequestDto authRequest = new AuthRequestDto("testuser", "password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AuthRequestDto> registerEntity = new HttpEntity<>(authRequest, headers);

        restTemplate.postForEntity(authUrl + "/register", registerEntity, String.class);

        HttpEntity<AuthRequestDto> loginEntity = new HttpEntity<>(authRequest, headers);

        ResponseEntity<Map<String, Object>> loginResponse =
                restTemplate.exchange(
                        authUrl + "/login",
                        HttpMethod.POST,
                        loginEntity,
                        new ParameterizedTypeReference<Map<String, Object>>() {}
                );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        Map<String, Object> data = (Map<String, Object>) loginResponse.getBody().get("data");

        assertNotNull(data);
        assertTrue(data.containsKey("token"));

        return data.get("token").toString();
    }
}
/*
TaskIntegrationTest tests the app more realistically than unit tests.
It checks:
Spring Boot starts
controllers work
services work
repositories work
database interaction works
JSON request/response flow works
Unlike TaskServiceTest, this test is not isolated. It tests multiple layers together.

1. Integration testing
Tests multiple layers working together.
2. Random port testing
Uses a real running server on a random port.
3. RestTemplate HTTP calls
Makes actual HTTP requests to the running test app.
4. Authenticated API testing
Because /tasks/** is protected, the test must include a JWT.
5. Test database cleanup
Deletes test data before each test to avoid test pollution.
*/

