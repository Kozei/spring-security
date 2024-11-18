package com.demo.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "Username field should not be empty")
    String username;

    @NotBlank(message = "Password field should not be empty")
    @Size(min = 6, max = 12, message = "password should be between 6 and 12 characters")
    String password;

    @NotBlank(message = "Email field should not be empty")
    @Email(message = "Invalid email format")
    String email;
}
