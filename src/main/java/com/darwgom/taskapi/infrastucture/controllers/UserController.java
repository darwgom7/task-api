package com.darwgom.taskapi.infrastucture.controllers;

import com.darwgom.taskapi.application.dto.*;
import com.darwgom.taskapi.application.usecases.UserUseCase;
import com.darwgom.taskapi.domain.exceptions.AuthException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            String successMessage = userUseCase.loginUser(loginDTO, response);
            return ResponseEntity.ok(successMessage);
        } catch (AuthException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/isloggedin")
    public ResponseEntity<Boolean> checkLoggedIn(@CookieValue(name = "access_token", required = false) String token) {
        boolean isLoggedIn = userUseCase.isLoggedIn(token);
        return ResponseEntity.ok(isLoggedIn);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        try {
            UserDTO userDTO = userUseCase.getCurrentUser(request);
            return ResponseEntity.ok(userDTO);
        } catch (AuthException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

}
