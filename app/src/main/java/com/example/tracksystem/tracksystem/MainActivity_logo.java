package com.example.tracksystem.tracksystem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;

public class MainActivity_logo extends AppCompatActivity {
     boolean connectToInterNet = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logo);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connectToInterNet = true;
        } else {
            connectToInterNet = false;
        }

        if(connectToInterNet){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    SharedPreferences sharedPref;
                    sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                    boolean login_check = sharedPref.getBoolean("is_login", false);

                    if (login_check == true) {
                        Intent SignedIn = new Intent(MainActivity_logo.this, Side_Menu.class);

                        startActivity(SignedIn);
                        (MainActivity_logo.this).overridePendingTransition(0, 0);

                    } else {
                        Intent Home_Activity = new Intent(MainActivity_logo.this, home.class);
                        startActivity(Home_Activity);
                        (MainActivity_logo.this).overridePendingTransition(0, 0);
                    }
                    finish();

                    /*
                    if (logedIn) {
                        Intent SignedIn = new Intent(MainActivity_logo.this, Side_Menu.class);

                        startActivity(SignedIn);
                    } else if (logedIn_withgoogle) {
                        Intent SignedIn = new Intent(MainActivity_logo.this, Side_Menu.class);

                        startActivity(SignedIn);
                    } else {
                        Intent Home_Activity = new Intent(MainActivity_logo.this, home.class);
                        startActivity(Home_Activity);
                    }*/
                }
            }, 3000);
        }

        else{
            Toast.makeText(MainActivity_logo.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }


}
