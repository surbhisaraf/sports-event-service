package com.example.event.exception;

import com.example.event.payload.response.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<String>> handleResourceNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new GenericResponse<>("Not Found ", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InvalidEventStatusException.class)
    public ResponseEntity<GenericResponse<String>> handleInvalidDoctorStatusException(InvalidEventStatusException ex) {
        return new ResponseEntity<>(new GenericResponse<>("Validation failed: ", ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponse<List<String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

        List<String> errors = bindingResult.getAllErrors().stream()
                .map(objectError -> {
                    if (objectError instanceof FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return objectError.getDefaultMessage();
                })
                .toList();

        return new ResponseEntity<>(new GenericResponse<>("Validation failed:",errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<String>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(new GenericResponse<>("Service error",ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
