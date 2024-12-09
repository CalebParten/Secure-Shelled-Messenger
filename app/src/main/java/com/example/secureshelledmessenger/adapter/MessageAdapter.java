package com.example.secureshelledmessenger.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private final List<Message> messages;
    private final String contactName;
    private final String key;
    private MainController mainController;

    public MessageAdapter(List<Message> messages, String contactName, String contactKey) {
        this.messages = messages;
        this.contactName = contactName;
        this.key = contactKey;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        mainController = MainController.getInstance(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = mainController.decryptMessage(messages.get(position), key);

        // Set the sender name
        if (message.getSenderId() == mainController.getCurrentUserID()) {
            holder.senderTextView.setText("You");
            // Set the sent message background (using drawable resource)
            holder.itemView.setBackgroundResource(R.drawable.sent_message_background);
        } else {
            holder.senderTextView.setText(contactName);
            // Set the received message background (using drawable resource)
            holder.itemView.setBackgroundResource(R.drawable.received_message_background);
        }

        // Set the message content
        holder.contentTextView.setText(message.getContent());

        // Align all messages to the left
        holder.itemView.setTranslationX(0);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView;
        TextView contentTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.message_sender);
            contentTextView = itemView.findViewById(R.id.message_content);
        }
    }
}
