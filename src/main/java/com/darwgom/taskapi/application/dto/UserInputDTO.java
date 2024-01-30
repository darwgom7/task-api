package com.darwgom.taskapi.application.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO {
    private String name;
    @Email
    private String email;
    private String password;
}
