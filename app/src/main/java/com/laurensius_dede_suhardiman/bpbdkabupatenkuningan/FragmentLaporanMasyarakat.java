package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class FragmentLaporanMasyarakat extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    LinearLayout llMenuLaporan, llMenuKirimLaporan, llMenuCekLaporan, llKirimLaporan, llCekLaporan, llError, llNoData, llDetail, llSuccess;
    Spinner spTanggal, spBulan, spTahun, spKategori;
    EditText etJudul, etKampung, etKelurahan, etKecamatan, etKabupaten, etKronologis;
    ProgressDialog pDialog;
    ImageView ivPreview;
    Button btnGallery, btnUpload, btnBatal, btnHapusGambar;
    RecyclerView rvLaporanMasyarakat;
    AdapterLaporanMasyarakat adapterLaporanMasyarakat;
    RecyclerView.LayoutManager mLayoutManager;
    Context context = getActivity();
    List<LaporanMasyarakat> laporanMasyarakat;
    WebView wvDetail;
    Button btnKembali;

    String JSON_data;
    Boolean loaddata;
    JSONArray response;
    int response_length = 0;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;

    long totalSize = 0;

    File sourceFile;

    String pengirim;
    String tanggal_kejadian;
    String judul;
    String kategori;
    String kampung;
    String kelurahan;
    String kecamatan;
    String kabupaten;
    String kronologis;
    String lampiran;
    String status = "1";
    String pref_id;


    public FragmentLaporanMasyarakat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View inflaterLaporanMasyarakat = inflater.inflate(R.layout.fragment_laporan_masyarakat, container, false);
        llMenuLaporan = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llMenuLaporan);
        llMenuKirimLaporan = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llMenuKirimLaporan);
        llMenuCekLaporan = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llMenuCekLaporan);
        llKirimLaporan = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llKirimLaporan);
        llCekLaporan = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llCekLaporan);
        llError = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llError);
        llNoData = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llNoData);
        llSuccess = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llSuccess);
        llDetail = (LinearLayout)inflaterLaporanMasyarakat.findViewById(R.id.llDetail);
        wvDetail = (WebView)inflaterLaporanMasyarakat.findViewById(R.id.wvDetail);
        rvLaporanMasyarakat = (RecyclerView)inflaterLaporanMasyarakat.findViewById(R.id.rvLaporanMasyarakat);
        spTanggal = (Spinner)inflaterLaporanMasyarakat.findViewById(R.id.spTanggal);
        spBulan = (Spinner)inflaterLaporanMasyarakat.findViewById(R.id.spBulan);
        spTahun = (Spinner)inflaterLaporanMasyarakat.findViewById(R.id.spTahun);
        spKategori = (Spinner)inflaterLaporanMasyarakat.findViewById(R.id.spKategori);
        etJudul = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etJudul);
        etKampung = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etKampung);
        etKelurahan = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etKelurahan);
        etKecamatan = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etKecamatan);
        etKabupaten = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etKabupaten);
        etKronologis = (EditText)inflaterLaporanMasyarakat.findViewById(R.id.etKronologis);
        ivPreview = (ImageView) inflaterLaporanMasyarakat.findViewById(R.id.ivPreview);
        btnGallery = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnGallery);
        btnUpload = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnUpload);
        btnBatal = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnBatal);
        btnHapusGambar = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnHapusGambar);
        btnKembali = (Button)inflaterLaporanMasyarakat.findViewById(R.id.btnKembali);
        return inflaterLaporanMasyarakat;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();
        pref_id  = pref.getString("ID",null);

        llMenuLaporan.setVisibility(View.VISIBLE);
        llMenuKirimLaporan.setVisibility(View.VISIBLE);
        llMenuCekLaporan.setVisibility(View.VISIBLE);
        llKirimLaporan.setVisibility(View.GONE);
        llCekLaporan.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> tanggalAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.tanggal, android.R.layout.simple_spinner_item);
        tanggalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTanggal.setAdapter(tanggalAdapter);
        ArrayAdapter<CharSequence> bulanAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.bulan, android.R.layout.simple_spinner_item);
        bulanAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spBulan.setAdapter(bulanAdapter);
        ArrayAdapter<CharSequence> tahunAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.tahun,android.R.layout.simple_spinner_item);
        tahunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTahun.setAdapter(tahunAdapter);

        ArrayAdapter<CharSequence> kategoriAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),R.array.kategori,android.R.layout.simple_spinner_item);
        kategoriAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spKategori.setAdapter(kategoriAdapter);

        llMenuKirimLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llMenuLaporan.setVisibility(View.GONE);
                llKirimLaporan.setVisibility(View.VISIBLE);
                llCekLaporan.setVisibility(View.GONE);
            }
        });

        llMenuCekLaporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llMenuLaporan.setVisibility(View.GONE);
                llKirimLaporan.setVisibility(View.GONE);
                llCekLaporan.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llDetail.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                new AsyncLaporanMasyarakat().execute();
            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pengirim = pref_id;
                tanggal_kejadian = spTahun.getSelectedItem().toString().concat("-")
                        .concat(spBulan.getSelectedItem().toString()).concat("-")
                        .concat(spTanggal.getSelectedItem().toString());
                judul = etJudul.getText().toString();
                kategori = String.valueOf(spKategori.getSelectedItemPosition() + 1);
                kampung = etKampung.getText().toString();
                kelurahan = etKelurahan.getText().toString();
                kecamatan = etKecamatan.getText().toString();
                kabupaten = etKabupaten.getText().toString();
                kronologis = etKronologis.getText().toString();
                new AsyncUploadLaporan().execute();
            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llMenuLaporan.setVisibility(View.VISIBLE);
                llKirimLaporan.setVisibility(View.GONE);
                llCekLaporan.setVisibility(View.GONE);
                ivPreview.setImageDrawable(null);
            }
        });

        btnHapusGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sourceFile = null;
                ivPreview.setImageDrawable(null);
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llMenuLaporan.setVisibility(View.GONE);
                llKirimLaporan.setVisibility(View.GONE);
                llCekLaporan.setVisibility(View.VISIBLE);
                llError.setVisibility(View.GONE);
                llNoData.setVisibility(View.GONE);
                llDetail.setVisibility(View.GONE);
                llSuccess.setVisibility(View.VISIBLE);
                new AsyncLaporanMasyarakat().execute();
            }
        });
        new AsyncLaporanMasyarakat().execute();
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                launchUploadActivity(true);
                previewGambar();
            } else if (resultCode == RESULT_CANCELED) {
                ivPreview.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        "Pengambilan gambar dibatalkan", Toast.LENGTH_SHORT)
                        .show();

            } else {
                Toast.makeText(getActivity(),
                        "Proses pengambilan gambar gagal", Toast.LENGTH_SHORT)
                        .show();
            }
        }else{
            Toast.makeText(getActivity(),
                    "Anda tidak diperkenankan upload selain gambar", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void previewGambar(){
//        Intent i = new Intent(getActivity(), UploadActivity.class);
//        i.putExtra("filePath", fileUri.getPath());
//        i.putExtra("isImage", isImage);
//        startActivity(i);
        ivPreview.setVisibility(View.VISIBLE);
        ivPreview.setAdjustViewBounds(true);
        File imgFile = new  File(fileUri.getPath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivPreview.setImageBitmap(myBitmap);
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"Android File Upload");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("", "Proses pembuatan folder 'Android File Upload' gagal");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        }  else {
            return null;
        }
        return mediaFile;
    }



    private class AsyncUploadLaporan extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
//            progressBar.setProgress(0);
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading . . .");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

//        @Override
//        protected void onProgressUpdate(Integer... progress) {
//            progressBar.setVisibility(View.VISIBLE);
//            progressBar.setProgress(progress[0]);
//            txtPercentage.setText(String.valueOf(progress[0]) + "%");
//        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            String url = getResources().getString(R.string.api)
                    .concat(getResources().getString(R.string.laporan_masyarakat_input));
                    Log.d("Response " , url);
            HttpPost httppost = new HttpPost(url);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {
                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });
                entity.addPart("pengirim",new StringBody(pengirim));
                entity.addPart("tanggal_kejadian", new StringBody(tanggal_kejadian));
                entity.addPart("judul", new StringBody(judul));
                entity.addPart("kategori", new StringBody(kategori));
                entity.addPart("kampung", new StringBody(kampung));
                entity.addPart("kelurahan", new StringBody(kelurahan));
                entity.addPart("kecamatan", new StringBody(kecamatan));
                entity.addPart("kabupaten", new StringBody(kabupaten));
                entity.addPart("kronologis", new StringBody(kronologis));
                entity.addPart("status", new StringBody(status));
                if(fileUri != null){
                    sourceFile = new  File(fileUri.getPath());
                    if(sourceFile.exists()){
                        entity.addPart("lampiran", new FileBody(sourceFile));
                    }
                }
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
//                    responseString = "Error occurred! Http Status Code: "
//                            + statusCode;
                    responseString = EntityUtils.toString(r_entity);
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(pDialog.isShowing()){
                pDialog.dismiss();
            }
            Log.d("Response " , result);
        }
    }

    private class AsyncLaporanMasyarakat extends AsyncTask<Void, Void, Void> {
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
                            .concat(getResources().getString(R.string.laporan_masyarakat_by_pengirim))
                            .concat(pref_id),HTTPSvc.GET);
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
                    laporanMasyarakat = new ArrayList<>();
                    try {
                        for (int x = 0; x < response_length; x++) {
                            JSONObject obj_laporan_masyarakat = response.getJSONObject(x);
                            int kategori = 0;
                            if(obj_laporan_masyarakat.get("kategori").equals("1")){
                                kategori = R.mipmap.ico_1;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("2")){
                                kategori = R.mipmap.ico_2;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("3")){
                                kategori = R.mipmap.ico_3;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("4")){
                                kategori = R.mipmap.ico_4;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("5")){
                                kategori = R.mipmap.ico_5;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("6")){
                                kategori = R.mipmap.ico_6;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("7")){
                                kategori = R.mipmap.ico_7;
                            }else
                            if(obj_laporan_masyarakat.get("kategori").equals("0")){
                                kategori = R.mipmap.ico_0;
                            }
                            laporanMasyarakat.add(new LaporanMasyarakat(
                                    kategori,
                                    obj_laporan_masyarakat.getString("id"),
                                    obj_laporan_masyarakat.getString("judul"),
                                    obj_laporan_masyarakat.getString("tanggal_buat"),
                                    obj_laporan_masyarakat.getString("status")));
                        }
                    }catch (final JSONException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    rvLaporanMasyarakat.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    rvLaporanMasyarakat.setLayoutManager(mLayoutManager);
                    adapterLaporanMasyarakat = new AdapterLaporanMasyarakat(laporanMasyarakat);
                    rvLaporanMasyarakat.setAdapter(adapterLaporanMasyarakat);
                    rvLaporanMasyarakat.addOnItemTouchListener(new RecyclerLaporanMasyarakatClickListener(context, new RecyclerLaporanMasyarakatClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View childVew, int position) {
                            LaporanMasyarakat lm = adapterLaporanMasyarakat.getItem(position);
                            llError.setVisibility(View.GONE);
                            llNoData.setVisibility(View.GONE);
                            llDetail.setVisibility(View.VISIBLE);
                            llSuccess.setVisibility(View.GONE);
                            wvDetail.loadUrl(getResources().getString(R.string.laporan_masyarakat_detail).concat(lm.id).concat("/"));
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
