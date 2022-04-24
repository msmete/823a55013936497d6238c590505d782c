package com.ms.spacecraft.utils

import com.ms.spacecraft.model.SpaceCraft
import com.ms.spacecraft.model.Station
import kotlin.math.abs

class Util {
    companion object {
        fun calculateDistanceCraftToStation(spaceCraft: SpaceCraft, station: Station): Float {
            val calculatedX = abs(spaceCraft.currentX - station.coordinateX)
            val calculatedY = abs(spaceCraft.currentY - station.coordinateY)
            return calculatedX + calculatedY
        }

        fun calculateDistanceBetweenStations(station1: Station, station2: Station): Float {
            val calculatedX = abs(station1.coordinateX - station2.coordinateX)
            val calculatedY = abs(station1.coordinateY - station2.coordinateY)
            return calculatedX + calculatedY
        }

    }
}