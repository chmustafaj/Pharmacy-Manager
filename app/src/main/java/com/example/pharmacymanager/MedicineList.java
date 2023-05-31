package com.example.pharmacymanager;

import java.util.ArrayList;

public class MedicineList {
    int id;
    ArrayList<Integer> medicinesArr = new ArrayList<>();

    public MedicineList() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Integer> getMedicinesArr() {
        return medicinesArr;
    }

    public void setMedicinesArr(ArrayList<Integer> medicinesArr) {
        this.medicinesArr = medicinesArr;
    }

    public void addToMedicineArr(int medicineId) {
        medicinesArr.add(medicineId);
    }

    public MedicineList(int id, ArrayList<Integer> medicinesArr) {
        this.id = id;
        this.medicinesArr = medicinesArr;
    }
}
