package com.example.nguyen.tokentesst;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nguyen.tokentesst.GCM.URLServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainLoginActivity extends AppCompatActivity{
    private EditText email, password;
    SharedPreferences sharedPref;
    Dialog MyDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressBar =(ProgressBar)findViewById(R.id.progresslogin);
        email = (EditText) findViewById(R.id.et_email);
        password = (EditText) findViewById(R.id.et_password);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        checkLogin();
    }


    public void MyCustomAlertDialog(){
        MyDialog = new Dialog(this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_alert);
        MyDialog.show();
        Button ok = (Button)MyDialog.findViewById(R.id.oklogin);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
    }

    private void checkLogin() {
        Boolean Registered;
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Registered = sharedPref.getBoolean("Registered", false);
        if (!Registered) {
        } else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }


    public void Dangnhap(View view) {
        final String username = email.getText().toString().trim();
        final String email = password.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLServer.Dangnhap, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                if (s.equals("")) {
                    MyCustomAlertDialog();
                    progressBar.setVisibility(View.GONE);
                } else {

                    try {
                        JSONObject parentObject = new JSONObject(s);

                        String xid = parentObject.getString("id_sp");
                        String xemail = parentObject.getString("sp_email");
                        String xname = parentObject.getString("sp_name");

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("Registered", true);
                        editor.putString("id", xid);
                        editor.putString("email", xemail);
                        editor.putString("name", xname);
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(MainLoginActivity.this, MainActivity.class));
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainLoginActivity.this, volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("email", username);
                parameters.put("password", email);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }

}
