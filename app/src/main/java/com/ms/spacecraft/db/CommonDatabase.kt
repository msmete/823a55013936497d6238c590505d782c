package com.ms.spacecraft.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station

@Database(entities = [SpaceCraft::class, Station::class], version = 1)
abstract class CommonDatabase : RoomDatabase() {
    abstract fun spaceCraftDao(): SpaceCraftDao
    abstract fun stationDao(): StationDao
}