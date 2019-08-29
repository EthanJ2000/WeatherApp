package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.weatherapp.Common.Common;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static Background background;
    private WeatherStatusColor backgroundTime;
    public static TextView txtTemp;
    public static TextView txtLocation;
    public static TextView txtHumidity;
    public static TextView txtDesc;
    public static InputMethodManager imm;

    EditText edtLocation;
    Button btnSearch;
    RelativeLayout searchBar;
    ConstraintLayout constraintLayout;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        searchBar = (RelativeLayout) findViewById(R.id.searchBar);
        edtLocation = (EditText) findViewById(R.id.edtLocation);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        ImageView view = findViewById(R.id.background);

        imm = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        //Search Button Disappear
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(MainActivity.this, "Screen Touched", Toast.LENGTH_LONG).show();

                //Search Bar Animation
                if (searchBar.getAlpha() == 1f) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    searchBar.animate().alpha(0f).setDuration(150);
                    searchBar.animate().translationY(-30);
                    btnSearch.setVisibility(View.VISIBLE);

                    if (searchBar.getAlpha() == 0f) {
                        searchBar.setVisibility(View.GONE);
                    }
                }

                return false;
            }
        });


        view.setImageResource(backgroundTime.getBackground());

//        Request Permissions
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallBack();
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                            Snackbar.make(constraintLayout, "Permission Allowed", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Allowed", Toast.LENGTH_LONG).show();


                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    Activity#requestPermissions
                                Snackbar.make(constraintLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Denied", Toast.LENGTH_LONG).show();
                                return;
                            }
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        } else {
                            Toast.makeText(getApplicationContext(), "Denied", Toast.LENGTH_LONG).show();
                            Snackbar.make(constraintLayout, "Permission Denied", Snackbar.LENGTH_LONG).show();
                        }


                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();


    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location = locationResult.getLastLocation();
                String latitude = Double.toString(locationResult.getLastLocation().getLatitude());
                String longitude = Double.toString(locationResult.getLastLocation().getLongitude());
                Toast.makeText(MainActivity.this, "Location: " + latitude + "/" + longitude, Toast.LENGTH_LONG).show();

                try {
                    TodaysWeather todaysWeather = new TodaysWeather();
                    todaysWeather.getWeatherInformation();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        backgroundTime = background.setWeatherBackground();

        theme.applyStyle(backgroundTime.getColor(), true);
        Log.i(TAG, "getTheme called");
        return theme;
    }

    public void searchClick(View view) {
        edtLocation.setText("");
        searchBar.setVisibility(View.VISIBLE);
        searchBar.animate().alpha(1f).setDuration(150);
        searchBar.animate().translationY(30);
        btnSearch.setVisibility(View.GONE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }


}
