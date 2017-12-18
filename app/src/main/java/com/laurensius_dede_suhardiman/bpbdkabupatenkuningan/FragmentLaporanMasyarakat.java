package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class FragmentLaporanMasyarakat extends Fragment {

    LinearLayout llMenuLaporan, llMenuKirimLaporan, llMenuCekLaporan, llKirimLaporan, llCekLaporan;

    ProgressDialog pDialog;

    ImageView ivPreview;

    Button btnGallery, btnUpload;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;

    long totalSize = 0;

    public FragmentLaporanMasyarakat() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflaterLaporanMasyarakat = inflater.inflate(R.layout.fragment_laporan_masyarakat, container, false);
        btnGallery = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnGallery);
        btnUpload = (Button) inflaterLaporanMasyarakat.findViewById(R.id.btnUpload);
        ivPreview = (ImageView) inflaterLaporanMasyarakat.findViewById(R.id.ivPreview);
        return inflaterLaporanMasyarakat;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncUploadLaporan().execute();
            }
        });
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
                Log.d("", "Oops! Failed create 'Android File Upload' directory");
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
                File sourceFile = new File(fileUri.getPath());
                entity.addPart("pengirim",new StringBody("2"));
                entity.addPart("tanggal_kejadian", new StringBody("2017-12-12"));
                entity.addPart("judul", new StringBody("laporan masyarakat"));
                entity.addPart("kategori", new StringBody("1"));
                entity.addPart("kampung", new StringBody("kampung"));
                entity.addPart("kelurahan", new StringBody("cilebak"));
                entity.addPart("kecamatan", new StringBody("Subang"));
                entity.addPart("kabupaten", new StringBody("abc@gmail.kuningan"));
                entity.addPart("kronologis", new StringBody("Kronologis peristiwa"));
                entity.addPart("lampiran", new FileBody(sourceFile));
                entity.addPart("status", new StringBody("Waiting Response"));
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

}
