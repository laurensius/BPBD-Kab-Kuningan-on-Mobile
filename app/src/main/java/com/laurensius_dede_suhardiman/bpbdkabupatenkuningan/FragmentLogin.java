package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentLogin extends Fragment {

    List<NameValuePair> data_login = new ArrayList<NameValuePair>(2);

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    EditText etUsername, etPassword;
    TextView tvNotif;
    Button btnLogin;
    ProgressDialog pDialog;

    String JSON_data;
    String code,message,severity;
    String id, username, password, nama, email, alamat, register_datetime;

    Boolean loaddata;

    public FragmentLogin() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterLogin = inflater.inflate(R.layout.fragment_login, container, false);
        tvNotif = (TextView)inflaterLogin.findViewById(R.id.tvNotif);
        etUsername = (EditText)inflaterLogin.findViewById(R.id.etUsername);
        etPassword = (EditText)inflaterLogin.findViewById(R.id.etPassword);
        btnLogin = (Button)inflaterLogin.findViewById(R.id.btnLogin);
        return inflaterLogin;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getApplicationContext().getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")){
                    tvNotif.setText("Pastikan semua field sudah terisi!");
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else{
                    data_login.add(new BasicNameValuePair("username", etUsername.getText().toString()));
                    data_login.add(new BasicNameValuePair("password", etPassword.getText().toString()));
                    new AsyncLogin().execute();
                }
            }
        });
    }

    private class AsyncLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            JSON_data = sh.makeServiceCall(getResources().getString(R.string.api).concat(getResources().getString(R.string.verifikasi)), HTTPSvc.POST, data_login);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    code = response.getString("code");
                    message = response.getString("message");
                    severity = response.getString("severity");
                    JSONArray data_user = response.getJSONArray("data_user");
                    JSONObject objUser = data_user.getJSONObject(0);
                    id = objUser.getString("id");
                    username = objUser.getString("username");
                    password = objUser.getString("password");
                    nama = objUser.getString("nama");
                    email = objUser.getString("email");
                    alamat = objUser.getString("alamat");
                    register_datetime = objUser.getString("register_datetime");
                } catch (final JSONException e) {
                    Log.e("BPBD ::", e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d("BPBD ::", "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata){
                tvNotif.setText(message);
                if(severity.equals("success")){
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else
                if(severity.equals("warning")){
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else
                if(severity.equals("danger")){
                    tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                }
                if(code.equals("MATCH")){
                    editor.putString("ID",id);
                    editor.putString("USERNAME",username);
                    editor.putString("PASSWORD",password);
                    editor.putString("NAMA",nama);
                    editor.putString("EMAIL",email);
                    editor.putString("ALAMAT",alamat);
                    editor.putString("REGISTER_DATETIME",register_datetime);
                    editor.commit();
                    etUsername.setText("");
                    etPassword.setText("");
                    Intent i = new Intent(getActivity(),MasterApps.class);
                    startActivity(i);
                    LandingPage.activity.finish();
                }
            }else{
                tvNotif.setText("Error! ");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }


}
