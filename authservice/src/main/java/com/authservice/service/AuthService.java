package com.authservice.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authservice.dto.APIResponse;
import com.authservice.dto.UserDto;
import com.authservice.entity.User;
import com.authservice.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public APIResponse<String> register(UserDto userDto) {
        APIResponse<String> response = new APIResponse<>();

        // Check whether username exists
        if (userRepository.existsByUsername(userDto.getUsername())) {
            response.setMessage("Registration failed");
            response.setStatus(500);
            response.setData("User with this username already exists");
            return response;
        }

        // Check whether Email exists
        if (userRepository.existsByEmail(userDto.getEmail())) {
            response.setMessage("Registration failed");
            response.setStatus(500);
            response.setData("User with this Email Id already exists");
            return response;
        }

        // Encode the password before saving it to the database
        String encode = passwordEncoder.encode(userDto.getPassword());
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        user.setPassword(encode);
        String role="ROLE_"+userDto.getRole().toUpperCase();
        user.setRole(role); // Ensure role is prefixed with "ROLE_"
        User savedUser = userRepository.save(user);

        response.setMessage("Registration Completed");
        response.setStatus(201);
        response.setData("User has been registered");
        return response;
    }
}