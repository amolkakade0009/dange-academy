package com.dangeacademy.service;

import com.dangeacademy.dto.UserRequestDto;
import com.dangeacademy.dto.UserResponseDto;
import com.dangeacademy.entity.User;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto dto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserRequestDto dto);

    void deleteUser(Long id);

    User register(User user);
}
