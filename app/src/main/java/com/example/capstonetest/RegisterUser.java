package com.example.capstonetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView banner, registerUser;
    private EditText editTextFullName, editTextAge, editTextEmail, editTextPassword, editTextPhoneNumber, editTextUsername;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        getSupportActionBar().hide();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser= (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextAge = (EditText) findViewById(R.id.age);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextUsername = (EditText) findViewById(R.id.username);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                Toast.makeText(this, "Your registration was successful!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private void registerUser(){
            String email= editTextEmail.getText().toString().trim();
            String username= editTextUsername.getText().toString().trim();
            String phonenumber= editTextPhoneNumber.getText().toString().trim();
            String password= editTextPassword.getText().toString().trim();
            String fullName = editTextFullName.getText ().toString().trim();
            String age = editTextAge.getText().toString().trim();
            String phoneNumber = editTextPhoneNumber.getText().toString().trim();
            if(fullName.isEmpty()){
                editTextFullName.setError("Full name is required!");
                editTextFullName.requestFocus ();
                return;
            }
            if(age.isEmpty()){
                editTextAge.setError("Age is required!");
                editTextAge.requestFocus();
                return;
            }
            if(Integer.parseInt(age) < 18 ){
                editTextAge.setError("You must be at least 18 years old!");
                editTextAge.requestFocus();
                return;
            }
            if(email.isEmpty()){
                editTextEmail. setError ("Email is required!");
                editTextEmail.requestFocus();
                return;
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.setError("Please provide valid email!");
                editTextEmail.requestFocus();
                return;
            }
            if(username.isEmpty()){
                editTextUsername.setError ("Username is required!");
                editTextUsername.requestFocus ();
                return;
            }
            if(phonenumber.isEmpty()){
                editTextPhoneNumber.setError ("Phone Number is required!");
                editTextPhoneNumber.requestFocus ();
                return;
            }
            if(password.isEmpty()){
                editTextPassword.setError ("Password is required!");
                editTextPassword.requestFocus ();
                return;
            }
            if(password.length() < 8){
                editTextPassword.setError ("Min password length should be 8 characters!");
                editTextPassword.requestFocus ();
                return;
            }

        //progressBar.setVisibility(View.VISIBLE);
            register();

    }

    public void register() {
        String email= editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();
        String username= editTextUsername.getText().toString().trim();
        String fullName = editTextFullName.getText ().toString().trim();
        String age = editTextAge.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        new Thread (() -> {
            try {
                Class.forName ("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com:3306?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true","admin","nuttertools");;
                Statement statement = connection.createStatement ();
                // add to RDS DB:
                statement.execute( "USE capstone");
                statement.execute( "INSERT INTO Customer VALUES(NULL,'" + username +"','" + password + "','" + fullName + "','" + phoneNumber + "','" + email + "',NULL)");
                connection.close();
            } catch (Exception e) {
                e.printStackTrace ();
            }}).start();
    }
}