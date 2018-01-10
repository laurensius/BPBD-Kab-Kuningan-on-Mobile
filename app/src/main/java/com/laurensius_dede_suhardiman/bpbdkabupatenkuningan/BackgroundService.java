package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

    Timer timer = new Timer();
    public BackgroundService(){}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("BPBD_ON_MOBILE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pref =  getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        pref_id = pref.getString("ID",null);
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
        timer.schedule(doAsynchronousTask, 0, 10000);
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
                    String code = response.getString("code");
                    Log.d("CODE :", code);
//                    ln = response.length();
//                    if(ln > 0){
//
//
//                    }
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

        }
    }
}