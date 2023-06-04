package com.catchpoint.tracing.demo.todo.controller;

import com.catchpoint.tracing.demo.todo.client.UserClient;
import com.catchpoint.tracing.demo.todo.config.ChaosConfiguration;
import com.catchpoint.tracing.demo.todo.http.HttpException;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.service.TodoService;
import com.catchpoint.tracing.demo.todo.model.Todo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author sozal
 */
@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final List<String> USER_EMAIL_LIST = Arrays.asList(
            "cwhite@todo.app",
            "awoods@todo.app",
            "mgordon@todo.app",
            "lfox@todo.app",
            "rwood@todo.app",
            "jbrooks@todo.app",
            "eadams@todo.app",
            "ehunter@todo.app",
            "imoss@todo.app",
            "swilliams@todo.app" // Not exist
    );
    private static final Random RANDOM = new Random();

    private final TodoService service;
    private final ChaosConfiguration chaosConfiguration;
    private final UserClient userClient;

    public TodoController(TodoService service, ChaosConfiguration chaosConfiguration,
                          UserClient userClient, @Value("${user.check.enabled:false}") boolean userCheckEnabled) {
        this.service = service;
        this.chaosConfiguration = chaosConfiguration;
        this.userClient = userCheckEnabled ? userClient : null;
    }

    private void checkUserAccess() {
        if (userClient != null) {
            String userEmail = USER_EMAIL_LIST.get(RANDOM.nextInt(USER_EMAIL_LIST.size()));
            try {
                User user = userClient.get("/users/get/" + userEmail, User.class);
                if (user == null) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }
            } catch (HttpException e) {
                if (e.getResponseCode() == HttpStatus.NOT_FOUND.value()) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                } else {
                    throw new ResponseStatusException(HttpStatus.resolve(e.getResponseCode()));
                }
            }
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Todo>> findTodos() {
        checkUserAccess();

        List<Todo> todos = service.findTodos();
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/add")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo request) throws Exception {
        checkUserAccess();

        chaosConfiguration.throwRandomException();

        Todo todo = service.addTodo(request);
        return ResponseEntity.ok(todo);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo request) {
        checkUserAccess();

        Todo todo = service.updateTodo(id, request);
        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        checkUserAccess();

        service.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/duplicate/{id}")
    public ResponseEntity<Todo> duplicateTodo(@PathVariable Long id) {
        checkUserAccess();

        Todo todo = service.duplicateTodo(id);
        return ResponseEntity.ok(todo);
    }

    @PostMapping("/clear-completed")
    public ResponseEntity<Void> clearCompletedTodo() {
        checkUserAccess();

        service.clearCompletedTodo();
        return ResponseEntity.ok().build();
    }

}
