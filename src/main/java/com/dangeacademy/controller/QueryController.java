package com.dangeacademy.controller;


import com.dangeacademy.entity.User;
import com.dangeacademy.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class QueryController {

    private final EmailService emailService;


    @PostMapping("/query")
    public ResponseEntity<String> sendUserQueryToAdmin(@RequestBody String massage , Authentication authentication){
/*
        SecurityContextHolder.getContext().getAuthentication().getName();
*/
        String  email = authentication.getName();
         emailService.sendEmailToAdminOfStudentQuery(email,massage);

        return ResponseEntity.ok("Query send Successfully");

    }


}
