package com.ms.spacecraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ms.spacecraft.model.Station

@Dao
interface StationDao {

    @Query("SELECT * FROM stations")
    fun getStationList(): LiveData<List<Station>>

    @Query("SELECT * FROM stations WHERE name = :name")
    fun getStationByName(name: String): Station

    @Query("SELECT COUNT(id) FROM stations")
    fun getStationCount(): Int

    @Update
    fun updateStation(station: Station)

    @Query("UPDATE stations SET isCompleted = true")
    fun disableAllStation()

    @Insert
    fun insertStation(vararg station: Station)
}