package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.dto.UserListDto;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.entity.warden.Warden;
import com.springsecurity.springsecurity.repository.StudentRepository;
import com.springsecurity.springsecurity.repository.UserRepository;
import com.springsecurity.springsecurity.repository.WardenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManageUsersService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private WardenRepository wardenRepository;

    // Fetch All Users (Students + Wardens)
    public List<UserListDto> getAllUsers() {

        List<UserListDto> result = new ArrayList<>();

        // Fetch all students
        List<Student> students = studentRepository.findAll();
        for (Student s : students) {
            result.add(
                    new UserListDto(
                            s.getUser().getUserId(),        // USER ID
                            s.getFullName(),
                            s.getUser().getEmail(),
                            "STUDENT",
                            s.getUser().isBlocked(),
                            s.getUser().isDeleted()         // Include deleted flag
                    )
            );
        }

        // Fetch all wardens
        List<Warden> wardens = wardenRepository.findAll();
        for (Warden w : wardens) {
            result.add(
                    new UserListDto(
                            w.getUser().getUserId(),
                            w.getFullName(),
                            w.getUser().getEmail(),
                            "WARDEN",
                            w.getUser().isBlocked(),
                            w.getUser().isDeleted()         // Include deleted flag
                    )
            );
        }

        return result;
    }

    // Block User by ID
    public void blockUser(Long id) {
        userService.blockUser(id);
    }

    // Unblock User by ID
    public void unblockUser(Long id) {
        userService.unblockUser(id);
    }
    @Transactional
    public void deleteUser(Long userId) {
        System.out.println("Deleting user with ID: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }



    // Undelete a user by ID
    @Transactional
    public void undeleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setDeleted(false); // restore the user
        userRepository.save(user);
    }

}
