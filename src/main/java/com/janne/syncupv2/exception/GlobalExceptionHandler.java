package com.janne.syncupv2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private String getPath(final WebRequest request) {
        return Arrays.stream(request.getDescription(false).split("=")).toList().get(1);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(DuplicateEmailException e, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(getPath(request))
                .error(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message("Email address already registered")
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException e, WebRequest request) {
        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> {
                            String defaultMessage = fieldError.getDefaultMessage();
                            return defaultMessage != null ? defaultMessage : "No error message available";
                        }
                ));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(getPath(request))
                .error(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<ErrorResponse> handleRequestException(RequestException e, WebRequest request) {
        return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder()
                .error(e.getErrorObject())
                .path(getPath(request))
                .status(e.getStatus())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, WebRequest request) {
        return ResponseEntity.internalServerError().body(ErrorResponse.builder()
                .status(501)
                .message("Internal Server Error")
                .timestamp(LocalDateTime.now())
                .path(getPath(request))
                .error("")
                .build());
    }
}
