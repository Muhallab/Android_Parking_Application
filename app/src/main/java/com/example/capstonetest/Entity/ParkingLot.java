package com.example.capstonetest.Entity;

import java.util.ArrayList;
import java.util.List;

public class ParkingLot {

    public String name,userID;
    public int totalSlots,occupiedSlots,availableSlots,amount2,amount3,amount4;
    public List<Slot> slotNos = new ArrayList<>();

    public ParkingLot(){}

    public ParkingLot(String name, double latitude,double longitude, String userID,
                       int totalSlots,int occupiedSlots,
                       int amount2,int amount3,int amount4,List<Slot> slotNos){
        this.name=name;
        this.userID=userID;
        this.totalSlots=totalSlots;
        this.occupiedSlots=occupiedSlots;
        this.availableSlots=totalSlots-occupiedSlots;
        this.amount2=amount2;
        this.amount3=amount3;
        this.amount4=amount4;
        this.slotNos=slotNos;
    }

    public void setData(String varName,int varData){
        if(varName.equals("availableSlots"))
            this.availableSlots=varData;
        if(varName.equals("occupiedSlots"))
            this.occupiedSlots=varData;
        if(varName.equals("totalSlots"))
            this.totalSlots=varData;
    }

    public String allocateSlot(String numberPlate){
        Slot slotNoInfo;
        for (int i = 0; i < this.slotNos.size(); i++){
            slotNoInfo=this.slotNos.get(i);
            if(!slotNoInfo.isFull){
                slotNoInfo.isFull=true;
                slotNoInfo.numberPlate=numberPlate;
                this.slotNos.set(i,slotNoInfo);
                return slotNoInfo.name;
            }
        }
        return null;
    }

    public boolean deallocateSlot(String slotName){
        Slot slotNoInfo;
        for (int i = 0; i < slotNos.size(); i++){
            slotNoInfo=slotNos.get(i);
            if(slotNoInfo.name.equals(slotName)){
                slotNoInfo.isFull=false;
                slotNoInfo.numberPlate="NONE";

                this.slotNos.set(i,slotNoInfo);
                return true;
            }
        }
        return false;
    }

    public void allocateSpace(){
        this.availableSlots -= 1;
        this.occupiedSlots += 1;
    }
    public void deallocateSpace(){
        this.availableSlots += 1;
        this.occupiedSlots -= 1;
    }
}
