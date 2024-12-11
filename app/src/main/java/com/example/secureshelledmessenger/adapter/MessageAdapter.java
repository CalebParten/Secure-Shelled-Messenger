/**
 * @author Caleb Parten
 * @author David Schmith
 * @author Mario Soto
 * @date 12/10/2024
 *
 * This file contains the adapter for the recycler view that is located on the chat page of the
 * application. It shows all of the messages between the logged in user and the contact.
 */
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
                .inflate(R.layout.message_item, parent, false);
        mainController = MainController.getInstance(view.getContext());
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

    //Definition for object that represents each item in list
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderTextView;
        TextView contentTextView;

        //Constructor for each item in recycler view
        public ViewHolder(View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.message_sender);
            contentTextView = itemView.findViewById(R.id.message_content);
        }
    }
}
