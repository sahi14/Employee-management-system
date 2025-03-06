package com.EMS.EmployeeManagementSystem.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "documents") 
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialDocs {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) 
    private User user;

    @Column(nullable = false, length = 255)
    private String documentName;

//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String documentUrl;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
