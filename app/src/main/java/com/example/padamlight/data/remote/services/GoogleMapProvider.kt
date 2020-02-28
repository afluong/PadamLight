package com.example.padamlight.data.remote.services

import com.example.padamlight.data.remote.dto.Direction

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GoogleMapProvider {

    companion object {

        private val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/"
    }

    private val googleMapService: GoogleMapService

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build()
        googleMapService = retrofit.create(GoogleMapService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        val okBuilder = OkHttpClient.Builder()
        return okBuilder.build()
    }

    fun getDirections(origin: String, destination: String, key: String): Call<Direction> {
        return googleMapService.getDirections(origin, destination, key)
    }

}
