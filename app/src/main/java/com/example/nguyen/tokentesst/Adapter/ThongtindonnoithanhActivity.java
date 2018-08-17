package com.example.nguyen.tokentesst.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.tokentesst.Data.MyData;
import com.example.nguyen.tokentesst.GCM.HttpParse;
import com.example.nguyen.tokentesst.GCM.URLServer;
import com.example.nguyen.tokentesst.MainActivity;
import com.example.nguyen.tokentesst.MainLoginActivity;
import com.example.nguyen.tokentesst.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Route;

public class ThongtindonnoithanhActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener {


    String HttpURL = "http://fsm.s-developers.com/api/updatedh";
    String finalResult;
    HttpParse httpParse = new HttpParse();
    HashMap<String, String> hashMap = new HashMap<>();
    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText etOrigin;
    private EditText etDestination;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Context mContext;
    private Activity mActivity;
    private TextView hoten1;
    private LinearLayout mRelativeLayout;
    private Button btn;
    private String dc;
    private PopupWindow mPopupWindow;
    private LocationManager locationManager;
    private android.location.LocationListener myLocationListener;
    private double latitude, longitude;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtindonnoithanh);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mContext = getApplicationContext();

        mActivity = ThongtindonnoithanhActivity.this;
        mRelativeLayout = (LinearLayout) findViewById(R.id.rl);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String id1 = b.getString("id");
        final String id_loaiship1 = b.getString("id_loaiship");
        final String id_ship = b.getString("id_shipper");
        final String ho_ten1 = b.getString("ho_ten");
        final String so_dt1 = b.getString("so_dt");
        final String dia_chi1 = b.getString("dia_chi");
        final String co_so1 = b.getString("co_so");
        final String ten_may1 = b.getString("ten_may");
        final String id_bh1 = b.getString("id_bh");
        final String so_tien1 = b.getString("so_tien");
        final String trang_thai1 = b.getString("trang_thai");
        final String ghi_chu1 = b.getString("ghi_chu");
        final String phukien1 = b.getString("phukien");
        final String user_nhap1 = b.getString("id_user");

        checkLocation();

        btn = (Button) findViewById(R.id.btnFindPath);
        etOrigin = (EditText) findViewById(R.id.etOrigin);
        etOrigin.setText(co_so1);
        etDestination = (EditText) findViewById(R.id.etDestination);
        etDestination.setText(dia_chi1);
        sendRequest();

        if (trang_thai1.equals("4")) {
            btn.setText("Đơn Hàng Đã Giao");
            btn.setBackgroundResource(R.drawable.radius_button);
        } else {
            btn.setText("Giao Hàng Thành Công");
            btn.setBackgroundResource(R.drawable.radius_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sdtht = ("0" + String.valueOf(so_dt1));

                    StudentRecordUpdate(String.valueOf(id1)
                            , String.valueOf(id_ship)
                            , String.valueOf("4"));


                }
            });
        }
    }

    public void checkLocation() {

        String serviceString = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) getSystemService(serviceString);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        myLocationListener = new android.location.LocationListener() {
            public void onLocationChanged(Location locationListener) {

                if (isGPSEnabled(ThongtindonnoithanhActivity.this)) {
                    if (locationListener != null) {
                        if (ActivityCompat.checkSelfPermission(ThongtindonnoithanhActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ThongtindonnoithanhActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        if (locationManager != null) {
                            locationListener = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (locationListener != null) {
                                latitude = locationListener.getLatitude();
                                longitude = locationListener.getLongitude();
                            }
                        }
                    }
                } else if (isInternetConnected(ThongtindonnoithanhActivity.this)) {
                    if (locationManager != null) {
                        locationListener = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (locationListener != null) {
                            latitude = locationListener.getLatitude();
                            longitude = locationListener.getLongitude();
                        }
                    }
                }
            }

            public void onProviderDisabled(String provider) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, myLocationListener);
    }

    public static boolean isInternetConnected(Context ctx) {
        ConnectivityManager connectivityMgr = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null) {
            if (wifi.isConnected()) {
                return true;
            }
        }
        if (mobile != null) {
            if (mobile.isConnected()) {
                return true;
            }
        }
        return false;
    }

    public boolean isGPSEnabled(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void sendRequest() {
        String origin = etOrigin.getText().toString();
        String destination = etDestination.getText().toString();
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng hcmus = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Bạn đang ở đây nè !!")
                .position(hcmus)));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }


    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Vui lòng chờ",
                "Đang tìm cung đường ngắn nhất...!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.iconship))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_done))
                    .title(route.endAddress)
                    .position(route.endLocation)));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {

                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);


                    View customView = inflater.inflate(R.layout.custom_layout, null);


                    mPopupWindow = new PopupWindow(
                            customView,
                            WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                    );

                    if (Build.VERSION.SDK_INT >= 21) {
                        mPopupWindow.setElevation(5.0f);
                    }
                    Bundle b = new Bundle();
                    b = getIntent().getExtras();
                    final String id2 = b.getString("id");
                    final String ho_ten2 = b.getString("ho_ten");
                    final String so_dt2 = b.getString("so_dt");
                    final String dia_chi2 = b.getString("dia_chi");
                    final String ten_may2 = b.getString("ten_may");
                    final String so_tien2 = b.getString("so_tien");

                    final String ghi_chu2 = b.getString("ghi_chu");

                    TextView hoten = (TextView) customView.findViewById(R.id.hoten);
                    TextView sdt = (TextView) customView.findViewById(R.id.sdt);
                    TextView tenmay = (TextView) customView.findViewById(R.id.tenmay);
                    TextView sotien = (TextView) customView.findViewById(R.id.sotien);
                    TextView diachi = (TextView) customView.findViewById(R.id.diachi);
                    TextView ghichu = (TextView) customView.findViewById(R.id.ghichu);
                    Button call = (Button) customView.findViewById(R.id.call);
                    call.setOnClickListener(new View.OnClickListener() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onClick(View view) {
                            int permissionCheck = ContextCompat.checkSelfPermission(ThongtindonnoithanhActivity.this, Manifest.permission.CALL_PHONE);
                            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ThongtindonnoithanhActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + String.valueOf("0" + so_dt2)));
                                if (ActivityCompat.checkSelfPermission(ThongtindonnoithanhActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                    startActivity(intent);
                                }
                            }
                        }
                    });
                    hoten.setText(String.valueOf(ho_ten2));
                    tenmay.setText(String.valueOf(ten_may2));
                    sdt.setText(String.valueOf("0" + so_dt2));
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    sotien.setText(format.format((Integer.valueOf(so_tien2))));
                    diachi.setText(String.valueOf(dia_chi2));
                    ghichu.setText(String.valueOf(ghi_chu2));
                    ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mPopupWindow.dismiss();
                        }
                    });
                    mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER, 0, 0);
                    return true;

                }
            });

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.rgb(0, 191, 255)).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Bundle b = new Bundle();
                    b = getIntent().getExtras();
                    final String so_dt2 = b.getString("so_dt");
                    intent.setData(Uri.parse("tel:" + String.valueOf(so_dt2)));
                    if (ActivityCompat.checkSelfPermission(ThongtindonnoithanhActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    }
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ThongtindonnoithanhActivity.this, MainLoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
        super.onBackPressed();
    }

    public void StudentRecordUpdate(String id, String id_shipper, String sh) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(ThongtindonnoithanhActivity.this, "Vui lòng chờ em tý... ", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {

                super.onPostExecute(httpResponseMsg);

                progressDialog.dismiss();

                Toast.makeText(ThongtindonnoithanhActivity.this, "Giao Hàng Thành Công", Toast.LENGTH_SHORT).show();

                startActivity( new Intent(ThongtindonnoithanhActivity.this, MainActivity.class));

            }

            @Override
            protected String doInBackground(String... params) {

                hashMap.put("id", params[0]);
                hashMap.put("id_shipper",params[1]);
                hashMap.put("trang_thai", params[2]);


                finalResult = httpParse.postRequest(hashMap, URLServer.UpdateDH);

                return finalResult;
            }


        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();

        studentRecordUpdateClass.execute(id,  id_shipper, sh);
    }

}
