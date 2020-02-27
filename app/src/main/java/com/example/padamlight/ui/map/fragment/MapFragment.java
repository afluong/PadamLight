package com.example.padamlight.ui.map.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


import com.example.padamlight.R;

import com.example.padamlight.data.local.Route;
import com.example.padamlight.data.remote.dto.Direction;
import com.example.padamlight.data.remote.dto.DirectionRoutesLegs;
import com.example.padamlight.data.remote.services.GoogleMapProvider;
import com.example.padamlight.enums.MarkerType;
import com.example.padamlight.data.local.Suggestion;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



/**
 * Map Fragment
 * Responsible of displaying map and interactions with it
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback, MapActionsDelegate {


    @Nullable
    private GoogleMap mMap;

    private GoogleMapProvider googleMapProvider;

    public MapFragment() {
        super(R.layout.fragment_map);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        googleMapProvider = new GoogleMapProvider();


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

    @Override
    public void drawRoute(final Suggestion from, final Suggestion to) {

        Call<Direction> response = googleMapProvider.getDirections(from.toString(), to.toString(), getString(R.string.google_maps_key));

        response.enqueue(new Callback<Direction>() {
            @Override
            public void onResponse(Call<Direction> call, Response<Direction> response) {
                if(response.body() != null){

                    Direction direction = response.body();

                    if(direction.getRoutes().length > 0){
                        DirectionRoutesLegs legs = direction.getRoutes()[0].getLegs()[0];
                        Route route = new Route(from.toString(), to.toString(),
                                legs.getStart_location().getLat(), legs.getStart_location().getLng(),
                                legs.getEnd_location().getLat(), legs.getEnd_location().getLng(),
                                direction.getRoutes()[0].getOverview_polyline().getPoints());

                        PolylineOptions polylineOptions = setupPolylines(getContext());
                        List<LatLng> pointsList = PolyUtil.decode(route.getOverviewPolyline());
                        for(LatLng point : pointsList){
                            polylineOptions.add(point);
                        }
                        Polyline currentPolyline = mMap.addPolyline(polylineOptions);
                        updateMap(from.getLatLng(), to.getLatLng());

                    }
                }

            }

            @Override
            public void onFailure(Call<Direction> call, Throwable t) {
                Toast.makeText(getContext(), "IMPOSSIBLE DE DESSINER LA ROUTE", Toast.LENGTH_LONG);
            }
        });
    }

    public PolylineOptions setupPolylines(Context context){
        PolylineOptions polylineOptions = new PolylineOptions();

        polylineOptions.width(20);
        polylineOptions.geodesic(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            polylineOptions.color(context.getResources().getColor(R.color.colorAccent, context.getTheme()));
        }else{
            polylineOptions.color(context.getResources().getColor(R.color.colorAccent));
        }

        return polylineOptions;

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


