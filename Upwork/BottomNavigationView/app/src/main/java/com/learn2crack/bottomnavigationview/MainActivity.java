package com.learn2crack.bottomnavigationview;

import android.app.FragmentTransaction;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity   {

    private BottomNavigationView mBottomNavigationView;
    public GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    // private Toolbar mAppToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBottomNavigation();
       // ActionBar actionBar = getActionBar();
        String dateString = android.text.format.DateFormat.format("EEEE d MMMM", new java.util.Date()).toString();
        String subDateString = android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();
        setSupportActionBar(toolbar);
        toolbar.setTitle(dateString);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));

        if (savedInstanceState == null) {
            loadHomeFragment();
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
                        return true;
                    case R.id.navigation_dua:
                        loadDuaFragment();
                        return true;
                    case R.id.navigation_quran:
                        loadQuranFragment();
                        return true;
                    case R.id.navigation_qibla:
                        loadNavigationFragment();
                        return true;
                    case R.id.navigation_more:
                        loadMoreFragment();
                        return true;
                }
                return false;
            }
        });
    }

    private void loadHomeFragment() {

        HomeFragment fragment = HomeFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadDuaFragment() {

        DuaFragment fragment = DuaFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadNavigationFragment() {

        NavigationFragment fragment = NavigationFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }
    private void loadQuranFragment() {

        QuranFragment fragment = QuranFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

    private void loadMoreFragment() {

        MoreFragment fragment = MoreFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame, fragment);
        ft.commit();
    }

}
