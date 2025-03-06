package com.EMS.EmployeeManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.FinancialDocs;

@Repository
public interface FinancialDocsRepository extends JpaRepository<FinancialDocs, Long> {
	
    @Query(value = "SELECT * FROM documents WHERE user_id = :userId", nativeQuery = true)
    List<FinancialDocs> findByUserId(Long userId);



}
