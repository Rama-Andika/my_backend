package com.oxysystem.general.exception;

import com.oxysystem.general.response.FailedResponse;
import org.hibernate.QueryTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice

public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailedResponse<?>> handleResourceNotFoundException(ResourceNotFoundException ex){
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        FailedResponse<?> response = new FailedResponse<>("Not found", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ResourceConflictException.class)
    public ResponseEntity<FailedResponse<?>> handleResourceConflictException(ResourceConflictException ex){
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        FailedResponse<?> response = new FailedResponse<>("Data conflict", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceUnauthorizedException.class)
    public ResponseEntity<FailedResponse<String>> handleResourceUnauthorizedException(ResourceUnauthorizedException ex){
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        FailedResponse<String> response = new FailedResponse<>("Unauthorized", ex.getMessage(), requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailedResponse<Map<String,String>>> handleValidationException(MethodArgumentNotValidException ex){
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));
        FailedResponse<Map<String,String>> apiResponse = new FailedResponse<>("Validation data failed", errors, requestId);
        return ResponseEntity.badRequest().body(apiResponse);

    }

    @ExceptionHandler(GrabException.class)
    public ResponseEntity<?> handleGrabMartException(GrabException ex){
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {} {}", requestId, ex.getMessage(), ex.getErrors());

        FailedResponse<Object> response = new FailedResponse<>(ex.getMessage(), ex.getErrors(), requestId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public ResponseEntity<FailedResponse<?>> handleSocketTimeout(SocketTimeoutException ex) {
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        FailedResponse<String> failedResponse = new FailedResponse<>("Request took too long. Please try again later.", null, requestId);
        return ResponseEntity.internalServerError().body(failedResponse);
    }

    @ExceptionHandler(QueryTimeoutException.class)
    public ResponseEntity<FailedResponse<?>> handleDbTimeout(QueryTimeoutException ex) {
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage());

        FailedResponse<String> failedResponse = new FailedResponse<>("Request took too long. Please try again later.", null, requestId);
        return ResponseEntity.internalServerError().body(failedResponse);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<FailedResponse<String>> handleGenericException(Exception ex) {
        String requestId = UUID.randomUUID().toString();
        logger.error("Error REQUEST ID: {} {}", requestId, ex.getMessage(), ex);

        FailedResponse<String> failedResponse = new FailedResponse<>("Something went wrong. Please try again later.", null, requestId);
        return ResponseEntity.internalServerError().body(failedResponse);
    }
}
