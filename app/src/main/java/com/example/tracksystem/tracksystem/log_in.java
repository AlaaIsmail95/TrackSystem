package com.example.tracksystem.tracksystem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class log_in extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button Login_Click, SignUp_Click;
    EditText txtEmail, txtPass;
    static String mail, Gmail, pass;
    static String E_MAIL;
    String url_server = "https://tracksystem7.000webhostapp.com/login.php";
    String json_url = "https://tracksystem7.000webhostapp.com/results_login.json";

    String url_server_go = "https://tracksystem7.000webhostapp.com/Glogin.php";
    String json_url_go = "https://tracksystem7.000webhostapp.com/results_Glogin.json";
    AlertDialog.Builder builder;

    boolean validate_mail = true;
    boolean validate_pass = true;


    TextView forgotPassword, signUp_Text;


    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    Button google_SignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        final userSession user_session = new userSession(this);


        txtEmail = (EditText) findViewById(R.id.txtmail);
        txtPass = (EditText) findViewById(R.id.txtpassword);

        builder = new AlertDialog.Builder(log_in.this);

        forgotPassword = (TextView) findViewById(R.id.Forgot_pass);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent forgotPassword_Activity = new Intent(log_in.this, forgot_password.class);
                startActivity(forgotPassword_Activity);
            }
        });

        signUp_Text = (TextView) findViewById(R.id.signUpText);
        signUp_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SignUp_Activity = new Intent(log_in.this, Sign_up.class);
                startActivity(SignUp_Activity);
            }
        });


        Login_Click = (Button) findViewById(R.id.Sigin_Button);

        txtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                    Matcher ms = ps.matcher(txtEmail.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        txtEmail.setError("invalid E-Mail");
                        txtEmail.setTextColor(Color.RED);
                        validate_mail = false;
                        Validate_loginButton();

                    } else {
                        txtEmail.setTextColor(Color.WHITE);
                        validate_mail = true;
                        Validate_loginButton();
                    }

                }
            }
        });

        txtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Pattern ps = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$");
                    Matcher ms = ps.matcher(txtPass.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        txtPass.setError("Password length should be at least 8");
                        txtPass.setTextColor(Color.RED);
                        validate_pass = false;
                        Validate_loginButton();
                    } else {
                        txtPass.setTextColor(Color.WHITE);
                        validate_pass = true;
                        Validate_loginButton();
                    }

                }
            }
        });

        Login_Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mail = txtEmail.getText().toString().toLowerCase();
                pass = txtPass.getText().toString();

                if (mail.equals("") || pass.equals("")) {
                    builder.setTitle("SomeThing went Wrong");
                    displayAlert("Please, Fill all fields of login....");
                } else {
                    final ProgressDialog pDialog = new ProgressDialog(log_in.this);
                    pDialog.setMessage("Signing In, Please wait...");
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
                                        String message = jsonObject.getString("message");

                                        pDialog.dismiss();

                                        if (code.equals("login_failed")) {
                                            builder.setTitle("Login error...");
                                            displayAlert(message);
                                        } else if (code.equals("login_success")) {


                                            String userID = jsonObject.getString("userID");
                                            String first_name = jsonObject.getString("FirstName");
                                            String last_name = jsonObject.getString("LastName");

                                            userSession.loginUser(mail, true, false, first_name, last_name, userID);


                                            SharedPreferences sharedPref;
                                            sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                                            boolean login_check = sharedPref.getBoolean("is_login", false);


                                            finish();
                                            Intent SignedIn = new Intent(log_in.this, Side_Menu.class);
                                            SignedIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(SignedIn);
                                            (log_in.this).overridePendingTransition(0, 0);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    pDialog.dismiss();

                                    builder.setMessage("Please try Again Later...");
                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            txtEmail.setText("");
                                            txtPass.setText("");

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
                            params.put("email", mail);
                            params.put("password", pass);
                            return params;
                        }
                    };
                    MySingleton.getInstance(log_in.this).addTorequestque(stringRequest);

                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                }
            }
        });

        // Sign Up With Google Account

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        google_SignIn = (Button) findViewById(R.id.google_SignIn);
        google_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });

    }


    public void Validate_loginButton() {
        if (validate_mail && validate_pass) {
            Login_Click.setEnabled(true);
            Login_Click.setBackgroundResource(R.drawable.log_but);
        } else {
            Login_Click.setEnabled(false);
            Login_Click.setBackgroundResource(R.drawable.log_but_2);
        }
    }


    public void displayAlert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtEmail.setText("");
                txtPass.setText("");

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Alert_google(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signin() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handelResult(GoogleSignInResult result) {
        final ProgressDialog pDialog = new ProgressDialog(log_in.this);
        pDialog.setMessage("Signing In, Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Gmail = account.getEmail();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server_go,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);

                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");

                                pDialog.dismiss();
                                if (code.equals("login_failed")) {
                                    builder.setTitle("Login error...");
                                    Alert_google(message);
                                } else if (code.equals("login_success")) {


                                    String userID = jsonObject.getString("userID");
                                    String first_name = jsonObject.getString("FirstName");
                                    String last_name = jsonObject.getString("LastName");

                                    userSession.loginUser(Gmail, true, true, first_name, last_name, userID);

                                    updateUI(true);


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();


                            builder.setMessage("Please try Again Later...");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Auth.GoogleSignInApi.signOut(googleApiClient);

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
                    params.put("email", Gmail);
                    params.put("go_pluse", "1");
                    return params;
                }
            };
            MySingleton.getInstance(log_in.this).addTorequestque(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            finish();
            Intent SignedIn = new Intent(log_in.this, Side_Menu.class);
            SignedIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(SignedIn);
            (log_in.this).overridePendingTransition(0, 0);
        } else {

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handelResult(result);
        }
    }


}

