package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.ContactAdapter;
import com.example.secureshelledmessenger.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private ContactAdapter adapter;
    private List<Contact> contactList;

    public ContactListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ContactAdapter(contactList, requireActivity());
        ContactAdapter contactAdapter = new ContactAdapter(contactList, getActivity());
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        adapter = new ContactAdapter(contactList, requireActivity()); // Pass the activity reference
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}