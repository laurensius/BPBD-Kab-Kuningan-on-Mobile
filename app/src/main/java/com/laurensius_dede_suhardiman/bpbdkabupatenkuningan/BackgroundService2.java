package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


public class BackgroundService2 extends Service {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    boolean loaddata;

    String JSON_data;

    Boolean init = true;

    int ln = 0;

    public static String pref_id;

    public static String id;
    public static String api_notif;

    String str_jml_peringatan_dini = "0";
    String str_jml_info_bencana = "0";
    String str_status_laporan_masyarakat = "0";
    String recent_status_laporan_masyarakat = "0";


    public int jml_peringatan_dini = 0;
    public int jml_info_bencana = 0;


    Timer timer = new Timer();
    public BackgroundService2(){}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("BPBD_ON_MOBILE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref =  getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        pref_id = pref.getString("ID",null);
        api_notif = getResources().getString(R.string.api).concat(getResources().getString(R.string.notifikasi)).concat(pref_id).concat("/");
        final Handler handler = new Handler();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            GetJSONDataNotif getjsondata = new GetJSONDataNotif();
                            getjsondata.execute();
                        } catch (Exception e) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 5000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private class GetJSONDataNotif extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d("Service BPBD","Do in background SERVICE NOTIFIKASI BPBD");
            HTTPSvc sh = new HTTPSvc();
            JSON_data = sh.makeServiceCall(api_notif, HTTPSvc.POST);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    JSONObject data = response.getJSONObject("data");
                    //Peringatan_dini
                    JSONArray arr_peringatan_dini = data.getJSONArray("peringatan_dini");
                    JSONObject obj_jml_peringatan_dini = arr_peringatan_dini.getJSONObject(0);
                    str_jml_peringatan_dini = obj_jml_peringatan_dini.getString("jumlah");
                    //info bencana
                    JSONArray arr_info_bencana = data.getJSONArray("info_bencana");
                    JSONObject obj_jml_info_bencana = arr_info_bencana.getJSONObject(0);
                    str_jml_info_bencana = obj_jml_info_bencana.getString("jumlah");
                    //Status laporan masyarakat
                    JSONArray arr_lap_masyarakat = data.getJSONArray("laporan_masyarakat");
                    ln = arr_lap_masyarakat.length();
                    str_status_laporan_masyarakat = "";
                    if(ln > 0){
                        loaddata=true;
                        for(int x=0; x<ln; x++){
                            JSONObject obj_lap_masy = arr_lap_masyarakat.getJSONObject(x);
                            String status = obj_lap_masy.getString("status");
                            str_status_laporan_masyarakat = str_status_laporan_masyarakat.concat(status);
                            Log.d("STATUS", status);
                        }
                    }
                } catch (final JSONException e) {
                    Log.e("Service BPBD", e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d("Service Service BPBD", "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(init){
                init = false;
                jml_peringatan_dini = Integer.parseInt(str_jml_peringatan_dini);
                jml_info_bencana = Integer.parseInt(str_jml_info_bencana);
                recent_status_laporan_masyarakat = str_status_laporan_masyarakat;
            }else{
                if(loaddata){
                    pref =  getSharedPreferences("BPBD_ON_MOBILE", 0);
                    editor = pref.edit();
                    if((jml_peringatan_dini <  Integer.parseInt(str_jml_peringatan_dini)) ||
                            (jml_info_bencana  <  Integer.parseInt(str_jml_info_bencana)) ||
                            (!recent_status_laporan_masyarakat.equals(str_status_laporan_masyarakat))){
                        Notification.Builder builder = new Notification.Builder(getApplication().getBaseContext());
                        Intent notificationIntent = new Intent(getApplication().getBaseContext(),LandingPage.class);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        if(jml_peringatan_dini < Integer.parseInt(str_jml_peringatan_dini)){
                            editor.putString("REDIRECT","peringatan_dini");
                            notificationIntent.putExtra("redirect", "peringatan_dini");
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                            builder.setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Notifikasi Peringatan Dini")
                                    .setContentText("Klik di sini untuk detail")
                                    .setContentIntent(pendingIntent);
                        }
                        if(jml_info_bencana < Integer.parseInt(str_jml_info_bencana)){
                            editor.putString("REDIRECT","info_bencana");
                            notificationIntent.putExtra("redirect", "info_bencana");
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                            builder.setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Notifikasi Info Bencana")
                                    .setContentText("Klik di sini untuk detail")
                                    .setContentIntent(pendingIntent);
                        }
                        if(!recent_status_laporan_masyarakat.equals(str_status_laporan_masyarakat)){
                            editor.putString("REDIRECT","laporan_masyarakat");
                            notificationIntent.putExtra("redirect", "laporan_masyarakat");
                            PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0,notificationIntent, 0);
                            builder.setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("Notifikasi Laporan Masyarakat")
                                    .setContentText("Cek status terkini laporan Anda di sini.")
                                    .setContentIntent(pendingIntent);
                        }
//                        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                        builder.setSound(alarmSound);

                        AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                        manager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
                        builder.setAutoCancel(true);
                        NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = builder.getNotification();
                        notification.sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notif);
                        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE ;
                        notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
                    }
                    editor.commit();
                    jml_peringatan_dini = Integer.parseInt(str_jml_peringatan_dini);
                    jml_info_bencana = Integer.parseInt(str_jml_info_bencana);
                    recent_status_laporan_masyarakat = str_status_laporan_masyarakat;
                }else{
                    Log.d("BPBD Service Lost : ","Data Null or False");
                }
            }
        }
    }
}