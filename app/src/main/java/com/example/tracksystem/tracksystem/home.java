package com.example.tracksystem.tracksystem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

public class home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void LoginClick(View view) {
        Intent Login_Activity = new Intent(home.this, log_in.class);
        startActivity(Login_Activity);
    }

    public void SignUpClick(View view) {
        Intent SignUp_Activity = new Intent(home.this, Sign_up.class);
        startActivity(SignUp_Activity);
    }
}
