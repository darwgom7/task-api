package com.darwgom.taskapi.application.usecases;

import com.darwgom.taskapi.application.dto.LoginDTO;
import com.darwgom.taskapi.application.dto.TokenDTO;
import com.darwgom.taskapi.application.dto.UserDTO;
import com.darwgom.taskapi.application.dto.UserInputDTO;
import com.darwgom.taskapi.application.dto.TaskDeleteDTO;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    UserDTO registerUser(UserInputDTO userInputDTO);
    TokenDTO loginUser(LoginDTO loginDTO);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID userId);
    UserDTO updateUser(UUID userId, UserInputDTO userInputDTO);
    TaskDeleteDTO deleteUser(UUID userId);
}
