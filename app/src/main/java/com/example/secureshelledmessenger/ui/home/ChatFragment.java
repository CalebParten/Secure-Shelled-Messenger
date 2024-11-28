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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.Message;
import com.example.secureshelledmessenger.adapter.MessageAdapter;
import com.example.secureshelledmessenger.R;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {
    private static final String ARG_CONTACT = "contact";

    private MainController mainController;

    private Contact contact;
    private List<Message> messageList;
    private Long contactID;

    private MessageAdapter messageAdapter;

    private TextView contactName;
    private TextView privateKey;
    private EditText messageInput;
    private Button sendButton;

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
        mainController = MainController.getInstance(this.getContext());
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

        contactName = view.findViewById(R.id.new_contact_name);
//        privateKey = view.findViewById(R.id.private_key);
        messageInput = view.findViewById(R.id.message_input);
        System.out.println(contact.getAssignedKey());

//        messageList = new ArrayList<>();
//        loadDummyMessages();

        contactName.setText(contact.getName());
//        privateKey.setText(contact.getAssignedKey());

        new Thread(new Runnable() {
            @Override
            public void run() {
                Long receiverID = mainController.getContactID(contact.getUsername());
                ArrayList<Message> newMessages = mainController.getConversation(mainController.getCurrentUserID(), receiverID);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageList = newMessages;

                        for(Message message: messageList){
                            System.out.println(message.getTimestamp());
                        }

                        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_messages);
                        messageAdapter = new MessageAdapter(messageList, contact.getName());
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(messageAdapter);

                        contactID = receiverID;
                    }
                });
            }
        }).start();

        messageInput = view.findViewById(R.id.message_input);
        sendButton = view.findViewById(R.id.send_button);


        sendButton.setOnClickListener(v -> {
            String messageContent = messageInput.getText().toString();
            if (!messageContent.isEmpty()) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mainController.sendMessage(mainController.getCurrentUserID(),
                                contactID, messageContent,
                                mainController.getCurrentPassword(),
                                contact.getAssignedKey());

                        ArrayList<Message> updatedMessages = mainController.getConversation(
                                mainController.getCurrentUserID(), contactID);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageList.clear();
                                messageList.addAll(updatedMessages);
                                messageAdapter.notifyDataSetChanged();
                                System.out.println("message sent");
                            }
                        });
                    }
                }).start();
                messageInput.setText("");
            }
        });
        return view;
    }

    public void loadDummyMessages(){
        String contactName = contact.getName();
        messageList.add(new Message((long)0,"hello",(long)0,(long)1,LocalDateTime.now()));
        messageList.add(new Message((long)0,"hello",(long)1,(long)0,LocalDateTime.now()));
        messageList.add(new Message((long)0,"How are you?",(long)0,(long)1,LocalDateTime.now()));
        messageList.add(new Message((long)0,"Good and you?",(long)1,(long)0,LocalDateTime.now()));
        messageList.add(new Message((long)0,"Good",(long)0,(long)1,LocalDateTime.now()));
    }


}
