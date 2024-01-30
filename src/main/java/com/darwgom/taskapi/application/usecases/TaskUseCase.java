package com.darwgom.taskapi.application.usecases;

import com.darwgom.taskapi.application.dto.*;

import java.util.List;
import java.util.UUID;

public interface TaskUseCase {
    TaskDTO registerTask(TaskInputDTO taskInputDTO);
    List<TaskDTO> getAllTasks();
    TaskDTO getTaskById(UUID taskId);
    TaskDTO updateTask(UUID taskId, TaskInputDTO taskInputDTO);
    TaskDeleteDTO deleteTask(UUID taskId);
}
