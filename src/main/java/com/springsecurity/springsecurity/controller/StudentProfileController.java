package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.StudentProfileDto;
import com.springsecurity.springsecurity.service.StudentService;
import com.springsecurity.springsecurity.service.s3service.S3ProfilePicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173") // React app port
@RestController
@RequestMapping("/student/profile")
public class StudentProfileController {

    @Autowired
    private S3ProfilePicService s3ProfilePicService;

    @Autowired
    private StudentService studentService;

    public StudentProfileController(S3ProfilePicService s3ProfilePicService,
                                    StudentService studentService) {
        this.s3ProfilePicService = s3ProfilePicService;
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
    @GetMapping
    public ResponseEntity<StudentProfileDto> getProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String email = authentication.getName();
        StudentProfileDto profile = studentService.getProfileByEmail(email);

        // 🔹 Debug log
        System.out.println("Fetched Profile URL: " + profile.getProfilePicUrl());

        return ResponseEntity.ok(profile);
    }


    // ✅ Complete or update profile
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
    @PostMapping("/upload-pic")
    public ResponseEntity<?> uploadProfilePic(@RequestParam("file") MultipartFile file,
                                              Principal principal) {
        try {
            System.out.println("🚀 Upload started");

            if (principal == null) {
                System.out.println("❌ Principal is null");
                return ResponseEntity.status(401).body("Unauthorized");
            }

            System.out.println("👤 Principal name: " + principal.getName());

            if (file == null || file.isEmpty()) {
                System.out.println("❌ File is null or empty");
                return ResponseEntity.badRequest().body("File is empty");
            }

            System.out.println("📦 File received: " + file.getOriginalFilename() + ", size=" + file.getSize());

            Long studentId = studentService.getStudentIdByEmail(principal.getName());
            if (studentId == null) {
                System.out.println("❌ Student ID not found for email: " + principal.getName());
                return ResponseEntity.badRequest().body("Student not found");
            }

            System.out.println("🆔 Student ID: " + studentId);

            String url = s3ProfilePicService.uploadProfilePic(file, studentId);

            System.out.println("✅ Upload success, URL: " + url);
            return ResponseEntity.ok(Map.of("url", url));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ IOException: " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ General Exception: " + e.getMessage());
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }

}
