package com.ms.spacecraft.remote

import com.ms.spacecraft.model.Station
import retrofit2.Call
import retrofit2.http.GET

interface StationApi {
    @GET("/v3/e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    fun getStationList(): Call<List<Station>>
}