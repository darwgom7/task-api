package com.darwgom.taskapi.domain.repositories;

import com.darwgom.taskapi.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

}

