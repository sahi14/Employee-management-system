package com.EMS.EmployeeManagementSystem.services;

import com.EMS.EmployeeManagementSystem.dto.MessagesDto;
import com.EMS.EmployeeManagementSystem.entity.Messages;
import com.EMS.EmployeeManagementSystem.entity.User;
import com.EMS.EmployeeManagementSystem.mapper.MessagesMapper;
import com.EMS.EmployeeManagementSystem.repository.MessagesRepository;
import com.EMS.EmployeeManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserRepository userRepository;

    public MessagesDto sendMessage(MessagesDto messagesDto) {
        User sender = userRepository.findById(messagesDto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findById(messagesDto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Check if the message is a reply
//        Messages parentMessage = null;
//        if (messagesDto.getParentMessageId() != null) {
//            parentMessage = messagesRepository.findById(messagesDto.getParentMessageId())
//                    .orElseThrow(() -> new RuntimeException("Parent message not found"));
//        }

        Messages message = MessagesMapper.mapToMessages(messagesDto, sender, receiver);
        message.setSentAt(LocalDateTime.now());

        Messages savedMessage = messagesRepository.save(message);
        return MessagesMapper.mapToMessagesDto(savedMessage);
    }

    // Get all messages received by a user (sorted)
    public List<MessagesDto> getMessagesForUser(Long userId) {
        List<Messages> messages = messagesRepository.findByReceiverIdOrderBySentAtAsc(userId);
        System.out.println(messages.stream()
                .map(MessagesMapper::mapToMessagesDto)
                .collect(Collectors.toList()));

        return messages.stream()
                .map(MessagesMapper::mapToMessagesDto)
                .collect(Collectors.toList());
    }

    // Get all messages sent by a user (sorted)
    public List<MessagesDto> getMessagesSentByUser(Long userId) {
        List<Messages> messages = messagesRepository.findBySenderIdOrderBySentAtAsc(userId);
        return messages.stream()
                .map(MessagesMapper::mapToMessagesDto)
                .collect(Collectors.toList());
    }

    // Get replies for a specific message (sorted)
//    public List<MessagesDto> getRepliesForMessage(Long messageId) {
//        List<Messages> replies = messagesRepository.findByParentMessageIdOrderBySentAtAsc(messageId);
//        return replies.stream()
//                .map(MessagesMapper::mapToMessagesDto)
//                .collect(Collectors.toList());
//    }
}
