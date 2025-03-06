package com.EMS.EmployeeManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan(basePackages = "com.EMS.EmployeeManagementSystem.entity")
@EnableJpaRepositories(basePackages = "com.EMS.EmployeeManagementSystem.repository")
@EnableScheduling

public class
EmployeeManagementSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementSystemApplication.class, args);
	}
}
