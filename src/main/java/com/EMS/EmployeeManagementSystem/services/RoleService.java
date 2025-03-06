package com.EMS.EmployeeManagementSystem.services;
import org.springframework.beans.factory.annotation.Autowired;

import com.EMS.EmployeeManagementSystem.dto.RoleDto;
import com.EMS.EmployeeManagementSystem.entity.Role;
import com.EMS.EmployeeManagementSystem.mapper.RoleMapper;
import com.EMS.EmployeeManagementSystem.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleService {

    @Autowired
    private final RoleRepository roleRepository;

    public RoleDto createRole(RoleDto roleDto) {
        Role role = RoleMapper.mapToRole(roleDto);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.mapToRoleDto(savedRole);
    }
}