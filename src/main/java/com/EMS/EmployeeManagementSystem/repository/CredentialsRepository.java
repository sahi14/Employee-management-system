package com.EMS.EmployeeManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.Credentials;

@Repository
public interface CredentialsRepository extends JpaRepository<Credentials, Long> {

    @Query(value = "SELECT * FROM credentials WHERE email = :email LIMIT 1", nativeQuery = true)
        Optional<Credentials> findByEmail(String email);

}
