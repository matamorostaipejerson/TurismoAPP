package com.example.turismopampas_hhh.Home.FragmentFoot.Mapa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/v2/directions/driving-car")
    Call<RouteResponse> getRoute(
            @Query("api_key") String key,
            @Query("start") String start,
            @Query("end") String end
    );
}
