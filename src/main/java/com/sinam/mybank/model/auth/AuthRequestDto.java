package com.sinam.mybank.model.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class AuthRequestDto {

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 3, message = "Password must be at least 3 characters")
    private String password;

    public AuthRequestDto() {
    }

    public AuthRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
