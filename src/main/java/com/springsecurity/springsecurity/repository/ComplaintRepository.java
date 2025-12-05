package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.student.Complaint;
import com.springsecurity.springsecurity.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStudent(Student student);
}
