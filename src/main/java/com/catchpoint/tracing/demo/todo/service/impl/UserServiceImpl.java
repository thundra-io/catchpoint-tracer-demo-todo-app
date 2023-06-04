package com.catchpoint.tracing.demo.todo.service.impl;

import com.catchpoint.tracing.demo.todo.entity.UserEntity;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.repository.UserRepository;
import com.catchpoint.tracing.demo.todo.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author sozal
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User getUser(String email) {
        UserEntity entity = getUserEntity(email);
        if (entity == null) {
            return null;
        }
        return new User(entity.getEmail(), entity.getFirstName(), entity.getLastName(), entity.getCreatedAt());
    }

    private UserEntity getUserEntity(String email) {
        return repository.findById(email).orElse(null);
    }

}
