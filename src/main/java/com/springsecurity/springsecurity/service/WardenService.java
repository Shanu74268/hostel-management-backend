package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.dto.WardenCreateDto;
import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.entity.warden.Warden;
import com.springsecurity.springsecurity.repository.UserRepository;
import com.springsecurity.springsecurity.repository.WardenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class WardenService {

    @Autowired
    private WardenRepository wardenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ CREATE WARDEN
    public void createWarden(WardenCreateDto dto, boolean isverified) {

        // ✅ Email validation
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // ✅ Contact validation
        if (wardenRepository.existsByContactNumber(dto.getContactNumber())) {
            throw new RuntimeException("Contact number already exists");
        }

        // ✅ Create User
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole("WARDEN");
        user.setBlocked(false);
        user.setDeleted(false);
        user.setVerified(isverified);

        userRepository.save(user);

        // ✅ Create Warden
        Warden warden = new Warden();
        warden.setFullName(dto.getFullName());
        warden.setContactNumber(dto.getContactNumber());
        warden.setAge(dto.getAge());
        warden.setHostelName(dto.getHostelName());
        warden.setAddress(dto.getAddress());
        warden.setCity(dto.getCity());
        warden.setState(dto.getState());
        warden.setZipCode(dto.getZipCode());
        warden.setNationality(dto.getNationality());
        warden.setUser(user);

        wardenRepository.save(warden);
    }

    // ✅ FETCH WARDEN BY USER ID
    public Warden getWardenByUserId(Long userId) {
        return wardenRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Warden not found with user id: " + userId));
    }
}
