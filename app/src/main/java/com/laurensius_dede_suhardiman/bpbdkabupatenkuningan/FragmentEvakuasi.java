package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class FragmentEvakuasi extends Fragment {

    WebView wvEvakuasi;
    ProgressDialog pDialog;

    public FragmentEvakuasi() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterEvakuasi = inflater.inflate(R.layout.fragment_evakuasi, container, false);
        wvEvakuasi = (WebView) inflaterEvakuasi.findViewById(R.id.wvEvakuasi);
        return inflaterEvakuasi;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading....");
        pDialog.setCancelable(false);

        wvEvakuasi.getSettings().setJavaScriptEnabled(true);
        wvEvakuasi.getSettings().setDomStorageEnabled(true);
        wvEvakuasi.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvEvakuasi.setWebViewClient(new WebViewClient(){
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
        wvEvakuasi.setWebChromeClient(new WebChromeClient());
//        wvEvakuasi.loadUrl(getResources().getString(R.string.evakuasi));
    }


}
