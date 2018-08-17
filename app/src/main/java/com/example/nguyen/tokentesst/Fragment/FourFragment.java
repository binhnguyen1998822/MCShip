package com.example.nguyen.tokentesst.Fragment;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nguyen.tokentesst.GCM.URLServer;
import com.example.nguyen.tokentesst.R;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FourFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    TextView user, tongdon, thunhap;
    private String id_sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_four, null);
        anhxa(view);
        donhang();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        id_sp = sharedPref.getString("id", "");
        String hoten = sharedPref.getString("name", "");
        user.setText(hoten);

        final ToggleButton toggleButton = (ToggleButton) view.findViewById(R.id.toggleButton1);
        toggleButton.setOnCheckedChangeListener(this);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("toggleButton", MODE_PRIVATE);
        toggleButton.setChecked(sharedPrefs.getBoolean("ischeck", true));
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (toggleButton.isChecked()) {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("toggleButton", MODE_PRIVATE).edit();
                    editor.putBoolean("ischeck", true);
                    editor.commit();
                    String isonline = "1";
                    onoff(isonline);
                    FirebaseMessaging.getInstance().subscribeToTopic("NTB");

                } else {
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("toggleButton", MODE_PRIVATE).edit();
                    editor.putBoolean("ischeck", false);
                    editor.commit();
                    String isonline = "";
                    onoff(isonline);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("NTB");
                }
            }
        });


        return view;

    }

    private void anhxa(View view) {
        user = (TextView) view.findViewById(R.id.nameuss);
        tongdon = (TextView) view.findViewById(R.id.etongsodon);
        thunhap = (TextView) view.findViewById(R.id.etongdoanhthu);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }


    public void onoff(final String isonline) {
        StringRequest request = new StringRequest(Request.Method.POST, URLServer.CheckOnline, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
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
                parameters.put("id_shiper", id_sp);
                parameters.put("sp_online", isonline);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }


    public void donhang() {
        {
            StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, URLServer.Donship, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        NumberFormat format = NumberFormat.getCurrencyInstance();
                        JSONObject parentObject = new JSONObject(response);
                        tongdon.setText(parentObject.getString("sodon"));
                        int tongtien = (parentObject.getInt("sodon")*30000);
                        thunhap.setText(format.format(tongtien));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();

                }
            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    String date = new SimpleDateFormat("yyyy/MM", Locale.getDefault()).format(new Date());
                    String dated = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());

                    parameters.put("time", date + "/1" + " 0:00:0" + " - " + date + "/" + dated + " 23:59:59");
                    parameters.put("id_shipper", id_sp);

                    return parameters;
                }
            };

            RequestQueue rQueue = Volley.newRequestQueue(getContext());
            rQueue.add(request);
        }

    }
}