package com.example.padamlight.data.local

import android.support.annotation.DrawableRes

import com.example.padamlight.R
import com.google.android.gms.maps.model.LatLng

/**
 * Suggestion model
 * Defines suggestion in From or To textfields
 */
data class Suggestion(val latLng: LatLng) {

    override fun toString(): String {
        return latLng.latitude.toString() + "," + latLng.longitude
    }
}
