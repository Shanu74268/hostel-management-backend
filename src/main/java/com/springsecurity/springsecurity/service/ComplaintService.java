package com.springsecurity.springsecurity.service;

import com.springsecurity.springsecurity.dto.ComplaintRequestDto;
import com.springsecurity.springsecurity.entity.student.Complaint;
import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.repository.ComplaintRepository;
import com.springsecurity.springsecurity.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private StudentRepository studentRepository;

    // ✅ Add a new complaint
    public String addComplaint(ComplaintRequestDto dto, String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Complaint complaint = new Complaint();
        complaint.setComplaintType(dto.getComplaintType());
        complaint.setDate(dto.getDate());
        complaint.setDescription(dto.getDescription());
        complaint.setStudent(student);
        complaint.setStatus("Pending");
        complaint.setResponse("Awaiting review");

        complaintRepository.save(complaint);
        return "Complaint submitted successfully";
    }

    // ✅ Fetch all complaints by the logged-in student
    public List<Complaint> getMyComplaints(String email) {
        Student student = studentRepository.findByUser_Email(email)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return complaintRepository.findByStudent(student);
    }
}
