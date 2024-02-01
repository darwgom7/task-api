package com.darwgom.taskapi.application.usecases;

import com.darwgom.taskapi.application.dto.LoginDTO;
import com.darwgom.taskapi.application.dto.TokenDTO;
import com.darwgom.taskapi.application.dto.UserDTO;
import com.darwgom.taskapi.application.dto.UserInputDTO;
import com.darwgom.taskapi.application.dto.TaskDeleteDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    UserDTO registerUser(UserInputDTO userInputDTO);
    String loginUser(LoginDTO loginDTO, HttpServletResponse response);
    List<UserDTO> getAllUsers();
    UserDTO getUserById(UUID userId);
    UserDTO updateUser(UUID userId, UserInputDTO userInputDTO);
    TaskDeleteDTO deleteUser(UUID userId);
    Boolean isLoggedIn(String token);
    UserDTO getCurrentUser(HttpServletRequest request);
}
