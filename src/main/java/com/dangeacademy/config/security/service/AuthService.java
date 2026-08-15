package com.dangeacademy.config.security.service;



import com.dangeacademy.config.security.dto.LoginRequest;
import com.dangeacademy.config.security.dto.SignupRequest;
import com.dangeacademy.enums.Role;
import com.dangeacademy.entity.User;
import com.dangeacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        //Encrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setMobileNumber(request.getMobileNumber());
        user.setRole(Role.STUDENT);


        userRepository.save(user);

        return "User Registered Successfully";
    }


    public Map<String, String> login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // Generate NEW session ID
        String sessionId = UUID.randomUUID().toString();

        // Replace previous session
        user.setSessionId(sessionId);

        userRepository.save(user);


        String token = jwtService.generateToken(user, sessionId);
        System.out.println("Session Id"+ "=" + sessionId );
        System.out.println("Token"+ "=" + token );


        Map<String,String> login_response=new HashMap<>();
        login_response.put("token",token);
        login_response.put("user_id",user.getId().toString());
        login_response.put("role",user.getRole().toString());
        login_response.put("email", user.getEmail());
        login_response.put("name",user.getName());
        login_response.put("mobileNumber",user.getMobileNumber());
        login_response.put("sessionId", sessionId);
/*
        login_response.put("isLogin",user.getIsLogin().toString());
*/
/*
        user.setIsLogin(true);
*/
        userRepository.save(user);

        return login_response;
    }




    public Map<String, String> logout(Long userId)
    {
        User  user=userRepository.findById(userId).orElseThrow();
        /*user.setIsLogin(false);*/
        user.setSessionId("");
        userRepository.save(user);
        Map<String,String> logout_response=new HashMap<>();
        logout_response.put("msg","Logout Successfull");
        return logout_response;
    }
}