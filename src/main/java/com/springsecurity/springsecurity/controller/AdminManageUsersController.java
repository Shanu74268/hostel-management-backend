package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.UserListDto;
import com.springsecurity.springsecurity.dto.WardenCreateDto;
import com.springsecurity.springsecurity.service.ManageUsersService;
import com.springsecurity.springsecurity.service.WardenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/manage-users")
public class AdminManageUsersController {

    @Autowired
    WardenService wardenService;

    @Autowired
    private ManageUsersService manageUsersService;

    // Fetch All Users
    @GetMapping("/all")
    public ResponseEntity<List<UserListDto>> getAllUsers() {
        return ResponseEntity.ok(manageUsersService.getAllUsers());
    }

    // Block User by ID
    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        manageUsersService.blockUser(id);
        return ResponseEntity.ok("User blocked successfully");
    }

    // Unblock User by ID
    @PutMapping("/unblock/{id}")
    public ResponseEntity<?> unblockUser(@PathVariable Long id) {
        manageUsersService.unblockUser(id);
        return ResponseEntity.ok("User unblocked successfully");
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {

        System.out.println("======= DELETE API HIT =======");
        System.out.println("UserId received in controller: " + userId);

        try {
            manageUsersService.deleteUser(userId);
            System.out.println("======= DELETE SUCCESS =======");
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {

            System.out.println("======= DELETE FAILED =======");
            System.out.println("Exception message: " + e.getMessage());
            System.out.println("Exception class: " + e.getClass().getName());
            e.printStackTrace();   // FULL STACKTRACE in server console

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());   // SHOW ERROR TO FRONTEND
        }
    }


    // 🔹 Restore (undelete) a user
    @PutMapping("/restore/{userId}")
    public ResponseEntity<String> undeleteUser(@PathVariable Long userId) {
        manageUsersService.undeleteUser(userId);
        return ResponseEntity.ok("User restored successfully");
    }


    // Create New Warden
    @PostMapping("/create-warden")
    public ResponseEntity<String> createWarden(@RequestBody WardenCreateDto request) {
        try {
            wardenService.createWarden(request,true);
            return ResponseEntity.ok("Warden created successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

}
