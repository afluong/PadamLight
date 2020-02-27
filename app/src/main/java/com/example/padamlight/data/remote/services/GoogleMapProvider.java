package com.example.padamlight.data.remote.services;

import com.example.padamlight.data.remote.dto.Direction;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapProvider {

    private static final String GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/";

    private GoogleMapService googleMapService;

    public GoogleMapProvider(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GOOGLE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        googleMapService = retrofit.create(GoogleMapService.class);
    }

    private OkHttpClient createOkHttpClient(){
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        return okBuilder.build();
    }

    public Call<Direction> getDirections(String origin, String destination, String key){
        return googleMapService.getDirections(origin, destination, key);
    }
}
