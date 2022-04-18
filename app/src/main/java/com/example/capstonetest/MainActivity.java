package com.example.capstonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstonetest.Entity.GlobalClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    GlobalClass global = new GlobalClass();
    DBMain DB = new DBMain();
    private TextView signIn;
    private TextView register;
    private EditText editTextusername, editTextpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextusername = (EditText) findViewById(R.id.email);
        editTextpassword = (EditText) findViewById(R.id.password);

        signIn = (Button) findViewById(R.id.signIn);
        register = (TextView) findViewById(R.id.register);
        signIn.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.signIn:
                if (loginUser()){
                    startActivity(new Intent(this, dashboard.class));
                }
                break;
        }
    }
    private boolean loginUser(){
        String username= editTextusername.getText().toString().trim();
        String password= editTextpassword.getText().toString().trim();
//        Toast.makeText(MainActivity.this,"username is " + username + " password is " + password ,Toast.LENGTH_SHORT).show();
        if(username.isEmpty()){
            editTextusername.setError("username is required!");
            editTextusername.requestFocus();
            return false;
        }
        if(password.isEmpty()){
            editTextpassword.setError("Password is required!");
            editTextpassword.requestFocus();
            return false;
        }
        User user = DB.read(username,password);
        if (user != null){
            Toast.makeText(MainActivity.this,"welcome to " + user.getRole() + " page, "+ user.getFullName(),Toast.LENGTH_LONG).show();
            global.setUserObj(user);
            return true;
        }
        else{
            editTextpassword.setError("Password and username do not match");
            editTextpassword.requestFocus();
            return false;
        }
    }
}

