package com.example.secureshelledmessenger.ui.home;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Message;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }

    private List<Message> loadDummyMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("Alice", "Hey, how are you?","Bob"));
        messages.add(new Message("You", "I am fine, thanks!","Bob"));
        messages.add(new Message("Alice", "Are you coming to the party?","Bob"));
        // Add more dummy messages as needed
        return messages;
    }
}