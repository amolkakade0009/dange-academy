package com.dangeacademy.dto;

import com.dangeacademy.entity.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequestDto {


    @NotBlank(message = "Full Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Invalid Mobile Number"
    )
    private String mobileNumber;

    @Size(min = 8, max = 20)
    private String password;

    private Role role;

}
