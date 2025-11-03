package com.tavares.recommender.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import org.springframework.web.servlet.NoHandlerFoundException;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private Model model;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        model = new ConcurrentModel();
    }

    @Test
    void testHandleGeneralError() {
        Exception ex = new RuntimeException("Test error message");

        String viewName = exceptionHandler.handleGeneralError(ex, model);

        assertEquals("error", viewName);
        assertEquals("Something went wrong 😢", model.getAttribute("errorTitle"));
        assertEquals("Test error message", model.getAttribute("errorMessage"));
        assertEquals("RuntimeException", model.getAttribute("errorType"));
    }

    @Test
    void testHandle404Error() {
        NoHandlerFoundException ex = new NoHandlerFoundException("GET", "/unknown", null);

        String viewName = exceptionHandler.handle404Error(ex, model);

        assertEquals("error", viewName);
        assertEquals("Page Not Found 🚧", model.getAttribute("errorTitle"));
        assertTrue(((String) Objects.requireNonNull(model.getAttribute("errorMessage")))
            .contains("doesn't exist"));
        assertEquals("404 Not Found", model.getAttribute("errorType"));
    }
}
