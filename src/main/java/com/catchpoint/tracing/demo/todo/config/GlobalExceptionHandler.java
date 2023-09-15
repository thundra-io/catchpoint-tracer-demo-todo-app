package com.catchpoint.tracing.demo.todo.config;

import com.catchpoint.tracing.demo.todo.model.Todo;
import org.hibernate.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<Todo> handleTransactionalException(TransactionException ex) {
        
        if ("transaction timeout expired".equals(ex.getMessage())) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).build();
        }
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
