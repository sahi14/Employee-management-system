package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.CredentialsDto;
import com.EMS.EmployeeManagementSystem.entity.Credentials;
import com.EMS.EmployeeManagementSystem.entity.User;
public class CredentialsMapper {
    public static CredentialsDto mapToCredentialsDto(Credentials credentials) {
        return new CredentialsDto(
                credentials.getId(),
                credentials.getEmail(),
                credentials.getPassword(),
                credentials.getUser().getId()
        );
    }
    public static Credentials mapToCredentials(CredentialsDto credentialsDto, User user) {
        return new Credentials(
                credentialsDto.getId(),
                credentialsDto.getEmail(),
                credentialsDto.getPassword(),
                user
        );
    }
}

