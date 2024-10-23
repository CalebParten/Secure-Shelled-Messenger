package com.example.secureshelledmessenger.model;

import java.time.LocalDateTime;

public class Message {
    private Long id;
    private String sender;
    private String content;
    private String receiver;
    private LocalDateTime timestamp;

    public Message(Long id,String sender, String content, String receiver) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.receiver = receiver;
        generateTimeStamp();
    }

    public void generateTimeStamp(){
        LocalDateTime time = LocalDateTime.now();
        this.timestamp = time;
    }

    public Long getId() {
        return id;
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

    public String getTimeString(){
        String hour = String.valueOf(timestamp.getHour());
        String minutes = String.valueOf(timestamp.getMinute());
        return hour +":"+ minutes;
    }

    public String getDateString(){
        String month = String.valueOf(timestamp.getMonthValue());
        String day = String.valueOf(timestamp.getDayOfMonth());
        String year = String.valueOf(timestamp.getYear());
        return month + "/" + day + "/" + year;
    }
}