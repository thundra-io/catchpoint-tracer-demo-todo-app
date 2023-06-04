package com.catchpoint.tracing.demo.todo.controller;

import com.catchpoint.tracing.demo.todo.config.ChaosConfiguration;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sozal
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final ChaosConfiguration chaosConfiguration;

    private final UserService service;
    public UserController(UserService service, ChaosConfiguration chaosConfiguration) {
        this.service = service;
        this.chaosConfiguration = chaosConfiguration;
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<User> getUser(@PathVariable String email) {
        User user = service.getUser(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

}
