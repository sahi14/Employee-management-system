package com.EMS.EmployeeManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
	
	 @Query(value = "SELECT * FROM login_history WHERE user_id = :userId ORDER BY login_time DESC", nativeQuery = true)
	 List<LoginHistory> findByUserId(Long userId);
	
	

}
