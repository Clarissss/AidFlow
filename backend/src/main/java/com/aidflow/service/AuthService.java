package com.aidflow.service;

import com.aidflow.dto.LoginRequest;
import com.aidflow.dto.LoginResponse;
import com.aidflow.entity.User;
import com.aidflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        // Verify role (handle both "field-worker" and "FIELD_WORKER" formats)
        String requestRole = request.getRole().toUpperCase().replace("-", "_");
        String userRole = user.getRole().name();
        
        if (!userRole.equals(requestRole)) {
            throw new RuntimeException("Invalid role");
        }
        
        if (!user.getActive()) {
            throw new RuntimeException("User is inactive");
        }
        
        String token = "mock-token-" + user.getId();
        
        return new LoginResponse(
            user.getId(),
            user.getUsername(),
            user.getName(),
            user.getRole().name(),
            token
        );
    }
    
    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
