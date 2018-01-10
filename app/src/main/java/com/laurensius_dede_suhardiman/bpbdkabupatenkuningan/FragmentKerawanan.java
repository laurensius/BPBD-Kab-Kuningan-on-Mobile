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


public class FragmentKerawanan extends Fragment {

    WebView wvKerawanan;
    ProgressDialog pDialog;

    public FragmentKerawanan() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterKerawanan = inflater.inflate(R.layout.fragment_kerawanan, container, false);
        wvKerawanan = (WebView) inflaterKerawanan.findViewById(R.id.wvKerawanan);
        return inflaterKerawanan;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading....");
        pDialog.setCancelable(false);

        wvKerawanan.getSettings().setJavaScriptEnabled(true);
        wvKerawanan.getSettings().setDomStorageEnabled(true);
        wvKerawanan.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvKerawanan.setWebViewClient(new WebViewClient(){
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
        wvKerawanan.setWebChromeClient(new WebChromeClient());
//        wvKerawanan.loadUrl(getResources().getString(R.string.kerawanan));
    }


}
