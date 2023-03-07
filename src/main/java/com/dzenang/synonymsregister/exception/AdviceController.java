package com.dzenang.synonymsregister.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

/**
 * Controller advice to handle exceptions in controllers.
 */
@Slf4j
@ControllerAdvice
public class AdviceController {

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperation(UnsupportedOperationException ex) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.BAD_REQUEST, ex.getMessage(), Collections.emptyList(), null);
        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorDTO errorDTO = new ErrorDTO(HttpStatus.NOT_FOUND, ex.getMessage(), Collections.emptyList(), null);
        return new ResponseEntity<>(errorDTO, new HttpHeaders(), errorDTO.getStatus());
    }
}
