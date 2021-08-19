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

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.aniket.earthquaketracker.MainActivity.EARTHQUAKE_COUNT;
import static com.aniket.earthquaketracker.MainActivity.LATITUDE;
import static com.aniket.earthquaketracker.MainActivity.LONGITUDE;
import static com.aniket.earthquaketracker.MainActivity.MIN_MAGNITUDE;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>>{

//    Bundle extras = getIntent().getExtras();
//    if (extras != null) {
//        String minMagnitude = extras.getString("MIN_MAGNITUDE");
//        //The key argument here must match that used in the other activity
//    }




    private String USGS_REQUEST_URL;
    private EarthquakeAdapter adapter;
    private TextView txtNoEarthquakes;
    private ProgressBar loadingSpinner;

    private double latitude;
    private double longitude;
    private String minMagnitude;
    private String earthquakeCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake);


        minMagnitude = getIntent().getStringExtra(MIN_MAGNITUDE);
        earthquakeCount = getIntent().getStringExtra(EARTHQUAKE_COUNT);
        latitude = getIntent().getDoubleExtra(LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(LONGITUDE, 0);


        Log.v("EarthquakeActivity", "latitude: " + latitude);


        txtNoEarthquakes = findViewById(R.id.txtNoEarthquakes);
        loadingSpinner = findViewById(R.id.loadingSpinner);

        Log.v("EarthquakeActivity", "Input received " + minMagnitude);

        USGS_REQUEST_URL = urlBuilder();
//        StringBuilder urlBuilder = new StringBuilder();


//        USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&starttime=2001-01-01&minmagnitude=" + minMagnitude + "&limit=" + earthquakeCount + "&latitude=" + String.valueOf(latitude) + "&longitude=" + String.valueOf(longitude) + "&maxradiuskm=1000";


        // Create a fake list of earthquake locations.
//        final List<Earthquake> earthquakes = QueryUtils.extractEarthquakes(USGS_REQUEST_URL);

//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));
//        earthquakes.add(new Earthquake(7.2, "San Fransisco", "Fev 2, 2016"));


        // Find a reference to the {@link ListView} in the layout
//        earthquakeList =findViewById(R.id.list);
//        earthquakeList.setLayoutManager(new LinearLayoutManager(this));
//        EarthquakeAdapter earthquakeAdapter = new EarthquakeAdapter(earthquakes);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of earthquakes
        adapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());
//        ArrayAdapter<Earthquake> adapter = new ArrayAdapter<Earthquake>(
//                this, R.layout.earthquake_list_item_layout, earthquakes);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentearthquake= adapter.getItem(position);


                Uri webpage = Uri.parse(currentearthquake.getUrl());
                Log.v("Url", String.valueOf(webpage));
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                try {
                    startActivity(intent);
                }
                catch (Exception e){
                    System.out.println(e);;
                }

            }
        });
        earthquakeListView.setEmptyView(txtNoEarthquakes);

//        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
//        task.execute(USGS_REQUEST_URL);

        if (isNetworkAvailable()) {
            androidx.loader.app.LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, EarthquakeActivity.this);
        }else{
            loadingSpinner.setVisibility(View.GONE);

            txtNoEarthquakes.setText("Check your Internet Connection");
        }
    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.e("EarthquakeActivity", USGS_REQUEST_URL);
        return new EarthquakeLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {
        loadingSpinner.setVisibility(View.GONE);
        adapter.clear();
        if (data != null && !data.isEmpty()){
            adapter.addAll(data);
        }

        txtNoEarthquakes.setText("No Earthquakes Found");

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {
        adapter.clear();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>>{
//
//        @Override
//        protected List<Earthquake> doInBackground(String... urls) {
//
//            if (urls.length < 1 || urls[0] == null) return null;
//            List<Earthquake> result = QueryUtils.fetchEarthquakeData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquake> data) {
//            adapter.clear();
//            if (data != null && !data.isEmpty()){
//                adapter.addAll(data);
//            }
//
//        }
//    }
    private String urlBuilder(){
        StringBuilder url = new StringBuilder();
        url.append("https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time");
        url.append("&minmagnitude="+ minMagnitude);
        url.append("&limit="+ earthquakeCount);
        url.append("&starttime=" + "2015-01-01");

        if (latitude != 0.0) url.append("&latitude=" + String.valueOf(latitude) + "&maxradiuskm=1000");
        if (longitude != 0.0) url.append("&longitude=" + String.valueOf(longitude));




        String USGSurl = url.toString();
        return USGSurl;
    }
}