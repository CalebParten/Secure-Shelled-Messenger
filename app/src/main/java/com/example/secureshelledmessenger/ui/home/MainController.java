package com.example.secureshelledmessenger.ui.home;

import android.content.Context;

import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.ContactData;

import java.util.ArrayList;

public class MainController {

    private static MainController mainController;
    private static ContactData contactDataModel;

    private MainController(Context context){
        contactDataModel = ContactData.getInstance(context);
    }

    public static MainController getInstance(Context context){
        if(mainController == null) {
            mainController = new MainController(context.getApplicationContext());
        }
        return mainController;
    }

    public static MainController getInstance(){
        return mainController;
    }

    public void addContact(String name, String username, String key){
        contactDataModel.addContact(name,username,key);
    }

    public void editContact(String newName, String username, String newKey){
        contactDataModel.editContact(newName,username,newKey);
    }

    public ArrayList<Contact> getContactsList(){
        for(Contact contact: contactDataModel.getContacts()){
            System.out.println(contact.getName());
        }
        return contactDataModel.getContacts();
    }

    public void deleteContact(int position){
        contactDataModel.deleteContact(position);
    }
}
