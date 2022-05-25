package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.net.URL;

public class AddClientActivity extends AppCompatActivity {
    private static final String TAG = "";
    private MaterialToolbar toolbar;
    private DrawerLayout drawer;
    private ImageView image;
    private EditText edtName, edtDesc;
    private NavigationView navigationView;
    private Button btnImage, btnEarly, btnMid, btnNight, btnAdd;
    private TextView txtWarning;
    Client newClient = new Client();
    DatabaseHelper dataBaseHelper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
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
        btnEarly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newClient.setEarly(true);
                Log.d(TAG, "onClick: trying to set early");
            }
        });
        btnMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newClient.setMid(true);
            }
        });
        btnNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newClient.setNight(true);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtDesc.getText().toString().equals("") || edtName.getText().toString().equals("")
                        || newClient.image == null) {
                    txtWarning.setVisibility(View.VISIBLE);
                } else {
                    txtWarning.setVisibility(View.GONE);
                    int early = 0;
                    int mid = 0;
                    int night = 0;
                    if (newClient != null) {
                        if (newClient.early != null) {
                            if (newClient.early) {
                                early = 1;
                                Log.d(TAG, "onClick: setting early");
                            } else {
                                early = 0;
                            }
                        }
                        if (newClient.mid != null) {
                            if (newClient.mid) {
                                mid = 1;
                            } else {
                                mid = 0;
                            }
                            ;
                        }
                        if (newClient.night != null) {
                            if (newClient.night) {
                                night = 1;
                            } else {
                                night = 0;
                            }
                        }

                        dataBaseHelper = new DatabaseHelper(AddClientActivity.this);
                        SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                        dataBaseHelper.insertClient(db, edtName.getText().toString(), edtDesc.getText().toString()
                                , newClient.getImage(), early, mid, night);
                        Log.d(TAG, "onClick: setting uri: " + newClient.getImage());
                        Intent intent = new Intent(AddClientActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }


            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.clients:
                        Intent intent = new Intent(AddClientActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.newMedicine:
                        Intent intent1 = new Intent(AddClientActivity.this, addMedicineActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.medicines:
                        Intent intent2 = new Intent(AddClientActivity.this, AllMedicinesActivity.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(AddClientActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (uri != null) {
            newClient.setImage(uri.toString());
            Log.d(TAG, "onActivityResult: setting uri:" + uri.toString());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);
        }
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        image = findViewById(R.id.image);
        btnImage = findViewById(R.id.btnImage);
        btnEarly = findViewById(R.id.btnEarly);
        btnMid = findViewById(R.id.btnMid);
        btnNight = findViewById(R.id.btnNight);
        btnAdd = findViewById(R.id.btnAddClient);
        edtDesc = findViewById(R.id.edtTextDesc);
        edtName = findViewById(R.id.edtTextName);
        navigationView = findViewById(R.id.navigationView);
        txtWarning = findViewById(R.id.warning);
    }
}