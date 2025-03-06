package com.EMS.EmployeeManagementSystem.controller;

import com.EMS.EmployeeManagementSystem.dto.MessagesDto;
import com.EMS.EmployeeManagementSystem.services.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {

    @Autowired
    private MessagesService messagesService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessagesDto messagesDto) {
        try {
            MessagesDto savedMessage = messagesService.sendMessage(messagesDto);
            return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<MessagesDto>> getMessagesForUser(@PathVariable Long userId) {
        List<MessagesDto> messages = messagesService.getMessagesForUser(userId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<MessagesDto>> getMessagesSentByUser(@PathVariable Long userId) {
        List<MessagesDto> messages = messagesService.getMessagesSentByUser(userId);
        return ResponseEntity.ok(messages);
    }

//    @GetMapping("/{messageId}/replies")
//    public ResponseEntity<List<MessagesDto>> getRepliesForMessage(@PathVariable Long messageId) {
//        List<MessagesDto> replies = messagesService.getRepliesForMessage(messageId);
//        return ResponseEntity.ok(replies);
//    }
}
