package com.example.padamlight

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
/*
* We just declare the same class than MapFragment and change the names of Type and Interface to not encounter problems.*/
class MapFragmentkt : Fragment(), OnMapReadyCallback, MapActionsDelegatekt {
    private var currentPolyline: Polyline? = null
    private var mMap: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Instanciate map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (mMap != null) {
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(47.902964, 1.9092510000000402), 16f))
        }
    }

    override fun updateMap(vararg latLngs: LatLng?) {
        if (mMap != null) {
            val builder = LatLngBounds.Builder()
            for (latLng in latLngs) {
                builder.include(latLng)
            }
            val bounds = builder.build()
            animateMapCamera(bounds)
            //FetchURL fetchURL = new FetchURL(mMap,getContext());
//fetchURL.execute(getUrl(latLngs[0], latLngs[1], "driving"), "driving");
        }
    }




    override fun updateMarker(type: MarkerTypekt?, markerName: String?, markerLatLng: LatLng?) {
        if (mMap != null) {
            val marker = MarkerOptions()
                    .position(markerLatLng!!)
                    .title(markerName)
                    .icon(getMarkerIcon(type))
            mMap!!.addMarker(marker)
        }
    }

    fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline!!.remove()
        currentPolyline = mMap!!.addPolyline(values[0] as PolylineOptions?)
    }

    override fun clearMap() {
        if (mMap != null) {
            mMap!!.clear()
        }
    }

    private fun animateMapCamera(bounds: LatLngBounds) {
        if (mMap != null) {
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    private fun getMarkerIcon(type: MarkerTypekt?): BitmapDescriptor {
        @DrawableRes var icon = R.drawable.ic_pickup
        icon = when (type) {
            MarkerTypekt.PICKUP -> {
                R.drawable.ic_pickup
            }
            MarkerTypekt.DROPOFF -> {
                R.drawable.ic_dropoff
            }
            else -> {
                R.drawable.ic_pickup
            }
        }
        return BitmapDescriptorFactory.fromResource(icon)
    }

    /*

    * */
    private fun getUrl(origin: LatLng, dest: LatLng, directionMode: String): String { // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude
        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude
        // Mode
        val mode = "mode=$directionMode"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$mode"
        // Output format
        val output = "json"
        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key)
    }

    companion object {
        fun newInstance(): MapFragmentkt {
            return MapFragmentkt()
        }
    }
}
/**
 *
 *
 */

/**
 *
 *
 */
/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
internal interface MapActionsDelegatekt {
    fun updateMap(vararg latLngs: LatLng?)
    fun updateMarker(type: MarkerTypekt?, markerName: String?, markerLatLng: LatLng?)
    fun clearMap()
}

/**
 * Market enum
 * Define if marker is pickup type or dropoff type
 */
enum class MarkerTypekt {
    PICKUP, DROPOFF
}
