package com.demo.demo.dto;

import java.util.Objects;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequestDto {

    @NotBlank(message = "Username field should not be empty")
    String username;

    @NotBlank(message = "Password field should not be empty")
    @Size(min = 6, max = 12, message = "password should be between 6 and 12 characters")
    String password;

    @NotBlank(message = "Email field should not be empty")
    @Email(message = "Invalid email format")
    String email;

    public @NotBlank(message = "Username field should not be empty") String getUsername() {
        return username;
    }

    public void setUsername(@NotBlank(message = "Username field should not be empty") String username) {
        this.username = username;
    }

    public @NotBlank(message = "Password field should not be empty") @Size(min = 6, max = 12, message = "password should be between 6 and 12 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password field should not be empty") @Size(min = 6, max = 12, message = "password should be between 6 and 12 characters") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Email field should not be empty") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email field should not be empty") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SignUpRequestDto that = (SignUpRequestDto) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(email);
        return result;
    }

    @Override
    public String toString() {
        return "SignUpRequestDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
