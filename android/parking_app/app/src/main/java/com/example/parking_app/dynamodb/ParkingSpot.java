package com.example.parking_app.dynamodb;

public class ParkingSpot {
    private String parking_spot_id;
    private boolean isBooked;
    private String parking_status;
    private String captured_plate_number;
    private User user;
    private String ts_booking;

    public ParkingSpot(String aParkingSpot, boolean aIsBooked, String aParkingStatus, String aCapturePlateNumber, User aUser, String aBookingTs ){
        parking_spot_id = aParkingSpot;
        isBooked = aIsBooked;
        parking_status = aParkingStatus;
        captured_plate_number = aCapturePlateNumber;
        user = aUser;
        ts_booking = aBookingTs;
    }
}
