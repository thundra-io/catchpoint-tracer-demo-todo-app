package com.catchpoint.tracing.demo.todo.repository;

import com.catchpoint.tracing.demo.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author sozal
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {

    List<TodoEntity> findByCompletedIsTrue();
    
    @Query("SELECT t FROM TodoEntity t WHERE t.createdAt < ?1")
    List<TodoEntity> findOldTodos(long timestamp);

}
