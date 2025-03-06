package com.EMS.EmployeeManagementSystem.repository;

import com.EMS.EmployeeManagementSystem.entity.RefreshToken;
import com.EMS.EmployeeManagementSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    @Modifying
    @Transactional
    void deleteByUser(User user);
}