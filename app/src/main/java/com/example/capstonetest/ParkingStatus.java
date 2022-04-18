package com.example.capstonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.capstonetest.Entity.BookedSlots;
import com.example.capstonetest.Entity.GlobalClass;
import com.example.capstonetest.Entity.ParkingSlot;

import java.util.Calendar;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class ParkingStatus extends AppCompatActivity {
    TextView location;
    TextView licensePlate;
    TextView checkIn;
    TextView checkOut;
    TextView totalPrice;
    GlobalClass globalClass;
    User userObject;
    BookedSlots bookingSlot = new BookedSlots();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_status);
        globalClass= (GlobalClass) new GlobalClass();
        userObject = globalClass.getUserObj();
        location = (TextView) findViewById(R.id.locationText);
        licensePlate = (TextView) findViewById(R.id.licensePlateText);
        checkIn = (TextView) findViewById(R.id.checkInText);
        checkOut = (TextView) findViewById(R.id.checkOutText);
        totalPrice = (TextView) findViewById(R.id.totalPriceText);
        bookingSlot.userID = userObject.getUserID();
        populateData();


    }
    public void populateData(){
        try {
            DBMain db = new DBMain();
            ResultSet rs = db.search("SELECT ReservedParking.ParkingSlot,ReservedParking.LicensePlate,ReservedParking.StartDateTime,ReservedParking.EndDateTime,Parkingslot.Location FROM ReservedParking,Parkingslot WHERE ReservedParking.ParkingSlot = Parkingslot.ID AND CustomerID = '" + userObject.getUserID() + "';");
            ResultSetMetaData rsD = rs.getMetaData();
            String[] spots = new String[10];
            int x = 0;
            int columns = rsD.getColumnCount();
            int parkingSlot=0;
            String startTime = null,endTime=null;
            while (rs.next()) {
                parkingSlot = Integer.parseInt(rs.getString("ParkingSlot"));
                location.setText(rs.getString("Location"));
                licensePlate.setText(rs.getString("LicensePlate"));
                checkIn.setText(rs.getString("EndDateTime"));
                checkOut.setText(rs.getString("StartDateTime"));
                startTime = rs.getString("StartDateTime");
                endTime = rs.getString("EndDateTime");
            }
            int parkingSlotID = parkingSlot;
            if(parkingSlotID == 1){
                bookingSlot.price = 3;
            }else if(parkingSlotID == 2){
                bookingSlot.price = 2;
            }
            else if(parkingSlotID == 3){
                bookingSlot.price = 1;
            }
            bookingSlot.slotID = parkingSlotID;
            Date startDate= (Date) new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(startTime);
            Date endDate = (Date) new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(endTime);
            Calendar calendar = new GregorianCalendar();
            if (calendar.getTime().after(endDate)){
                bookingSlot.endTime = calendar.getTime();
            }else bookingSlot.endTime = endDate;
            bookingSlot.startTime = startDate;
            bookingSlot.calcAmount();
            String amountStr=String.valueOf(bookingSlot.amount);
            totalPrice.setText(amountStr);
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}