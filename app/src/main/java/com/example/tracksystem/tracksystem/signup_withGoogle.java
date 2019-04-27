package com.example.tracksystem.tracksystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup_withGoogle extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {


    TextView signIn_Text, email;

    EditText firstname, lastname, ph_num;
    String fName, lName, mail, phone_Number;
    Button go_Signup;
    String server_url = "https://tracksystem7.000webhostapp.com/Gsignup.php";

    boolean validate_fName = true;
    boolean validate_lName = true;
    boolean validate_phNumber = true;
    AlertDialog.Builder builder;

    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_google);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        Bundle bundle = getIntent().getExtras();
        fName = bundle.getString("firstName");
        lName = bundle.getString("LastName");
        mail = bundle.getString("EMail");


        firstname = (EditText) findViewById(R.id.etFirstName);
        firstname.setText(fName);
        lastname = (EditText) findViewById(R.id.etLastName);
        lastname.setText(lName);
        email = (TextView) findViewById(R.id.etEmail);
        email.setText(mail);
        ph_num = (EditText) findViewById(R.id.etPhone);
        go_Signup = (Button) findViewById(R.id.Google_bu);
        builder = new AlertDialog.Builder(signup_withGoogle.this);


        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("^[\\p{L} .'-]+$");
                    Matcher ms = ps.matcher(firstname.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        firstname.setError("invalid First Name");
                        firstname.setTextColor(Color.RED);
                        validate_fName = false;
                        Validate_signUpButton();
                    } else {
                        firstname.setTextColor(Color.WHITE);
                        validate_fName = true;
                        Validate_signUpButton();
                    }

                }
            }
        });

        lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("^[\\p{L} .'-]+$");
                    Matcher ms = ps.matcher(lastname.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        lastname.setError("invalid Last Name");
                        lastname.setTextColor(Color.RED);
                        validate_lName = false;
                        Validate_signUpButton();
                    } else {
                        lastname.setTextColor(Color.WHITE);
                        validate_lName = true;
                        Validate_signUpButton();
                    }

                }
            }
        });
        ph_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("^01[0-2]{1}[0-9]{8}");
                    Matcher ms = ps.matcher(ph_num.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        ph_num.setError("Wrong Phone Number");
                        ph_num.setTextColor(Color.RED);
                        validate_phNumber = false;
                        Validate_signUpButton();
                    } else {
                        ph_num.setTextColor(Color.WHITE);
                        validate_phNumber = true;
                        Validate_signUpButton();
                    }

                }
            }
        });


        go_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fName = firstname.getText().toString();
                lName = lastname.getText().toString();
                phone_Number = ph_num.getText().toString();

                //Validate all InputText

                if (fName.equals("") || lName.equals("") || phone_Number.equals("")) {
                    builder.setTitle("");
                    builder.setMessage("You should fill all fields");
                    displayAlert("Input_error");
                } else {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                public void onResponse(String response) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(response);

                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        String message = jsonObject.getString("message");

                                        builder.setTitle("");
                                        builder.setMessage(message);
                                        displayAlert(code);
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
                            params.put("firstName", fName);
                            params.put("lastName", lName);
                            params.put("email", mail);
                            params.put("phoneNumber", phone_Number);

                            return params;
                        }
                    };

                    MySingleton.getInstance(signup_withGoogle.this).addTorequestque(stringRequest);
                }
            }

        });


    }

    public void Validate_signUpButton() {
        if (validate_fName && validate_lName && validate_phNumber) {
            go_Signup.setEnabled(true);
            go_Signup.setBackgroundResource(R.drawable.google);
        } else {
            go_Signup.setEnabled(false);
            go_Signup.setBackgroundResource(R.drawable.log_but_2);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

                finish();
                Intent intent = new Intent(signup_withGoogle.this, Sign_up.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (code.equals("Input_error")) {
                    ph_num.setText("");
                } else if (code.equals("registration_success")) {


                    final ProgressDialog dialog = ProgressDialog.show(signup_withGoogle.this, "",
                            "Signing UP , Please wait...", true);

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent VirifcationCode = new Intent(signup_withGoogle.this, confirm_code_google.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phoneNumber", phone_Number);
                            bundle.putString("EMail", mail);
                            bundle.putString("firstName", fName);
                            bundle.putString("lastName", lName);
                            VirifcationCode.putExtras(bundle);

                            startActivity(VirifcationCode);
                            finish();
                        }
                    }, 3000);


                } else if (code.equals("registration_failed")) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            finish();
                            Intent intent = new Intent(signup_withGoogle.this, Sign_up.class);
                            startActivity(intent);
                        }
                    });
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}
