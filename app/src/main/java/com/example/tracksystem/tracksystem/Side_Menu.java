package com.example.tracksystem.tracksystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class Side_Menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {


    private GoogleApiClient googleApiClient;
    userSession user_Session;

    TextView nav_user, nav_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side__menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_Session = new userSession(this);


        /// to change the nav header text

        SharedPreferences sharedPref;
        sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String first_name = sharedPref.getString("FirstName", "");
        String last_name = sharedPref.getString("LastName", "");
        String user_email = sharedPref.getString("Username", "");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        nav_user = (TextView) hView.findViewById(R.id.user_nav);
        nav_user.setText(first_name + " " + last_name);

        nav_email = (TextView) hView.findViewById(R.id.email_nav);
        nav_email.setText(user_email);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        Fragment fragment = new choose_trackOrTimeTable();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.screen_area, fragment);

        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.side__menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();
        if (id == R.id.nav_profile) {

            fragment = new profile1();
        } else if (id == R.id.nav_main) {
            fragment = new choose_trackOrTimeTable();
        } else if (id == R.id.nav_Tracking) {
            fragment = new TrackTrain();
        } else if (id == R.id.nav_TimeTable) {
            fragment = new TimeTable_Options();
        } else if (id == R.id.nav_ContactUs) {

            fragment = new ContactUs();
        } else if (id == R.id.nav_logOut) {


            SharedPreferences sharedPref;
            sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            boolean login_check = sharedPref.getBoolean("is_login", true);
            boolean GOlogin_check = sharedPref.getBoolean("is_login", false);
            if (GOlogin_check == true) {
                final ProgressDialog dialog = ProgressDialog.show(Side_Menu.this, "",
                        "Signing out , Please wait...", true);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                dialog.dismiss();
                                user_Session.loginUser("", false, false, "", "", "");
                                Intent logOut = new Intent(Side_Menu.this, home.class);
                                logOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(logOut);
                                finish();

                            }
                        });

                    }
                }, 2000);
            } else {
                final ProgressDialog dialog = ProgressDialog.show(Side_Menu.this, "",
                        "Signing out , Please wait...", true);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        dialog.dismiss();
                        user_Session.loginUser("", false, false, "", "", "");
                        Intent logOut = new Intent(Side_Menu.this, home.class);
                        logOut.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(logOut);
                        finish();

                    }
                }, 2000);
            }
        }


        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area, fragment);
            ft.addToBackStack(null);
            ft.commit();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
