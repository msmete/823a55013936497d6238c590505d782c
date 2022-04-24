package com.ms.spacecraft.model

interface StationTravelCallBack {
    fun onTravel(station: Station, position: Int)
}