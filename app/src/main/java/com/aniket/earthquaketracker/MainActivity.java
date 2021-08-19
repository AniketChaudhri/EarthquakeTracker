package com.aniket.earthquaketracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
//import android.app.LoaderManager;
import android.Manifest;
import android.app.LoaderManager.LoaderCallbacks;
//import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Spinner locationSpinner;
    private Button btnReduceMagnitude;
    private Button btnIncreaseMagnitude;
    private Button btnReduceEarthquakeCount;
    private Button btnIncreaseEarthquakeCount;
    private TextView txtEarthquakeCount;
    private TextView txtMinMagnitude;
    private int minMagnitude = 5;
    private int earthquakeCount = 20;
    private double latitude;
    private double longitude;
    public static final String MIN_MAGNITUDE = "com.aniket.earthquaketracker.MIN_MAGNITUDE";
    public static final String EARTHQUAKE_COUNT = "com.aniket.earthquaketracker.EARTHQUAKE_COUNT";
    public static final String LONGITUDE = "com.aniket.LONGITUDE";
    public static final String LATITUDE = "com.aniket.LATITUDE";

    private Button btnLocation;
    private Switch switchCurrentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
////
//        LocationListener locationListener = new MyLocationListener();
//
//        locationManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        locationSpinner = findViewById(R.id.locationSpinner);

        btnReduceMagnitude = findViewById(R.id.btnReduceMagnitude);
        btnIncreaseMagnitude = findViewById(R.id.btnIncreaseMagnitude);
        txtMinMagnitude = findViewById(R.id.txtMinMagnitude);

        btnIncreaseEarthquakeCount = findViewById(R.id.btnIncreaseEarthquakeCount);
        btnReduceEarthquakeCount = findViewById(R.id.btnReduceEarthquakeCount);
        txtEarthquakeCount = findViewById(R.id.txtEarthquakeCount);
        txtEarthquakeCount.setText(String.valueOf(earthquakeCount));
        btnLocation = findViewById(R.id.btnLocation);
        switchCurrentLocation = findViewById(R.id.switch1);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


//        int minMagnitude = Integer.valueOf((String) txtMinMagnitude.getText());


        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switchCurrentLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            getLocation();
                            Toast.makeText(MainActivity.this, "You checked location" + latitude, Toast.LENGTH_SHORT).show();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                            getLocation();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Check your GPS", Toast.LENGTH_SHORT).show();
                    }
                    
                    
                }
            }
        });
        
        btnReduceMagnitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceMagnitude();
            }
        });
        btnIncreaseMagnitude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseMagnitude();
            }
        });
        btnReduceEarthquakeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reduceEarthquakeCount();
            }
        });
        btnIncreaseEarthquakeCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseEarthquakeCount();
            }
        });

        button = findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EarthquakeActivity.class);
                String minMagnitude = txtMinMagnitude.getText().toString();
                String earthquakeCount = txtEarthquakeCount.getText().toString();
                Log.v("Main Activity", "min magnitude passed to activity:" + minMagnitude + minMagnitude.getClass().getName());
                Log.v("Main Activity", "latitude passed to EarthquakeActivity: " + latitude);

                intent.putExtra(MIN_MAGNITUDE, minMagnitude);
                intent.putExtra(EARTHQUAKE_COUNT, earthquakeCount);
                intent.putExtra(LONGITUDE, longitude);
                intent.putExtra(LATITUDE, latitude);


//                EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
//                String message = editText.getText().toString();
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });


    }

    private void getLocation() {
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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        longitude = addresses.get(0).getLongitude();
                        Log.v("Main Activity", "longitude received: " + longitude);
                        latitude = addresses.get(0).getLatitude();
                        Log.v("Main Activity", "latitude received: " + latitude);

                        Toast.makeText(MainActivity.this, "Location" + addresses.get(0).getLatitude(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void reduceMagnitude(){
        if (minMagnitude == 0) {
            Toast.makeText(this, "Magnitude cannot be less than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        minMagnitude --;
        txtMinMagnitude.setText(String.valueOf(minMagnitude));
//        return minMagnitude-1;
    }
    private void increaseMagnitude(){
        if (minMagnitude == 10) {
            Toast.makeText(this, "Limit Reached", Toast.LENGTH_SHORT).show();
            return;
        }
        minMagnitude ++;
        txtMinMagnitude.setText(String.valueOf(minMagnitude));
    }
    public void reduceEarthquakeCount(){
        if (earthquakeCount == 0) {
            Toast.makeText(this, "Earthquake Count cannot be less than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        earthquakeCount -= 5;
        txtEarthquakeCount.setText(String.valueOf(earthquakeCount));
//        return minEarthquakeCount-1;
    }
    private void increaseEarthquakeCount(){
        earthquakeCount += 5;
        txtEarthquakeCount.setText(String.valueOf(earthquakeCount));
    }






}