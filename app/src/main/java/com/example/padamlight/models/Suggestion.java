package com.example.padamlight.models;

import android.support.annotation.DrawableRes;

import com.example.padamlight.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Suggestion model
 * Defines suggestion in From or To textfields
 */
public class Suggestion {
    private LatLng latLng;

    @DrawableRes
    private int icon;

    public Suggestion(LatLng latLng) {
        this.latLng = latLng;
        this.icon = R.drawable.ic_favorite;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @DrawableRes
    public int getIcon() {
        return icon;
    }
}
