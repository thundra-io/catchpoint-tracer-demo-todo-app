package com.catchpoint.tracing.demo.todo.controller;

import com.catchpoint.tracing.demo.todo.config.RandomExceptionConfiguration;
import com.catchpoint.tracing.demo.todo.service.TodoService;
import com.catchpoint.tracing.demo.todo.model.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Random;

/**
 * @author sozal
 */
@RestController
@RequestMapping("/todos")
public class TodoController {
    
    private final RandomExceptionConfiguration randomExceptionConfiguration;
    
    private final TodoService service;
    public TodoController(TodoService service, RandomExceptionConfiguration randomExceptionConfiguration) {
        this.service = service;
        this.randomExceptionConfiguration = randomExceptionConfiguration;
    }                                                                                                      

    @GetMapping("/list")
    public ResponseEntity<List<Todo>> findTodos() {
        List<Todo> todos = service.findTodos();
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/add")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo request) throws Exception {
        randomExceptionConfiguration.throwRandomException();
        Todo todo = service.addTodo(request);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo request) {
        Todo todo = service.updateTodo(id, request);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        service.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/duplicate/{id}")
    public ResponseEntity<Todo> duplicateTodo(@PathVariable Long id) {
        Todo todo = service.duplicateTodo(id);
        return ResponseEntity.ok(todo);
    }

    @PostMapping("/clear-completed")
    public ResponseEntity<Void> clearCompletedTodo() {
        service.clearCompletedTodo();
        return ResponseEntity.ok().build();
    }

}
