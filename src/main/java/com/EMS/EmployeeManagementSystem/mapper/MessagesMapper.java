package com.EMS.EmployeeManagementSystem.mapper;

import com.EMS.EmployeeManagementSystem.dto.MessagesDto;
import com.EMS.EmployeeManagementSystem.entity.Messages;
import com.EMS.EmployeeManagementSystem.entity.User;

public class MessagesMapper {

    public static MessagesDto mapToMessagesDto(Messages messages) {
        return new MessagesDto(
                messages.getId(),
                messages.getSender().getId(),
                messages.getReceiver().getId(),
                messages.getMessage(),
                messages.getSentAt()
//                messages.getParentMessage() != null ? messages.getParentMessage().getId() : null
        );
    }

    public static Messages mapToMessages(MessagesDto messagesDto, User sender, User receiver) {
        return new Messages(
                messagesDto.getId(),
                sender,
                receiver,
                messagesDto.getMessage(),
                messagesDto.getSentAt()
//                parentMessage
        );
    }
}
