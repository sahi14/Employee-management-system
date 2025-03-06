package com.EMS.EmployeeManagementSystem.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
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

public class NotificationsDto {
	 private Long id;
	 @NotBlank(message = "Message cannot be empty")
	 private String message;
	 private LocalDateTime time;
	 @NotNull(message = "Users list cannot be null")
	 private List<Long> userIds; 

}
