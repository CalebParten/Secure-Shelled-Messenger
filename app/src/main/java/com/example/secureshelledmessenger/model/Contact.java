package com.example.secureshelledmessenger.model;
import java.io.Serializable;

public class Contact implements Serializable {
    private Long userId;
    private String name;
    private String phoneNumber;

    public Contact(Long userId,String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Long getUserId() {
        return userId;
    }
}
