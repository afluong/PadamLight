package com.example.padamlight

import android.support.annotation.DrawableRes
import com.google.android.gms.maps.model.LatLng

class Suggestionkt(val latLng: LatLng) {
    @get:DrawableRes
    @DrawableRes
    val icon: Int

    init {
        icon = R.drawable.ic_favorite
    }


}
