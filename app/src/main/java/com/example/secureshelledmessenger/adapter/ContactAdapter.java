/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file contains the adapter for the recycler view that is located on the contacts page of the
 * application. It sets each item in the recycler view to show the contact's name and key assigned
 * to that contact.
 */
package com.example.secureshelledmessenger.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.model.RecentMessage;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    // Instantiate necessary objects
    private MainController mainController;
    private List<Contact> contacts;
    private FragmentActivity activity;

    //Constructor
    public ContactAdapter(List<Contact> contacts, FragmentActivity activity) {
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method executes when the item is bound to the recycler view list.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Gather current contact
        Contact contact = contacts.get(position);
        holder.bind(contact);

//        Testing recent messages for each contact
//        for(int i = 0; i < mainController.getRecentMessages().size(); i++){
//            System.out.println(mainController.getRecentMessages().get(i).getContent());
//        }
//        System.out.println(mainController.getRecentMessages().size());

        /*
         * Set a listener for each item in the recycler view that prompts the application to go
         * to the edit page for the contact.
         */
        holder.itemView.setOnClickListener(v -> {

            //pass the contact object that is selected to the edit page
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact",contacts.get(position));
            bundle.putSerializable("action","edit");
            bundle.putSerializable("position",holder.getAdapterPosition());

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_contacts_to_contact, bundle);
        });

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    //Definition for object that represents each item in list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        //instantiate text views
        TextView contactName;
        TextView privateKey;

        //Constructor for each item in recycler view
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.new_contact_name);// Ensure this ID matches your layout
            privateKey = itemView.findViewById(R.id.private_key_view);
        }

        //set the text views to the contacts information
        public void bind(Contact contact) {
            contactName.setText(contact.getName());
            privateKey.setText(contact.getAssignedKey());
        }
    }
}
