/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file defines the Contact object with its parameters.
 */
package com.example.secureshelledmessenger.model;
import java.io.Serializable;

public class Contact implements Serializable {
    private String name;
    private String username;
    private String assignedKey;

    public Contact(String name,String username,String assignedKey) {
        this.name = name;
        this.username = username;
        this.assignedKey = assignedKey;
    }

    public String getName() {
        return name;
    }

    public String getUsername(){return username;}

    public String getAssignedKey(){return assignedKey;}

    public void setName(String name){
        this.name = name;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setAssignedKey(String key){
        this.assignedKey = key;
    }
}
