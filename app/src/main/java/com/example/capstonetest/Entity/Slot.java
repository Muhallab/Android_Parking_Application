package com.example.capstonetest.Entity;
import java.io.Serializable;

public class Slot implements Serializable{
    public String name,numberPlate;
    public boolean isFull;

    public Slot(){}

    public Slot(String name, boolean isFull){
        this.name=name;
        this.isFull=isFull;
        this.numberPlate="NONE";
    }
}