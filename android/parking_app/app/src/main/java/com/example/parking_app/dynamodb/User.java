package com.example.parking_app.dynamodb;

public class User {
    private int user_id;
    private String user_carplate_number;
    private String user_name;
    private String user_phone;
    private boolean user_hasMadePayment;
    private String ts_payment;
    private double user_fee;


    public User(int aUserId, String aUserCarPlateNumber, String aUserName, String aUserPhone, boolean aUserPayment, String aUserTsPayment, double aFee){
        user_id = aUserId;
        user_carplate_number = aUserCarPlateNumber;
        user_name = aUserName;
        user_phone = aUserPhone;
        user_hasMadePayment = aUserPayment;
        ts_payment = aUserTsPayment;
        user_fee = aFee;
    }



    public double getUser_fee() {
        return user_fee;
    }

    public void setUser_fee(double user_fee) {
        this.user_fee = user_fee;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_carplate_number() {
        return user_carplate_number;
    }

    public void setUser_carplate_number(String user_carplate_number) {
        this.user_carplate_number = user_carplate_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public boolean isUser_hasMadePayment() {
        return user_hasMadePayment;
    }

    public void setUser_hasMadePayment(boolean user_hasMadePayment) {
        this.user_hasMadePayment = user_hasMadePayment;
    }

    public String getTs_payment() {
        return ts_payment;
    }

    public void setTs_payment(String ts_payment) {
        this.ts_payment = ts_payment;
    }
}
