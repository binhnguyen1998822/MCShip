package com.example.nguyen.tokentesst.Data;

/**
 * Created by Think-PC on 3/1/2018.
 */

public class MyDataDonhang {

    private int id_shipper;
    private String sodon;

    public MyDataDonhang(int id_shipper, String sodon) {
        this.id_shipper = id_shipper;
        this.sodon = sodon;
    }

    public int getId_shipper() {
        return id_shipper;
    }

    public void setId_shipper(int id_shipper) {
        this.id_shipper = id_shipper;
    }

    public String getSodon() {
        return sodon;
    }

    public void setSodon(String sodon) {
        this.sodon = sodon;
    }
}
