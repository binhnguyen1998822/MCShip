package com.example.nguyen.tokentesst.Fragment;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
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
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.ACTIVITY_SERVICE;


public class OneFragment extends Fragment {
    ProgressBar processBar;
    SwipeRefreshLayout swipeRefreshLayout;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    private int info;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, null);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        getdatahd();
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        data_list = new ArrayList<>();
        processBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(getContext(), data_list);
        recyclerView.setAdapter(adapter);
        return view;
    }


    private void getdatahd() {

        StringRequest request = new StringRequest(com.android.volley.Request.Method.GET, URLServer.GetDH, new com.android.volley.Response.Listener<String>() {
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

                        if (object.getInt("trang_thai") == 2 && object.getInt("id_loaiship") == 3) {
                            data_list.add(data);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                if(data_list != null)
                {
                    processBar.setVisibility(View.GONE);
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getContext(), volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(getContext());
        rQueue.add(request);

    }


    public void reload() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}