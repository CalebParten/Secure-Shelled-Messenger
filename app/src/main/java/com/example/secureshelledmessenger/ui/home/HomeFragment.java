package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.ChatAdapter;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.ContactData;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private ContactData contactData;
    private ChatAdapter adapter;
    private ArrayList<Contact> contacts;

    public HomeFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the data and UI elements here
        contactData = ContactData.getInstance(getContext());
        contacts = new ArrayList<>(contactData.getContacts());  // Fetch contacts from ContactData

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ChatAdapter(contacts, getActivity());  // Pass the contacts directly
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh contacts list to reflect any changes
        updateContactsList();
    }

    private void updateContactsList() {
        ArrayList<Contact> updatedContacts = new ArrayList<>(contactData.getContacts());
        if (!updatedContacts.equals(contacts)) {
            contacts.clear();
            contacts.addAll(updatedContacts);
            adapter.notifyDataSetChanged();  // Notify the adapter to refresh the data
        }
    }
}
