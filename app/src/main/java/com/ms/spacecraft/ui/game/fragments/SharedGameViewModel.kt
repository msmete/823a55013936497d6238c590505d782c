package com.ms.spacecraft.ui.game.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ms.spacecraft.db.SpaceCraftRepository
import com.ms.spacecraft.db.StationRepository
import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.ui.start.StartViewModel
import com.ms.spacecraft.utils.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedGameViewModel @Inject constructor(
    private val spaceCraftRepository: SpaceCraftRepository,
    private val stationRepository: StationRepository
) : ViewModel() {

    private var damageJob: Job? = null
    private val _damageCounter = MutableLiveData<String>().apply {
        value = ""
    }
    val damageCounter: LiveData<String> = _damageCounter

    private val _endMessage = MutableLiveData<EndMessageType>().apply {
        value = EndMessageType.NONE
    }
    val endMessage: LiveData<EndMessageType> = _endMessage

    var localSpaceCraft: SpaceCraft? = null
    var localStationList: List<Station>? = null

    fun spaceCraftLiveData(): LiveData<SpaceCraft> {
        return spaceCraftRepository.getSpaceCraft()!!
    }

    fun stationListLiveData(): LiveData<List<Station>> {
        return stationRepository.getStationList()!!
    }

    fun travel(station: Station) {
        localSpaceCraft?.let {
            travel(
                it,
                station,
                Util.calculateDistanceCraftToStation(it, station),
                true
            )
        }
    }

    private fun travel(
        mySpaceCraft: SpaceCraft,
        station: Station,
        totalDistance: Float,
        isCostAvailable: Boolean
    ) {
        if (isCostAvailable) {
            mySpaceCraft.EUS -= totalDistance.toInt()
            if (mySpaceCraft.UGS >= station.need) {
                mySpaceCraft.UGS -= station.need
                station.stock += station.need
                station.need = 0
            } else {
                station.stock += mySpaceCraft.UGS
                station.need -= mySpaceCraft.UGS
                mySpaceCraft.UGS = 0
            }
        }
        mySpaceCraft.currentStation = station.name
        mySpaceCraft.currentX = station.coordinateX
        mySpaceCraft.currentY = station.coordinateY
        spaceCraftRepository.updateSpaceCraft(mySpaceCraft)

        if (isCostAvailable) {
            station.isCompleted = true
            stationRepository.updateStation(station)
            checkAndFinishGame()
        }

    }

    fun addFavorite(station: Station) {
        station.isFavorite = !station.isFavorite
        stationRepository.updateStation(station)
    }

    fun startTimerForDamage() {
        if (damageJob == null) {
            damageJob = viewModelScope.launch {
                localSpaceCraft?.let {
                    var counter = it.damageCounter
                    while (true) {
                        _damageCounter.postValue("${counter}sn")
                        delay(1000)
                        counter--
                        if (counter == 0) {
                            damageSpaceCraft(localSpaceCraft!!)
                            counter = it.damageCounter
                        }
                    }
                }

            }
            checkAndFinishGame()
        }
    }

    private fun damageSpaceCraft(spaceCraft: SpaceCraft) {
        spaceCraft.damageCapacity -= 10
        spaceCraftRepository.updateSpaceCraft(spaceCraft)
        checkAndFinishGame()
    }

    private fun checkAndFinishGame() {
        var endMessageType = EndMessageType.NONE
        var isFinished = false

        localSpaceCraft?.let { spaceCraft ->
            when {
                spaceCraft.UGS == 0 -> {
                    isFinished = true
                    endMessageType = EndMessageType.UGS
                }
                spaceCraft.damageCapacity == 0 -> {
                    isFinished = true
                    endMessageType = EndMessageType.DAMAGE
                }
                else -> {
                    localStationList?.let { stations ->
                        isFinished = true
                        endMessageType = EndMessageType.EUS
                        stations.forEach {
                            if (!it.isCompleted && spaceCraft.EUS > Util.calculateDistanceCraftToStation(
                                    spaceCraft,
                                    it
                                )
                            ) {
                                isFinished = false
                            }
                        }
                    }
                }
            }
        }

        if (isFinished) {
            damageJob?.cancel()
            val station = stationRepository.getStationByName(StartViewModel.startStation)
            localSpaceCraft?.let {
                travel(
                    it,
                    station,
                    Util.calculateDistanceCraftToStation(it, station),
                    false
                )
                stationRepository.disableAllStation()
                _endMessage.postValue(endMessageType)
            }

        }
    }

    enum class EndMessageType {
        NONE,
        UGS,
        DAMAGE,
        EUS
    }
}

