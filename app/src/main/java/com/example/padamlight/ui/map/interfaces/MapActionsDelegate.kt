package com.example.padamlight.ui.map.interfaces

import com.example.padamlight.enums.MarkerType
import com.example.padamlight.data.local.Suggestion
import com.google.android.gms.maps.model.LatLng

/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
interface MapActionsDelegate {
    fun updateMap(vararg latLngs: LatLng)

    fun updateMarker(type: MarkerType, markerName: String, markerLatLng: LatLng)

    fun clearMap()

    fun drawRoute(from: Suggestion, to: Suggestion)
}
