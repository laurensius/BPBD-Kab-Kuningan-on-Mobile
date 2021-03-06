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
import android.widget.TextView;


public class FragmentBeranda extends Fragment {

    WebView wvBeranda;
    ProgressDialog pDialog;

    public FragmentBeranda() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterBeranda = inflater.inflate(R.layout.fragment_beranda, container, false);
        wvBeranda = (WebView) inflaterBeranda.findViewById(R.id.wvBeranda);
        return inflaterBeranda;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading....");
        pDialog.setCancelable(false);

        wvBeranda.getSettings().setJavaScriptEnabled(true);
        wvBeranda.getSettings().setDomStorageEnabled(true);
        wvBeranda.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvBeranda.setWebViewClient(new WebViewClient(){
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
        wvBeranda.setWebChromeClient(new WebChromeClient());
        wvBeranda.loadUrl(getResources().getString(R.string.beranda));
    }


}
