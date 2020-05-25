package com.example.parking_app;

import android.os.Parcel;
import android.os.Parcelable;

public class BookingParcel implements Parcelable {

    private String parkingSpotId;
    private String name;
    private String carPlate;
    private String phone;
    private String timestamp;
    private String userId;


    public BookingParcel(String aParkingSpotId, String aUserId, String aName, String aCarPlate, String aPhone, String aTimestamp) {
        parkingSpotId = aParkingSpotId;
        userId = aUserId;
        name = aName;
        carPlate = aCarPlate;
        phone = aPhone;
        timestamp = aTimestamp;
    }


    private BookingParcel(Parcel in) {
        parkingSpotId = in.readString();
        userId = in.readString();
        name = in.readString();
        carPlate = in.readString();
        phone = in.readString();
        timestamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parkingSpotId);
        dest.writeString(userId);
        dest.writeString(name);
        dest.writeString(carPlate);
        dest.writeString(phone);
        dest.writeString(timestamp);
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

    public String getParkingSpotId() {
        return parkingSpotId;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getCarPlate() {
        return carPlate;
    }


    public String getPhone() {
        return phone;
    }

    public String getTimestamp() {
        return timestamp;
    }


}
