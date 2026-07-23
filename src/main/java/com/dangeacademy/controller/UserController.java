package com.dangeacademy.controller;

import com.dangeacademy.dto.UserRequestDto;
import com.dangeacademy.dto.UserResponseDto;
import com.dangeacademy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
/*
@CrossOrigin("*")
*/
public class UserController {

    private final UserService userService;


    // Get All Users
    @GetMapping()
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get User By Id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Update User
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser( @PathVariable Long id, @Valid @RequestBody UserRequestDto dto) {

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    // Delete User
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User Deleted Successfully");
    }

}
