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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class FragmentPeringatanDini extends Fragment {

    LinearLayout llError, llNoData, llSuccess, llDetail;
    RecyclerView rvPeringatanDini;
    AdapterPeringatanDini adapterPeringatanDini;
    RecyclerView.LayoutManager mLayoutManager;
    Context context = getActivity();
    List<PeringatanDini> peringatanDini;
    ProgressDialog pDialog;
    WebView wvDetail;
    Button btnKembali;

    String JSON_data;
    Boolean loaddata;
    JSONArray response;
    int response_length = 0;

    public FragmentPeringatanDini() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterPeringatanDini = inflater.inflate(R.layout.fragment_peringatan_dini, container, false);
        llError = (LinearLayout)inflaterPeringatanDini.findViewById(R.id.llError);
        llNoData = (LinearLayout)inflaterPeringatanDini.findViewById(R.id.llNoData);
        llSuccess = (LinearLayout)inflaterPeringatanDini.findViewById(R.id.llSuccess);
        llDetail = (LinearLayout)inflaterPeringatanDini.findViewById(R.id.llDetail);
        wvDetail = (WebView)inflaterPeringatanDini.findViewById(R.id.wvDetail);
        btnKembali = (Button)inflaterPeringatanDini.findViewById(R.id.btnKembali);
        rvPeringatanDini = (RecyclerView)inflaterPeringatanDini.findViewById(R.id.rvPeringatanDini);
        return inflaterPeringatanDini;
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
        new AsyncPeringatanDini().execute();
    }

    private class AsyncPeringatanDini extends AsyncTask<Void, Void, Void> {
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
                    .concat(getResources().getString(R.string.peringatan_dini_published)),
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
                    peringatanDini = new ArrayList<>();
                    try {
                        for (int x = 0; x < response_length; x++) {
                            JSONObject obj_peringatan_dini = response.getJSONObject(x);
                            peringatanDini.add(new PeringatanDini(
                                    obj_peringatan_dini.getString("id"),
                                    obj_peringatan_dini.getString("judul"),
                                    obj_peringatan_dini.getString("tanggal_publish")));
                        }
                    }catch (final JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    rvPeringatanDini.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    rvPeringatanDini.setLayoutManager(mLayoutManager);
                    adapterPeringatanDini = new AdapterPeringatanDini(peringatanDini);
                    rvPeringatanDini.setAdapter(adapterPeringatanDini);
                    rvPeringatanDini.addOnItemTouchListener(new RecyclerPeringatanDiniClickListener(context, new RecyclerPeringatanDiniClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View childVew, int position) {
                            PeringatanDini pd = adapterPeringatanDini.getItem(position);
                            llError.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                            llDetail.setVisibility(View.VISIBLE);
                            llSuccess.setVisibility(View.GONE);
                            wvDetail.loadUrl(getResources().getString(R.string.peringatan_dini_detail).concat(pd.id).concat("/"));
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
