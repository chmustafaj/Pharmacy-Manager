package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AssignToClient extends AppCompatActivity {
    private static final String TAG = "";
    private RecyclerView recyclerView;
    private ClientsDialogAdapter adapter;
    private DrawerLayout drawer;
    private MaterialToolbar toolbar;
    private NavigationView navigationView;
    private int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_to_client);
        initViews();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_closed, //setting up the drawer
                R.string.drawer_closed
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        Intent intent = getIntent();
        medicineId = intent.getIntExtra("medicine_id", 0);
        adapter = new ClientsDialogAdapter();
        adapter.setMedicineID(medicineId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(AssignToClient.this, RecyclerView.VERTICAL, false));
        new GetAllClients().execute();

        Log.d(TAG, "onCreate: Medicine id " + medicineId);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newClients:
                        Intent intent = new Intent(AssignToClient.this, AddClientActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newMedicine:
                        Intent intent1 = new Intent(AssignToClient.this, addMedicineActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.medicines:
                        Intent intent2 = new Intent(AssignToClient.this, AllMedicinesActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.clients:
                        Intent intent3 = new Intent(AssignToClient.this, MainActivity.class);
                        startActivity(intent3);
                        break;

                }
                return false;
            }
        });
    }

    private class GetAllClients extends AsyncTask<Void, Void, ArrayList<Client>> {
        private static final String TAG = "";
        private DatabaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DatabaseHelper(AssignToClient.this);
        }

        @Override
        protected ArrayList<Client> doInBackground(Void... voids) {
            try {
                SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("clients", null, null, null, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        ArrayList<Client> clients = new ArrayList<>();
                        int idIndex = cursor.getColumnIndex("id");
                        int nameIndex = cursor.getColumnIndex("name");
                        int imageUriIndex = cursor.getColumnIndex("image");
                        for (int i = 0; i < cursor.getCount(); i++) {
                            Client c = new Client();
                            c.setId(cursor.getInt(idIndex));
                            c.setName(cursor.getString(nameIndex));
                            c.setImage(cursor.getString(imageUriIndex));
                            Log.d(TAG, "doInBackground: getting uri:" + cursor.getString(imageUriIndex));
                            clients.add(c);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        db.close();
                        return clients;
                    } else {
                        cursor.close();
                        db.close();
                    }
                } else {
                    db.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Client> clients) {
            super.onPostExecute(clients);
            if (null != clients) {
                adapter.setClients(clients);
            } else {
                adapter.setClients(new ArrayList<Client>());
            }
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.clientsRecViewDialog);
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);

    }
}