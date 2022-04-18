package com.example.capstonetest.Entity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstonetest.R;

import java.util.ArrayList;
import java.util.Date;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {

    private ArrayList<OrderHistoryEntity> historyList;

    public recyclerAdapter(ArrayList<OrderHistoryEntity> list){
        this.historyList = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView licensePlate;
        private TextView location;
        private TextView entranceDate;
        private TextView exitDate;
        private TextView price;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            licensePlate = itemView.findViewById(R.id.licensePlateText);
            location = itemView.findViewById(R.id.locationText);
            entranceDate = itemView.findViewById(R.id.EntranceDateText);
            exitDate = itemView.findViewById(R.id.ExitDateText);
            price = itemView.findViewById(R.id.PriceText);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String licenseplate = historyList.get(position).numberPlate;
        holder.licensePlate.setText(licenseplate);
        String location = historyList.get(position).location;
        holder.location.setText(location);
        String entranceDate = historyList.get(position).startTime;
        holder.entranceDate.setText(entranceDate);
        String exitDate = historyList.get(position).checkoutTime;
        holder.exitDate.setText(exitDate);
        String price = historyList.get(position).price;
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
