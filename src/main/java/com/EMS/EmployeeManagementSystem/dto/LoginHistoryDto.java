package com.EMS.EmployeeManagementSystem.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginHistoryDto {
	private Long id;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    private LocalDateTime loginTime; 

}
