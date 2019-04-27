package com.example.tracksystem.tracksystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profile1 extends Fragment {
    AlertDialog.Builder builder;
    Button btn_change;
    EditText fname, lname, email, new_password, old_password, confirm_password, phonenum;

    String First_Name, Last_Name, phone_Num, Email, newpass, userid;

    String validateNames = "^[\\p{L} .'-]+$";
    String validatePassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    String validateMoblie = "^01[0-2]{1}[0-9]{8}";
    String validateEmail = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    String url_server_display_profile = "https://tracksystem7.000webhostapp.com/display_profile.php";
    String url_server_edit_profile = "https://tracksystem7.000webhostapp.com/edit_profile.php";


    boolean validate_fName = true;
    boolean validate_lName = true;
    boolean validate_mail = true;
    boolean validate_pass = true;
    boolean validate_phNumber = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile1, null);


        SharedPreferences sharedPref;
        sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userid = sharedPref.getString("UserID", "");

        fname = (EditText) view.findViewById(R.id.etFirstName);
        lname = (EditText) view.findViewById(R.id.etLastName);
        email = (EditText) view.findViewById(R.id.etEmail);
        old_password = (EditText) view.findViewById(R.id.etPassword);
        new_password = (EditText) view.findViewById(R.id.etNewPassword);
        confirm_password = (EditText) view.findViewById(R.id.et_conPass);
        phonenum = (EditText) view.findViewById(R.id.etPhone);
        btn_change = (Button) view.findViewById(R.id.change_Button);

        fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                First_Name = fname.getText().toString();
                if (!hasFocus) {
                    Pattern ps = Pattern.compile(validateNames);
                    Matcher ms = ps.matcher(First_Name);
                    boolean bs = ms.matches();
                    if (bs) {

                        fname.setTextColor(Color.WHITE);
                        validate_fName = true;
                        Validate_profileButton();
                    } else {
                        fname.setTextColor(Color.RED);
                        validate_fName = false;
                        Validate_profileButton();
                        fname.setError("invalid first name");
                    }
                }
            }

        });


        lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Last_Name = lname.getText().toString();
                if (!hasFocus) {
                    Pattern ps = Pattern.compile(validateNames);
                    Matcher ms = ps.matcher(Last_Name);
                    boolean bs = ms.matches();
                    if (bs) {
                        lname.setTextColor(Color.WHITE);
                        validate_lName = true;
                        Validate_profileButton();
                    } else {
                        lname.setTextColor(Color.RED);
                        validate_lName = false;
                        Validate_profileButton();
                        lname.setError("invalid last name");
                    }
                }
            }

        });


        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Email = email.getText().toString();
                if (!hasFocus) {
                    Pattern ps = Pattern.compile(validateEmail);
                    Matcher ms = ps.matcher(Email);
                    boolean bs = ms.matches();
                    if (bs) {
                        email.setTextColor(Color.WHITE);
                        validate_mail = true;
                        Validate_profileButton();
                    } else {
                        email.setTextColor(Color.RED);
                        validate_mail = false;
                        Validate_profileButton();
                        email.setError("invalid Email");
                    }
                }
            }

        });

        new_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                newpass = new_password.getText().toString();
                if (!hasFocus) {
                    Pattern ps = Pattern.compile(validatePassword);
                    Matcher ms = ps.matcher(newpass);
                    boolean bs = ms.matches();
                    if (bs) {

                        new_password.setTextColor(Color.WHITE);
                        validate_pass = true;
                        Validate_profileButton();
                    } else {

                        new_password.setTextColor(Color.RED);
                        validate_pass = false;
                        Validate_profileButton();
                        new_password.setError("Password shoud be at least 1 digit , 1 cap-character , 1 small-character");
                    }
                }
            }

        });

        phonenum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                phone_Num = phonenum.getText().toString();
                if (!hasFocus) {
                    Pattern ps = Pattern.compile(validateMoblie);
                    Matcher ms = ps.matcher(phone_Num);
                    boolean bs = ms.matches();
                    if (bs) {

                        phonenum.setTextColor(Color.WHITE);
                        validate_phNumber = true;
                        Validate_profileButton();
                    } else {

                        email.setError("invalid phoneNumber");

                        phonenum.setTextColor(Color.RED);
                        validate_phNumber = false;
                        Validate_profileButton();
                    }
                }
            }

        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server_display_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String FirstName = jsonObject.getString("FirstName");
                            String LastName = jsonObject.getString("LastName");
                            String phoneNum = jsonObject.getString("PhoneNum");
                            String Email = jsonObject.getString("Email");

                            fname.setText(FirstName);
                            lname.setText(LastName);
                            phonenum.setText(phoneNum);
                            email.setText(Email);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userid);
                return params;
            }
        };
        MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server_edit_profile,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String Answer = jsonObject.getString("answer");

                                    if (Answer.equals("Data sent !")) {

                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Data changed successfully")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                        getFragmentManager().popBackStack();
                                                    }
                                                });
                                        final AlertDialog alert = builder.create();
                                        alert.show();

                                    } else if (Answer.equals("wrong password")) {
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("Old Password doesn't match")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                        old_password.setText("");
                                                        new_password.setText("");
                                                        confirm_password.setText("");
                                                    }
                                                });
                                        final AlertDialog alert = builder.create();
                                        alert.show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("user_id", userid);
                        params.put("FirstName", fname.getText().toString());
                        params.put("LastName", lname.getText().toString());
                        params.put("PhoneNum", phonenum.getText().toString());
                        params.put("Email", email.getText().toString());
                        params.put("password", new_password.getText().toString());
                        params.put("oldPassword", old_password.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);

            }
        });

        return view;
    }

    public void Validate_profileButton() {
        if (validate_fName && validate_lName && validate_phNumber && validate_mail && validate_pass) {
            btn_change.setEnabled(true);
            btn_change.setBackgroundResource(R.drawable.log_but);
        } else {
            btn_change.setEnabled(false);
            btn_change.setBackgroundResource(R.drawable.log_but_2);
        }
    }
}
