package com.EMS.EmployeeManagementSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EMS.EmployeeManagementSystem.entity.Messages;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, Long> {

    // Get all messages sent by a user, ordered by sent_at (oldest first)
    List<Messages> findBySenderIdOrderBySentAtAsc(Long senderId);

    // Get all messages received by a user, ordered by sent_at (oldest first)
    List<Messages> findByReceiverIdOrderBySentAtAsc(Long receiverId);

    // Get replies for a message, ordered by sent_at (oldest first)
//    List<Messages> findByParentMessageIdOrderBySentAtAsc(Long parentMessageId);
}
