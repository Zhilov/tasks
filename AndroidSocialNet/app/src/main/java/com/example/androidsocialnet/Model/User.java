package com.example.androidsocialnet.Model;

public class User {

    private String userFirstName;
    private String userSecondName;
    private String phoneNumber;


    public User(){

    }

    public User(String userFirstName, String userSecondName, String phoneNumber) {
        this.userFirstName = userFirstName;
        this.userSecondName = userSecondName;
        this.phoneNumber = phoneNumber;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserSecondName() {
        return userSecondName;
    }

    public void setUserSecondName(String userSecondName) {
        this.userSecondName = userSecondName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
