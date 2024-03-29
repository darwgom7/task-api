package com.darwgom.taskapi.application.usecases.impl;

import com.darwgom.taskapi.application.dto.*;
import com.darwgom.taskapi.application.dto.TaskDeleteDTO;
import com.darwgom.taskapi.application.usecases.UserUseCase;
import com.darwgom.taskapi.domain.entities.User;
import com.darwgom.taskapi.domain.exceptions.AuthException;
import com.darwgom.taskapi.domain.exceptions.EmailAlreadyRegisteredException;
import com.darwgom.taskapi.domain.exceptions.EntityNotFoundException;
import com.darwgom.taskapi.domain.repositories.UserRepository;
import com.darwgom.taskapi.infrastucture.security.JwtTokenProvider;
import com.darwgom.taskapi.utilities.EmailFormatValidator;
import com.darwgom.taskapi.utilities.PasswordValidator;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserUseCaseImpl implements UserUseCase {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDTO registerUser(UserInputDTO userInputDTO) {

        User user = modelMapper.map(userInputDTO, User.class);
        PasswordValidator.validatePassword(userInputDTO.getPassword());
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));

        EmailFormatValidator.validEmailFormat(userInputDTO.getEmail());

        if (userRepository.findByEmail(userInputDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException(userInputDTO.getEmail());
        }

        User savedUser = userRepository.save(user);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public String loginUser(LoginDTO loginDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtTokenProvider.createToken(authentication.getName());

            User user = userRepository.findByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + loginDTO.getEmail()));

            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            Cookie cookie = new Cookie("access_token", jwt);
            cookie.setHttpOnly(Boolean.TRUE);
            cookie.setPath("/");
            cookie.setSecure(Boolean.TRUE);
            response.addCookie(cookie);

            return "Login successful";

        } catch (AuthenticationException e) {
            throw new AuthException("Invalid username or password");
        }
    }


    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(user -> {
            return modelMapper.map(user, UserDTO.class);
        }).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(UUID userId, UserInputDTO userInputDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setName(userInputDTO.getName());
        user.setEmail(userInputDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);
    }

    @Override
    public TaskDeleteDTO deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        userRepository.delete(user);
        return new TaskDeleteDTO("User deleted successfully");
    }

    @Override
    public Boolean isLoggedIn(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProvider.getKey())
                    .build()
                    .parseClaimsJws(token);
            return Boolean.TRUE;
        } catch (JwtException e) {
            return Boolean.FALSE;
        }
    }

    @Override
    public UserDTO getCurrentUser(HttpServletRequest request) {
        String jwt = extractJwtFromCookie(request);
        if (jwt == null || jwt.isEmpty()) {
            throw new UsernameNotFoundException("No JWT token found in request cookies");
        }
        String email = jwtTokenProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("User not found with email: " + email));

        return modelMapper.map(user, UserDTO.class);
    }

    private String extractJwtFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
