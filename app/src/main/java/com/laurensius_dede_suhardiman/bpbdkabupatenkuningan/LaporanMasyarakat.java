package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

/**
 * Created by Laurensius D.S on 12/14/2017.
 */

public class LaporanMasyarakat {
    int icon;
    String id;
    String judul;
    String tanggal_kirim;
    String status;

    LaporanMasyarakat(int icon, String id, String judul, String tanggal_kirim, String status){
        this.icon = icon;
        this.id = id;
        this.judul = judul;
        this.tanggal_kirim = tanggal_kirim;
        this.status = status;
    }

}
