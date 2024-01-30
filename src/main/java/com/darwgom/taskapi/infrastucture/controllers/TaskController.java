package com.darwgom.taskapi.infrastucture.controllers;

import com.darwgom.taskapi.application.dto.*;
import com.darwgom.taskapi.application.usecases.TaskUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("api/tasks")
public class TaskController {
    @Autowired
    private TaskUseCase taskUseCase;

    @PostMapping
    public ResponseEntity<TaskDTO> registerTask(@RequestBody TaskInputDTO taskInputDTO) {
        TaskDTO newTask = taskUseCase.registerTask(taskInputDTO);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasksDTO = taskUseCase.getAllTasks();
        return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable UUID id) {
        TaskDTO taskDTO = taskUseCase.getTaskById(id);
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable UUID taskId, @RequestBody TaskInputDTO taskInputDTO) {
        TaskDTO updatedTask = taskUseCase.updateTask(taskId, taskInputDTO);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDeleteDTO> deleteTask(@PathVariable UUID id) {
        TaskDeleteDTO response = taskUseCase.deleteTask(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
