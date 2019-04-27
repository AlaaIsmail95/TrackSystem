package com.example.tracksystem.tracksystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class change_pass extends AppCompatActivity {

    EditText pass, con_pass;
    String newPassword, ConfirmPassword, mail, phoneNumber;

    Button btChange;

    String server_url = "https://tracksystem7.000webhostapp.com/resetpass.php";

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        pass = (EditText) findViewById(R.id.et_newPass);
        con_pass = (EditText) findViewById(R.id.et_conPass);

        btChange = (Button) findViewById(R.id.change_bu) ;

        builder = new AlertDialog.Builder(change_pass.this);



        Bundle bundle = getIntent().getExtras();
        mail = bundle.getString("mail");
        phoneNumber = bundle.getString("phoneNumber");


        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Pattern ps = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
                    Matcher ms = ps.matcher(pass.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        pass.setError("Password shoud be at least 1 digit , 1 cap-character , 1 small-character");
                        pass.setTextColor(Color.RED);
                        btChange.setEnabled(false);
                        btChange.setBackgroundResource(R.drawable.log_but_2);
                    }else {
                        pass.setTextColor(Color.BLACK);
                        btChange.setEnabled(true);
                        btChange.setBackgroundResource(R.drawable.log_but);
                    }

                }
            }
        });

    }

    public void Change_password(View view) {
        newPassword = pass.getText().toString();
        ConfirmPassword = con_pass.getText().toString();
        if (newPassword.equals("") || ConfirmPassword.equals("")) {
            builder.setTitle("");
            builder.setMessage("You should fill all fields");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    pass.setText("");
                    con_pass.setText("");
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            if (!(newPassword.equals(ConfirmPassword))) {
                builder.setTitle("");
                builder.setMessage("Password not match");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pass.setText("");
                        con_pass.setText("");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("password", newPassword);
                        params.put("Email", mail);
                        params.put("phoneNumber", phoneNumber);
                        return params;
                    }
                };

                MySingleton.getInstance(change_pass.this).addTorequestque(stringRequest);


                builder.setTitle("");
                builder.setMessage("password changed");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent login_activity = new Intent(change_pass.this,log_in.class);
                        startActivity(login_activity);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        }
    }
}
