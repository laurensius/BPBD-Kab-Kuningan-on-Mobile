package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentInfoBencana extends Fragment {

    LinearLayout llError, llNoData, llSuccess, llDetail;
    RecyclerView rvInfoBencana;
    AdapterInfoBencana adapterInfoBencana;
    RecyclerView.LayoutManager mLayoutManager;
    Context context = getActivity();
    List<InfoBencana> infoBencana;
    ProgressDialog pDialog;
    WebView wvDetail;
    Button btnKembali;

    String JSON_data;
    Boolean loaddata;
    JSONArray response;
    int response_length = 0;

    public FragmentInfoBencana() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterInfoBencana = inflater.inflate(R.layout.fragment_info_bencana, container, false);
        llError = (LinearLayout)inflaterInfoBencana.findViewById(R.id.llError);
        llNoData = (LinearLayout)inflaterInfoBencana.findViewById(R.id.llNoData);
        llSuccess = (LinearLayout)inflaterInfoBencana.findViewById(R.id.llSuccess);
        llDetail = (LinearLayout)inflaterInfoBencana.findViewById(R.id.llDetail);
        wvDetail = (WebView)inflaterInfoBencana.findViewById(R.id.wvDetail);
        btnKembali = (Button)inflaterInfoBencana.findViewById(R.id.btnKembali);
        rvInfoBencana = (RecyclerView)inflaterInfoBencana.findViewById(R.id.rvInfoBencana);
        return inflaterInfoBencana;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        llError.setVisibility(View.GONE);
        llNoData.setVisibility(View.GONE);
        llDetail.setVisibility(View.GONE);
        llSuccess.setVisibility(View.VISIBLE);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading....");
        pDialog.setCancelable(false);

        wvDetail.getSettings().setJavaScriptEnabled(true);
        wvDetail.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvDetail.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                if(pDialog.isShowing())
                    pDialog.dismiss();
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if(pDialog.isShowing())
                    pDialog.dismiss();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llError.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llDetail.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
            }
        });
        new AsyncInfoBencana().execute();
    }

    private class AsyncInfoBencana extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading . . .");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG, "Do in background");
            HTTPSvc sh = new HTTPSvc();
            JSON_data = sh.makeServiceCall(getResources().getString(R.string.api)
                    .concat(getResources().getString(R.string.info_bencana_published)),
                    HTTPSvc.GET);
            if(JSON_data!=null){
                try {
                    JSONObject jsonObj = new JSONObject(JSON_data);
                    response = jsonObj.getJSONArray("response");
                    response_length = response.length();
                } catch (final JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
                loaddata=true;
            }
            else{
                loaddata=false;
            }
            Log.d(TAG, "JSON data : " + JSON_data);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(loaddata) {
                if(response_length > 0){
                    infoBencana = new ArrayList<>();
                    try {
                        for (int x = 0; x < response_length; x++) {
                            JSONObject obj_info_bencana = response.getJSONObject(x);
                            int kategori = 0;
                            if(obj_info_bencana.get("kategori").equals("1")){
                                kategori = R.mipmap.ico_1;
                            }else
                            if(obj_info_bencana.get("kategori").equals("2")){
                                kategori = R.mipmap.ico_2;
                            }else
                            if(obj_info_bencana.get("kategori").equals("3")){
                                kategori = R.mipmap.ico_3;
                            }else
                            if(obj_info_bencana.get("kategori").equals("4")){
                                kategori = R.mipmap.ico_4;
                            }else
                            if(obj_info_bencana.get("kategori").equals("5")){
                                kategori = R.mipmap.ico_5;
                            }else
                            if(obj_info_bencana.get("kategori").equals("6")){
                                kategori = R.mipmap.ico_6;
                            }else
                            if(obj_info_bencana.get("kategori").equals("7")){
                                kategori = R.mipmap.ico_7;
                            }else
                            if(obj_info_bencana.get("kategori").equals("8")){
                                kategori = R.mipmap.ico_8;
                            }
                            infoBencana.add(new InfoBencana(
                                    kategori,
                                    obj_info_bencana.getString("id"),
                                    obj_info_bencana.getString("judul"),
                                    obj_info_bencana.getString("tanggal_publish")));
                        }
                    }catch (final JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    rvInfoBencana.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    rvInfoBencana.setLayoutManager(mLayoutManager);
                    adapterInfoBencana = new AdapterInfoBencana(infoBencana);
                    rvInfoBencana.setAdapter(adapterInfoBencana);
                    rvInfoBencana.addOnItemTouchListener(new RecyclerInfoBencanaClickListener(context, new RecyclerInfoBencanaClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View childVew, int position) {
                            InfoBencana ib = adapterInfoBencana.getItem(position);
                            llError.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                            llDetail.setVisibility(View.VISIBLE);
                            llSuccess.setVisibility(View.GONE);
                            wvDetail.loadUrl(getResources().getString(R.string.info_bencana_detail).concat(ib.id).concat("/"));
                        }
                    }));
                }else{
                    llError.setVisibility(View.GONE);
                    llNoData.setVisibility(View.VISIBLE);
                    llSuccess.setVisibility(View.GONE);
                }
            }else{
                llError.setVisibility(View.VISIBLE);
                llNoData.setVisibility(View.GONE);
                llSuccess.setVisibility(View.GONE);
            }
        }
    }


}
