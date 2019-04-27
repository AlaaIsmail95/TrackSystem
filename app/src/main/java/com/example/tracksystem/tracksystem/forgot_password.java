package com.example.tracksystem.tracksystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class forgot_password extends AppCompatActivity {

    EditText EMail, phoneNumber;
    String mail, ph_number;
    Button sendcode_click;

    String server_url = "https://tracksystem7.000webhostapp.com/checkpass.php";
    String json_url = "https://tracksystem7.000webhostapp.com/results_checkpass.json";

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        builder = new AlertDialog.Builder(forgot_password.this);

        EMail = (EditText) findViewById(R.id.fr_mail);
        phoneNumber = (EditText) findViewById(R.id.fr_phone);

        sendcode_click = (Button) findViewById(R.id.send_codeBu);

        EMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                    Matcher ms = ps.matcher(EMail.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        EMail.setError("invalid E-Mail");
                        EMail.setTextColor(Color.RED);
                        sendcode_click.setEnabled(false);
                        sendcode_click.setBackgroundResource(R.drawable.log_but_2);
                    } else {
                        EMail.setTextColor(Color.BLACK);
                        sendcode_click.setEnabled(true);
                        sendcode_click.setBackgroundResource(R.drawable.log_but);
                    }

                }
            }
        });

        phoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("^01[0-2]{1}[0-9]{8}");
                    Matcher ms = ps.matcher(phoneNumber.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        phoneNumber.setError("Wrong Phone Number");
                        phoneNumber.setTextColor(Color.RED);
                        sendcode_click.setEnabled(false);
                        sendcode_click.setBackgroundResource(R.drawable.log_but_2);
                    } else {
                        phoneNumber.setTextColor(Color.BLACK);
                        sendcode_click.setEnabled(true);
                        sendcode_click.setBackgroundResource(R.drawable.log_but);
                    }

                }
            }
        });


        sendcode_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail = EMail.getText().toString();
                ph_number = phoneNumber.getText().toString();


                if (mail.equals("") || ph_number.equals("")) {

                    validation_fields("Input_error");
                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                public void onResponse(String response) {


                                    try {

                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");

                                        validation_fields(code);

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();

                            params.put("email", mail);
                            params.put("phoneNumber", ph_number);


                            return params;
                        }
                    };

                    MySingleton.getInstance(forgot_password.this).addTorequestque(stringRequest);
                }

            }
        });

    }


    public void validation_fields(final String code) {
        if (code.equals("Input_error")) {
            builder.setTitle("");
            builder.setMessage("You should fill all fields");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EMail.setText("");
                    phoneNumber.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        } else if (code.equals("T")) {

            Intent VirifcationCode = new Intent(forgot_password.this, confirm_code_2.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", ph_number);
            bundle.putString("Email", mail);
            VirifcationCode.putExtras(bundle);

            startActivity(VirifcationCode);
            finish();


        } else if (code.equals("F")) {

            builder.setTitle("");
            builder.setMessage("User not Exist");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EMail.setText("");
                    phoneNumber.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }

}
