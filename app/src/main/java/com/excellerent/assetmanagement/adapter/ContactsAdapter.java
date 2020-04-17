package com.excellerent.assetmanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.excellerent.assetmanagement.R;
import com.excellerent.assetmanagement.activities.SaveToDatabaseActivity;
import com.excellerent.assetmanagement.db.entity.Contact;

import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contact> contactssList;
    private SaveToDatabaseActivity saveToDatabaseActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView emil;


        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            emil = view.findViewById(R.id.email);

        }
    }


    public ContactsAdapter(Context context, ArrayList<Contact> contacts, SaveToDatabaseActivity saveToDatabaseActivity) {
        this.context = context;
        this.contactssList = contacts;
        this.saveToDatabaseActivity = saveToDatabaseActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final Contact contact = contactssList.get(position);
        final String brand = "";
        final String serial = "";

        holder.name.setText(contact.getName());
        holder.emil.setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                saveToDatabaseActivity.addAndEditContacts(true, contact, position, brand, serial);
            }
        });

    }

    @Override
    public int getItemCount() {

        return contactssList.size();
    }


}

