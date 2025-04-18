package com.example.locnguyen.techzone.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.example.locnguyen.techzone.models.Role;
import com.example.locnguyen.techzone.validators.annotations.UserNameUnique;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@UserNameUnique(emailField = "userName", idField = "id")
public class UserRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "User Name Is Required")
    @Length(min = 6, message = "Username must be at least 6 characters")
    @Length(max = 100, message = "Username must not exceed 100 characters")
    @Email(message = "Invalid email address")
    private String userName;

    @NotBlank(message = "Password Is Required")
    @Length(min = 6, message = "password must be at least 6 characters")
    @Length(max = 50, message = "password must not exceed 50 characters")
    private String password;

    @NotBlank(message = "First Name Is Required")
    @Length(min = 2, message = "firstName must be at least 2 characters")
    @Length(max = 50, message = "firstName must not exceed 100 characters")
    private String firstName;

    @NotBlank(message = "Last Name Is Required")
    @Length(min = 2, message = "lastName must be at least 2 characters")
    @Length(max = 50, message = "lastName must not exceed 100 characters")
    private String lastName;

    private Boolean isActive;
    private Role role;

    public UserRequest() {}
}
