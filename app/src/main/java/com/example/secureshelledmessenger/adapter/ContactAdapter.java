package com.example.secureshelledmessenger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.ui.home.ChatFragment;

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
            if (activity != null) {
                ChatFragment chatFragment = ChatFragment.newInstance(contact);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, chatFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name); // Ensure this ID matches your layout
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());
        }
    }
}
