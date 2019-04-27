package com.example.tracksystem.tracksystem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.Spanned;
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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sign_up extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextView signIn_Text;

    EditText firstname, lastname, email, pass, confirmPass, ph_num;
    String fName, lName, mail, Password, ConfirmPassword, phone_Number;
    Button bt_Signup, google_SignUp;

    String server_url = "https://tracksystem7.000webhostapp.com/check_register1.php";
    String json_url = "https://tracksystem7.000webhostapp.com/results_register.json";

    AlertDialog.Builder builder;

    boolean validate_fName = true;
    boolean validate_lName = true;
    boolean validate_mail = true;
    boolean validate_pass =true;
    boolean validate_phNumber = true;

    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firstname = (EditText) findViewById(R.id.etFirstName);
        lastname = (EditText) findViewById(R.id.etLastName);
        email = (EditText) findViewById(R.id.etEmail);
        pass = (EditText) findViewById(R.id.etPassword);
        confirmPass = (EditText) findViewById(R.id.etConfirmPassword);
        ph_num = (EditText) findViewById(R.id.etPhone);
        bt_Signup = (Button) findViewById(R.id.SignUp_Button);
        builder = new AlertDialog.Builder(Sign_up.this);

        signIn_Text = (TextView) findViewById(R.id.signInText);
        signIn_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login_Activity = new Intent(Sign_up.this, log_in.class);
                startActivity(Login_Activity);
            }
        });





        firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Pattern ps = Pattern.compile("^[\\p{L} .'-]+$");
                    Matcher ms = ps.matcher(firstname.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        firstname.setError("invalid First Name");
                        firstname.setTextColor(Color.RED);
                        validate_fName = false;
                        Validate_signUpButton();
                    }else {
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
                if (!hasFocus){
                    Pattern ps = Pattern.compile("^[\\p{L} .'-]+$");
                    Matcher ms = ps.matcher(lastname.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        lastname.setError("invalid Last Name");
                        lastname.setTextColor(Color.RED);
                        validate_lName = false;
                        Validate_signUpButton();
                    }else {
                        lastname.setTextColor(Color.WHITE);
                        validate_lName = true;
                        Validate_signUpButton();
                    }

                }
            }
        });



        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Pattern ps = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
                    Matcher ms = ps.matcher(email.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        email.setError("invalid E-Mail");
                        email.setTextColor(Color.RED);
                        validate_mail = false;
                        Validate_signUpButton();
                    }else {
                        email.setTextColor(Color.WHITE);
                        validate_mail = true;
                        Validate_signUpButton();
                    }

                }
            }
        });



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
                        validate_pass = false;
                        Validate_signUpButton();
                    }else {
                        pass.setTextColor(Color.WHITE);
                        validate_pass = true;
                        Validate_signUpButton();
                    }

                }
            }
        });

        ph_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Pattern ps = Pattern.compile("^01[0-2]{1}[0-9]{8}");
                    Matcher ms = ps.matcher(ph_num.getText().toString());
                    boolean bs = ms.matches();
                    if (bs == false) {
                        ph_num.setError("Wrong Phone Number");
                        ph_num.setTextColor(Color.RED);
                        validate_phNumber = false;
                        Validate_signUpButton();
                    }else {
                        ph_num.setTextColor(Color.WHITE);
                        validate_phNumber = true;
                        Validate_signUpButton();
                    }

                }
            }
        });





        bt_Signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vIew) {


                fName = firstname.getText().toString();
                lName = lastname.getText().toString();
                mail = email.getText().toString().toLowerCase();
                Password = pass.getText().toString();
                ConfirmPassword = confirmPass.getText().toString();
                phone_Number = ph_num.getText().toString();

                //Validate all InputText

                if (fName.equals("") || lName.equals("") || mail.equals("") || Password.equals("") ||
                        ConfirmPassword.equals("") || phone_Number.equals("")) {
                    builder.setTitle("Something Wrong");
                    builder.setMessage("You should fill all fields");
                    displayAlert("Input_error");
                } else {

                    //Validate That password and confirm password are matching

                    if (!(Password.equals(ConfirmPassword))) {
                        builder.setTitle("Something Wrong");
                        builder.setMessage("the password not match");
                        displayAlert("Input_error");
                    } else {



                        final ProgressDialog pDialog = new ProgressDialog(Sign_up.this);
                        pDialog.setMessage("Please wait...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                                new Response.Listener<String>() {
                                    public void onResponse(String response) {


                                        pDialog.dismiss();
                                        try {

                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");
                                            String message = jsonObject.getString("message");

                                            if (code.equals("registration_success")) {


                                                Intent VirifcationCode = new Intent(Sign_up.this, confirm_code.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("fName",fName);
                                                bundle.putString("lName",lName);
                                                bundle.putString("e_mail",mail);
                                                bundle.putString("password",Password);
                                                bundle.putString("phoneNumber",phone_Number);
                                                VirifcationCode.putExtras(bundle);

                                                startActivity(VirifcationCode);
                                                finish();


                                            }else {

                                                builder.setMessage(message);
                                                displayAlert(code);
                                            }
                                        } catch (JSONException e) {

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

                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {

                                Map<String, String> params = new HashMap<String, String>();
                                params.put("firstName", fName);
                                params.put("lastName", lName);
                                params.put("email", mail);
                                params.put("password", Password);
                                params.put("phoneNumber", phone_Number);
                                params.put("gpluse", "0");

                                return params;
                            }
                        };

                        MySingleton.getInstance(Sign_up.this).addTorequestque(stringRequest);
                    }
                }
            }
        });

        // Sign Up With Google Account

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


        google_SignUp = (Button) findViewById(R.id.SginUp_google);
        google_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    public void  Validate_signUpButton(){
        if (validate_fName && validate_lName && validate_phNumber && validate_mail && validate_pass){
            bt_Signup.setEnabled(true);
            bt_Signup.setBackgroundResource(R.drawable.log_but);
        }else {
            bt_Signup.setEnabled(false);
            bt_Signup.setBackgroundResource(R.drawable.log_but_2);
        }
    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (code.equals("Input_error")) {
                    pass.setText("");
                    confirmPass.setText("");
                } else if (code.equals("registration_failed")) {

                    firstname.setText("");
                    lastname.setText("");
                    email.setText("");
                    pass.setText("");
                    confirmPass.setText("");
                    ph_num.setText("");
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signup() {

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void handelResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            fName = account.getGivenName();
            lName = account.getFamilyName();
            mail = account.getEmail();
            updateUI(true);
        } else {
            updateUI(false);
        }

    }

    private void updateUI(boolean isLogin) {
        if (isLogin) {
            Intent SignUpWithGoogle = new Intent(Sign_up.this,signup_withGoogle.class);
            Bundle bundle = new Bundle();
            bundle.putString("firstName",fName);
            bundle.putString("LastName",lName);
            bundle.putString("EMail",mail);
            SignUpWithGoogle.putExtras(bundle);
            startActivity(SignUpWithGoogle);
            finish();
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
