package com.catchpoint.tracing.demo.todo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Random;


@Configuration
public class RandomExceptionConfiguration {

    private static final short RANDOM_EXCEPTION_RATIO = 5;

    @Value("${spring.application.random-exception-enabled:false}")
    private boolean isRandomException;

    private final Random random = new Random();
    private static class ServerDownException extends Exception {
        public ServerDownException() {
            super("Server is down!");
        }
    }
    
    private static class DatabaseDownException extends Exception {
        public DatabaseDownException() {
            super("Database is down!");
        }
    }
    
    private static class NetworkUnreachableException extends Exception {
        public NetworkUnreachableException() {
            super("Network is unreachable!");
        }
    }
    
    private static class DiskFullException extends Exception {
        public DiskFullException() {
            super("Disk is full!");
        }
    }
    
    private static class OutOfMemoryException extends Exception {
        public OutOfMemoryException() {
            super("Out of memory!");
        }
    }                 
    
    public void throwRandomException() throws Exception {
        if (isRandomException && random.nextInt(RANDOM_EXCEPTION_RATIO) == 0) {
            switch (random.nextInt(RANDOM_EXCEPTION_RATIO)) {
                case 0:
                    throw new ServerDownException();
                case 1:
                    throw new DatabaseDownException();
                case 2:
                    throw new NetworkUnreachableException();
                case 3:
                    throw new DiskFullException();
                case 4:
                    throw new OutOfMemoryException();
            }
        }
    }
    
}
