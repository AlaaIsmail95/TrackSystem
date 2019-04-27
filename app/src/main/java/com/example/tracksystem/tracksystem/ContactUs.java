package com.example.tracksystem.tracksystem;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ContactUs extends Fragment {
    String userid;
    EditText msg_content;
    TextView counter;
    Button submit;
    String url_server="https://tracksystem7.000webhostapp.com/feedBack.php";
    String lang_type,ch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contact_us, container, false);

        SharedPreferences sharedPref;
        sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        userid = sharedPref.getString("UserID", "");


        msg_content=(EditText)view.findViewById(R.id.et_mess);
        counter=(TextView)view.findViewById(R.id.counter);

        counter.setText("300");
        submit=(Button)view.findViewById(R.id.contact);

        msg_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = msg_content.length();
                if(length<=300){
                    String convert = String.valueOf(300-length);
                    counter.setText(convert);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        lang_type = Locale.getDefault().getDisplayLanguage();

        if(lang_type.equals("English"))
        {
            ch="EN";
        }
        else {
            ch="AR";
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_server,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {

                                    /*JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
*/

                                    if (ch.equals("EN")){

                                        //     String Answer_EN = jsonObject.getString("answer");
                                        //    if(Answer_EN.equals("Data sent !!!!!!!!!")){
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("your feed back has sent, thanks !")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                                                        @SuppressWarnings("unused") final int id) {
                                                        getFragmentManager().popBackStack();
                                                    }
                                                });
                                        final AlertDialog alert = builder.create();
                                        alert.show();
                                        //  }


                                    }else {
                                        //    String Answer_AR=jsonObject.getString("answer");
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage("تم ارسال الرسالة بنجاح")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                                                        @SuppressWarnings("unused") final int id) {
                                                        getFragmentManager().popBackStack();
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
                        params.put("language",lang_type);
                        params.put("feed_back",msg_content.getText().toString());
                        return params;
                    }
                };
                MySingleton.getInstance(getActivity()).addTorequestque(stringRequest);



            }
        });


        return view ;
    }

}
