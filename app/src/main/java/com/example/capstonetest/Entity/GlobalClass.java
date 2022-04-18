package com.example.capstonetest.Entity;


import com.example.capstonetest.User;

public class GlobalClass {
    private User userObj;
    private static GlobalClass singelton= null;

    public static GlobalClass getInstance(){
        if (singelton == null)
            singelton = new GlobalClass();
        return singelton;
    }
    public User getUserObj(){
        return singelton.userObj;
    }
    public void setUserObj(User userObj){
        this.singelton.userObj =userObj;
    }


}
