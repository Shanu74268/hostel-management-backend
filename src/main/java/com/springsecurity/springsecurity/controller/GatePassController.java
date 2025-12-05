package com.springsecurity.springsecurity.controller;

import com.springsecurity.springsecurity.dto.GatePassRequestDto;
import com.springsecurity.springsecurity.entity.student.GatePass;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.repository.StudentRepository;
import com.springsecurity.springsecurity.service.GatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("gatepass")
public class GatePassController {
    @Autowired
    private GatePassService gatePassService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyGatePass(@RequestBody GatePassRequestDto dto, Principal principal) {
        System.out.println("✅ applyGatePass() reached");
        String message = gatePassService.applyGatePass(dto, principal);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/my")
    public ResponseEntity<List<GatePass>> getMyGatePasses(Principal principal) {
        return ResponseEntity.ok(gatePassService.getMyGatePasses(principal));
    }

    @GetMapping("/all")
    public ResponseEntity<List<GatePass>> getAllGatePasses() {
        return ResponseEntity.ok(gatePassService.getAllGatePasses());
    }
}