package com.example.pharmacymanager;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class EditMedicineDialog extends DialogFragment {
    private EditText name, desc;
    private Button btnDone;
    private DatabaseHelper databaseHelper;
    private int medicineID;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.edit_medicine_dialog, null);
        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setView(view).setTitle("Edit");
        Bundle bundle = getArguments();
        if (null != bundle) {
            medicineID = bundle.getInt("medicine_id");
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name", name.getText().toString());
                values.put("description", desc.getText().toString());
                db.update("medicines", values, "id=?", new String[]{String.valueOf(medicineID)});
                Intent intent = new Intent(getActivity(), AllMedicinesActivity.class);
                startActivity(intent);
                dismiss();
            }
        });
        return builder.create();
    }

    private void initViews(View view) {
        name = view.findViewById(R.id.newName);
        desc = view.findViewById(R.id.newDescription);
        btnDone = view.findViewById(R.id.btnDone);
    }
}
