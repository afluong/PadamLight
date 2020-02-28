package com.example.padamlight.data.remote.services

import com.example.padamlight.data.remote.dto.Direction

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapService {

    @GET("directions/json")
    fun getDirections(
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String): Call<Direction>
}
