package com.example.tracksystem.tracksystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrackTrain extends Fragment {

    AutoCompleteTextView train_ID;
    String Train_ID_text, ipAddress, lng, lat;
    Button bu_track;
    double longitude, latitude;
    boolean ValidTrainId = false;

    AlertDialog.Builder builder;

    String train_class, train_speed, arrival_station;
    double train_Lat, train_Lon;
    String trains_IDs[] = {"1", "2012", "2018", "797", "2007"};

    // String url_server = "https://tracksystem7.000webhostapp.com/track.php";
    String url_server = "https://tracksystem7.000webhostapp.com/track2_hardware.php";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_track_train, null);

        final NetInfo netInfo = new NetInfo(getActivity());
        train_ID = (AutoCompleteTextView) view.findViewById(R.id.train_ID);

        SharedPreferences sharedPref;
        sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        final String E_mail = sharedPref.getString("Username", "");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, trains_IDs);
        train_ID.setAdapter(arrayAdapter);
        bu_track = (Button) view.findViewById(R.id.tracking_button);
        bu_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipAddress = netInfo.getIPAddress();

                Train_ID_text = train_ID.getText().toString();

                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                        }
                    }
                } else {

                    LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);


                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                    }

                    try {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lng = String.valueOf(longitude);
                    lat = String.valueOf(latitude);


                    for (int i = 0; i < trains_IDs.length; i++) {
                        if (Train_ID_text.equals(trains_IDs[i])) {
                            ValidTrainId = true;
                            break;
                        }

                        if ((i + 1) == trains_IDs.length && ValidTrainId == false) {
                            Toast.makeText(getActivity(), "Invalid Train ID", Toast.LENGTH_LONG).show();
                        }
                    }


                    if (ValidTrainId) {

                        final ProgressDialog pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Please wait...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);

                                            String code = jsonObject.getString("code");
                                            pDialog.dismiss();
                                            if (code.equals("tracking_success")) {
                                                train_class = jsonObject.getString("class");
                                                train_speed = jsonObject.getString("speed");
                                                train_Lat = jsonObject.getDouble("locationX");
                                                train_Lon = jsonObject.getDouble("locationY");
                                                arrival_station = jsonObject.getString("ArrStation");

                                                Intent Maps_actvity = new Intent(getActivity(), MapsActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("Email", E_mail);
                                                bundle.putString("train_id", Train_ID_text);
                                                bundle.putString("ip", ipAddress);
                                                bundle.putString("class", train_class);
                                                bundle.putString("speed", train_speed);
                                                bundle.putDouble("train_Lat", train_Lat);
                                                bundle.putDouble("train_Lon", train_Lon);
                                                bundle.putString("ArrStation", arrival_station);
                                                Maps_actvity.putExtras(bundle);
                                                startActivity(Maps_actvity);
                                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                                                getActivity().finish();

                                            } else {
                                                builder.setMessage("the train didn't Start the trip yet");
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        train_ID.setText("");

                                                    }
                                                });
                                                AlertDialog alertDialog = builder.create();
                                                alertDialog.show();
                                            }

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
                                params.put("locationY", lng);
                                params.put("locationX", lat);
                                params.put("IpAddress", ipAddress);
                                params.put("Email", E_mail);
                                return params;
                            }
                        };
                        MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);

                        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    }
                }
            }
        });

        return view;
    }


}
