package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.UserRequest;
import com.springsecurity.springsecurity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRequest request) {
        Map<String, String> response = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response); // ✅ Return Map wrapped in ResponseEntity
    }
}