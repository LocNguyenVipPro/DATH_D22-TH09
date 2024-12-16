package com.example.sm.minh.eshop.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileRequest {
    private Integer id;

    @NotNull
    @NotBlank(message = "Phone Number Is Required!")
    @Size(min = 10, max = 10, message = "Phone number must have exactly 10 characters")
    private String phoneNumber;

    private String bio;

    @Override
    public String toString() {
        return "UserProfileRequest{" + "id="
                + id + ", phoneNumber='"
                + phoneNumber + '\'' + ", bio='"
                + bio + '\'' + ", address='"
                + address + '\'' + ", gender="
                + gender + '}';
    }

    @NotNull
    @NotBlank(message = "address Is Required!")
    @Length(min = 6, message = "address must be at least 6 characters")
    @Length(max = 255, message = "address must not exceed 255 characters")
    private String address;

    private boolean gender;
}
