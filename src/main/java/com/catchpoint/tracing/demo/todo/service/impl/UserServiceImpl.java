package com.catchpoint.tracing.demo.todo.service.impl;

import com.catchpoint.tracing.demo.todo.entity.UserEntity;
import com.catchpoint.tracing.demo.todo.model.User;
import com.catchpoint.tracing.demo.todo.repository.UserRepository;
import com.catchpoint.tracing.demo.todo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author sozal
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final Random random = new Random();

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

    public UserEntity getUserEntity(String email) {
        // Inject random delay
        if (random.nextInt() % 2 == 0) {
            try {
                Thread.sleep(1000 + random.nextInt(500));
            } catch (InterruptedException e) {
            }
        }
        return repository.findById(email).orElse(null);
    }

}
