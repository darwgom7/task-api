package com.darwgom.taskapi.infrastucture.controllers;

import com.darwgom.taskapi.application.dto.*;
import com.darwgom.taskapi.application.usecases.UserUseCase;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private UserUseCase userUseCase;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserInputDTO userInputDTO) {
        UserDTO newUser = userUseCase.registerUser(userInputDTO);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> loginUser(@RequestBody LoginDTO loginDTO) {
        TokenDTO tokenDTO = userUseCase.loginUser(loginDTO);
        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> usersDTO = userUseCase.getAllUsers();
        return new ResponseEntity<>(usersDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        UserDTO userDTO = userUseCase.getUserById(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable UUID userId, @RequestBody UserInputDTO userInputDTO) {
        UserDTO updatedUser = userUseCase.updateUser(userId, userInputDTO);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDeleteDTO> deleteUser(@PathVariable UUID id) {
        TaskDeleteDTO response = userUseCase.deleteUser(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
