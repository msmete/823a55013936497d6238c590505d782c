package com.ms.spacecraft.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ms.spacecraft.model.SpaceCraft

@Dao
interface SpaceCraftDao {

    @Query("SELECT * FROM spacecraft WHERE id = :id")
    fun getSpaceCraft(id: String): LiveData<SpaceCraft>?

    @Insert
    fun insertSpaceCraft(vararg spaceCraft: SpaceCraft)

    @Update
    fun updateSpaceCraft(spaceCraft: SpaceCraft)

    @Query("SELECT COUNT(id) FROM spacecraft")
    fun getSpaceCraftCount(): Int

}