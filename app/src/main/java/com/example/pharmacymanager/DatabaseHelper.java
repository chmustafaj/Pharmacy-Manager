package com.example.pharmacymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pharmacy_manager";
    private static final int DB_VERSION = 12;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlStatement = "create table clients(id integer primary key autoincrement, name text, description text, image text, color text, early integer, mid integer, late integer)";
        sqLiteDatabase.execSQL(sqlStatement);
        String sqlStatement2 = "create table medicines(id integer primary key autoincrement,name text, description text, image text)";
        sqLiteDatabase.execSQL(sqlStatement2);
        String sqlStatement3 = "CREATE TABLE client_medicine(client_id INTEGER, medicine_id INTEGER, FOREIGN KEY(client_id) REFERENCES clients(id), FOREIGN KEY(medicine_id) REFERENCES medicines(id), PRIMARY KEY (client_id, medicine_id))";
        sqLiteDatabase.execSQL(sqlStatement3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void insertClient(SQLiteDatabase db, String name, String desc, String uri, int early, int mid, int night) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", desc);
        values.put("early", early);
        values.put("mid", mid);
        values.put("late", night);
        values.put("image", uri.toString());
        db.insert("clients", null, values);
    }

    public void insertMedicine(SQLiteDatabase db, String name, String desc, String uri) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", desc);
        values.put("image", uri);
        db.insert("medicines", null, values);
    }
    public void assignMedicineToClient(SQLiteDatabase db, int clientId, int medicineId) {
        try {
            // Insert the assignment into the client_medicine table
            ContentValues values = new ContentValues();
            values.put("client_id", clientId);
            values.put("medicine_id", medicineId);
            long rowId = db.insert("client_medicine", null, values);

            // Check if the insertion was successful
            if (rowId == -1) {
                // There was an error assigning the medicine
                // Handle the error case, such as displaying an error message
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during the assignment process
            e.printStackTrace();
        }
    }
    public ArrayList<Medicine> getMedicinesForClient(int clientId, SQLiteDatabase db) {
        ArrayList<Medicine> medicines = new ArrayList<>();


        Cursor cursor = null;
        try {
            // Query the client_medicine table to retrieve medicines for the specified client ID
            String query = "SELECT medicines.* FROM medicines INNER JOIN client_medicine ON medicines.id = client_medicine.medicine_id WHERE client_medicine.client_id" + " = " + clientId;


            cursor = db.rawQuery(query, null);
            Log.d("TAG", "getMedicinesForClient: cursor "+cursor);

            if (cursor != null && cursor.moveToFirst()) {
                int columnIndexId = cursor.getColumnIndex("id");
                int columnIndexName = cursor.getColumnIndex("name");
                int columnIndexDescription = cursor.getColumnIndex("description");
                int columnIndexImage = cursor.getColumnIndex("image");


                // Process the query results
                do {
                    // Extract medicine details from the cursor
                    int medicineId = cursor.getInt(columnIndexId);
                    String medicineName = cursor.getString(columnIndexName);
                    String medicineDescription = cursor.getString(columnIndexDescription);
                    String medicineimage = cursor.getString(columnIndexImage);

                    // Create a Medicine object and add it to the list
                    Medicine medicine = new Medicine(medicineId, medicineName, medicineDescription, medicineimage);
                    medicines.add(medicine);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // Handle any exceptions that may occur during the retrieval process
            e.printStackTrace();
        } finally {
            // Close the cursor and database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return medicines;
    }






    public void changeColor(SQLiteDatabase db, String color, int id) {

        db.rawQuery("update clients set color=" + color + " where id=?;", new String[]{String.valueOf(id)});
    }
}
