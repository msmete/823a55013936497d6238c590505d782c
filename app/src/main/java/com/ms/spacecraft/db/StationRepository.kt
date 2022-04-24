package com.ms.spacecraft.db

import androidx.lifecycle.LiveData
import com.ms.spacecraft.model.Station
import javax.inject.Inject

class StationRepository @Inject constructor(private val stationDao: StationDao) {

    fun getStationList(): LiveData<List<Station>> {
        return stationDao.getStationList()
    }

    fun getStationByName(name: String): Station {
        return stationDao.getStationByName(name)
    }

    fun getStationCount(): Int {
        return stationDao.getStationCount()
    }

    fun updateStation(station: Station) {
        stationDao.updateStation(station)
    }

    fun disableAllStation() {
        stationDao.disableAllStation()
    }

    fun insertStations(stationList: List<Station>) {
        stationList.forEach {
            stationDao.insertStation(it)
        }
    }
}

