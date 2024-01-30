package com.darwgom.taskapi.application.usecases.impl;

import com.darwgom.taskapi.application.dto.*;
import com.darwgom.taskapi.application.usecases.TaskUseCase;
import com.darwgom.taskapi.domain.entities.Task;
import com.darwgom.taskapi.domain.entities.User;
import com.darwgom.taskapi.domain.exceptions.EntityNotFoundException;
import com.darwgom.taskapi.domain.repositories.TaskRepository;
import com.darwgom.taskapi.domain.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Service
@EnableCaching
public class TaskUseCaseImpl implements TaskUseCase {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public TaskDTO registerTask(TaskInputDTO taskInputDTO) {
        String currentUserName = validateUserAuthenticated();

        User user = userRepository.findByEmail(currentUserName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUserName));

        Task task = modelMapper.map(taskInputDTO, Task.class);
        task.setUser(user);

        Task savedTask = taskRepository.save(task);

        return modelMapper.map(savedTask, TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(task -> {
            return modelMapper.map(task, TaskDTO.class);
        }).collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        return modelMapper.map(task, TaskDTO.class);
    }

    @Override
    public TaskDTO updateTask(UUID taskId, TaskInputDTO taskInputDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        task.setTitle(taskInputDTO.getTitle());
        task.setDescription(taskInputDTO.getDescription());
        task.setCompleted(taskInputDTO.getCompleted());
        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    @Override
    public TaskDeleteDTO deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        taskRepository.delete(task);
        return new TaskDeleteDTO("Task deleted successfully");
    }

    public String validateUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user!");
        }

        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        } else {
            throw new IllegalStateException("The authentication principal is not of a recognized type!");
        }
    }
}
