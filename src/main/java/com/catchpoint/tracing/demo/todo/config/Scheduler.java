package com.catchpoint.tracing.demo.todo.config;

import com.catchpoint.tracing.demo.todo.service.TodoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@ConditionalOnProperty(value = "spring.schedule.enabled", havingValue = "true", matchIfMissing = true)
@EnableScheduling
public class Scheduler {

    private final TodoService service;
    
    public Scheduler(TodoService service) {
        this.service = service;
    }
    
    @Scheduled(fixedRate = 5 * 60 * 1000)
    private void clearOldTodos() {
        service.clearOldTodos();
    }
}
