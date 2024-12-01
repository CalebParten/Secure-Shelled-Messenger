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

    private MainController mainController;

    private List<Contact> contacts;
    private FragmentActivity activity;

    public ChatAdapter(ArrayList<Contact> contacts, FragmentActivity activity){
        this.contacts = contacts;
        this.activity = activity;
        this.mainController = MainController.getInstance();
    }

   @NonNull
   @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                long receiverID = mainController.getContactID(contact.getUsername());
//                long userID = mainController.getCurrentUserID();
//                ArrayList<Message> conversation = mainController.getConversation(userID,receiverID);
//                for(int i = conversation.size() - 1; i >= 0; i--){
//                    System.out.println("checked");
//                    if(conversation.get(i).getSenderId() == receiverID){
//                        mainController.replaceRecentMessage(contact.getUsername(),
//                                conversation.get(i).getContent(),
//                                conversation.get(i).getTimestamp());
//                        break;
//                    }
//                }
//            }
//        }).start();

        holder.itemView.setOnClickListener(view -> {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            contactName = itemView.findViewById(R.id.new_contact_name);
        }

        public void bind(Contact contact){
            contactName.setText(contact.getName());
        }
    }
}
