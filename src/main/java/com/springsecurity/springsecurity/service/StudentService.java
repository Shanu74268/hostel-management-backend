package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.dto.RegisterRequest;
import com.springsecurity.springsecurity.dto.StudentProfileDto;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.exception.*;
import com.springsecurity.springsecurity.repository.StudentRepository;
import com.springsecurity.springsecurity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Slf4j
@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtServiceForEmailVerification jwtServiceForEmailVerification;

    @Autowired
    private EmailVerificationService emailVerificationService;

    private static final String UNIVERSITY_EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";

    public Student registerStudent(RegisterRequest registerRequest, boolean isAdmin) {

        // Validate Email
        if (!Pattern.matches(UNIVERSITY_EMAIL_REGEX, registerRequest.getEmail())) {
            throw new InvalidEmailException("Invalid University Email id: " + registerRequest.getEmail());
        }

        // Check if User already exists
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());
        }

        // Create User Entity
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole("STUDENT");

        // Set verified flag depending on who is creating
        user.setVerified(isAdmin);// true if admin, false if self-registration

        User savedUser = userRepository.save(user);

        // Create Student Entity
        Student student = new Student();
        student.setFullName(registerRequest.getFullName());
        student.setUser(savedUser);

        Student savedStudent = studentRepository.save(student);

        // Only generate verification email if NOT admin
        if (!isAdmin) {
            try {
                String token = jwtServiceForEmailVerification.generateTokenForAccountVerification(savedUser.getEmail());
                emailVerificationService.sendVerificationEmail(savedUser.getEmail(), token);
            } catch (Exception e) {
                log.error("Failed to send verification email: {}", e.getMessage());
                throw new EmailSendingException("Registration successful but failed to send verification email.");
            }
        }

        return savedStudent;
    }



    public String verifyAccount(String token) {
        String email;

        try {
            email = jwtServiceForEmailVerification.extractEmail(token);
        } catch (ExpiredJwtException ex) {
            // ✅ Extract email from expired token
            email = ex.getClaims().getSubject();
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (user.isVerified()) {
                    return "The verification link has expired or is invalid. Verified: true";
                } else {
                    return "The verification link has expired or is invalid. Verified: false";
                }
            }
            return "The verification link has expired or is invalid. Verified: false";
        } catch (Exception e) {
            return "The verification link has expired or is invalid. Verified: false";
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return "User not found for this token.";
        }

        User user = optionalUser.get();

        if (!jwtServiceForEmailVerification.isTokenValid(token, email)) {
            if (user.isVerified()) {
                return "The verification link has expired or is invalid. Verified: true";
            } else {
                return "The verification link has expired or is invalid. Verified: false";
            }
        }

        if (user.isVerified()) {
            return "User is already verified.";
        }

        user.setVerified(true);
        userRepository.save(user);
        return "Email verified successfully! Now you can log in.";
    }



    public void resendVerification(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        if (user.isVerified()) {
            throw new UserAlreadyVerifiedException("User is already verified.");
        }

        try {
            String token = jwtServiceForEmailVerification.generateTokenForAccountVerification(email);
            emailVerificationService.sendVerificationEmail(email, token);
        } catch (Exception e) {
            log.error("Failed to resend verification email: {}", e.getMessage());
            throw new EmailSendingException("Failed to resend verification email. Try again later.");
        }
    }
    private String extractEmailFromBrokenToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtServiceForEmailVerification.getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ Profile Logic
    public boolean isProfileComplete(String email) {
        return studentRepository.findByUser_Email(email)
                .map(Student::isProfileComplete)
                .orElse(false);
    }

    public String completeProfile(StudentProfileDto dto, String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new UserNotFoundException("Student not found for email: " + email));

        student.setFullName(dto.getFullName());
        student.setFatherName(dto.getFatherName());
        student.setMotherName(dto.getMotherName());
        student.setContact(dto.getContact());
        student.setBranch(dto.getBranch());
        student.setCourse(dto.getCourse());
        student.setYear(dto.getYear());
        student.setRollNumber(dto.getRollNumber());
        student.setRoomNumber(dto.getRoomNumber());
        student.setScholarType(dto.getScholarType());
        student.setGender(dto.getGender());
        student.setDob(dto.getDob());
        student.setAddress(dto.getAddress());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setProfilePicUrl(dto.getProfilePicUrl());
        student.setProfileComplete(true);

        studentRepository.save(student);
        return "Profile completed successfully!";
    }

    public StudentProfileDto getProfileByEmail(String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found for email: " + email));

        StudentProfileDto dto = new StudentProfileDto();
        dto.setFullName(student.getFullName());
        dto.setEmail(student.getUser().getEmail());
        dto.setFatherName(student.getFatherName());
        dto.setMotherName(student.getMotherName());
        dto.setContact(student.getContact());
        dto.setBranch(student.getBranch());
        dto.setCourse(student.getCourse());
        dto.setYear(student.getYear());
        dto.setRollNumber(student.getRollNumber());
        dto.setRoomNumber(student.getRoomNumber());
        dto.setScholarType(student.getScholarType());
        dto.setGender(student.getGender());
        dto.setDob(student.getDob());
        dto.setAddress(student.getAddress());
        dto.setCity(student.getCity());
        dto.setState(student.getState());
        dto.setProfilePicUrl(student.getProfilePicUrl());
        return dto;
    }
    public Long getStudentIdByEmail(String email) {
        return studentRepository.findByUser_Email(email)
                .map(Student::getStudentId)
                .orElseThrow(() -> new RuntimeException("Student not found for email: " + email));
    }


}
