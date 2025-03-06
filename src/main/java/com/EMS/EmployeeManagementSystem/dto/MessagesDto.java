package com.EMS.EmployeeManagementSystem.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessagesDto {
    private Long id;

    @NotNull(message = "Sender ID cannot be null")
    private Long senderId;

    @NotNull(message = "Receiver ID cannot be null")
    private Long receiverId;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    // ✅ Ensure sentAt is always set when creating a new message
    private LocalDateTime sentAt = LocalDateTime.now();

//    private Long parentMessageId;
}
