/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file defines the Recent Message Object that is saved in shared preferences.
 */
package com.example.secureshelledmessenger.model;

import java.time.LocalDateTime;

public class RecentMessage {

    private String sender;
    private String content;
    private LocalDateTime time;

    public RecentMessage(String sender, String content, LocalDateTime time){
        this.sender = sender;
        this.content = content;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
