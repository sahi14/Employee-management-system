package com.EMS.EmployeeManagementSystem.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Notifications {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	 
	@ManyToMany
	@JoinTable(
	        name = "user_notifications",
	        joinColumns = @JoinColumn(name = "notification_id"),
	        inverseJoinColumns = @JoinColumn(name = "user_id")
	    )
	 private List<User> users;
	 private String message;
	 private LocalDateTime time;
}
