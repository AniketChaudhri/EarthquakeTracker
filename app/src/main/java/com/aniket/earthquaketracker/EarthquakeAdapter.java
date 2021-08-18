package com.aniket.earthquaketracker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private final ArrayList<Earthquake> localDataSet;
    private TextView magnitude;
    private TextView txtprimaryLocation;
    private TextView txtoffsetLocation;
    private TextView date;
    private TextView time;
    private GradientDrawable magnitudeCircle;


    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> objects) {
        super(context, 0, objects);
        this.localDataSet = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentItemView = convertView;
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item_layout, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        if (currentEarthquake != null) {
            magnitude = currentItemView.findViewById(R.id.Magnitude);
            txtprimaryLocation = currentItemView.findViewById(R.id.txtprimaryLocation);
            txtoffsetLocation = currentItemView.findViewById(R.id.txtoffsetLocation);

            magnitudeCircle = (GradientDrawable) magnitude.getBackground();
            int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());
            magnitudeCircle.setColor(magnitudeColor);

            date = currentItemView.findViewById(R.id.Date);
            time = currentItemView.findViewById(R.id.Time);

            double mag = currentEarthquake.getMagnitude();
            DecimalFormat formatter = new DecimalFormat("0.0");
            String magToDisplay = formatter.format(mag);
            magnitude.setText(magToDisplay);
//            place.setText(String.valueOf(currentEarthquake.getPlace()));
            date.setText(String.valueOf(currentEarthquake.getDate()));
            time.setText(String.valueOf(currentEarthquake.getTime()));

            String exactLocation = currentEarthquake.getPlace();
            Log.v("Location error", position+ " original location parsed: " + exactLocation);
            String primaryLocation;
            String offsetLocation;
            if (exactLocation.contains("of")){
                int indexStart = exactLocation.indexOf("of");
                primaryLocation = exactLocation.substring(indexStart+3, exactLocation.length());
                offsetLocation = exactLocation.substring(0, indexStart+3);
            }
            else{
                primaryLocation = exactLocation;
                offsetLocation = "Near the";
            }
            txtprimaryLocation.setText(primaryLocation);
            txtoffsetLocation.setText(offsetLocation);



        }


        return currentItemView;
    }

    private int getMagnitudeColor(double magnitudeValue){
        int magnitudeFloor = (int) Math.floor(magnitudeValue);
        int magnitudeColorResourceId;
        switch(magnitudeFloor){
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }

}

