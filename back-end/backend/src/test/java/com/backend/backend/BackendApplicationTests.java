package com.backend.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class) // Enable Mockito for JUnit 5
public class BackendApplicationTests {

    // Mock dependencies
    @Mock
    private User userService;

    @Mock
    private WebScraper webScraper;

}
