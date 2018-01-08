package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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


public class FragmentChat extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    WebView wvChat;
    ProgressDialog pDialog;

    String pref_id;

    public FragmentChat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterChat = inflater.inflate(R.layout.fragment_chat, container, false);
        wvChat = (WebView)inflaterChat.findViewById(R.id.wvChat);
        return inflaterChat;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        pref_id  = pref.getString("ID",null);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading....");
        pDialog.setCancelable(false);

        wvChat.getSettings().setJavaScriptEnabled(true);
        wvChat.getSettings().setDomStorageEnabled(true);
        wvChat.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        wvChat.setWebViewClient(new WebViewClient(){
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
        wvChat.setWebChromeClient(new WebChromeClient());
        wvChat.loadUrl(getResources().getString(R.string.chat).concat(pref_id).concat("/"));

    }


}
