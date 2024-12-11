/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file contains the adapter for the recycler view that is located on the home page of the
 * application. It sets each item in the recycler view to show the contact's name.
 */
package com.example.secureshelledmessenger.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    // Instantiate necessary objects
    private MainController mainController;
    private List<Contact> contacts;
    private FragmentActivity activity;

    // Constructor for adapter
    public ChatAdapter(ArrayList<Contact> contacts, FragmentActivity activity){
        this.contacts = contacts;
        this.activity = activity;
        this.mainController = MainController.getInstance();
    }

    /**
     * This method executes whenever an item in the recycler view is created.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Create the view and inflate the elements from hom_contact_item.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_contact_item,
                parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method executes when the item is bound to the recycler view list.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);

        /*
         * Set a listener for each item in the recycler view that prompts the application to go
         * to that contact's chat.
         */
        holder.itemView.setOnClickListener(view -> {

            //Pass the contact object to the chat view
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact",contacts.get(position));

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_home_to_chat,bundle);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //Definition for object that represents each item in list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //instantiate text view
        TextView contactName;

        //Constructor for each item in recycler view
        public ViewHolder(@NonNull View itemView){
            super(itemView);
            contactName = itemView.findViewById(R.id.new_contact_name);
        }

        //Set the textview to the contacts name
        public void bind(Contact contact){
            contactName.setText(contact.getName());
        }
    }
}
