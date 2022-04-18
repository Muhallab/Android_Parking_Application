package com.example.capstonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonetest.Entity.GlobalClass;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout reserveParkingBtn,parkingStatusBtn,orderHistory;
    private ImageView logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        GlobalClass globalClass= GlobalClass.getInstance();
        User userObject = globalClass.getUserObj();
        TextView role = (TextView) findViewById(R.id.roleMessage);
        role.setText(userObject.getRole() + " Page");
        reserveParkingBtn = (LinearLayout) findViewById(R.id.reserveParkingBtn);
        parkingStatusBtn = (LinearLayout) findViewById(R.id.parkingStatusBtn);
        orderHistory = (LinearLayout) findViewById(R.id.orderHistoryBtn);
        logoutBtn = (ImageView) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, MainActivity.class));
            }
        });
        orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, OrderHistory.class));
            }
        });
        parkingStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, ParkingStatus.class));
            }
        });
        reserveParkingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(dashboard.this, reserve_parking.class));
            }
        });

        TextView name = (TextView) findViewById(R.id.nameMessage);
        name.setText(userObject.getFullName() + "'s Page");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reserveParkingBtn:
                startActivity(new Intent(dashboard.this, reserve_parking.class));
                break;
            case R.id.logoutBtn:
                    startActivity(new Intent(dashboard.this, MainActivity.class));
                break;
            case R.id.parkingStatusBtn:
                startActivity(new Intent(dashboard.this, ParkingStatus.class));
                break;
        }
    }
}