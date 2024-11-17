package com.example.secureshelledmessenger.model;

import java.time.LocalDateTime;
public class Message {

    private Long id;
    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime timestamp;

    public Message(Long messageId, String messageContent, Long senderIdResponse, Long receiverIdResponse, LocalDateTime timestamp) {
        this.setId(messageId);
        this.setContent(messageContent);
        this.setSenderId(senderIdResponse);
        this.setReceiverId(receiverIdResponse);
        this.setTimestamp(timestamp);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long long1) {
        this.senderId = long1;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString(){
        return "{"+this.getId()+","+this.getContent()+","+this.getSenderId()+","+this.getReceiverId()+","+this.getTimestamp()+"}";
    }
}

