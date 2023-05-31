package com.example.pharmacymanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class addMedicineActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private DrawerLayout drawer;
    private ImageView image;
    private EditText edtName, edtDesc;
    private NavigationView navigationView;
    private Button btnImage, btnAddMedicine;
    private TextView txtWarning;
    private Medicine newMedicine = new Medicine();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.newClients:
                        Intent intent = new Intent(addMedicineActivity.this, AddClientActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.clients:
                        Intent intent1 = new Intent(addMedicineActivity.this, MainActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.medicines:
                        Intent intent2 = new Intent(addMedicineActivity.this, AllMedicinesActivity.class);
                        startActivity(intent2);
                        break;

                }
                return false;
            }
        });
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(addMedicineActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        btnAddMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtName.getText().toString().equals("") || edtDesc.getText().toString().equals("")
                        || newMedicine.image == null) {
                    txtWarning.setVisibility(View.VISIBLE);
                } else {
                    txtWarning.setVisibility(View.GONE);
                    newMedicine.setName(edtName.getText().toString());
                    newMedicine.setDesc(edtDesc.getText().toString());
                    DatabaseHelper databaseHelper = new DatabaseHelper(addMedicineActivity.this);
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    databaseHelper.insertMedicine(db, newMedicine.name, newMedicine.desc, newMedicine.image);
                    Intent intent = new Intent(addMedicineActivity.this, AllMedicinesActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        newMedicine.setImage(uri.toString());
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(bitmap);

    }

    void initViews() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer);
        image = findViewById(R.id.medicineImage);
        btnImage = findViewById(R.id.btnImage);
        navigationView = findViewById(R.id.navigationView);
        btnAddMedicine = findViewById(R.id.btnAddMedicine);
        edtName = findViewById(R.id.edtTextName);
        edtDesc = findViewById(R.id.edtTextDesc);
        txtWarning = findViewById(R.id.warningMedicine);
    }
}