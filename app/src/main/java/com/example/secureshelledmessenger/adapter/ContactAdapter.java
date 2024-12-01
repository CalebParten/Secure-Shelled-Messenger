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

    private MainController mainController;

    private List<Contact> contacts;
    private FragmentActivity activity;

    public ContactAdapter(List<Contact> contacts, FragmentActivity activity) {
        this.contacts = contacts;
        this.activity = activity;
        this.mainController = MainController.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);

        for(int i = 0; i < mainController.getRecentMessages().size(); i++){
            System.out.println(mainController.getRecentMessages().get(i).getContent());
        }
        System.out.println(mainController.getRecentMessages().size());

        holder.itemView.setOnClickListener(v -> {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;
        TextView privateKey;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.new_contact_name);// Ensure this ID matches your layout
            privateKey = itemView.findViewById(R.id.private_key_view);
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());
            privateKey.setText(contact.getAssignedKey());
        }
    }
}
