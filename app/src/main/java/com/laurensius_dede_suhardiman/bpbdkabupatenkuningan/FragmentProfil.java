package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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


public class FragmentProfil extends Fragment {


    SharedPreferences pref;
    SharedPreferences.Editor editor;


    List<NameValuePair> data_update = new ArrayList<NameValuePair>(9);

    TextView tvNotif;
    EditText etUsername, etPassword, etNama, etEmail, etAlamat;
    Button btnUpdate;
    ProgressDialog pDialog;

    String JSON_data;
    String id,username, password, nama, email, alamat, status;
    String code, messsage, severity;

    Boolean loaddata;

    String pref_id;
    public FragmentProfil() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterProfil = inflater.inflate(R.layout.fragment_profil, container, false);
        tvNotif = (TextView)inflaterProfil.findViewById(R.id.tvNotif);
        etUsername = (EditText)inflaterProfil.findViewById(R.id.etUsername);
        etPassword = (EditText)inflaterProfil.findViewById(R.id.etPassword);
        etNama = (EditText)inflaterProfil.findViewById(R.id.etNama);
        etEmail = (EditText)inflaterProfil.findViewById(R.id.etEmail);
        etAlamat = (EditText)inflaterProfil.findViewById(R.id.etAlamat);
        btnUpdate = (Button)inflaterProfil.findViewById(R.id.btnUpdate);
        return inflaterProfil ;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        pref_id  = pref.getString("ID",null);
        new AsyncEdit().execute();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = pref_id;
                String upt_username = etUsername.getText().toString();
                String upt_password = etPassword.getText().toString();
                String upt_nama = etNama.getText().toString();
                String upt_email = etEmail.getText().toString();
                String upt_alamat = etAlamat.getText().toString();
                String upt_status = status;
                if(!username.equals("") && username.length() >= 6 &&
                        !password.equals("") && password.length() >= 6 &&
                        !nama.equals("") && !alamat.equals("") && isValidEmail(email)){
                    data_update.add(new BasicNameValuePair("id", id));
                    data_update.add(new BasicNameValuePair("username", upt_username));
                    data_update.add(new BasicNameValuePair("password", upt_password));
                    data_update.add(new BasicNameValuePair("nama", upt_nama));
                    data_update.add(new BasicNameValuePair("alamat", upt_alamat));
                    data_update.add(new BasicNameValuePair("email", upt_email));
                    data_update.add(new BasicNameValuePair("status", upt_status));
                    new AsyncUpdate().execute();
                }else{
                    tvNotif.setText("");
                    tvNotif.setVisibility(View.VISIBLE);
                    if(username.equals("") || username.length() < 6){
                        tvNotif.append("Username tidak boleh kosong dan minimal 6 digit\n");
                    }
                    if(password.equals("") || password.length() < 6){
                        tvNotif.append("Password tidak boleh kosong dan minimal 6 digit\n");
                    }
                    if(nama.equals("")){
                        tvNotif.append("Nama tidak boleh kosong\n");
                    }
                    if(alamat.equals("")){
                        tvNotif.append("Alamat tidak boleh kosong\n");
                    }
                    if(!isValidEmail(email)){
                        tvNotif.append("Format email tidak benar\n");
                    }
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private class AsyncEdit extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Mohon menunggu...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            JSON_data = sh.makeServiceCall(getResources().getString(R.string.api).concat(getResources().getString(R.string.user_edit)).concat(pref_id), HTTPSvc.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonArr = new JSONObject(JSON_data);
                    JSONArray response = jsonArr.getJSONArray("response");
                    if(response.length() > 0){
                        JSONObject obj = response.getJSONObject(0);
                        id = obj.getString("id");
                        username = obj.getString("username");
                        password = obj.getString("password");
                        nama = obj.getString("nama");
                        email = obj.getString("email");
                        alamat = obj.getString("alamat");
                        status = obj.getString("status");
                        loaddata = true;
                    }
                } catch (final JSONException e) {
                    Log.e("BPBD ::", e.getMessage());
                }
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
                etUsername.setText(username);
                etPassword.setText(password);
                etNama.setText(nama);
                etAlamat.setText(alamat);
                etEmail.setText(email);
            }
        }
    }

    private class AsyncUpdate extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Proses update data...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HTTPSvc sh = new HTTPSvc();
            JSON_data = sh.makeServiceCall(getResources().getString(R.string.api).concat(getResources().getString(R.string.user_update)).concat(pref_id), HTTPSvc.POST, data_update);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    code = response.getString("code");
                    messsage = response.getString("message");
                    severity = response.getString("severity");
                    loaddata = true;
                } catch (final JSONException e) {
                    Log.e("BPBD ::", e.getMessage());
                }
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
            tvNotif.setVisibility(View.VISIBLE);
            tvNotif.setText(messsage);
            if(loaddata){
                if(severity.equals("success")){
                    tvNotif.setBackgroundColor(Color.parseColor("#A5D6A7"));
                }else
                if(severity.equals("warning")){
                    tvNotif.setBackgroundColor(Color.parseColor("#FFF59D"));
                }else
                if(severity.equals("danger")){
                    tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
                }
            }else{
                tvNotif.setText("Error !");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }


}
