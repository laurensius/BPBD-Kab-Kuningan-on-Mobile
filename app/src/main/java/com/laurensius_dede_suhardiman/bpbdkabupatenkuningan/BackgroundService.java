package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class BackgroundService extends Service {

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

    public int jml_peringatan_dini = 0;
    public int jml_info_bencana = 0;
    public String status_laporan_masyarakat = "";
    
    Timer timer = new Timer();
    public BackgroundService(){}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("BPBD_ON_MOBILE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        api_notif = getResources().getString(R.string.api).concat(getResources().getString(R.string.notifikasi));
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
                Log.d("Service 1",api_notif);
                Log.d("Service 1",JSON_data);
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
            }else{
                pref =  getSharedPreferences("BPBD_ON_MOBILE", 0);
                editor = pref.edit();
                if((jml_peringatan_dini <  Integer.parseInt(str_jml_peringatan_dini)) || (jml_info_bencana  <  Integer.parseInt(str_jml_info_bencana))){
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
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    builder.setSound(alarmSound);
                    builder.setAutoCancel(true);
                    NotificationManager notificationManager = (NotificationManager) getApplication().getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
                    Notification notification = builder.getNotification();
                    notificationManager.notify(R.drawable.notification_template_icon_bg, notification);
                }
                editor.commit();
                jml_peringatan_dini = Integer.parseInt(str_jml_peringatan_dini);
                jml_info_bencana = Integer.parseInt(str_jml_info_bencana);
            }
        }
    }
}