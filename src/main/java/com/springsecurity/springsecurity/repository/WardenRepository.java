package com.springsecurity.springsecurity.repository;

import com.springsecurity.springsecurity.entity.user.User;
import com.springsecurity.springsecurity.entity.warden.Warden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WardenRepository extends JpaRepository<Warden, Long> {

    // 🔹 Find by linked User object
    Optional<Warden> findByUser(User user);

    // 🔹 Check if contact number already used
    boolean existsByContactNumber(String contactNumber);

    // 🔹 Find warden by userId directly
    Optional<Warden> findByUser_UserId(Long userId);
}
