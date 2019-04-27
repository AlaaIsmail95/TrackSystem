package com.example.tracksystem.tracksystem;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    startTracking track;

    Activity myActivity;
    boolean tracking;
    String train_class, train_speed, arrival_station, Train_ID_text, ipAddress, user_lat, user_lng, E_mail;
    double train_Lat, train_Lon;

    TextView train_class_tv, train_speed_tv, train_statin_tv, train_lon_tv, train_lat_tv;

    Marker train_marker, assuit_marker, malway_marker, Minya_marker;
    LatLng marker_pos;

    BottomSheetDialog bottomSheetDialog;
    View parentView;
    BottomSheetBehavior bottomSheetBehavior;

    FloatingActionButton getTrain;

    String url_server = "https://tracksystem7.000webhostapp.com/track.php";
    //String url_server = "https://tracksystem7.000webhostapp.com/track2_hardware.php";
    NewMessageNotification notification = new NewMessageNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        getTrain = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        bottomSheetDialog = new BottomSheetDialog(MapsActivity.this);
        parentView = getLayoutInflater().inflate(R.layout.train_marker_info, null);
        train_class_tv = (TextView) parentView.findViewById(R.id.train_class);
        train_speed_tv = (TextView) parentView.findViewById(R.id.train_Speed);
        train_statin_tv = (TextView) parentView.findViewById(R.id.train_arrival_station);
        train_lon_tv = (TextView) parentView.findViewById(R.id.train_longitude);
        train_lat_tv = (TextView) parentView.findViewById(R.id.Train_latitude);
        bottomSheetDialog.setContentView(parentView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) parentView.getParent());
        bottomSheetBehavior.setPeekHeight(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));


        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        } else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1000, new MyLocationListener(this));


        myActivity = this;


        Bundle bundle = getIntent().getExtras();
        E_mail = bundle.getString("Email");
        Train_ID_text = bundle.getString("train_id");
        ipAddress = bundle.getString("ip");
        train_class = bundle.getString("class");
        train_speed = bundle.getString("speed");
        train_Lat = bundle.getDouble("train_Lat");
        train_Lon = bundle.getDouble("train_Lon");
        arrival_station = bundle.getString("ArrStation");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        track = new startTracking();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        googleMap.setOnMarkerClickListener(this);

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setCompassEnabled(false);


        LatLng assuit = new LatLng(27.180184, 31.188141);
        assuit_marker = mMap.addMarker(new MarkerOptions().position(assuit).title("Assuit").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_marker2)));
        LatLng Malawy = new LatLng(27.732873, 30.850035);
        malway_marker = mMap.addMarker(new MarkerOptions().position(Malawy).title("Malawy").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_marker2)));
        LatLng Minya = new LatLng(28.096781, 30.753852);
        Minya_marker = mMap.addMarker(new MarkerOptions().position(Minya).title("Minya").icon(BitmapDescriptorFactory.fromResource(R.drawable.station_marker2)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(assuit, 16));

        marker_pos = new LatLng(train_Lat, train_Lon);
        train_marker = mMap.addMarker(new MarkerOptions().position(marker_pos).title("train").icon(BitmapDescriptorFactory.fromResource(R.drawable.train_marker)));

        getTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CameraPosition cp = CameraPosition.builder().target(marker_pos).zoom(13).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), 2000, null);
            }
        });

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            tracking = true;
            track.start();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker.equals(train_marker)) {
            train_class_tv.setText(train_class);
            train_speed_tv.setText(train_speed);
            train_statin_tv.setText(arrival_station);
            train_lon_tv.setText(String.valueOf(train_Lon));
            train_lat_tv.setText(String.valueOf(train_Lat));

            bottomSheetDialog.show();
        }
        if (marker.equals(assuit_marker)) {
            notification.notify(this , "Assuit",1);
        }
        if (marker.equals(malway_marker)) {
            notification.notify(this , "Malway",1);
        }
        if (marker.equals(Minya_marker)) {
            notification.notify(this , "Minya",1);
        }
        return false;
    }


    class startTracking extends Thread {
        public void run() {
            while (tracking) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                        }
                    }
                }


                LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);


                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null) {
                    location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                }

                user_lng = String.valueOf(location.getLongitude());
                user_lat = String.valueOf(location.getLatitude());


                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if (train_marker == null) {
                                        //train_marker.remove();
                                        marker_pos = new LatLng(train_Lat, train_Lon);
                                        train_marker = mMap.addMarker(new MarkerOptions().position(marker_pos).title("train").icon(BitmapDescriptorFactory.fromResource(R.drawable.train_marker)));
                                    }

                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                                    train_class = jsonObject.getString("class");
                                    train_speed = jsonObject.getString("speed");
                                    train_Lat = jsonObject.getDouble("locationX");
                                    train_Lon = jsonObject.getDouble("locationY");
                                    arrival_station = jsonObject.getString("ArrStation");


                                    train_class_tv.setText(train_class);
                                    train_speed_tv.setText(train_speed);
                                    train_statin_tv.setText(arrival_station);
                                    train_lon_tv.setText(String.valueOf(train_Lon));
                                    train_lat_tv.setText(String.valueOf(train_Lat));

                                    train_marker.setPosition(new LatLng(train_Lat, train_Lon));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                error.printStackTrace();

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("TrainID", Train_ID_text);
                        params.put("locationY", user_lng);
                        params.put("locationX", user_lat);
                        params.put("IpAddress", ipAddress);
                        params.put("Email", E_mail);
                        return params;
                    }
                };

                MySingleton.getInstance(MapsActivity.this).addTorequestque(stringRequest);


                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        tracking = false;
        track.interrupt();
        MapsActivity.this.finish();
        Intent intent = new Intent(MapsActivity.this, Side_Menu.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    private class MyLocationListener implements LocationListener {
        Context context;

        public MyLocationListener(Context context) {
            this.context = context;
        }


        public void onLocationChanged(Location location) {


        }

        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String s) {
            tracking = true;
            track = new startTracking();

            try {
                if (track.getState() == Thread.State.NEW) {
                    track.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(MapsActivity.this, "Tracking Started", Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            tracking = false;
            Toast.makeText(MapsActivity.this, "Please, Turn the GPS on. \nTracking Stopped", Toast.LENGTH_LONG).show();
        }
    }
}
