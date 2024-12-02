package com.ssm.server.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssm.server.api.model.Message;
import com.ssm.server.api.service.MessageService;
import com.ssm.server.api.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;

    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody MessageRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit Send"); }
        if (!userService.authenticateUserById(request.getSenderId(), request.getPassword())) { return ResponseEntity.status(403).build();}
        Message sentMessage = messageService.sendMessage(request.getSenderId(), request.getReceiverId(), request.getContent());
        return ResponseEntity.ok(sentMessage);
    }

    @PostMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(@RequestBody ConversationRequest request) {
        try {
            List<Message> messages = messageService.getMessagesForConversation(
                    request.getSenderId(),
                    request.getReceiverId(),
                    request.getPassword()
            );
            return ResponseEntity.ok(messages);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build(); // 403 Forbidden if password is incorrect
        }
    }

    @PostMapping("/received")
    public ResponseEntity<List<Message>> getReceivedMessages(@RequestBody MessageAccessRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit Received"); }
        if (!userService.authenticateUserById(request.getUserId(), request.getPassword())) { return ResponseEntity.status(403).build();}
        List<Message> receivedMessages = messageService.getReceivedMessages(request.getUserId());
        return ResponseEntity.ok(receivedMessages);
    }

    @PostMapping("/sent")
    public ResponseEntity<List<Message>> getSentMessages(@RequestBody MessageAccessRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit Sent"); }
        if (!userService.authenticateUserById(request.getUserId(), request.getPassword())) { return ResponseEntity.status(403).build();}
        List<Message> sentMessages = messageService.getSentMessages(request.getUserId());
        return ResponseEntity.ok(sentMessages);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteMessage(@RequestBody MessageDeleteRequest request) {
        if (com.ssm.server.api.ServerApiApplication.debug()) { System.out.println("Hit Delete"); }
        if (!userService.authenticateUserById(request.getUserId(), request.getPassword())) { return ResponseEntity.status(403).build();}
        boolean deleted = messageService.deleteMessage(request.getUserId(), request.getMessageId());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(404).build();
    }

    public static class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;
        private String password;

        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class ConversationRequest {
        private Long senderId;
        private Long receiverId;
        private String password;

        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }

        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
    
    public static class MessageAccessRequest {
        private Long userId;
        private String password;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class MessageDeleteRequest {
        private Long userId;
        private Long messageId;
        private String password;

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
        public Long getMessageId() { return messageId; }
        public void setMessageId(Long messageId) { this.messageId = messageId; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}
