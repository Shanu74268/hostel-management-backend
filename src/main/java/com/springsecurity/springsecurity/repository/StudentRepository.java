package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.student.Student;
import com.springsecurity.springsecurity.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByUser_Email(String email);
    Optional<Student> findByUser(User user);

}
