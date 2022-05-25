package com.example.pharmacymanager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClientsDialogAdapter extends RecyclerView.Adapter<ClientsDialogAdapter.ViewHolder> {
    private static final String TAG = "";
    private ArrayList<Client> clients = new ArrayList<>();
    int medicineID;
    Context context;
    DatabaseHelper databaseHelper = new DatabaseHelper(context);

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(clients.get(position).getName());
        Uri uri = Uri.parse(clients.get(position).getImage());
        holder.image.setImageURI(uri);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clientID = clients.get(position).getId();
                MedicineList medicineList = Utils.getInstance(view.getContext()).getMedicineListByClientID(clientID);
                if (medicineList == null) {       //If it is the first medicine being assigned to a client, a new medicine list will be made
                    medicineList = new MedicineList();
                }

                boolean isPresent = false;
                for (int i = 0; i < medicineList.medicinesArr.size(); i++) {
                    if (medicineID == medicineList.medicinesArr.get(i)) {
                        isPresent = true;
                    }
                }
                if (!isPresent) {
                    Utils.getInstance(view.getContext()).deleteMedicineList(medicineList);
                    medicineList.medicinesArr.add(medicineID);
                    Log.d(TAG, "onClick: Medicine id trying to add " + medicineID);
                    medicineList.setId(clientID);
                    Utils.getInstance(view.getContext()).addMedicineArrayToDB(medicineList);
                    Log.d(TAG, "onClick: Medicine list trying to add " + medicineList.medicinesArr);
                    Log.d(TAG, "onClick:Medicines assigned to client " + clientID + Utils.getInstance(context).getMedicineListByClientID(clientID).medicinesArr);
                } else {
                    Toast.makeText(view.getContext(), "Medicine already assigned", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(view.getContext(), AllMedicinesActivity.class);
                view.getContext().startActivity(intent);

            }
        });
    }

    public void setMedicineID(int id) {
        medicineID = id;
    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView image;
        private CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtClientName);
            image = itemView.findViewById(R.id.clientImage);
            card = itemView.findViewById(R.id.clientItem);
        }
    }
}
