package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.LoginHistoryDto;
import com.EMS.EmployeeManagementSystem.entity.LoginHistory;
import com.EMS.EmployeeManagementSystem.entity.User;
public class LoginHistoryMapper {
    public static LoginHistoryDto mapToLoginHistoryDto(LoginHistory loginHistory) {
        return new LoginHistoryDto(
                loginHistory.getId(),
                loginHistory.getUser().getId(),
                loginHistory.getLoginTime()
        );
    }
    public static LoginHistory mapToLoginHistory(LoginHistoryDto loginHistoryDto, User user) {
        return new LoginHistory(
                loginHistoryDto.getId(),
                user,
                loginHistoryDto.getLoginTime()
        );
    }
}

