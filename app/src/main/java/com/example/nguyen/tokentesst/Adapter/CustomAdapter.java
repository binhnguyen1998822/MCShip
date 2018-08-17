package com.example.nguyen.tokentesst.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nguyen.tokentesst.Data.MyData;
import com.example.nguyen.tokentesst.GCM.HttpParse;
import com.example.nguyen.tokentesst.GCM.URLServer;
import com.example.nguyen.tokentesst.R;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    ProgressDialog progressDialog;
    String finalResult;
    HttpParse httpParse = new HttpParse();
    HashMap<String, String> hashMap = new HashMap<>();
    TextView tv;
    Intent intent;
    private String id_sp;
    private Context context;
    private List<MyData> my_data;

    public CustomAdapter(Context context, List<MyData> my_data) {
        this.context = context;
        this.my_data = my_data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.khohang.setText(String.valueOf(my_data.get(position).getId_user()));
        holder.hoten.setText(String.valueOf(my_data.get(position).getHo_ten()));
        holder.sdt.setText(String.valueOf("0" + my_data.get(position).getSo_dt()));
        holder.tenmay.setText(String.valueOf(my_data.get(position).getTen_may()));
        NumberFormat format = NumberFormat.getCurrencyInstance();
        holder.sotien.setText(String.valueOf(format.format(my_data.get(position).getSo_tien())));
        holder.diachi.setText(String.valueOf(my_data.get(position).getDia_chi()));
        holder.ghichu.setText(String.valueOf(my_data.get(position).getGhi_chu()));

        if (my_data.get(position).getTrang_thai() == 3) {
            holder.xemdon.setText("Xem Đơn Hàng Đang Giao");
        }
        if (my_data.get(position).getTrang_thai() == 4) {
            holder.xemdon.setText("Xem Lại Đơn Hàng");
        }

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        id_sp = sharedPref.getString("id", "");

        if (my_data.get(position).getId_loaiship() == 3 && my_data.get(position).getTrang_thai() == 2) {
            holder.vitri.setText("# Ship Nội Thành");
            holder.xemdon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    StudentRecordUpdate(String.valueOf(my_data.get(position).getId())
                            , String.valueOf(id_sp)
                            , String.valueOf("3"));


                    Anhxaintent(position);

                }
            });
        }
        if (my_data.get(position).getTrang_thai() == 4 || my_data.get(position).getTrang_thai() == 3) {
            holder.vitri.setText("# Ship Nội Thành");
            holder.xemdon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Anhxaintent(position);
                }
            });
        }

    }

    private void Anhxaintent(int position) {
        intent = new Intent(context, ThongtindonnoithanhActivity.class);
        intent.putExtra("id", String.valueOf(my_data.get(position).getId()));
        intent.putExtra("id_loaiship", String.valueOf(my_data.get(position).getId_loaiship()));
        intent.putExtra("id_shipper", String.valueOf(id_sp));
        intent.putExtra("ho_ten", String.valueOf(my_data.get(position).getHo_ten()));
        intent.putExtra("co_so", String.valueOf(my_data.get(position).getId_user()));
        intent.putExtra("so_dt", String.valueOf(my_data.get(position).getSo_dt()));
        intent.putExtra("dia_chi", String.valueOf(my_data.get(position).getDia_chi()));
        intent.putExtra("ten_may", String.valueOf(my_data.get(position).getTen_may()));
        intent.putExtra("id_bh", String.valueOf(my_data.get(position).getId_bh()));
        intent.putExtra("so_tien", String.valueOf(my_data.get(position).getSo_tien()));
        intent.putExtra("trang_thai", String.valueOf(my_data.get(position).getTrang_thai()));
        intent.putExtra("ghi_chu", String.valueOf(my_data.get(position).getGhi_chu()));
        intent.putExtra("phukien", String.valueOf(my_data.get(position).getPhu_kien()));
        intent.putExtra("id_user", String.valueOf(my_data.get(position).getId_user()));
        context.startActivity(intent);

    }


    @Override
    public int getItemCount() {
        return my_data.size();
    }

    // Method to Update Student Record.
    public void StudentRecordUpdate(String id, String id_shipper, String sh) {

        class StudentRecordUpdateClass extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog = ProgressDialog.show(context, "Vui lòng chờ em tý... ", null, true, true);
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                context.startActivity(intent);

            }

            @Override
            protected String doInBackground(String... params) {
                hashMap.put("id", params[0]);
                hashMap.put("id_shipper", params[1]);
                hashMap.put("trang_thai", params[2]);
                finalResult = httpParse.postRequest(hashMap, URLServer.UpdateDH);
                return finalResult;
            }
        }

        StudentRecordUpdateClass studentRecordUpdateClass = new StudentRecordUpdateClass();
        studentRecordUpdateClass.execute(id, id_shipper, sh);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView xemdon, vitri, hoten, tenmay, sotien, sdt, diachi, khohang, ghichu;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            vitri = (TextView) itemView.findViewById(R.id.ship);
            hoten = (TextView) itemView.findViewById(R.id.hoten);
            tenmay = (TextView) itemView.findViewById(R.id.tenmay);
            sotien = (TextView) itemView.findViewById(R.id.sotien);
            sdt = (TextView) itemView.findViewById(R.id.sdt);
            diachi = (TextView) itemView.findViewById(R.id.diachi);
            xemdon = (TextView) itemView.findViewById(R.id.xemdon);
            khohang = (TextView) itemView.findViewById(R.id.co_so);
            ghichu = (TextView) itemView.findViewById(R.id.ghichu);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }

    }
}
