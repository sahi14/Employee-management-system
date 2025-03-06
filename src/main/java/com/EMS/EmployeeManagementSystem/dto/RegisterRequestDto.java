package com.EMS.EmployeeManagementSystem.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private UserDto userDto;
    private String firstName;
    private String lastName;
    private String email;
    private String roleName;
    private Boolean status;
    private String password;
}
