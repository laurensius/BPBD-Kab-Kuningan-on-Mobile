package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FragmentDaftar extends Fragment {


    List<NameValuePair> data_daftar = new ArrayList<NameValuePair>(9);

    TextView tvNotif;
    EditText etUsername, etPassword, etNama, etEmail, etAlamat;
    Button btnDaftar;
    ProgressDialog pDialog;

    String JSON_data;
    String code,message,severity;
    String username, password, nama, email, alamat;

    Boolean loaddata;

    public FragmentDaftar() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterDaftar = inflater.inflate(R.layout.fragment_daftar, container, false);
        tvNotif = (TextView)inflaterDaftar.findViewById(R.id.tvNotif);
        etUsername = (EditText)inflaterDaftar.findViewById(R.id.etUsername);
        etPassword = (EditText)inflaterDaftar.findViewById(R.id.etPassword);
        etNama = (EditText)inflaterDaftar.findViewById(R.id.etNama);
        etEmail = (EditText)inflaterDaftar.findViewById(R.id.etEmail);
        etAlamat = (EditText)inflaterDaftar.findViewById(R.id.etAlamat);
        btnDaftar = (Button)inflaterDaftar.findViewById(R.id.btnDaftar);
        return inflaterDaftar;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                nama = etNama.getText().toString();
                email = etEmail.getText().toString();
                alamat = etAlamat.getText().toString();
                if(!username.equals("") && username.length() >= 6 &&
                   !password.equals("") && password.length() >= 6 &&
                   !nama.equals("") && !alamat.equals("") && isValidEmail(email)){
                    data_daftar.add(new BasicNameValuePair("username", username));
                    data_daftar.add(new BasicNameValuePair("password", password));
                    data_daftar.add(new BasicNameValuePair("nama", nama));
                    data_daftar.add(new BasicNameValuePair("alamat", alamat));
                    data_daftar.add(new BasicNameValuePair("email", email));
                    data_daftar.add(new BasicNameValuePair("tipe", "2"));
                    data_daftar.add(new BasicNameValuePair("status", "1"));
                    new AsyncDaftar().execute();
                }else{
                    tvNotif.setText("");
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

    private class AsyncDaftar extends AsyncTask<Void, Void, Void> {
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
            JSON_data = sh.makeServiceCall(getResources().getString(R.string.api).concat(getResources().getString(R.string.verifikasi_daftar)), HTTPSvc.POST, data_daftar);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    JSONObject response = jsonObj.getJSONObject("response");
                    code = response.getString("code");
                    message = response.getString("message");
                    severity = response.getString("severity");
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
                if(code.equals("SUCCESS")){
                    etUsername.setText("");
                    etPassword.setText("");
                    etNama.setText("");
                    etEmail.setText("");
                    etAlamat.setText("");
                }
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
            }else{
                tvNotif.setText("Error !");
                tvNotif.setBackgroundColor(Color.parseColor("#EF9A9A"));
            }
        }
    }



}
