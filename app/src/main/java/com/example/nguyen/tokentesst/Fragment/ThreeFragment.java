package com.example.nguyen.tokentesst.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.nguyen.tokentesst.Adapter.CustomAdapter;
import com.example.nguyen.tokentesst.Data.MyData;
import com.example.nguyen.tokentesst.GCM.URLServer;
import com.example.nguyen.tokentesst.R;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import static android.content.Context.ACTIVITY_SERVICE;


public class ThreeFragment extends Fragment {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    private String id_sp;
    ProgressBar processBar;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, null);

        setHasOptionsMenu(true);
        Anhxa(view);
        String datefitter = "";
        loaddh(datefitter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        data_list = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new CustomAdapter(getContext(), data_list);
        recyclerView.setAdapter(adapter);
        return view;
    }
    public void rangerpic() {
        SmoothDateRangePickerFragment smoothDateRangePickerFragment =
                SmoothDateRangePickerFragment
                        .newInstance(new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                            @Override
                            public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                                       int yearStart, int monthStart,
                                                       int dayStart, int yearEnd,
                                                       int monthEnd, int dayEnd) {
                                String datefitter = yearStart + "/" + (++monthStart)
                                        + "/" + dayStart + " 0:00:0" + " - " + yearEnd + "/"
                                        + (++monthEnd) + "/" + dayEnd + " 23:59:59";
                                loaddh(datefitter);
                                getActivity().setTitle(datefitter);
                            }
                        });
        smoothDateRangePickerFragment.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }
    public void loaddh(final String datefitter) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, URLServer.LocHD, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jarray = new JSONArray(response);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);
                        MyData data = new MyData(object.getInt("id"),
                                object.getInt("id_loaiship"),
                                object.getInt("id_shipper"),
                                object.getInt("id_bh"),
                                object.getInt("so_dt"),
                                object.getInt("so_tien"),
                                object.getInt("trang_thai"),
                                object.getString("ho_ten"),
                                object.getString("dia_chi"),
                                object.getString("ten_may"),
                                object.getString("ghi_chu"),
                                object.getString("phukien"),
                                object.getString("id_user"),
                                object.getString("co_so"));
                        if (object.getInt("trang_thai") == 4 && object.getInt("id_loaiship") == 3) {
                            data_list.add(data);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter.notifyDataSetChanged();
                if (data_list != null) {
                    processBar.setVisibility(View.GONE);
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
                Log.d("xx", volleyError + "");
            }
        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                String y = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                String m = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                String d = new SimpleDateFormat("dd", Locale.getDefault()).format(new Date());
                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                id_sp = sharedPref.getString("id", "");
                if (datefitter.equals("")) {
                    parameters.put("id_shipper", id_sp);
                    parameters.put("datefitter", y + "/" + m + "/" + "1" + " 0:00:0" + " - " + y + "/" + m + "/" + d + " 23:59:59");
                } else {
                    parameters.put("datefitter", datefitter);
                    parameters.put("id_shipper", id_sp);
                }
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.your_menu_xml, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                data_list.clear();
                adapter.notifyDataSetChanged();
                rangerpic();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void reload() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void Anhxa(View view) {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        processBar = (ProgressBar) view.findViewById(R.id.progressBar);
    }

}