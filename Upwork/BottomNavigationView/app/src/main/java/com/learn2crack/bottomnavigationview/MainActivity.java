package com.learn2crack.bottomnavigationview;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.location.LocationListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Geocoder geocoder;
    private LocationManager mLocationManager;
    public static List<Address> addresses;
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public static SharedPreferences Settings;
    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private BottomNavigationView mBottomNavigationView;
    private Toolbar toolbar;
    private String mdateString;

    //public abstract void setStatusBarColor (int color);
    // private Toolbar mAppToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBottomNavigation();
        // ActionBar actionBar = getActionBar();
        mdateString = DateFormat.format("EEEE d MMMM", new Date()).toString();
        setSupportActionBar(toolbar);
        toolbar.setTitle(mdateString);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        if (savedInstanceState == null) {
            loadHomeFragment();
        }

        //location
        geocoder = new Geocoder(this, Locale.getDefault());

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = mLocationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = mLocationManager.getLastKnownLocation(provider);
        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        }

    }

    private void setupBottomNavigation() {

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomNavigationView);
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_start_page:
                        loadHomeFragment();
                        Log.v(TAG, "loadHomeFragment");
                        return true;
                    case R.id.navigation_dua:
                        loadDuaFragment();
                        Log.v(TAG, "loadDuaFragment");
                        return true;
                    case R.id.navigation_quran:
                        loadQuranFragment();
                        Log.v(TAG, "loadQuranFragment");
                        return true;
                    case R.id.navigation_qibla:
                        loadNavigationFragment();
                        Log.v(TAG, "loadNavigationFragment");
                        return true;
                    case R.id.navigation_more:
                        loadMoreFragment();
                        Log.v(TAG, "loadMoreFragment");
                        return true;
                }
                return false;
            }
        });
    }

    private void loadHomeFragment() {

        HomeFragment fragment = HomeFragment.getHomeFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        //ft.addToBackStack(null);
        ft.commit();
        toolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);

    }

    private void loadDuaFragment() {

        DuaFragment fragment = DuaFragment.getDuaFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        toolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void loadNavigationFragment() {

        NavigationFragment fragment = NavigationFragment.getNavigationFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        toolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
   }

    private void loadQuranFragment() {

        QuranFragment fragment = QuranFragment.getQuranFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        toolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void loadMoreFragment() {

        MoreFragment fragment = MoreFragment.getMoreFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        toolbar.setVisibility(View.VISIBLE);
        mBottomNavigationView.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        window.setStatusBarColor(0xFFFFFF);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 1, this);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationManager.removeUpdates(this);
        if(ProgressFragment.t.isAlive()) {
            ProgressFragment.t.interrupt();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationManager.removeUpdates(this);
        if(ProgressFragment.t.isAlive()) {
            ProgressFragment.t.interrupt();
        }
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
        if(ProgressFragment.t.isAlive()) {
            ProgressFragment.t.interrupt();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat =  (location.getLatitude());
        double lon =  (location.getLongitude());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            //mLatTextView.setText(addresses.get(0).getAddressLine(0));
            // mLonTextView.setText(addresses.get(0).getLocality());
            Settings = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = MainActivity.Settings.edit();
            editor.putString("lastLocation", addresses.get(0).getLocality()); //складываем элементы массива
            editor.apply();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public boolean requestPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void onClickEvening(View view){
        EveningFragment fragment = EveningFragment.getEveningFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        loadNewToolbar();
        //toolbar.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what do you want here
                refreshToolbar();
                loadHomeFragment();

            }
        });
    }

    public void onClickMorning(View view){

        MorningFragment fragment = MorningFragment.getMorningFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
        loadNewToolbar();
        //toolbar.setVisibility(View.GONE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what do you want here
                refreshToolbar();
                loadHomeFragment();

            }
        });
    }
    public void refreshToolbar(){
        //toolbar.removeAllViews();
        toolbar.setNavigationIcon(null);
        toolbar.setTitle(mdateString);
    }

    public void loadNewToolbar(){
        mBottomNavigationView.setVisibility(View.GONE);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
    }
}
