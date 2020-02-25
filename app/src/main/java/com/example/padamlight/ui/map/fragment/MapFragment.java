package com.example.padamlight.ui.map.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.padamlight.R;

import com.example.padamlight.enums.MarkerType;
import com.example.padamlight.ui.base.BaseFragment;
import com.example.padamlight.ui.map.interfaces.MapActionsDelegate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.Bind;

/**
 * Map Fragment
 * Responsible of displaying map and interactions with it
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, MapActionsDelegate {


    @Nullable
    private GoogleMap mMap;

    public MapFragment() {
        super(R.layout.fragment_map);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initUi() {
        // Instanciate map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }


    public static MapFragment newInstance() {
        return new MapFragment();
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

}


