package com.example.padamlight.interfaces

import com.example.padamlight.enums.MarkerType
import com.google.android.gms.maps.model.LatLng

interface MapActionsDelegate {

		fun updateMap(vararg latLngs: LatLng)

		fun updateMarker(type: MarkerType, markerName: String, markerLatLng: LatLng)

		fun clearMap()

		fun buildItinenary(from: LatLng, to: LatLng)
}