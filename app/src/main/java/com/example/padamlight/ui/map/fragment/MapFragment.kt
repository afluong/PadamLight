package com.example.padamlight.ui.map.fragment


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.widget.Toast
import com.example.padamlight.R
import com.example.padamlight.data.local.Route
import com.example.padamlight.data.local.Suggestion
import com.example.padamlight.data.remote.dto.Direction
import com.example.padamlight.data.remote.services.GoogleMapProvider
import com.example.padamlight.enums.MarkerType
import com.example.padamlight.ui.base.BaseFragment
import com.example.padamlight.ui.map.interfaces.MapActionsDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Map Fragment
 * Responsible of displaying map and interactions with it
 */
class MapFragment : BaseFragment(R.layout.fragment_map), OnMapReadyCallback, MapActionsDelegate {

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }


    private var mMap: GoogleMap? = null

    private var googleMapProvider: GoogleMapProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleMapProvider = GoogleMapProvider()


    }

    override fun initUi() {
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

    override fun updateMap(vararg latLngs: LatLng) {
        if (mMap != null) {
            val builder = LatLngBounds.Builder()
            for (latLng in latLngs) {
                builder.include(latLng)
            }
            val bounds = builder.build()
            animateMapCamera(bounds)
        }
    }

    override fun updateMarker(type: MarkerType, markerName: String, markerLatLng: LatLng) {
        if (mMap != null) {
            val marker = MarkerOptions()
                    .position(markerLatLng)
                    .title(markerName)
                    .icon(getMarkerIcon(type))

            mMap!!.addMarker(marker)
        }
    }

    override fun clearMap() {
        if (mMap != null) {
            mMap!!.clear()
        }
    }

    override fun drawRoute(from: Suggestion, to: Suggestion) {

        val response = googleMapProvider?.getDirections(from.toString(), to.toString(), getString(R.string.google_maps_key))

        response?.enqueue(object : Callback<Direction> {
            override fun onResponse(call: Call<Direction>, response: Response<Direction>) {
                if (response.body() != null) {

                    val direction = response.body()

                    if (direction?.routes!!.isNotEmpty()) {
                        val legs = direction.routes!![0].legs!![0]
                        val route = Route(from.toString(), to.toString(),
                                legs.start_location!!.lat, legs.start_location!!.lng,
                                legs.end_location!!.lat, legs.end_location!!.lng,
                                direction.routes!![0].overview_polyline!!.points!!)

                        val polylineOptions = setupPolylines(context)
                        val pointsList = PolyUtil.decode(route.overviewPolyline)
                        for (point in pointsList) {
                            polylineOptions.add(point)
                        }
                        mMap!!.addPolyline(polylineOptions)
                        updateMap(from.latLng, to.latLng)

                    }
                }

            }

            override fun onFailure(call: Call<Direction>, t: Throwable) {
                Toast.makeText(context, R.string.no_road, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setupPolylines(context: Context?): PolylineOptions {
        val polylineOptions = PolylineOptions()

        polylineOptions.width(20f)
        polylineOptions.geodesic(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            polylineOptions.color(context!!.resources.getColor(R.color.colorAccent, context.theme))
        } else {
            polylineOptions.color(context!!.resources.getColor(R.color.colorAccent))
        }

        return polylineOptions

    }

    private fun animateMapCamera(bounds: LatLngBounds) {
        if (mMap != null) {
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    private fun getMarkerIcon(type: MarkerType): BitmapDescriptor {
        @DrawableRes var icon: Int

        icon = when (type) {
            MarkerType.PICKUP -> {
                R.drawable.ic_pickup
            }
            MarkerType.DROPOFF -> {
                R.drawable.ic_dropoff
            }
        }
        return BitmapDescriptorFactory.fromResource(icon)
    }



}


