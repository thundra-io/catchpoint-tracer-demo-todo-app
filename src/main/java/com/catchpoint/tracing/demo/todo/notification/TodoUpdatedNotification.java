package com.catchpoint.tracing.demo.todo.notification;

import com.catchpoint.tracing.demo.todo.model.Todo;

/**
 * @author sozal
 */
public class TodoUpdatedNotification implements NotificationEvent {

    public static final String EVENT_NAME = "TodosUpdated";

    private Todo todo;

    public TodoUpdatedNotification() {
    }

    public TodoUpdatedNotification(Todo todo) {
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
        return "TodoUpdatedNotification{" +
                "eventName=" + getEventName() +
                ", todo=" + todo +
                '}';
    }

}
