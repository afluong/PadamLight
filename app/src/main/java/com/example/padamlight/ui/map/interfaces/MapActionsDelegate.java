package com.example.padamlight.ui.map.interfaces;

import com.example.padamlight.enums.MarkerType;
import com.example.padamlight.data.local.Suggestion;
import com.google.android.gms.maps.model.LatLng;

/**
 * Map interface
 * Implement this in your page where there is a map to use map methods
 */
public interface MapActionsDelegate {
    void updateMap(LatLng... latLngs);

    void updateMarker(MarkerType type, String markerName, LatLng markerLatLng);

    void clearMap();

    void drawRoute(Suggestion from, Suggestion to);
}
