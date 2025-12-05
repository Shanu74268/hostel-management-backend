package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.configuration.CustomUserDetails;
import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.exception.InvalidCredentialsException;
import com.springsecurity.springsecurity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    // LOGIN remains the same
    public Map<String, String> login(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            if (authentication.isAuthenticated()) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                User user = userDetails.getUser();

                if (!user.isVerified()) {
                    throw new InvalidCredentialsException("You must verify your account before logging in.");
                }

                String token = jwtService.generateToken(user.getEmail(), user.getRole());
                return Map.of("token", token, "role", "ROLE_" + user.getRole());
            } else {
                throw new InvalidCredentialsException("Invalid username or password.");
            }

        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid username or password.");
        } catch (Exception e) {
            throw new InvalidCredentialsException("Login failed: " + e.getMessage());
        }
    }

    // BLOCK USER (by ID)
    public void blockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(true);
        userRepository.save(user);
    }

    // UNBLOCK USER (by ID)
    public void unblockUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBlocked(false);
        userRepository.save(user);
    }

    // SOFT DELETE USER (by ID)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);  // Mark as deleted instead of removing
        userRepository.save(user);
    }

    // UNDELETE USER (by ID)
    public void undeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(false);
        userRepository.save(user);
    }
}
