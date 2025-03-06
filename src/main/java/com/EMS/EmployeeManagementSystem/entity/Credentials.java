package com.EMS.EmployeeManagementSystem.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "credentials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credentials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;
}
