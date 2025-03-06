package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.RoleDto;
import com.EMS.EmployeeManagementSystem.entity.Role;

public class RoleMapper {

    public static RoleDto mapToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setRoleName(role.getRoleName());
        return roleDto;
    }

    public static Role mapToRole(RoleDto roleDto) {
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setRoleName(roleDto.getRoleName());
        return role;
    }
}
