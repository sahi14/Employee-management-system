package com.EMS.EmployeeManagementSystem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.EMS.EmployeeManagementSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

 // Native query to get active users
    @Query(value = "SELECT * FROM users WHERE status = TRUE", nativeQuery = true)
    Optional<User> findActiveUsers();
    
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET first_name = :firstName, last_name = :lastName, email = :email, status = :status WHERE id = :id", nativeQuery = true)
    void updateUserDetails(
    		@Param("id") Long id,
    		@Param("firstName") String firstName,
    		@Param("lastName") String lastName,
    		@Param("email") String email,
    		@Param("status") boolean status
	    );
    // Native query to fetch users based on their role
    @Query(value = "SELECT u.* FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE r.role_name = :roleName", nativeQuery = true)
    List<User> findByRole(@Param("roleName") String roleName);

    // Use a native query to find users with status 'false' (pending approval)
    @Query(value = "SELECT * FROM users WHERE status IS FALSE", nativeQuery = true)
    List<User> findPendingApprovalUsers();

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM users WHERE email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
    Optional<User> findById(@Param("id") Long id);

}
