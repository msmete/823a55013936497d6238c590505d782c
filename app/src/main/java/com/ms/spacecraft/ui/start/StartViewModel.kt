package com.ms.spacecraft.ui.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ms.spacecraft.db.SpaceCraftRepository
import com.ms.spacecraft.db.StationRepository
import com.ms.spacecraft.model.BasicCallBack
import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station
import com.ms.spacecraft.remote.StationRemote
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val spaceCraftRepository: SpaceCraftRepository,
    private val stationRepository: StationRepository,
    private val statinRemote: StationRemote
) : ViewModel() {
    private val totalPoint = 15

    companion object {
        const val startStation = "Dünya"
    }

    private val _speed = MutableLiveData<Int>().apply {
        value = 1
    }
    val speed: LiveData<Int> = _speed

    private val _capacity = MutableLiveData<Int>().apply {
        value = 1
    }
    val capacity: LiveData<Int> = _capacity

    private val _durability = MutableLiveData<Int>().apply {
        value = 1
    }
    val durability: LiveData<Int> = _durability

    private val _totalPointText = MutableLiveData<String>().apply {
        value = "3/15"
    }
    val totalPointText: LiveData<String> = _totalPointText

    fun controlAndIncreaseSpeedBar(newValue: Int) {
        if (newValue + capacity.value!! + durability.value!! <= totalPoint) {
            _speed.value = newValue
            updateTotalPointText()
        } else {
            _speed.value = speed.value
        }
    }

    fun controlAndIncreaseCapacityBar(newValue: Int) {
        if (newValue + speed.value!! + durability.value!! <= totalPoint) {
            _capacity.value = newValue
            updateTotalPointText()
        } else {
            _capacity.value = capacity.value
        }
    }

    fun controlAndIncreaseDurabilityBar(newValue: Int) {
        if (newValue + speed.value!! + capacity.value!! <= totalPoint) {
            _durability.value = newValue
            updateTotalPointText()
        } else {
            _durability.value = durability.value
        }
    }

    private fun updateTotalPointText() {
        val currentPoint = speed.value!! + capacity.value!! + durability.value!!
        _totalPointText.postValue("$currentPoint/$totalPoint")
    }

    fun controlTotalPointForSave(): Boolean {
        return (speed.value!! + capacity.value!! + durability.value!!) == totalPoint
    }

    fun saveSpaceCraft(name: String): Boolean {

        val stationEarth = stationRepository.getStationByName(startStation)
        val spaceCraft = SpaceCraft(
            1,
            name,
            (capacity.value!! * 10000),
            (speed.value!! * 20),
            (durability.value!! * 10000),
            100,
            (durability.value!! * 10),
            startStation,
            stationEarth.coordinateX,
            stationEarth.coordinateY
        )

        return try {
            spaceCraftRepository.saveSpaceCraft(spaceCraft)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getStationList(callback: BasicCallBack) {
        if (stationRepository.getStationCount() == 0) {
            statinRemote.getStationList(object : Callback<List<Station>> {
                override fun onResponse(
                    call: Call<List<Station>>,
                    response: Response<List<Station>>
                ) {
                    try {
                        stationRepository.insertStations(response.body()!!)
                        callback.onResult(true)
                    } catch (e: java.lang.Exception) {
                        callback.onResult(false)
                    }
                }

                override fun onFailure(call: Call<List<Station>>, t: Throwable) {
                    callback.onResult(false)
                }
            })
        } else {
            callback.onResult(true)
        }
    }

    fun controlSavedDataExist(): Boolean {
        return stationRepository.getStationCount() > 0 && spaceCraftRepository.getSpaceCraftCount() > 0
    }
}