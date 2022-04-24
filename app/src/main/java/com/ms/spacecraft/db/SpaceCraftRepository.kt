package com.ms.spacecraft.db

import androidx.lifecycle.LiveData
import com.ms.spacecraft.model.SpaceCraft
import javax.inject.Inject

class SpaceCraftRepository @Inject constructor(private val spaceCraftDao: SpaceCraftDao) {

    fun saveSpaceCraft(spaceCraft: SpaceCraft) {
        spaceCraftDao.insertSpaceCraft(spaceCraft)
    }

    fun getSpaceCraft(): LiveData<SpaceCraft>? {
        return spaceCraftDao.getSpaceCraft("1")
    }

    fun updateSpaceCraft(spaceCraft: SpaceCraft) {
        spaceCraftDao.updateSpaceCraft(spaceCraft)
    }

    fun getSpaceCraftCount(): Int {
        return spaceCraftDao.getSpaceCraftCount()
    }

}