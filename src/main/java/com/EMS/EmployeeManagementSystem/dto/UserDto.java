package com.EMS.EmployeeManagementSystem.dto;

import java.util.List;

import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.Notifications;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
    private Boolean status;
    private Credentials credentials;
}
