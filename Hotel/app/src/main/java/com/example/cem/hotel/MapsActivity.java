package com.example.cem.hotel;

import android.app.Dialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment fragment;
    HotelDatabaseHelper hotelDB;

    String city = null;


    double mLatitude = 0;
    double mLongitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        hotelDB = new HotelDatabaseHelper(this);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());


        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mGoogleMap = fragment.getMap();
            mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            mGoogleMap.getUiSettings().setScrollGesturesEnabled(true);
            city = getIntent().getStringExtra("city");

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);


            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/textsearch/json?");
            //https://maps.googleapis.com/maps/api/place/nearbysearch/json?
//                    https://maps.googleapis.com/maps/api/place/radarsearch/json?location=51.503186,-0.126446&radius=5000&types=museum&key=AIzaSyCRQWy93Q_7cGwKovjDialkIqr-8yaYKOU
            //https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotels+in+Izmir+Turkey&key=AIzaSyCRQWy93Q_7cGwKovjDialkIqr-8yaYKOU
//                    sb.append("query=hotels+in+"+region+"+"+country);
            sb.append("query=hotels+in+" + city);
//                    sb.append("location="+mLatitude+","+mLongitude);
//                    sb.append("&rankby=distance");
//                    sb.append("&types="+type);
//                    sb.append("&sensor=true");
            sb.append("&key=AIzaSyCRQWy93Q_7cGwKovjDialkIqr-8yaYKOU");
//
            // Creating a new non-ui thread task to download Google place json data
            PlacesTask placesTask = new PlacesTask();

            // Invokes the "doInBackground()" method of the class PlaceTask
            placesTask.execute(sb.toString());

        }

    }


    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("URL Download Error", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    HashMap<String, String> hotelNameAndAddressList = new HashMap<>();

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }


        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers
            mGoogleMap.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list

                HashMap<String, String> hmPlace = list.get(i);

                CharSequence place_id = hmPlace.get("place_id");

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting address of the place
                String formatted_address = hmPlace.get("formatted_address");

                // Gettin name of the place
                String name = hmPlace.get("place_name");


                LatLng latLng = new LatLng(lat, lng);

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                //This will be displayed on taping the marker
                markerOptions.title(name);

                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(8));

                mGoogleMap.addMarker(markerOptions);
                // Placing a marker on the touched position
                hotelNameAndAddressList.put(name, formatted_address);

            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    String hotelName = null;
    String hotelAddress = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /* Take selected Marker's name and address */
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                hotelName = marker.getTitle();
                hotelAddress = hotelNameAndAddressList.get(marker.getTitle());
                return false;
            }
        });

        switch (item.getItemId()) {
            case R.id.addToSavedHotelList:
                // will add to rycyclerview list
                addToDB(hotelName, hotelAddress);

                return true;
            case R.id.showHotelList:
                Intent i = new Intent(this, HotelListActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    // Add Hotel's name and address to DB
    public void addToDB(String hotelName, String hotelAddress) {
        boolean isAdded = hotelDB.insertHotel(hotelName, hotelAddress);
        //
        if (isAdded) {
            Toast.makeText(MapsActivity.this, "Added to Hotel List", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MapsActivity.this, "Couldnt added.Hotel already exists in Hotel List", Toast.LENGTH_SHORT).show();
        }
        hotelDB.close();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
}