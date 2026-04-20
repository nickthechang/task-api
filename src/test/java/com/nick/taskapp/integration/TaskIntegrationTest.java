package com.nick.taskapp.integration;

import com.nick.taskapp.dto.TaskRequestDto;
import com.nick.taskapp.model.Priority;
import com.nick.taskapp.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TaskRepository taskRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    private String baseUrl;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/tasks";
    }

    @Test
    void shouldCreateAndGetTask() {
        TaskRequestDto request = new TaskRequestDto(
                "Integration Test Task",
                false,
                Priority.HIGH,
                LocalDate.of(2026, 4, 20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TaskRequestDto> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> createResponse =
                restTemplate.postForEntity(baseUrl, entity, String.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertTrue(createResponse.getBody().contains("Integration Test Task"));

        ResponseEntity<String> getResponse =
                restTemplate.getForEntity(baseUrl, String.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertTrue(getResponse.getBody().contains("Integration Test Task"));
    }
}