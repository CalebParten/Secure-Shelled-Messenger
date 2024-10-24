package com.example.secureshelledmessenger.model;
import java.io.Serializable;

public class Contact implements Serializable {
    private Long userId;
    private String name;

    public Contact(Long userId,String name) {
        this.userId = userId;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public Long getUserId() {
        return userId;
    }
}
