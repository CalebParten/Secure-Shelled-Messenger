package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.ContactAdapter;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.model.User;

import java.util.ArrayList;
import java.util.List;

public class ContactListFragment extends Fragment {

    private ContactAdapter adapter;
    private ArrayList<Contact> contactList;
    MainActivity mainActivity;
    Button createContactButton;

    public ContactListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
            User user = mainActivity.getGlobalUser();
            contactList = user.getContacts();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ContactAdapter(contactList, requireActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createContactButton = view.findViewById(R.id.create_contact_button);

        createContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCreateContact(view);
            }
        });
        for(Contact contact: contactList){
            System.out.println(contact.getName());
        }
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        adapter = new ContactAdapter(contactList, requireActivity()); // Pass the activity reference
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
            User user = mainActivity.getGlobalUser();
            contactList = user.getContacts();
            adapter.notifyDataSetChanged();
        }
    }

    public void goToCreateContact(View view){
        Bundle bundle = new Bundle();
        bundle.putString("action","create");
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_contacts_to_edit_contact,bundle);
    }
}