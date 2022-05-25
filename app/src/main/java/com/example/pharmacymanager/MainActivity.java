package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.KeyguardManager;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "";
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    RelativeLayout mainRelLayout;
    DrawerLayout drawer;
    private TextView noClients;
    private MaterialToolbar toolbar;
    private RecyclerView clientsRecyclerView;
    private ClientsRecyclerViewAdapter adapter;
    private NavigationView navigationView;
    private ArrayList<Client> allClients;
    private DatabaseHelper dataBaseHelper;
    private Button addClient;
    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;
    //private boolean askedForFingerPrint=false; //to ask for fingerprint only once when started the app

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        Log.d(TAG, "onCreate: askedofrfingerprint" + Utils.getInstance(this).askedForFingerPrint);
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
        adapter = new ClientsRecyclerViewAdapter();
        clientsRecyclerView.setAdapter(adapter);
        clientsRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        new GetAllClients().execute();
        if (!Utils.getInstance(this).askedForFingerPrint) {
            BiometricManager biometricManager = BiometricManager.from(this);
            switch (biometricManager.canAuthenticate()) {
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    authenticateApp();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    Toast.makeText(this, "Device Doesn't Have Fingerprint", Toast.LENGTH_SHORT).show();
                    authenticateApp();
                    break;
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    Toast.makeText(this, "No Fingerprint Assigned", Toast.LENGTH_SHORT).show();
                    authenticateApp();

            }
            Executor executor = ContextCompat.getMainExecutor(this);
            biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Utils.getInstance(MainActivity.this).askedForFingerPrint = true;
                    mainRelLayout.setVisibility(View.VISIBLE);

                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                }
            });
            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Biometric Authentication").setDeviceCredentialAllowed(true)
                    .build();
            biometricPrompt.authenticate(promptInfo);
        } else {
            mainRelLayout.setVisibility(View.VISIBLE);
        }

        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newClients:
                        Intent intent = new Intent(MainActivity.this, AddClientActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newMedicine:
                        Intent intent1 = new Intent(MainActivity.this, addMedicineActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.medicines:
                        Intent intent2 = new Intent(MainActivity.this, AllMedicinesActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });

    }

    void initViews() {
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        mainRelLayout = findViewById(R.id.mainRelLayout);
        clientsRecyclerView = findViewById(R.id.clientsRecView);
        navigationView = findViewById(R.id.navigationView);
        noClients = findViewById(R.id.noClients);
        addClient = findViewById(R.id.addClientBtn);
    }

    private class GetAllClients extends AsyncTask<Void, Void, ArrayList<Client>> {
        private DatabaseHelper dataBaseHelper;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataBaseHelper = new DatabaseHelper(MainActivity.this);
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
                        int descIndex = cursor.getColumnIndex("description");
                        int colorIndex = cursor.getColumnIndex("color");
                        int earlyIndex = cursor.getColumnIndex("early");
                        int midIndex = cursor.getColumnIndex("mid");
                        int nightIndex = cursor.getColumnIndex("late");
                        int imageUriIndex = cursor.getColumnIndex("image");
                        for (int i = 0; i < cursor.getCount(); i++) {
                            Client c = new Client();
                            boolean early, mid, night;    //converting int to bool
                            if (cursor.getInt(earlyIndex) == 1) {
                                early = true;
                            } else {
                                early = false;
                            }
                            if (cursor.getInt(midIndex) == 1) {
                                mid = true;
                            } else {
                                mid = false;
                            }
                            if (cursor.getInt(nightIndex) == 1) {
                                night = true;
                            } else {
                                night = false;
                            }
                            c.setId(cursor.getInt(idIndex));
                            c.setName(cursor.getString(nameIndex));
                            c.setDesc(cursor.getString(descIndex));
                            c.setEarly(early);
                            c.setNight(night);
                            c.setMid(mid);
                            c.setColor(cursor.getString(colorIndex));
                            c.setImage(cursor.getString(imageUriIndex));
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
                allClients = clients;
                noClients.setVisibility(View.GONE);
                addClient.setVisibility(View.GONE);
            } else {
                noClients.setVisibility(View.VISIBLE);
                addClient.setVisibility(View.VISIBLE);
                adapter.setClients(new ArrayList<Client>());
            }
        }
    }

    //method to authenticate app
    private void authenticateApp() {
        //Get the instance of KeyGuardManager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            Intent i = keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock), getResources().getString(R.string.confirm_pattern));
            try {
                //Start activity for result
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {
                mainRelLayout.setVisibility(View.VISIBLE);
                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                try {

                    //Start activity for result
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception ex) {

                    //If app is unable to find any Security settings then user has to set screen lock manually
                    //textView.setText(getResources().getString(R.string.setting_label));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOCK_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mainRelLayout.setVisibility(View.VISIBLE);
                } else {
                }
                break;
            case SECURITY_SETTING_REQUEST_CODE:
                //When user is enabled Security settings then we don't get any kind of RESULT_OK
                //So we need to check whether device has enabled screen lock or not
                if (isDeviceSecure()) {
                    //If screen lock enabled show toast and start intent to authenticate user
                    Toast.makeText(this, getResources().getString(R.string.device_is_secure), Toast.LENGTH_SHORT).show();
                    authenticateApp();
                } else {
                }

                break;
        }
    }

    /**
     * method to return whether device has screen lock enabled or not
     **/
    private boolean isDeviceSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure();


    }

    //On Click of button do authentication again
    public void authenticateAgain(View view) {
        authenticateApp();
    }

}