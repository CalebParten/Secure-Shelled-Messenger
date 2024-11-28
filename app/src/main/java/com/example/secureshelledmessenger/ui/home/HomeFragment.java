package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.adapter.ChatAdapter;
import com.example.secureshelledmessenger.adapter.ContactAdapter;
import com.example.secureshelledmessenger.databinding.FragmentHomeBinding;
import com.example.secureshelledmessenger.model.Contact;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private MainController mainController = MainController.getInstance(this.getContext());

    private ChatAdapter adapter;
    private ArrayList<Contact> contacts;
    MainActivity mainActivity;



    public HomeFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contacts = mainController.getContactsList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ChatAdapter(contacts,getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        contacts.clear();
        contacts.addAll(mainController.getContactsList());
        adapter.notifyDataSetChanged();
    }
}