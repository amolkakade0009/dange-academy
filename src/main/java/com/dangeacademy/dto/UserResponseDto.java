package com.dangeacademy.dto;

import com.dangeacademy.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {


    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private Role role;


}
