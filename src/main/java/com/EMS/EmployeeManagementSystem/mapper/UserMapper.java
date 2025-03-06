package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.UserDto;
import com.EMS.EmployeeManagementSystem.dto.UserResponseDto;
import com.EMS.EmployeeManagementSystem.entity.Role;
import com.EMS.EmployeeManagementSystem.entity.User;

public class UserMapper {

	public static UserDto mapToUserDto(User user) {
		return new UserDto(
				user.getId(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				(user.getRole() != null) ? user.getRole().getRoleName() : null,
				user.getStatus(),
				user.getCredentials()
		);
	}

	public static User mapToUser(UserDto userDto, Role role) {
		return new User(
				userDto.getId(),
				userDto.getFirstName(),
				userDto.getLastName(),
				userDto.getEmail(),
				role,
				userDto.getStatus(),
				userDto.getCredentials()
		);
	}

	public static UserResponseDto mapToUserResponseDto(User user) {
		return UserResponseDto.builder()
				.id(user.getId())
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.roleName(user.getRole().getRoleName())
				.status(user.getStatus())
				.build();
	}
}
