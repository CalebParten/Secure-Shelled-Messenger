package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.adapter.MessageAdapter;
import com.example.secureshelledmessenger.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static final String ARG_CONTACT = "contact";

    private Contact contact;
    private List<Message> messageList;

    public ChatFragment() {
    }

    public static ChatFragment newInstance(Contact contact) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, (Serializable) contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contact = (Contact) getArguments().getSerializable(ARG_CONTACT);
            if (contact == null) {
                throw new NullPointerException("Contact is null");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        TextView contactName = view.findViewById(R.id.contact_name);
        contactName.setText(contact.getName());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_messages);
        MessageAdapter messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        EditText messageInput = view.findViewById(R.id.message_input);
        Button sendButton = view.findViewById(R.id.send_button);

        sendButton.setOnClickListener(v -> {
            String messageContent = messageInput.getText().toString();
            if (!messageContent.isEmpty()) {
                Message message = new Message("You", messageContent);
                messageList.add(message);
                messageInput.setText("");
            }
        });

        return view;
    }
}
