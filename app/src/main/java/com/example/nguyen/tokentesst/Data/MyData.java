package com.example.nguyen.tokentesst.Data;

/**
 * Created by Editor on 2/11/2018.
 */

public class MyData {

    private int id, id_loaiship,id_shipper, id_bh, so_dt, so_tien, trang_thai;
    private String ho_ten, dia_chi, ten_may, ghi_chu, phu_kien,co_so,id_user;


    public MyData(int id, int id_loaiship, int id_shipper, int id_bh, int so_dt, int so_tien, int trang_thai, String ho_ten, String dia_chi, String ten_may, String ghi_chu, String phu_kien, String co_so, String id_user) {
        this.id = id;
        this.id_loaiship = id_loaiship;
        this.id_shipper = id_shipper;
        this.id_bh = id_bh;
        this.so_dt = so_dt;
        this.so_tien = so_tien;
        this.trang_thai = trang_thai;
        this.ho_ten = ho_ten;
        this.dia_chi = dia_chi;
        this.ten_may = ten_may;
        this.ghi_chu = ghi_chu;
        this.phu_kien = phu_kien;
        this.co_so = co_so;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_loaiship() {
        return id_loaiship;
    }

    public void setId_loaiship(int id_loaiship) {
        this.id_loaiship = id_loaiship;
    }

    public int getId_shipper() {
        return id_shipper;
    }

    public void setId_shipper(int id_shipper) {
        this.id_shipper = id_shipper;
    }

    public int getId_bh() {
        return id_bh;
    }

    public void setId_bh(int id_bh) {
        this.id_bh = id_bh;
    }

    public int getSo_dt() {
        return so_dt;
    }

    public void setSo_dt(int so_dt) {
        this.so_dt = so_dt;
    }

    public int getSo_tien() {
        return so_tien;
    }

    public void setSo_tien(int so_tien) {
        this.so_tien = so_tien;
    }

    public int getTrang_thai() {
        return trang_thai;
    }

    public void setTrang_thai(int trang_thai) {
        this.trang_thai = trang_thai;
    }

    public String getHo_ten() {
        return ho_ten;
    }

    public void setHo_ten(String ho_ten) {
        this.ho_ten = ho_ten;
    }

    public String getDia_chi() {
        return dia_chi;
    }

    public void setDia_chi(String dia_chi) {
        this.dia_chi = dia_chi;
    }

    public String getTen_may() {
        return ten_may;
    }

    public void setTen_may(String ten_may) {
        this.ten_may = ten_may;
    }

    public String getGhi_chu() {
        return ghi_chu;
    }

    public void setGhi_chu(String ghi_chu) {
        this.ghi_chu = ghi_chu;
    }

    public String getPhu_kien() {
        return phu_kien;
    }

    public void setPhu_kien(String phu_kien) {
        this.phu_kien = phu_kien;
    }

    public String getCo_so() {
        return co_so;
    }

    public void setCo_so(String co_so) {
        this.co_so = co_so;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
