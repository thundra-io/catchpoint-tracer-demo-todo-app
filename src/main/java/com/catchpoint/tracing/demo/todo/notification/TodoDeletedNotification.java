package com.catchpoint.tracing.demo.todo.notification;

/**
 * @author sozal
 */
public class TodoDeletedNotification implements NotificationEvent {

    public static final String EVENT_NAME = "TodoDeleted";

    private Long id;

    public TodoDeletedNotification() {
    }

    public TodoDeletedNotification(Long id) {
        this.id = id;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TodoDeletedNotification{" +
                "eventName=" + getEventName() +
                ", id=" + id +
                '}';
    }

}
