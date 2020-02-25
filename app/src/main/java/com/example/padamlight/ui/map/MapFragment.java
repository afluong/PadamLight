package com.example.padamlight.ui.map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.padamlight.R;
import com.example.padamlight.utils.DirectionsParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Map Fragment
 * Responsible of displaying map and interactions with it
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, MapActionsDelegate {

    String duration;
    @Nullable
    private GoogleMap mMap;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instanciate map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.902964, 1.9092510000000402), 16f));
        }
    }

    @Override
    public void updateMap(ArrayList<LatLng> latLngs) {
        if (mMap != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngs) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            animateMapCamera(bounds);
        }
    }

    @Override
    public void updateMarker(ArrayList<MarkerType> type, ArrayList<String> markerName, ArrayList<LatLng> latlngs) {
        if (mMap != null) {
            MarkerOptions marker = new MarkerOptions();
            for (int i = 0; i < latlngs.size(); i++) {
                marker.position(latlngs.get(i));
                marker.title(markerName.get(i));
                marker.icon(getMarkerIcon(type.get(i)));
                mMap.addMarker(marker);
            }
        }
    }

    @Override
    public void displayItinerary(ArrayList<LatLng> latLngs) {
        String url = getRequestUrl(latLngs.get(0), latLngs.get(1));
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);

    }

    public String getRequestUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getContext().getString(R.string.google_maps_key);
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
                duration = directionsParser.parseDuration(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList<LatLng> points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
                displayAlert(duration, points);
            } else {
                Toast.makeText(getActivity(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    void displayAlert(String duration, final ArrayList<LatLng> addresses) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setMessage(getActivity().getString(R.string.message_duration) + " " + duration);
        alertBuilder.setCancelable(true);

        alertBuilder.setPositiveButton(
                "Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        alertBuilder.setNegativeButton(
                "Google maps",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://maps.google.com/maps?saddr=" +
                                        addresses.get(0).latitude + ","
                                        + addresses.get(0).longitude
                                        + "&daddr=" +
                                        addresses.get(addresses.size() - 1).latitude + ","
                                        + addresses.get(addresses.size() - 1).longitude));
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    @Override
    public void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void animateMapCamera(LatLngBounds bounds) {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60));
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 60));
        }
    }

    private BitmapDescriptor getMarkerIcon(MarkerType type) {
        @DrawableRes int icon = R.drawable.ic_pickup;

        switch (type) {
            case PICKUP: {
                icon = R.drawable.ic_pickup;
                break;
            }
            case DROPOFF: {
                icon = R.drawable.ic_dropoff;
                break;
            }
        }
        return BitmapDescriptorFactory.fromResource(icon);
    }

}

/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
interface MapActionsDelegate {
    void updateMap(ArrayList<LatLng> latLngs);

    void updateMarker(ArrayList<MarkerType> type, ArrayList<String> markerName, ArrayList<LatLng> latlngs);

    void displayItinerary(ArrayList<LatLng> latLngs);

    void clearMap();

}

/**
 * Market enum
 * Define if marker is pickup type or dropoff type
 */
enum MarkerType {
    PICKUP, DROPOFF;
}
