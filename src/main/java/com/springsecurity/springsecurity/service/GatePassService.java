package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.dto.GatePassRequestDto;
import com.springsecurity.springsecurity.entity.student.GatePass;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.repository.GatePassRepository;
import com.springsecurity.springsecurity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class GatePassService {

    @Autowired
    private GatePassRepository gatePassRepository;

    @Autowired
    private StudentRepository studentRepository;

    public String applyGatePass(GatePassRequestDto dto, Principal principal) {
        String email = principal.getName();

        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        GatePass gatePass = new GatePass();
        gatePass.setLeaveType(dto.getLeaveType());
        gatePass.setDate(dto.getDate());
        gatePass.setTimeFrom(dto.getTimeFrom());
        gatePass.setTimeTo(dto.getTimeTo());
        gatePass.setReason(dto.getReason());
        gatePass.setStatus("PENDING");
        gatePass.setStudent(student);

        gatePassRepository.save(gatePass);

        return "Gate pass applied successfully!";
    }

    public List<GatePass> getMyGatePasses(Principal principal) {
        String email = principal.getName();
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return gatePassRepository.findByStudent(student);
    }

    public List<GatePass> getAllGatePasses() {
        return gatePassRepository.findAll();
    }
}
