package com.example.pharmacymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "pharmacy_manager";
    private static final int DB_VERSION = 11;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlStatement = "create table clients(id integer primary key autoincrement, name text, description text, image text, color text, early integer, mid integer, late integer)";
        sqLiteDatabase.execSQL(sqlStatement);
        String sqlStatement2 = "create table medicines(id integer primary key autoincrement,name text, description text, image text)";
        sqLiteDatabase.execSQL(sqlStatement2);
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

    public void changeColor(SQLiteDatabase db, String color, int id) {

        db.rawQuery("update clients set color=" + color + " where id=?;", new String[]{String.valueOf(id)});
    }
}
