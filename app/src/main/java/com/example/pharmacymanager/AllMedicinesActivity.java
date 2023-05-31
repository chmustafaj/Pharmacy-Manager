package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AllMedicinesActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView medicinesRecyclerView;
    private DrawerLayout drawer;
    private MedicineAdapter adapter;
    private Button addMedicines;
    private TextView noMedicines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        initViews();
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_closed, //setting up the drawer
                R.string.drawer_closed
        );
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newClients:
                        Intent intent = new Intent(AllMedicinesActivity.this, AddClientActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newMedicine:
                        Intent intent1 = new Intent(AllMedicinesActivity.this, addMedicineActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.clients:
                        Intent intent2 = new Intent(AllMedicinesActivity.this, MainActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
        adapter = new MedicineAdapter();
        medicinesRecyclerView.setAdapter(adapter);
        medicinesRecyclerView.setLayoutManager(new LinearLayoutManager(AllMedicinesActivity.this, RecyclerView.VERTICAL, false));
        new GetAllMedicines().execute();
        addMedicines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllMedicinesActivity.this, addMedicineActivity.class);
                startActivity(intent);
            }
        });
    }

    private class GetAllMedicines extends AsyncTask<Void, Void, ArrayList<Medicine>> {
        private static final String TAG = "";
        private DatabaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DatabaseHelper(AllMedicinesActivity.this);
        }

        @Override
        protected ArrayList<Medicine> doInBackground(Void... voids) {
            try {
                SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
                Cursor cursor = db.query("medicines", null, null, null, null, null, null);
                if (null != cursor) {
                    if (cursor.moveToFirst()) {
                        ArrayList<Medicine> medicines = new ArrayList<>();
                        int idIndex = cursor.getColumnIndex("id");
                        int nameIndex = cursor.getColumnIndex("name");
                        int descIndex = cursor.getColumnIndex("description");
                        int imageUriIndex = cursor.getColumnIndex("image");
                        for (int i = 0; i < cursor.getCount(); i++) {
                            Medicine m = new Medicine();
                            m.setId(cursor.getInt(idIndex));
                            m.setName(cursor.getString(nameIndex));
                            m.setDesc(cursor.getString(descIndex));
                            ;
                            m.setImage(cursor.getString(imageUriIndex));
                            Log.d(TAG, "doInBackground: getting uri:" + cursor.getString(imageUriIndex));
                            medicines.add(m);
                            cursor.moveToNext();
                        }
                        cursor.close();
                        db.close();
                        return medicines;
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
        protected void onPostExecute(ArrayList<Medicine> medicines) {
            super.onPostExecute(medicines);
            if (null != medicines) {
                adapter.setMedicines(medicines);
                addMedicines.setVisibility(View.GONE);
                noMedicines.setVisibility(View.GONE);
            } else {
                addMedicines.setVisibility(View.VISIBLE);
                noMedicines.setVisibility(View.VISIBLE);
                adapter.setMedicines(new ArrayList<Medicine>());
            }
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        medicinesRecyclerView = findViewById(R.id.medicinesRecView);
        drawer = findViewById(R.id.drawer);
        addMedicines = findViewById(R.id.addMedicinesBtn);
        noMedicines = findViewById(R.id.noMedicines);
    }
}