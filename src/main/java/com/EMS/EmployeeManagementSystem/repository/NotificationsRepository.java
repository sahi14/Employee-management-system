package com.EMS.EmployeeManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.Notifications;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
	
    @Query(value = "SELECT n.* FROM notifications n " +
                   "JOIN user_notifications un ON n.id = un.notification_id " +
                   "WHERE un.user_id = :userId ORDER BY n.time DESC", nativeQuery = true)
    List<Notifications> findByUserId(Long userId);

}
