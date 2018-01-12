package com.laurensius_dede_suhardiman.bpbdkabupatenkuningan;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LandingPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Fragment fragment;
    Dialog dialBox;

    public static Activity act ;

    public static Activity activity;
    public static boolean isInFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        act = this;
        pref = getApplicationContext().getSharedPreferences("BPBD_ON_MOBILE", 0);
        editor = pref.edit();

        setContentView(R.layout.activity_landingpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        String redirect;
        Intent i = getIntent();
        redirect = pref.getString("REDIRECT",null);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        if (redirect != null) {
            if(redirect.equals("peringatan_dini")){
                tx.replace(R.id.FrameMain, new FragmentPeringatanDini());
                Toast.makeText(LandingPage.this,redirect,Toast.LENGTH_LONG).show();
            }else
            if(redirect.equals("info_bencana")){
                tx.replace(R.id.FrameMain, new FragmentInfoBencana());
                Toast.makeText(LandingPage.this,redirect,Toast.LENGTH_LONG).show();
            }
        } else {
            tx.replace(R.id.FrameMain, new FragmentBeranda());
            Toast.makeText(LandingPage.this,redirect,Toast.LENGTH_LONG).show();
        }
        tx.commit();

        dialBox = createDialogBox();

        String pref_id = pref.getString("ID",null);
        if(pref_id != null){
            if(isNotifServiceOn(BackgroundService.class)){
                stopService(new Intent(getBaseContext(), BackgroundService.class));
            }
            Intent inten = new Intent(this,MasterApps.class);
            startActivity(inten);
            finish();
        }

        if(isNotifServiceOn(BackgroundService.class)){
            Log.d("BPBD_ON_MOBILE","Service sudah dalam kondisi ON");
        }else {
            startService(new Intent(getBaseContext(), BackgroundService.class));
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dialBox.show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.landingpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        jalankanFragment(id);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        jalankanFragment(item.getItemId());
        return true;
    }

    public void jalankanFragment(int id){
        fragment = null;
        if (id == R.id.nav_beranda) {
            fragment = new FragmentBeranda();
        }else if (id == R.id.nav_peringatan_dini) {
            fragment = new FragmentPeringatanDini();
        }else if (id == R.id.nav_info_bencana) {
            fragment = new FragmentInfoBencana();
        }else if (id == R.id.nav_peta_kerawanan) {
            fragment = new FragmentKerawanan();
        }else if (id == R.id.nav_peta_evakuasi) {
            fragment = new FragmentEvakuasi();
        }else if (id == R.id.nav_login) {
            fragment = new FragmentLogin();
        }else if (id == R.id.nav_daftar) {
            fragment = new FragmentDaftar();
        }else if (id == R.id.action_tentang){
//            fragment = new FragmentTentang();
        }else if (id == R.id.action_petunjuk){
//            fragment = new FragmentBantuan();
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.FrameMain, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private Dialog createDialogBox(){
        dialBox = new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Keluar")
                .setMessage("Apakah Anda yakin akan keluar dari aplikasi ?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialBox.dismiss();
                    }
                })
                .create();
        return dialBox;
    }

    private boolean isNotifServiceOn(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
