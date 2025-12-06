package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.StudentProfileDto;
import com.springsecurity.springsecurity.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173") // React app port
@RestController
@RequestMapping("/student/profile")
public class StudentProfileController {

    @Autowired
    private StudentService studentService;

    public StudentProfileController(StudentService studentService) {
        this.studentService = studentService;
    }

    // ✅ Check if profile is complete
    @GetMapping("/status")
    public ResponseEntity<Boolean> isProfileComplete(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(false);
        }
        String email = authentication.getName();
        boolean status = studentService.isProfileComplete(email);
        return ResponseEntity.ok(status);
    }

    // ✅ Get profile details
    @GetMapping
    public ResponseEntity<StudentProfileDto> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }

        String email = authentication.getName();
        StudentProfileDto profile = studentService.getProfileByEmail(email);

        // 🔹 Debug log
        System.out.println("Fetched Profile (no S3): " + profile);

        return ResponseEntity.ok(profile);
    }

    // ✅ Complete or update profile (NON-S3)
    @PostMapping("/complete")
    public ResponseEntity<String> completeProfile(@RequestBody StudentProfileDto dto,
                                                  Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String email = authentication.getName();
        String msg = studentService.completeProfile(dto, email);
        return ResponseEntity.ok(msg);
    }
}
