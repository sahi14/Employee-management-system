package com.EMS.EmployeeManagementSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query(value = "SELECT * FROM roles WHERE role_name = :roleName", nativeQuery = true)
    Optional<Role> findByRoleName(String roleName);



}
