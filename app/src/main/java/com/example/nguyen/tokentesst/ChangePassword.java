package com.example.nguyen.tokentesst;

import android.app.Dialog;
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

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {
    Dialog MyDialog;
    ProgressBar progressBar;
    String id_sp;
    private EditText  password, newpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        id_sp = sharedPref.getString("id", "");
        progressBar =(ProgressBar)findViewById(R.id.progressdoimatkhau);
        password = (EditText) findViewById(R.id.cpass);
        newpassword = (EditText) findViewById(R.id.cnewpass);
    }

    public void MyCustomAlertDialog() {
        MyDialog = new Dialog(this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(R.layout.custom_alert);
        MyDialog.show();
        Button ok = (Button) MyDialog.findViewById(R.id.oklogin);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.cancel();
            }
        });
    }


    public void doimatkhau(View view) {
        final String pass = password.getText().toString().trim();
        final String newpass = newpassword.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.POST, URLServer.DoiMatKhau, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {


                if (s.equals("1")) {
                    Toast.makeText(ChangePassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    MyCustomAlertDialog();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id_shipper", id_sp);
                parameters.put("newpassword", newpass);
                parameters.put("password", pass);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(request);
    }
}
