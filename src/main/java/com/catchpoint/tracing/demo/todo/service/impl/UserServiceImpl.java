package com.catchpoint.tracing.demo.todo.service.impl;

import com.catchpoint.tracing.demo.todo.entity.UserEntity;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.repository.UserRepository;
import com.catchpoint.tracing.demo.todo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return new User(entity.getEmail(), entity.getFirstName(),
                entity.getLastName(), entity.getCreatedAt());
    }

    private UserEntity getUserEntity(String email) {
        return repository.findById(email).orElse(null);
    }


    @Override
    public void deleteUserEntity(String email) {
        Optional<UserEntity> entityOptional = repository.findById(email);
        entityOptional.ifPresent(repository::delete);
    }

}
