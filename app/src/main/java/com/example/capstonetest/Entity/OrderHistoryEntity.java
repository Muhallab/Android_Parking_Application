package com.example.capstonetest.Entity;

import java.io.Serializable;
import java.util.Date;

public class OrderHistoryEntity implements Serializable {
    public String numberPlate,location;
    public int userID;
    public String startTime, checkoutTime;
    public String price;

    public OrderHistoryEntity(){}

}
