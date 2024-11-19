package com.example.secureshelledmessenger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<Message> messages;
    private String contactName;
    private MainController mainController;


    public MessageAdapter(List<Message> messages, String contactName) {
        this.messages = messages;
        this.contactName = contactName;
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
        Message message = messages.get(position);

        holder.senderTextView.setText(String.valueOf(message.getSenderId()));
        holder.contentTextView.setText(message.getContent());
        if(message.getSenderId() == mainController.getCurrentUserID()){
            holder.senderTextView.setText(mainController.getCurrentUsername());
            holder.itemView.setTranslationX(400);
            holder.itemView.setBackgroundResource(R.drawable.background_message_item);
        }
        else{
            holder.senderTextView.setText(contactName);
            holder.itemView.setBackgroundResource(R.drawable.background_sender_message_item);
        }
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
