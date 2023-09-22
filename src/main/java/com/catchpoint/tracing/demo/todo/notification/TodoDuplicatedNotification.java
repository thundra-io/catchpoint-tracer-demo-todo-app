package com.catchpoint.tracing.demo.todo.notification;

import com.catchpoint.tracing.demo.todo.model.Todo;

/**
 * @author sozal
 */
public class TodoDuplicatedNotification implements NotificationEvent {

    public static final String EVENT_NAME = "TodoDuplicated";

    private Todo todo;

    public TodoDuplicatedNotification() {
    }

    public TodoDuplicatedNotification(Todo todo) {
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
        return "TodoDuplicatedNotification{" +
                "eventName=" + getEventName() +
                ", todo=" + todo +
                '}';
    }

}
