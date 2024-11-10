package com.example.secureshelledmessenger.model;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private Long id;
    private String username;
    private String password;
    private ArrayList<Contact> contacts;

    public User(Long id, String username, String password, ArrayList<Contact> contacts){
        this.id = id;
        this.username = username;
        this.password = password;
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void addContact(Contact contact){
        contacts.add(contact);
    }

    public void removeContact(int position){
        contacts.remove(position);
    }

    public void setContacts(ArrayList<Contact> contacts){
        this.contacts = contacts;
    }
}
