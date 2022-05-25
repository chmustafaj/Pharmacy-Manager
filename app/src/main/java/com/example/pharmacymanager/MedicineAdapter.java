package com.example.pharmacymanager;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    private ArrayList<Medicine> medicines = new ArrayList<>();
    Context context;
    boolean inClientActivity = false;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (inClientActivity) {
            holder.assignClient.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
        }
        holder.name.setText(medicines.get(position).getName());
        if (medicines.get(position).getImage() != null) {
            Uri uri = Uri.parse(medicines.get(position).getImage());
            holder.image.setImageURI(uri);
        }

        holder.desc.setText(medicines.get(position).getDesc());
        holder.assignClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup parent = (ViewGroup) view.getParent();
                parent.removeView(view);
                int id = medicines.get(position).getId();
                Intent intent = new Intent(view.getContext(), AssignToClient.class);
                intent.putExtra("medicine_id", id);
                view.getContext().startActivity(intent);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditMedicineDialog dialog = new EditMedicineDialog();
                Bundle bundle = new Bundle();
                bundle.putInt("medicine_id", medicines.get(position).getId());
                dialog.setArguments(bundle);
                dialog.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "plan detail dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return medicines.size();
    }

    public void setMedicines(ArrayList<Medicine> medicines) {
        this.medicines = medicines;
        notifyDataSetChanged();
    }

    public void setActivityClientActivity(boolean b) //if we are using the adapter in the client activity, the buttons will be gone
    {
        inClientActivity = b;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, desc;
        private Button assignClient, edit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.medicineImg);
            name = itemView.findViewById(R.id.medicineName);
            desc = itemView.findViewById(R.id.txtDesc);
            assignClient = itemView.findViewById(R.id.btnAssignClient);
            edit = itemView.findViewById(R.id.btnEdit);

        }
    }
}
