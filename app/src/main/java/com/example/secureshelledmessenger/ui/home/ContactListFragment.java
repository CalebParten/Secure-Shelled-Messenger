package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.ContactAdapter;
import com.example.secureshelledmessenger.databinding.FragmentContactListBinding;
import com.example.secureshelledmessenger.model.Contact;

import java.util.ArrayList;

public class ContactListFragment extends Fragment {

    private FragmentContactListBinding binding;
    private ContactAdapter adapter;
    private ArrayList<Contact> contactList;
    private MainController mainController;

    public ContactListFragment() {
        // Default constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize MainController with a valid context
        mainController = MainController.getInstance(requireContext());
        contactList = mainController.getContactsList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize view binding
        binding = FragmentContactListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up RecyclerView
        binding.recyclerViewContacts.setLayoutManager(new LinearLayoutManager(requireActivity()));
        adapter = new ContactAdapter(contactList, requireActivity());
        binding.recyclerViewContacts.setAdapter(adapter);

        // Handle "Create Contact" button click
        binding.createContactButton.setOnClickListener(v -> goToCreateContact(v));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh contact list and notify adapter
        contactList.clear();
        contactList.addAll(mainController.getContactsList());
        adapter.notifyDataSetChanged();
    }

    private void goToCreateContact(View view) {
        // Navigate to the "Create Contact" screen
        Bundle bundle = new Bundle();
        bundle.putString("action", "create");
        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_contacts_to_edit_contact, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up binding reference
        binding = null;
    }
}
