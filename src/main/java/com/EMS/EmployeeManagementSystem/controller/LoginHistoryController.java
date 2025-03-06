package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.dto.LoginHistoryDto;
import com.EMS.EmployeeManagementSystem.services.LoginHistoryService;
import com.EMS.EmployeeManagementSystem.services.JwtService;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/login-history")
public class LoginHistoryController {

    @Autowired
    private LoginHistoryService loginHistoryService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/record/{userId}")
    public ResponseEntity<String> recordLogin(@PathVariable Long userId) {
        try {
            loginHistoryService.recordLogin(userId);
            return ResponseEntity.ok("Login recorded successfully for user ID: " + userId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with ID: " + userId);
        }
    }

    @GetMapping("/loginHistory")
    public ResponseEntity<List<LoginHistoryDto>> getLoginHistory() {
        List<LoginHistoryDto> history = loginHistoryService.getAllLoginHistory();
        return ResponseEntity.ok(history);
    }
//    @GetMapping("/loginHistory")
//    public ResponseEntity<Page<LoginHistoryDto>> getLoginHistory(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size); // Creating pageable object
//        Page<LoginHistoryDto> history = loginHistoryService.getAllLoginHistory(pageable);
//        return ResponseEntity.ok(history);
//    }
}