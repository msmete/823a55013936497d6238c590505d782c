package com.ms.spacecraft.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spacecraft")
data class SpaceCraft(
    @PrimaryKey var id: Int,
    var name: String,
    var UGS: Int,
    var EUS: Int,
    var DS: Int,
    var damageCapacity: Int,
    var damageCounter: Int,
    var currentStation: String,
    var currentX: Float,
    var currentY: Float
)
