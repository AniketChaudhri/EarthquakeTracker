package com.aniket.earthquaketracker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
//    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmagnitude=6&limit=10";
    private static final String LOG_TAG = "#TODO";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    public static List<Earthquake> fetchEarthquakeData(String requestURL){
        URL url = createURL(requestURL);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPRequest(url);
        }
        catch(IOException e){
            Log.e(LOG_TAG, "Problem makeing HTTP request");

        }
        List<Earthquake> earthquakes = extractEarthquakes(jsonResponse);

        return earthquakes;
    }
    private static URL createURL(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;

    }
    private static String makeHTTPRequest(URL url) throws IOException{
        String jsonResponse="";

        if (url == null) return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        }catch (IOException e) {System.out.println(e);}
        finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }
        return jsonResponse;



    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private QueryUtils() {
    }

    /**
     * Return a list of {@link Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Earthquake> extractEarthquakes(String earthquakeJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject earthquakesJSON = new JSONObject(earthquakeJSON);
            JSONArray features = earthquakesJSON.getJSONArray("features");

            for (int i=0; i<features.length();i++){
                JSONObject earthquake = features.getJSONObject(i);
                JSONObject earthquakeProperties = earthquake.getJSONObject("properties");
                double magnitude = earthquakeProperties.getDouble("mag");
                String location = earthquakeProperties.getString("place");
                if (location == "null") continue;
                String time = earthquakeProperties.getString("time");
                String url = earthquakeProperties.getString("url");


                Date dateObject = new Date(Long.parseLong(time));
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat timeFormat = new SimpleDateFormat("KK:mm a");
                String dateToDisplay = dateFormat.format(dateObject);
                String timeToDisplay = timeFormat.format(dateObject);
//                System.out.println(dateToDisplay);
                earthquakes.add(new Earthquake(magnitude, location, dateToDisplay, timeToDisplay, url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            Log.v("QueryUtils", earthquakeJSON);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

}