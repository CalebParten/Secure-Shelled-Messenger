package com.example.secureshelledmessenger.model;

public class Message {
    private String sender;
    private String content;
    private String receiver;

    public Message(String sender, String content, String receiver) {
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getReceiver() {
        return receiver;
    }
}