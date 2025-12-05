package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.ComplaintRequestDto;
import com.springsecurity.springsecurity.entity.student.Complaint;
import com.springsecurity.springsecurity.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaint")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/add")
    public ResponseEntity<String> addComplaint(@RequestBody ComplaintRequestDto dto,
                                               Authentication authentication) {
        String email = authentication.getName();
        String msg = complaintService.addComplaint(dto, email);
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Complaint>> getMyComplaints(Authentication authentication) {
        String email = authentication.getName();
        List<Complaint> list = complaintService.getMyComplaints(email);
        return ResponseEntity.ok(list);
    }
}
