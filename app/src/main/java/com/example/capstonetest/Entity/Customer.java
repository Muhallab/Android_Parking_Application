package com.example.capstonetest;


public class Customer extends User {
    private double Balance;

    public Customer(int userID, String fullName, String userName, String pass, String mobileNumber, String emailAddress, String role, double balance) {
        super(userID, fullName, userName, pass, mobileNumber, emailAddress, role);
        Balance = balance;
    }
}
