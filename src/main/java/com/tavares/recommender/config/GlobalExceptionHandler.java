package com.tavares.recommender.config;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle all uncaught exceptions (e.g., OpenAI API errors, null pointers, etc.)
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneralError(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Something went wrong 😢");
        model.addAttribute("errorMessage", ex.getMessage() != null ? ex.getMessage() : "Unexpected server error.");
        model.addAttribute("errorType", ex.getClass().getSimpleName());
        return "error"; // Renders error.html
    }

    /**
     * Handle 404 Not Found errors (bad URLs)
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle404Error(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Page Not Found 🚧");
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
        model.addAttribute("errorType", "404 Not Found");
        return "error";
    }
}

