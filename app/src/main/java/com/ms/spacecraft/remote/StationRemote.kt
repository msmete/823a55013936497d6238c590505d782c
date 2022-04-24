package com.ms.spacecraft.remote

import com.ms.spacecraft.model.Station
import retrofit2.Callback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StationRemote @Inject constructor(private val api: StationApi) {

    fun getStationList(callback: Callback<List<Station>>) {
        api.getStationList().enqueue(callback)
    }
}