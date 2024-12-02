package com.ssm.server.api.service;

import com.ssm.server.api.model.Message;
import com.ssm.server.api.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService;

    public List<Message> getMessagesForConversation(Long senderId, Long receiverId, String password) {
        // Authenticate the sender
        if (userService.authenticateUserById(senderId, password)) {
            List<Message> messages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
            messages.addAll(messageRepository.findBySenderIdAndReceiverId(receiverId, senderId));
            return messages;
        }
        throw new SecurityException("Invalid password");
    }

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getReceivedMessages(Long userId) {
        return messageRepository.findByReceiverId(userId);
    }

    public List<Message> getSentMessages(Long userId) {
        return messageRepository.findBySenderId(userId);
    }

    public boolean deleteMessage(Long userId, Long messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        if (message.isPresent() && message.get().getSenderId().equals(userId)) {
            messageRepository.delete(message.get());
            return true;
        }
        return false;
    }
}
