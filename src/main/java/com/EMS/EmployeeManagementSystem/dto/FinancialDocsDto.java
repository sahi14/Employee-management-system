package com.EMS.EmployeeManagementSystem.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
public class FinancialDocsDto {
	
	private Long id;
    @NotNull(message = "User ID cannot be null")
    private Long userId;
    @NotBlank(message = "Document name is required")
    private String documentName;
//    @NotBlank(message = "Document URL is required")
//    private String documentUrl;
    private LocalDateTime uploadedAt;

}
