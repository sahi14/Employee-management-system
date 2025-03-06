package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.NotificationsDto;
import com.EMS.EmployeeManagementSystem.entity.Notifications;
import com.EMS.EmployeeManagementSystem.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationsMapper {

    public static NotificationsDto mapToNotificationsDto(Notifications notifications) {
        return NotificationsDto.builder()
                .id(notifications.getId())
                .message(notifications.getMessage())
                .time(notifications.getTime())
                .userIds(notifications.getUsers().stream()
                        .map(User::getId)
                        .collect(Collectors.toList()))
                .build();
    }


    public static Notifications mapToNotifications(NotificationsDto dto, List<User> users) {
        return new Notifications(
                dto.getId(),
                users,
                dto.getMessage(),
                dto.getTime()
        );
    }
}
