package com.example.tracksystem.tracksystem;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Time_table extends Fragment {

    String url_server = "https://tracksystem7.000webhostapp.com/table.php";
    RecyclerView recyclerView;
    AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_table, container, false);

        builder = new AlertDialog.Builder(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final Bundle bundle = getArguments();

        final String arrival_Station = bundle.getString("arrival_station");
        final String Departure_Station = bundle.getString("Departure_Station");
        final String train_class = bundle.getString("train_class");
        String language;

        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            language = "En";
        } else {
            language = "Ar";
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        final ArrayList<table_Items> table_item = new ArrayList<table_Items>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String train_id = jsonObject.getString("train_id");
                                String train_class = jsonObject.getString("t_class");
                                String DepartureTime = jsonObject.getString("DepartureTime");
                                String arrival_time = jsonObject.getString("ArrivalTime");

                                if (train_id.equals("no")) {
                                    pDialog.dismiss();

                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage("No train found , please select another train")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                    getFragmentManager().popBackStack();
                                                }
                                            });
                                    final AlertDialog alert = builder.create();
                                    alert.show();


                                }

                                table_item.add(new table_Items(train_id, train_class, DepartureTime, arrival_time));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            myAdapter mAdapter = new myAdapter(table_item, getActivity());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(mAdapter);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            pDialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                builder.setMessage("Please try Again Later...");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().popBackStack();


                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("class", train_class);
                params.put("ArrStation", arrival_Station);
                params.put("DesStation", Departure_Station);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String trainID = table_item.get(position).trainID.toString();
                String trainclass = table_item.get(position).trainClass.toString();
                String arr_time = table_item.get(position).arrival_time.toString();
                String dep_time = table_item.get(position).dep_time.toString();


                Bundle bundle1 = new Bundle();
                bundle1.putString("id", trainID);
                bundle1.putString("t_class", trainclass);
                bundle1.putString("arr_time", arr_time);
                bundle1.putString("dep_time", dep_time);

                Fragment tableDetails = new timeTableDetails();

                tableDetails.setArguments(bundle1);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.screen_area, tableDetails);
                ft.addToBackStack(null);
                ft.commit();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }
}