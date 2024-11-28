package com.example.secureshelledmessenger.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Contact;

import java.util.ArrayList;


public class ContactEditFragment extends Fragment {

    private MainController mainController;

    private EditText nameField;
    private EditText userField;
    private EditText keyField;
    private Button submitButton;
    private Button deleteButton;

    private Contact contact;
    private String action;
    private int position;

    MainActivity mainActivity;

    public ContactEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getActivity() != null && getActivity() instanceof MainActivity) {
            mainActivity = (MainActivity) getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle != null){
            contact = (Contact)bundle.getSerializable("contact");
            action = bundle.getString("action");
            position = bundle.getInt("position");
        }

        mainController = MainController.getInstance(mainActivity.getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameField = view.findViewById(R.id.new_contact_name_field);

        userField = view.findViewById(R.id.new_contact_user_field);
        keyField = view.findViewById(R.id.private_key);
        submitButton = view.findViewById(R.id.submit_button);
        deleteButton = view.findViewById(R.id.delete_button);

        if(action.equals("edit")){
            nameField.setText(contact.getName());
            keyField.setText(contact.getAssignedKey());
            userField.setText(String.valueOf(contact.getUsername()));
            userField.setEnabled(false);
        }
        if(action.equals("create")){
            deleteButton.setText("Cancel");
            submitButton.setText("Submit");
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(deleteButton.getText().toString().equals("Cancel")){
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
                if(deleteButton.getText().toString().equals("Delete")){
                    mainController.deleteContactByUsername(contact.getUsername());
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }

            }
        });


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = userField.getText().toString();
                String name = nameField.getText().toString();
                String key = keyField.getText().toString();

                if(action.equals("create")){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            long checkedID = mainController.getContactID(username);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(checkedID > 0){
                                        mainController.addContact(name,username,key);
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        fragmentManager.popBackStack();
                                    }
                                    else{
                                        userField.setError("This username does not exist");
                                    }
                                }
                            });
                        }
                    }).start();
                }
                else if(action.equals("edit")){
                    mainController.editContact(name,username,key);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
            }
        });
    }
}