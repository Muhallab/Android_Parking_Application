package com.example.capstonetest.Entity;

import android.util.Log;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BookedSlots implements Serializable {
    public String numberPlate,location;
    public int userID,hasPaid,slotID;
    public Date startTime, endTime, checkoutTime;
    public double price,amount;

    public BookedSlots(){}

    public BookedSlots(Integer userID,Integer slotID, String numberPlate, Date startTime, Date endTime, int hasPaid, double price,String location){
        this.userID=userID;
        this.slotID=slotID;
        this.numberPlate=numberPlate;
        this.hasPaid=hasPaid;
        this.startTime=startTime;
        this.endTime=endTime;
        this.price = price;
        this.location = location;
    }

    public void calcAmount(){
        if(this.userID!=0) {
            long diffInMillies = Math.abs(this.endTime.getTime() - this.startTime.getTime());
            long diffHour = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            long diffMin = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS) - diffHour * 60;
            if (diffMin > 0) {
                diffHour += 1;
            }
            Log.i("diffHourMin", diffHour + " " + diffMin);
            this.amount = ((int) diffHour) * this.price;
        }else{
            this.amount=0;
        }
    }

    public Boolean timeDiffValid(){
        long diffInMillies = Math.abs(this.endTime.getTime() - this.startTime.getTime());
        long diffHour = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        long diffMin = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS) - diffHour * 60;
        if(diffHour>0 || diffMin>=15)return true;
        return false;
    }

    public static Comparator<BookedSlots> DateComparator = new Comparator<BookedSlots>() {
        public int compare(BookedSlots s1, BookedSlots s2) {
            /*For ascending order*/
//            return s1.startTime.compareTo(s2.startTime);
            /*For descending order*/
            return s2.startTime.compareTo(s1.startTime);
        }
    };
}
