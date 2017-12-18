package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

/**
 * Created by Laurensius D.S on 12/14/2017.
 */

public class InfoBencana {
    int icon;
    String id;
    String judul;
    String tanggal_publish;

    InfoBencana(int icon,String id, String judul, String tanggal_publish){
        this.icon = icon;
        this.id = id;
        this.judul = judul;
        this.tanggal_publish = tanggal_publish;
    }

}
