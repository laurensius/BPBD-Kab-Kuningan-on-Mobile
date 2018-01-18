package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

/**
 * Created by Laurensius D.S on 12/14/2017.
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HTTPSvc {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;

    public HTTPSvc() {}

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method,List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
            if (method == POST) {
                HttpPost httpPost = new HttpPost(url);
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    //
//                    httpPost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
//                    httpPost.addHeader("Cookie", "__test=687a8fe4928a42566a5a95eedea99979; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
//                    httpPost.addHeader("Cookie", "ci_session=e44fd7452b29302ea916dd508440a7455b847912; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
                }
                httpResponse = httpClient.execute(httpPost);
            } else if (method == GET) {
                if (params != null) {
                    String paramString = URLEncodedUtils
                            .format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
//                httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/12.10240 ");
//                httpGet.addHeader("Cookie", "__test=687a8fe4928a42566a5a95eedea99979; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
//                httpGet.addHeader("Cookie", "ci_session=e44fd7452b29302ea916dd508440a7455b847912; expires=Thu, 31-Dec-37 23:55:55 GMT; path=/");
                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            response = null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            response = null;
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }
        return response;
    }
}