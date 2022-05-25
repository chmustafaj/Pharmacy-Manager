package com.example.pharmacymanager;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {
    String name, desc;

    protected Client(Parcel in) {
        name = in.readString();
        desc = in.readString();
        image = in.readString();
        color = in.readString();
        byte tmpEarly = in.readByte();
        early = tmpEarly == 0 ? null : tmpEarly == 1;
        byte tmpMid = in.readByte();
        mid = tmpMid == 0 ? null : tmpMid == 1;
        byte tmpNight = in.readByte();
        night = tmpNight == 0 ? null : tmpNight == 1;
        id = in.readInt();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(desc);
        parcel.writeString(image);
        parcel.writeString(color);
        parcel.writeByte((byte) (early == null ? 0 : early ? 1 : 2));
        parcel.writeByte((byte) (mid == null ? 0 : mid ? 1 : 2));
        parcel.writeByte((byte) (night == null ? 0 : night ? 1 : 2));
        parcel.writeInt(id);
    }

    String image;
    Boolean early, mid, night;
    int id;
    String color;

    public Client(int id, String name, String desc, String image, Boolean early, Boolean mid, Boolean night) {
        this.name = name;
        this.desc = desc;
        this.image = image;
        this.early = early;
        this.mid = mid;
        this.night = night;
    }

    public Client() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getEarly() {
        return early;
    }

    public void setEarly(Boolean early) {
        this.early = early;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getMid() {
        return mid;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setMid(Boolean mid) {
        this.mid = mid;
    }

    public Boolean getNight() {
        return night;
    }

    public void setNight(Boolean night) {
        this.night = night;
    }
}
