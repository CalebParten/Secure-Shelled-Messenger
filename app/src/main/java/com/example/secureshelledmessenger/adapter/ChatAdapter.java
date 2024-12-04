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
import com.example.secureshelledmessenger.model.RecentMessage;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private MainController mainController;
    private List<Contact> contacts;
    private FragmentActivity activity;
    private Map<String, RecentMessage> recentMessageMap;

    public ChatAdapter(List<Contact> contacts, FragmentActivity activity) {
        this.contacts = contacts;
        this.activity = activity;
        this.mainController = MainController.getInstance(activity.getApplicationContext());

        this.recentMessageMap = new HashMap<>();
        loadRecentMessages();
    }

    private void loadRecentMessages() {
        List<RecentMessage> recentMessages = mainController.getRecentMessages();
        for (RecentMessage message : recentMessages) {
            recentMessageMap.put(message.getSender(), message);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_contact_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);

        holder.itemView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact", contact);

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_home_to_chat, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, recentMessageTextView;
        ChatAdapter chatAdapter; // Store a reference to the ChatAdapter

        public ViewHolder(@NonNull View itemView, ChatAdapter chatAdapter) {
            super(itemView);
            this.contactName = itemView.findViewById(R.id.new_contact_name);
            this.recentMessageTextView = itemView.findViewById(R.id.recent_message);
            this.chatAdapter = chatAdapter;
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());

//            // Retrieve the recent message from the map directly using the contact's username
//            RecentMessage recentMessage = chatAdapter.recentMessageMap.get(contact.getUsername());
//
//            // Display the recent message, or a default message if none found
//            if (recentMessage != null) {
//                recentMessageTextView.setText(recentMessage.getContent());
//            } else {
//                recentMessageTextView.setText("No messages yet");
//            }
        }
    }
}
