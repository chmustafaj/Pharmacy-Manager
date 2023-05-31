package com.example.pharmacymanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class ClientsRecyclerViewAdapter extends RecyclerView.Adapter<ClientsRecyclerViewAdapter.ViewHolder> { ;
    String color;
    private ArrayList<Client> clients = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(clients.get(position).getName());
        if (clients.get(position).getColor() != null) {
            if (clients.get(position).getColor().equals("yellow")) {
                holder.clientItem.setCardBackgroundColor(Color.YELLOW);
            } else if (clients.get(position).getColor().equals("orange")) {
                holder.clientItem.setCardBackgroundColor(Color.parseColor("#FFA500"));
            } else if (clients.get(position).getColor().equals("red")) {
                holder.clientItem.setCardBackgroundColor(Color.RED);
            }
        }

        if (clients.get(position).early) {
            holder.txtEarly.setVisibility(View.VISIBLE);
        }
        if (clients.get(position).mid) {
            holder.txtMid.setVisibility(View.VISIBLE);
        }
        if (clients.get(position).night) {
            holder.txtNight.setVisibility(View.VISIBLE);
        }
        Uri uri = Uri.parse(clients.get(position).getImage());
        holder.image.setImageURI(uri);
        holder.clientItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), clientActivity.class);
                intent.putExtra("incoming_item", clients.get(position));
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return clients.size();
    }

    public void setClients(ArrayList<Client> clients) {
        this.clients = clients;
        notifyDataSetChanged();
    }

    public void setColor(String c) {
        color = c;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtEarly, txtMid, txtNight;
        private ImageView image;
        private CardView clientItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEarly = itemView.findViewById(R.id.txtEarly);
            txtMid = itemView.findViewById(R.id.txtMid);
            txtNight = itemView.findViewById(R.id.txtNight);
            txtName = itemView.findViewById(R.id.txtClientName);
            image = itemView.findViewById(R.id.clientImage);
            clientItem = itemView.findViewById(R.id.clientItem);
        }
    }
}