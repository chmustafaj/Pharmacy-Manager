package com.example.pharmacymanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {
    public boolean askedForFingerPrint = false;
    private static Utils instance;
    private SharedPreferences sharedPreferences;
    private static final String MEDICINE_ARRAY_KEY = "medicine_array_key";

    private Utils(Context context) {
        sharedPreferences = context.getSharedPreferences("data_base", Context.MODE_PRIVATE);
        //The Utils will be a singleton class. There will be only one instance of AllBooks
        if (null == getAllMedicineLists()) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            ArrayList<MedicineList> medicines = new ArrayList<>();
            editor.putString(MEDICINE_ARRAY_KEY, gson.toJson(medicines));
            editor.commit();
        }
    }

    public ArrayList<MedicineList> getAllMedicineLists() {
        Gson gson = new Gson();

        Type type = new TypeToken<ArrayList<MedicineList>>() {
        }.getType();
        ArrayList<MedicineList> allMedicineLists = gson.fromJson(sharedPreferences.getString(MEDICINE_ARRAY_KEY, null), type);
        return allMedicineLists;
    }

    public boolean addMedicineArrayToDB(MedicineList medicineList) {
        ArrayList<MedicineList> allMedicinesLists = getAllMedicineLists();
        if (null != allMedicinesLists) {
            if (allMedicinesLists.add(medicineList)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //Removing the array from the database, then adding the updated one
                editor.remove(MEDICINE_ARRAY_KEY);
                editor.putString(MEDICINE_ARRAY_KEY, gson.toJson(allMedicinesLists));
                editor.commit();
                return true;
            }
        }

        return false;
    }

    public MedicineList getMedicineListByClientID(int id) {
        ArrayList<MedicineList> medicineLists = getAllMedicineLists();
        if (null != medicineLists) {
            for (MedicineList m : medicineLists) {
                if (m.getId() == id) {
                    return m;
                }
            }

        }
        return null;
    }

    public boolean deleteMedicineList(MedicineList medicineList) {
        ArrayList<MedicineList> medicineLists = getAllMedicineLists();
        if (null != medicineList) {
            for (MedicineList b : medicineLists) {
                if (b.getId() == medicineList.getId()) {
                    if (medicineLists.remove(b)) {
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(MEDICINE_ARRAY_KEY);
                        editor.putString(MEDICINE_ARRAY_KEY, gson.toJson(medicineLists));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //There will be only one instace of the utils class
    public static Utils getInstance(Context context) {
        if (null != instance) {
            return instance;
        } else {
            instance = new Utils(context);
            return instance;
        }

    }

}
