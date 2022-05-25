package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class clientActivity extends AppCompatActivity {
    private static final String TAG = "";
    private DrawerLayout drawer;
    private MaterialToolbar toolbar;
    private Button btnEarly, btnMid, btnLate;
    private NavigationView navigationView;
    private ImageView image;
    private TextView name, desc, noMedicines;
    private RecyclerView medicinesRecView;
    private Client incomingItem;
    private MedicineAdapter adapter;
    private MedicineList medicinesIDs;
    private CardView clientItem;
    private ArrayList<Medicine> clientMedicines = new ArrayList<>();
    private ArrayList<Medicine> allMedicines = new ArrayList<>();
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
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
        if (intent != null) {
            incomingItem = intent.getParcelableExtra("incoming_item");
            if (null != incomingItem) {
                Uri uri = Uri.parse(incomingItem.getImage());
                image.setImageURI(uri);
                name.setText(incomingItem.getName());
                desc.setText(incomingItem.getDesc());
            }
        }

        adapter = new MedicineAdapter();
        adapter.setActivityClientActivity(true);
        Log.d(TAG, "onCreate: Medicines: " + clientMedicines);
        medicinesRecView.setLayoutManager(new LinearLayoutManager(clientActivity.this, RecyclerView.VERTICAL, false));
        medicinesRecView.setAdapter(adapter);
        new GetAllMedicines().execute();
        btnEarly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricManager biometricManager = BiometricManager.from(clientActivity.this);
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Toast.makeText(clientActivity.this, "No Fingerprint Assigned", Toast.LENGTH_SHORT).show();

                }
                Executor executor = ContextCompat.getMainExecutor(clientActivity.this);
                biometricPrompt = new BiometricPrompt(clientActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        databaseHelper = new DatabaseHelper(clientActivity.this);
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("color", "yellow");
                        db.update("clients", values, "id=?", new String[]{String.valueOf(incomingItem.id)});
                        Intent intent = new Intent(clientActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setDeviceCredentialAllowed(true)
                        .build();
                biometricPrompt.authenticate(promptInfo);
            }
        });
        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricManager biometricManager = BiometricManager.from(clientActivity.this);
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Toast.makeText(clientActivity.this, "No Fingerprint Assigned", Toast.LENGTH_SHORT).show();

                }
                Executor executor = ContextCompat.getMainExecutor(clientActivity.this);
                biometricPrompt = new BiometricPrompt(clientActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        databaseHelper = new DatabaseHelper(clientActivity.this);
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("color", "orange");
                        db.update("clients", values, "id=?", new String[]{String.valueOf(incomingItem.id)});
                        Intent intent = new Intent(clientActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setDeviceCredentialAllowed(true)
                        .build();
                biometricPrompt.authenticate(promptInfo);
                databaseHelper = new DatabaseHelper(clientActivity.this);

            }
        });
        btnLate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BiometricManager biometricManager = BiometricManager.from(clientActivity.this);
                switch (biometricManager.canAuthenticate()) {
                    case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                        Toast.makeText(clientActivity.this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                        break;
                    case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                        Toast.makeText(clientActivity.this, "No Fingerprint Assigned", Toast.LENGTH_SHORT).show();

                }
                Executor executor = ContextCompat.getMainExecutor(clientActivity.this);
                biometricPrompt = new BiometricPrompt(clientActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        databaseHelper = new DatabaseHelper(clientActivity.this);
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("color", "red");
                        db.update("clients", values, "id=?", new String[]{String.valueOf(incomingItem.id)});
                        Intent intent = new Intent(clientActivity.this, MainActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });
                promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setDeviceCredentialAllowed(true)
                        .build();
                biometricPrompt.authenticate(promptInfo);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newClients:
                        Intent intent = new Intent(clientActivity.this, AddClientActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newMedicine:
                        Intent intent1 = new Intent(clientActivity.this, addMedicineActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.medicines:
                        Intent intent2 = new Intent(clientActivity.this, AllMedicinesActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.clients:
                        Intent intent3 = new Intent(clientActivity.this, MainActivity.class);
                        startActivity(intent3);
                        break;
                }
                return false;
            }
        });

    }

    private void initViews() {
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        image = findViewById(R.id.imageClientActivity);
        name = findViewById(R.id.txtName);
        desc = findViewById(R.id.txtDesc);
        medicinesRecView = findViewById(R.id.medicineRecyclerViewClientActivity);
        btnEarly = findViewById(R.id.btnEarlyVerify);
        btnMid = findViewById(R.id.btnMidVerify);
        btnLate = findViewById(R.id.btnLateVerify);
        clientItem = findViewById(R.id.clientItem);
        noMedicines = findViewById(R.id.noMedicinesClientActivity);

    }

    private class GetAllMedicines extends AsyncTask<Void, Void, ArrayList<Medicine>> {
        private static final String TAG = "";
        private DatabaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DatabaseHelper(clientActivity.this);
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
                Log.d(TAG, "onPostExecute: incoming item " + incomingItem.getId() + incomingItem.getName());
                medicinesIDs = Utils.getInstance(clientActivity.this).getMedicineListByClientID(incomingItem.getId());
                if (null != medicinesIDs) {
                    for (int i = 0; i < medicinesIDs.medicinesArr.size(); i++) {
                        clientMedicines.add(medicines.get(medicinesIDs.medicinesArr.get(i) - 1));
                    }
                }
                if (clientMedicines.size() != 0) {
                    adapter.setMedicines(clientMedicines);
                    noMedicines.setVisibility(View.GONE);
                } else {
                    noMedicines.setVisibility(View.VISIBLE);
                }
            } else {
                adapter.setMedicines(new ArrayList<Medicine>());
            }
        }
    }
}