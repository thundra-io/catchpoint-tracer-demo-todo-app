package com.catchpoint.tracing.demo.todo.notification;

import com.catchpoint.tracing.demo.todo.model.Todo;

/**
 * @author sozal
 */
public class TodoAddedNotification implements NotificationEvent {

    public static final String EVENT_NAME = "TodoAdded";

    private Todo todo;

    public TodoAddedNotification() {
    }

    public TodoAddedNotification(Todo todo) {
        this.todo = todo;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public Todo getTodo() {
        return todo;
    }

    public void setTodo(Todo todo) {
        this.todo = todo;
    }

    @Override
    public String toString() {
        return "TodoAddedNotification{" +
                "eventName=" + getEventName() +
                ", todo=" + todo +
                '}';
    }
    
}
