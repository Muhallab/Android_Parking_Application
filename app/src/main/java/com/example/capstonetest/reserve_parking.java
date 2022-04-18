package com.example.capstonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.capstonetest.Entity.BookedSlots;
import com.example.capstonetest.Entity.GlobalClass;
import com.example.capstonetest.Entity.ParkingLot;
import com.example.capstonetest.Entity.ParkingSlot;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class reserve_parking extends AppCompatActivity {

    Spinner numberPlateSpinner, location, availableSpots;
    TextView amountText, endDateText, endTimeText;
    Button bookBtn;
    ImageView backBtn;
    LinearLayout endDate, endTime;
    ArrayList<ParkingSlot> availableSpotsList = new ArrayList<ParkingSlot>();
    List<Integer> numberPlateWheeler = new ArrayList<Integer>();
    List<String> numberPlateNumber = new ArrayList<String>();
    BookedSlots bookingSlot = new BookedSlots();
    ParkingLot parkingArea;
    Calendar calendar;
    GlobalClass globalClass = GlobalClass.getInstance();
    User userObject = globalClass.getUserObj();
    ArrayList<String> parkingSpots = new ArrayList<String>(); //= {"VIP parking", "Economy parking","Normal Parking"};
    int[] parkingSpotsIndex = new int[3];
    String[] locations = {"Dundas Bay Location"}; //= {"VIP parking", "Economy parking","Normal Parking"};
    ArrayList<String> numberPlates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_parking);
        calendar = new GregorianCalendar();
        bookingSlot.startTime=bookingSlot.endTime=bookingSlot.checkoutTime=calendar.getTime();
        bookingSlot.userID = userObject.getUserID();
        location = findViewById(R.id.location);
        availableSpots = (Spinner) findViewById((R.id.availableSpotsSpinner));
        numberPlateSpinner = (Spinner) findViewById((R.id.numperPlateSpinner));
        backBtn = (ImageView) findViewById(R.id.backBtn);
        amountText = findViewById(R.id.amountText);
        endDate = findViewById(R.id.endDate);
        endTime = findViewById(R.id.endTime);
        endDateText = findViewById(R.id.endDateText);
        endTimeText = findViewById(R.id.endTimeText);
        bookBtn = findViewById(R.id.bookBtn);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(reserve_parking.this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        location.setAdapter(adapter);
        try {
            populateSpinnerAvailableSpots();
            populateSpinnerNumberPlates();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        backBtn.setOnClickListener(view -> startActivity(new Intent(this, dashboard.class)));
        endDate.setOnClickListener(view -> {showDatePicker(endDateText);});
        endTime.setOnClickListener(view -> {showDatePicker(endTimeText);});
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(endDateText);
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(endTimeText);
            }
        });
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(bookingSlot.endTime.equals(bookingSlot.startTime)){
                    Toast.makeText(reserve_parking.this,
                            "Please set the end time!", Toast.LENGTH_SHORT).show();
                }else if(!bookingSlot.timeDiffValid()){
                    Toast.makeText(reserve_parking.this,
                            "Less time difference (<15 minutes)!", Toast.LENGTH_SHORT).show();
                }else{
                   DBMain db = new DBMain();
                   java.util.Date date = null;
                   java.sql.Timestamp timeStampEnd = null;
                   java.sql.Timestamp timeStampStart = null;
                   SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                   bookingSlot.numberPlate = numberPlates.get(numberPlateSpinner.getSelectedItemPosition());
                   bookingSlot.location = locations[location.getSelectedItemPosition()];
                   java.sql.Date dt = new java.sql.Date(bookingSlot.endTime.getTime());
                   java.sql.Time sqlTime = new java.sql.Time(bookingSlot.endTime.getTime());
                   java.sql.Date dt2 = new java.sql.Date(bookingSlot.startTime.getTime());
                   java.sql.Time sqlTime2 = new java.sql.Time(bookingSlot.startTime.getTime());
                   try {
                       date = simpleDateFormat.parse(dt.toString()+" "+sqlTime.toString());
                       timeStampEnd = new java.sql.Timestamp(date.getTime());
                       date = simpleDateFormat.parse(dt2.toString()+" "+sqlTime2.toString());
                       timeStampStart = new java.sql.Timestamp(date.getTime());

                   } catch (ParseException e) {
                       e.printStackTrace();
                   }
                   if(db.search("SELECT ID FROM AvailableParking WHERE ParkingSlot = '"+bookingSlot.slotID+"';")!=null){
                        db.executeUpdate("INSERT INTO ReservedParking(ParkingSlot,LicensePlate,CustomerID,EndDateTime,StartDateTime) VALUES('"+bookingSlot.slotID+"','"+bookingSlot.numberPlate+"','"+bookingSlot.userID+"',CAST('"+timeStampEnd+"' AS DATETIME),CAST('"+timeStampStart+"' AS DATETIME));");
                        db.executeQuery("SET SQL_SAFE_UPDATES = 0;");
                        db.executeUpdate("DELETE FROM AvailableParking WHERE ParkingSlot = '"+bookingSlot.slotID+"';");
                   }
                   Toast.makeText(reserve_parking.this, "reservation Successful", Toast.LENGTH_LONG).show();
                   startActivity(new Intent(reserve_parking.this, dashboard.class));
               }
            }
        });
    }

    private void populateSpinnerAvailableSpots() throws SQLException {
        try {
            DBMain db = new DBMain();
            ResultSet rs = db.search("SELECT ParkingSlot FROM AvailableParking;");
            ResultSetMetaData rsD = rs.getMetaData();
            String[] spots = new String[3];
            int x = 0;
            int columns = rsD.getColumnCount();
            while (rs.next()) {
                    parkingSpotsIndex[x] = Integer.parseInt(rs.getString("ParkingSlot"));
                    if (Integer.parseInt(rs.getString("ParkingSlot")) == 1) {
                        spots[x] = "Near Entrance to destination : 3$/h";
                        availableSpotsList.add(new ParkingSlot(1, "Near Entrance to destination", 3.0));
                    } else if (Integer.parseInt(rs.getString("ParkingSlot")) == 2) {
                        spots[x] = "Near exit of parking lot : 2$/h";
                        availableSpotsList.add(new ParkingSlot(2, "Near exit of parking lot", 2.0));
                    } else if (Integer.parseInt(rs.getString("ParkingSlot")) == 3) {
                        spots[x] = "Near Entrance to parking lot : 1$/h";
                        availableSpotsList.add(new ParkingSlot(3, "Near Entrance to parking lot", 1.0));
                    }
                    x++;
            }
            db.closeCon();
            int i;
            for (i = 0; i < 3; i++)
                if (spots[i] != null) {
                    parkingSpots.add(spots[i]);
                }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(reserve_parking.this, android.R.layout.simple_spinner_item, parkingSpots);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            availableSpots.setAdapter(adapter);
        }catch (Exception e) {
            SharedPreferences msalPrefs = getSharedPreferences("com.microsoft.identity.client.account_credential_cache", Context.MODE_PRIVATE);
            msalPrefs.edit().clear().commit();
            Log.w("MSAL", e);
        }

    }
    private void populateSpinnerNumberPlates() throws SQLException {
        try {
            DBMain db = new DBMain();
            ResultSet rs = db.search("SELECT LicensePlate FROM Vehicle WHERE CustomerID ='" +userObject.getUserID() +"';");
            ResultSetMetaData rsD = rs.getMetaData();
            String[] spots = new String[10];
            int x = 0;
            int columns = rsD.getColumnCount();
            while (rs.next()) {
                numberPlates.add(rs.getString("LicensePlate"));
            }
            db.closeCon();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(reserve_parking.this, android.R.layout.simple_spinner_item, numberPlates);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Toast.makeText(reserve_parking.this, availableSpots.toString(), Toast.LENGTH_LONG).show();
            numberPlateSpinner.setAdapter(adapter);
        }catch (Exception e) {
            SharedPreferences msalPrefs = getSharedPreferences("com.microsoft.identity.client.account_credential_cache", Context.MODE_PRIVATE);
            msalPrefs.edit().clear().commit();
            Log.w("MSAL", e);
        }

    }
    private void showDatePicker(final TextView button) {
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, final int date) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,date);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy");
                button.setText(simpleDateFormat.format(calendar.getTime()));
                bookingSlot.endTime = bookingSlot.checkoutTime = calendar.getTime();
                calcRefreshAmount();
            }
        };
        DatePickerDialog datePickerDialog=new DatePickerDialog(reserve_parking.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }
    private void showTimePicker(final TextView button) {
        TimePickerDialog.OnTimeSetListener timeSetListener= new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND, 0);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm a");
                bookingSlot.endTime = bookingSlot.checkoutTime = calendar.getTime();

                if(bookingSlot.endTime.after(bookingSlot.startTime)){
                    button.setText(simpleDateFormat.format(calendar.getTime()));
                    bookingSlot.endTime = bookingSlot.checkoutTime = calendar.getTime();
                    calcRefreshAmount();
                }else{
                    bookingSlot.endTime = bookingSlot.checkoutTime = bookingSlot.startTime;
                    Toast.makeText(reserve_parking.this,
                            "Please select a time after Present time!", Toast.LENGTH_SHORT).show();
                }
            }
        };
        TimePickerDialog timePickerDialog=new TimePickerDialog(reserve_parking.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
        timePickerDialog.show();
    }
    private void calcRefreshAmount() {
        setParkingSpotPrice();
        bookingSlot.calcAmount();
        String amountStr=String.valueOf(bookingSlot.amount);
        amountText.setText(amountStr);
    }
    private void setParkingSpotPrice(){
        int parkingSlotID = parkingSpotsIndex[availableSpots.getSelectedItemPosition()];
        if(parkingSlotID == 1){
            bookingSlot.price = 3;
        }else if(parkingSlotID == 2){
            bookingSlot.price = 2;
        }
        else if(parkingSlotID == 3){
            bookingSlot.price = 1;
        }
        bookingSlot.userID = userObject.getUserID();
        bookingSlot.slotID = parkingSlotID;


    }
}