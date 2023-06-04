package com.catchpoint.tracing.demo.todo.repository;

import com.catchpoint.tracing.demo.todo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sozal
 */
public interface UserRepository extends JpaRepository<UserEntity, String> {
}
