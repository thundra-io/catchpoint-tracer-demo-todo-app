package com.catchpoint.tracing.demo.todo.controller;

import com.catchpoint.trace.api.invocation.annotations.InvocationAPI;
import com.catchpoint.tracing.demo.todo.client.UserClient;
import com.catchpoint.tracing.demo.todo.config.ChaosConfiguration;
import com.catchpoint.tracing.demo.todo.http.HttpException;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.notification.NotificationService;
import com.catchpoint.tracing.demo.todo.notification.TodoAddedNotification;
import com.catchpoint.tracing.demo.todo.notification.TodoDeletedNotification;
import com.catchpoint.tracing.demo.todo.notification.TodoDuplicatedNotification;
import com.catchpoint.tracing.demo.todo.notification.TodoUpdatedNotification;
import com.catchpoint.tracing.demo.todo.notification.TodosClearedNotification;
import com.catchpoint.tracing.demo.todo.service.TodoService;
import com.catchpoint.tracing.demo.todo.model.Todo;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author sozal
 */
@RestController
@RequestMapping("/todos")
public class TodoController {

    private static final int TEN_SECONDS_AS_MILLIS = 10 * 1000;
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
    private final NotificationService notificationService;
    private final ChaosConfiguration chaosConfiguration;
    private final UserClient userClient;
    private final Tracer tracer = GlobalTracer.get();

    public TodoController(@Autowired TodoService service,
                          @Autowired(required = false) NotificationService notificationService,
                          @Autowired ChaosConfiguration chaosConfiguration,
                          @Autowired UserClient userClient,
                          @Value("${user.check.enabled:false}") boolean userCheckEnabled) {
        this.service = service;
        this.notificationService = notificationService;
        this.chaosConfiguration = chaosConfiguration;
        this.userClient = userCheckEnabled ? userClient : null;
    }
                                                                            
    public void handleMap(Map<String, String> map) {
        if (map.containsKey("x-chaos-inject-error") && map.get("x-chaos-inject-error").equals("true")) {
            throw new RuntimeException("This error is thrown for testing purpose.");
        } else if (map.containsKey("x-chaos-inject-latency") && map.get("x-chaos-inject-latency").equals("true")) {
            chaosConfiguration.setEnabled(false);
            try {
                Thread.sleep(TEN_SECONDS_AS_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            chaosConfiguration.restore();
        }
    }

    public void checkUserAccess(Map<String, String> headers) {
        handleMap(headers);

        String userEmail = USER_EMAIL_LIST.get(RANDOM.nextInt(USER_EMAIL_LIST.size()));
        if (headers.containsKey("x-chaos-inject-auth-error") &&
                headers.get("x-chaos-inject-auth-error").equals("true")) {
            userEmail = USER_EMAIL_LIST.get(USER_EMAIL_LIST.size() - 1);
        }

        if (userClient != null) {
            InvocationAPI.setTag("email", userEmail);
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
    public ResponseEntity<List<Todo>> findTodos(@RequestHeader Map<String, String> headers) {
        checkUserAccess(headers);

        List<Todo> todos = service.findTodos();
        return ResponseEntity.ok(todos);
    }

    @PostMapping("/add")
    public ResponseEntity<Todo> addTodo(@Valid @RequestBody Todo request,
                                        @RequestHeader Map<String, String> headers)
            throws Exception {
        checkUserAccess(headers);

        chaosConfiguration.throwRandomException();

        Todo todo = service.addTodo(request);

        if (notificationService != null) {
            notificationService.sendNotification(new TodoAddedNotification(todo));
        }

        return ResponseEntity.ok(todo);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @Valid @RequestBody Todo request,
                                           @RequestHeader Map<String, String> headers)
            throws Exception {
        checkUserAccess(headers);

        Todo todo = service.updateTodo(id, request);

        if (notificationService != null) {
            notificationService.sendNotification(new TodoUpdatedNotification(todo));
        }

        return ResponseEntity.ok(todo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @RequestHeader Map<String, String> headers)
            throws Exception {
        checkUserAccess(headers);

        service.deleteTodo(id);

        if (notificationService != null) {
            notificationService.sendNotification(new TodoDeletedNotification(id));
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/duplicate/{id}")
    public ResponseEntity<Todo> duplicateTodo(@PathVariable Long id, @RequestHeader Map<String, String> headers)
            throws Exception {
        checkUserAccess(headers);

        Todo todo = service.duplicateTodo(id);

        if (notificationService != null) {
            notificationService.sendNotification(new TodoDuplicatedNotification(todo));
        }

        return ResponseEntity.ok(todo);
    }

    @PostMapping("/clear-completed")
    public ResponseEntity<Void> clearCompletedTodo(@RequestHeader Map<String, String> headers)
            throws Exception {
        checkUserAccess(headers);

        service.clearCompletedTodo();

        if (notificationService != null) {
            notificationService.sendNotification(new TodosClearedNotification());
        }

        return ResponseEntity.ok().build();
    }

}
