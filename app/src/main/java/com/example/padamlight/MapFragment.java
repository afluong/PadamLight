package com.example.padamlight;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.padamlight.jsonparser.DataParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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
    private Polyline currentPolyline;

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
    public void updateMap(LatLng... latLngs) {
        if (mMap != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : latLngs) {
                builder.include(latLng);
            }
            LatLngBounds bounds = builder.build();
            animateMapCamera(bounds);
            //FetchURL fetchURL = new FetchURL(mMap,getContext());
            //fetchURL.execute(getUrl(latLngs[0], latLngs[1], "driving"), "driving");
        }
    }

    @Override
    public void updateMarker(MarkerType type, String markerName, LatLng markerLatLng) {
        if (mMap != null) {
            MarkerOptions marker = new MarkerOptions()
                    .position(markerLatLng)
                    .title(markerName)
                    .icon(getMarkerIcon(type));

            mMap.addMarker(marker);
        }
    }

    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
    @Override
    public void clearMap() {
        if (mMap != null) {
            mMap.clear();
        }
    }

    private void animateMapCamera(LatLngBounds bounds) {
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
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
    /*

    * */
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

}
/**
 *
 *
 */

/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
interface MapActionsDelegate {
    void updateMap(LatLng... latLngs);

    void updateMarker(MarkerType type, String markerName, LatLng markerLatLng);

    void clearMap();
}

/**
 * Market enum
 * Define if marker is pickup type or dropoff type
 */
enum MarkerType {
    PICKUP, DROPOFF;
}
