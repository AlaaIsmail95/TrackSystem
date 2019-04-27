package com.example.tracksystem.tracksystem;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class timeTableDetails extends Fragment {


    String url_server = "https://tracksystem7.000webhostapp.com/tableDetails.php";
    RecyclerView recyclerView;
    AlertDialog.Builder builder;

    TextView tv_t_id, tv_t_class, tv_t_arrTime, tv_t_depTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_table_details, container, false);


        tv_t_id = (TextView) view.findViewById(R.id.tbD_trainID);
        tv_t_class = (TextView) view.findViewById(R.id.tbD_trainClass);
        tv_t_arrTime = (TextView) view.findViewById(R.id.tbD_arrivalTime);
        tv_t_depTime = (TextView) view.findViewById(R.id.tbD_DepartureTime);


        builder = new AlertDialog.Builder(getActivity());
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final Bundle bundle = getArguments();

        final String t_id = bundle.getString("id");
        final String t_arr_time = bundle.getString("arr_time");
        final String t_dep_time = bundle.getString("dep_time");
        final String t_class = bundle.getString("t_class");


        tv_t_id.setText(t_id);
        tv_t_class.setText(t_class);
        tv_t_arrTime.setText(t_arr_time);
        tv_t_depTime.setText(t_dep_time);
        String language;

        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            language = "En";
        } else {
            language = "Ar";
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_2);


        final ArrayList<tableDetailsItems> tableDetailsItems = new ArrayList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String t_station = jsonObject.getString("station_name");

                                String arrival_time = jsonObject.getString("ArrivalTime");

                                tableDetailsItems.add(new tableDetailsItems(t_station, arrival_time));
                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                            tableDetailesAdapter tableDetailesAdapter = new tableDetailesAdapter(tableDetailsItems, getActivity());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                            recyclerView.setAdapter(tableDetailesAdapter);

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
                params.put("train_id", t_id);

                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);


        return view;
    }


}
