package com.EMS.EmployeeManagementSystem.dto;

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
public class CredentialsDto {
    private Long id;
	private String email;
    private String password;
    private Long userId;

}
