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

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> contacts;
    private FragmentActivity activity;

    public ContactAdapter(List<Contact> contacts, FragmentActivity activity) {
        this.contacts = contacts;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);

        holder.itemView.setOnClickListener(v -> {
//            if (activity != null) {
//                ChatFragment chatFragment = ChatFragment.newInstance(contact);
//                activity.getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment_activity_main, chatFragment)
//                        .addToBackStack(null)
//                        .commit();
//            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact",contacts.get(position));

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
        TextView contactPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);// Ensure this ID matches your layout
            contactPhoneNumber = itemView.findViewById(R.id.contact_phone);
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());
            contactPhoneNumber.setText(contact.getPhoneNumber());
        }
    }
}
