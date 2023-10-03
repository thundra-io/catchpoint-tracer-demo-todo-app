package com.catchpoint.tracing.demo.todo.notification;

/**
 * @author sozal
 */
public class TodosClearedNotification implements NotificationEvent {

    public static final String EVENT_NAME = "TodosCleared";

    public TodosClearedNotification() {
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public String toString() {
        return "TodosClearedNotification{" +
                "eventName=" + getEventName() +
                '}';
    }

}
