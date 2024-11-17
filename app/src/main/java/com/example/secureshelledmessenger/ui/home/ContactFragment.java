package com.example.secureshelledmessenger.ui.home;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.MessageAdapter;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.Message;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private Contact contact;
    private List<Message> messageList;

    private TextView contactName;
    private TextView privateKey;
    private EditText messageInput;
    private ImageButton sendButton;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            contact = (Contact)bundle.getSerializable("contact");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact,container,false);

        contactName = view.findViewById(R.id.new_contact_name);
        privateKey = view.findViewById(R.id.private_key);
        messageInput = view.findViewById(R.id.message_input);

        messageList = new ArrayList<>();
        loadDummyMessages();

        contactName.setText(contact.getName());
        privateKey.setText(contact.getAssignedKey());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_messages);
        MessageAdapter messageAdapter = new MessageAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(messageAdapter);

        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.btn_send_message);

        sendButton.setOnClickListener(v -> {
            String messageContent = messageInput.getText().toString();
            if(!messageContent.isEmpty()){
                Message message = new Message((long)0,messageContent, (long)0,(long)1,LocalDateTime.now());
                messageList.add(message);
                messageInput.setText("");
                messageAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    private void loadDummyMessages() {
        messageList.add(new Message((long)0,"Hey, how are you?", (long)0,(long)1, LocalDateTime.now()));
        messageList.add(new Message((long)0,"I am fine, thanks!", (long)1,(long)0, LocalDateTime.now()));
        messageList.add(new Message((long)0,"Are you coming to the party?", (long)0,(long)1, LocalDateTime.now()));
        // Add more dummy messages as needed
    }


}