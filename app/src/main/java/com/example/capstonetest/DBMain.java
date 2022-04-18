package com.example.capstonetest;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capstonetest.Entity.GlobalClass;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.transform.Result;


public class DBMain extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private String username;
    private String password;
    static Connection con;
    GlobalClass globalClass= GlobalClass.getInstance();
    User userObject = globalClass.getUserObj();

    private static void getCon(){
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://capstone.cayk5wnefmpg.us-east-1.rds.amazonaws.com:3306/capstone?allowPublicKeyRetrieval=true&useSSL=false&autoReconnect=true","admin","nuttertools");
            System.out.println("Connection established.");
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void closeCon() throws SQLException {
        con.close();
    }
    public User read(String Username, String Pass){
       return new auth().doInBackground(Username,Pass);
    }
    public ResultSet search(String sql){

        return new search().doInBackground(sql);
    }

    public void executeQuery(String sql){
        try{
            getCon();
            Statement st = con.createStatement();
            System.out.println(sql);
            st.executeQuery(sql);
            System.out.println("Query executed!");
            con.close();
        }catch (Exception e) {
            System.out.println("something went wrong");
        }
    }
    public void executeUpdate(String sql){
        try{
            getCon();
            Statement st = con.createStatement();
            System.out.println(sql);
            st.executeUpdate(sql);
            System.out.println("Query executed!");
            con.close();
        }catch (Exception e) {
            System.out.println("something went wrong");
        }
    }

    class auth extends AsyncTask<String, String, User> {
        String records = "", error = "";

        @Override
        protected User doInBackground(String... data) {
            try {
                getCon();
                Log.d(TAG,"This is " + data[0] + " and " + data[1]);
                String q1 = "select * from Customer WHERE Username = '" + data[0] + "' AND Password = '" + data[1] + "';";
                Statement statement = con.createStatement();
                PreparedStatement ps = con.prepareStatement(q1);
                //statement.execute("USE capstone");
                statement.execute(q1);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int userID = Integer.parseInt(rs.getString("ID"));
                    String fullName = rs.getString("FullName");
                    String username = rs.getString("Username");
                    String pass = rs.getString("Password");
                    String mobileNumber = rs.getString("MobileNumber");
                    String emailAddress = rs.getString("Email");
                    userObject = new Manager(userID, fullName, username, pass, mobileNumber, emailAddress, "Customer");
                    globalClass.setUserObj(userObject);
                    return userObject;
                } else {
                    System.out.println("Wrong password");
                }
                con.close();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    class search extends AsyncTask<String, String, ResultSet> {
        String records = "", error = "";

        @Override
        protected ResultSet doInBackground(String... data) {
            try {
                getCon();
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(data[0]);
                ResultSetMetaData rsD = rs.getMetaData();
                int columns = rsD.getColumnCount();
                if(columns ==0)
                    return null;
                sleep(1000);
                return rs;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}


