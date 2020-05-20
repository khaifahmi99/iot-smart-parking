package com.example.parking_app.dynamodb;

public class User {
    private String userId;
    private String userCarPlate;
    private String userName;
    private String userPhone;
    private boolean userHasMadePayment;
    private String tsPayment;


    public User(String aUserId, String aUserCarPlateNumber, String aUserName, String aUserPhone, boolean aUserPayment, String aUserTsPayment){
        userId = aUserId;
        userCarPlate = aUserCarPlateNumber;
        userName = aUserName;
        userPhone = aUserPhone;
        userHasMadePayment = aUserPayment;
        tsPayment = aUserTsPayment;
    }




    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserCarPlate() {
        return userCarPlate;
    }

    public void setUserCarPlate(String userCarPlate) {
        this.userCarPlate = userCarPlate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public boolean isUserHasMadePayment() {
        return userHasMadePayment;
    }

    public void setUserHasMadePayment(boolean userHasMadePayment) {
        this.userHasMadePayment = userHasMadePayment;
    }

    public String getTsPayment() {
        return tsPayment;
    }

    public void setTsPayment(String tsPayment) {
        this.tsPayment = tsPayment;
    }
}
