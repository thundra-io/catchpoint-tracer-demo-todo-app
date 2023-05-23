package com.catchpoint.tracing.demo.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Random;


@Configuration
public class ChaosConfiguration {

    private static final short RANDOM_EXCEPTION_RATIO = 5;

    @Value("${chaos.enabled:false}")
    private boolean isRandomException;

    private final Random random = new Random();
    private static class ServerDownException extends Exception {
        ServerDownException() {
            super("Server is down!");
        }
    }
    
    private static class DatabaseDownException extends Exception {
        DatabaseDownException() {
            super("Database is down!");
        }
    }
    
    private static class NetworkUnreachableException extends Exception {
        NetworkUnreachableException() {
            super("Network is unreachable!");
        }
    }
    
    private static class DiskFullException extends Exception {
        DiskFullException() {
            super("Disk is full!");
        }
    }
    
    private static class OutOfMemoryException extends Exception {
        OutOfMemoryException() {
            super("Out of memory!");
        }
    }

    @SuppressWarnings("magicnumber")
    public void throwRandomException() throws Exception {
        if (isRandomException && random.nextInt(RANDOM_EXCEPTION_RATIO) == 0) {
            switch (random.nextInt(RANDOM_EXCEPTION_RATIO)) {
                case 1:
                    throw new DatabaseDownException();
                case 2:
                    throw new NetworkUnreachableException();
                case 3:
                    throw new DiskFullException();
                case 4:
                    throw new OutOfMemoryException();
                default:
                    throw new ServerDownException();
            }
        }
    }
    
}
