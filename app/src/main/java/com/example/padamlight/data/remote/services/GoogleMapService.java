package com.example.padamlight.data.remote.services;

import com.example.padamlight.data.remote.dto.Direction;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapService {

    @GET("directions/json")
    Call<Direction> getDirections(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key);
}
