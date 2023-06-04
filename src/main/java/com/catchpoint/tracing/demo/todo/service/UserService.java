package com.catchpoint.tracing.demo.todo.service;

import com.catchpoint.tracing.demo.todo.model.User;

/**
 * @author sozal
 */
public interface UserService {

    User getUser(String email);
    
}
