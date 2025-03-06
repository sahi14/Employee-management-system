package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.dto.LoginHistoryDto;
import com.EMS.EmployeeManagementSystem.entity.LoginHistory;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.mapper.LoginHistoryMapper;
import com.EMS.EmployeeManagementSystem.repository.LoginHistoryRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class LoginHistoryService {

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;
    @Autowired
    private UserRepository userRepository;

    //Record a successful login
    public void recordLogin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LoginHistory loginHistory = LoginHistory.builder()
                .user(user)
                .loginTime(LocalDateTime.now())
                .build();

        loginHistoryRepository.save(loginHistory);
    }
    public List<LoginHistoryDto> getLoginHistoryForUser(Long userId) {
        List<LoginHistory> loginHistoryList = loginHistoryRepository.findByUserId(userId);

        return loginHistoryList.stream()
                .map(LoginHistoryMapper::mapToLoginHistoryDto)
                .collect(Collectors.toList());
    }

    public List<LoginHistoryDto> getAllLoginHistory() {
        List<LoginHistory> historyList = loginHistoryRepository.findAll();
        return historyList.stream()
                .map(history -> new LoginHistoryDto(history.getId(), history.getUser().getId(), history.getLoginTime()))
                .collect(Collectors.toList());
    }

}
