package com.example.secureshelledmessenger.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secureshelledmessenger.MainActivity;
import com.example.secureshelledmessenger.R;
import com.example.secureshelledmessenger.model.Contact;
import com.example.secureshelledmessenger.ui.home.MainController;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {

    private MainController mainController;

    private List<Contact> contacts;
    private FragmentActivity activity;
    private ImageView optionsButton;

    public ContactAdapter(List<Contact> contacts, FragmentActivity activity) {
        this.contacts = contacts;
        this.activity = activity;
        this.mainController = MainController.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
        optionsButton = holder.itemView.findViewById(R.id.options_image);

        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("contact",contacts.get(position));

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_contacts_to_contact, bundle);
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.contact_options, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if(menuItem.getTitle().equals("Delete")){
//                            contacts.remove(holder.getAdapterPosition());
                            mainController.deleteContact(holder.getAdapterPosition());
                            contacts = mainController.getContactsList();
                            notifyDataSetChanged();
                        }
                        if(menuItem.getTitle().equals("Edit")){
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("contact",contacts.get(holder.getAdapterPosition()));
                            bundle.putString("action","edit");
                            bundle.putInt("position",holder.getAdapterPosition());

                            NavController navController = Navigation.findNavController(view);
                            navController.navigate(R.id.action_contacts_to_edit_contact, bundle);
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.new_contact_name);// Ensure this ID matches your layout
        }

        public void bind(Contact contact) {
            contactName.setText(contact.getName());
        }
    }
}
