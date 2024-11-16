package com.example.secureshelledmessenger.ui.home;

import android.content.Context;

import com.example.secureshelledmessenger.model.ApiData;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.ContactData;

import java.util.ArrayList;

public class MainController {

    private static MainController mainController;
    private static ContactData contactDataModel;
    private static ApiData apiDataModel;

    private MainController(Context context){
        contactDataModel = ContactData.getInstance(context);
        apiDataModel = ApiData.getInstance(context);
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

    public void getUserString(String username){
        System.out.println("Request Result: " + apiDataModel.requestLookupUser(username));
    }

    public void createUser(String username, String password){
        System.out.println("Create User Result: " + apiDataModel.requestCreateUser(username, password));
    }

    public String checkLogin(String username, String password){
        return apiDataModel.requestLogin(username,password);
    }
}
