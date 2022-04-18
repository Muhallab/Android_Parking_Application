package com.example.capstonetest;

import java.security.PrivateKey;

public class User {
    private int userID;
    private String fullName;
    private String userName;
    private String pass;
    private String mobileNumber;
    private String emailAddress;
    protected String role;

    public User(int userID, String fullName, String userName, String pass, String mobileNumber, String emailAddress, String role) {
        this.userID = userID;
        this.fullName = fullName;
        this.userName = userName;
        this.pass = pass;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPass() {
        return pass;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
