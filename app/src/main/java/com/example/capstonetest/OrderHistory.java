package com.example.capstonetest;

import android.os.Bundle;

import com.example.capstonetest.Entity.BookedSlots;
import com.example.capstonetest.Entity.GlobalClass;
import com.example.capstonetest.Entity.OrderHistoryEntity;
import com.example.capstonetest.Entity.recyclerAdapter;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstonetest.databinding.ActivityOrderHistoryBinding;

import java.security.spec.ECField;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Date;

public class OrderHistory extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityOrderHistoryBinding binding;
    private ArrayList<OrderHistoryEntity> historyList;
    private RecyclerView recyclerView;
    GlobalClass globalClass = GlobalClass.getInstance();
    User userObject = globalClass.getUserObj();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        recyclerView = findViewById(R.id.recyclerView);
        binding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        historyList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        populateInfo();
        setAdapter();

    }

    private void setAdapter() {
        recyclerAdapter adapter = new recyclerAdapter(historyList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void populateInfo() {
        try {
            DBMain db = new DBMain();
            ResultSet rs = db.search("SELECT ParkingHistory.LicensePlate,Parkingslot.Location,ParkingHistory.EntranceDate,ParkingHistory.ExitDate,ParkingHistory.Price FROM ParkingHistory,Parkingslot WHERE ParkingHistory.ParkingSlot =  Parkingslot.ID AND ParkingHistory.CustomerID ='"+ userObject.getUserID()+"';");
            ResultSetMetaData rsD = rs.getMetaData();
            int x = 0;
            int columns = rsD.getColumnCount();
            while (rs.next()) {
                    String licensePlate = rs.getString("LicensePlate");
                    OrderHistoryEntity temp = new OrderHistoryEntity();
                    temp.numberPlate = rs.getString("LicensePlate");
                    temp.userID = userObject.getUserID();
                    temp.location = rs.getString("Location");
                    temp.startTime = rs.getString("EntranceDate");
                    temp.checkoutTime = rs.getString("ExitDate");
                    temp.price = rs.getString("Price");
                    historyList.add(temp);
            }}
            catch(Exception e){
                e.printStackTrace();
            }
    }
}