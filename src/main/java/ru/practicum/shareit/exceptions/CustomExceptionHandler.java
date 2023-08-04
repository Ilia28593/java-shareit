package ru.practicum.shareit.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.model.ApiErrorResponse;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(UnsupportedStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleException(UnsupportedStatusException ex) {
        ApiErrorResponse response = new ApiErrorResponse(ex.getMessage());

        return ResponseEntity.badRequest()
                .header("Error message", ex.getMessage())
                .body(response);
    }
}
