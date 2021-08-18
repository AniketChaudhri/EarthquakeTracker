package com.aniket.earthquaketracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
//import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
//import android.content.Loader;
import android.app.LoaderManager.LoaderCallbacks;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
    public static final String MIN_MAGNITUDE = "com.aniket.earthquaketracker.MIN_MAGNITUDE";
    public static final String EARTHQUAKE_COUNT = "com.aniket.earthquaketracker.EARTHQUAKE_COUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationSpinner = findViewById(R.id.locationSpinner);

        btnReduceMagnitude = findViewById(R.id.btnReduceMagnitude);
        btnIncreaseMagnitude = findViewById(R.id.btnIncreaseMagnitude);
        txtMinMagnitude = findViewById(R.id.txtMinMagnitude);

        btnIncreaseEarthquakeCount = findViewById(R.id.btnIncreaseEarthquakeCount);
        btnReduceEarthquakeCount = findViewById(R.id.btnReduceEarthquakeCount);
        txtEarthquakeCount = findViewById(R.id.txtEarthquakeCount);
        txtEarthquakeCount.setText(String.valueOf(earthquakeCount));
//        int minMagnitude = Integer.valueOf((String) txtMinMagnitude.getText());



        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this, R.array.location_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

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

                intent.putExtra(MIN_MAGNITUDE, minMagnitude);
                intent.putExtra(EARTHQUAKE_COUNT, earthquakeCount);

//                EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
//                String message = editText.getText().toString();
//                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
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