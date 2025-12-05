package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.student.GatePass;
import com.springsecurity.springsecurity.entity.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatePassRepository extends JpaRepository<GatePass,Long> {
    List<GatePass> findByStudent(Student student);
}
