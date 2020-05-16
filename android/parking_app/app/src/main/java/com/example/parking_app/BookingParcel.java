package com.example.parking_app;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingParcel implements Parcelable {

    String name;
    String carPlate;
    String phone;
    long timestamp;


    public BookingParcel(String aName, String aCarPlate, String aPhone, Long aTimestamp){
        name = aName;
        carPlate = aCarPlate;
        phone = aPhone;
        timestamp = aTimestamp;
    }


    private BookingParcel(Parcel in) {
       name = in.readString();
       carPlate = in.readString();
       phone = in.readString();
       timestamp = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(carPlate);
        dest.writeString(phone);
        dest.writeLong(timestamp);
    }


    public static final Parcelable.Creator<BookingParcel> CREATOR
            = new Parcelable.Creator<BookingParcel>() {
        public BookingParcel createFromParcel(Parcel in) {
            return new BookingParcel(in);
        }

        public BookingParcel[] newArray(int size) {
            return new BookingParcel[size];
        }
    };


    public String getName() {
        return name;
    }

    public String getCarPlate() {
        return carPlate;
    }


    public String getPhone() {
        return phone;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
