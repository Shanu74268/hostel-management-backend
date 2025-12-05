package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.RegisterRequest;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.exception.InvalidTokenException;
import com.springsecurity.springsecurity.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class RegisterStudent {

    private static final Logger log = LoggerFactory.getLogger(RegisterStudent.class);

    @Autowired
    private StudentService studentService;

    // Self-registration (students signing up themselves)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        log.info("Creating User (Self-registration)");

        Student savedStudent = studentService.registerStudent(registerRequest, false); // isAdmin = false
        log.info("User Created Successfully");
        return ResponseEntity.status(201)
                .body("Registered successfully! Please check email to verify your account.");
    }

    // Admin creating a student
    @PostMapping("/admin/create-student")
    public ResponseEntity<?> createStudentByAdmin(@RequestBody RegisterRequest registerRequest){
        log.info("Creating User (Admin)");

        Student savedStudent = studentService.registerStudent(registerRequest, true); // isAdmin = true
        log.info("Student created by Admin successfully");
        return ResponseEntity.status(201)
                .body("Student created successfully by admin!");
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam("token") String token) {
        try {
            String result = studentService.verifyAccount(token);
            return ResponseEntity.status(200).body(result);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Something went wrong.");
        }
    }

    // Resend verification email
    @GetMapping("/resend-verification")
    public ResponseEntity<String> resendVerification(@RequestParam("email") String email){
        studentService.resendVerification(email);
        return ResponseEntity.status(201).body("Verification link sent! Please check your email.");
    }
}
