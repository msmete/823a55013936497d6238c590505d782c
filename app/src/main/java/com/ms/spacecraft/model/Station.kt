package com.ms.spacecraft.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stations")
data class Station(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("coordinateX")
    val coordinateX: Float,
    @SerializedName("coordinateY")
    val coordinateY: Float,
    @SerializedName("capacity")
    val capacity: Int,
    @SerializedName("stock")
    var stock: Int,
    @SerializedName("need")
    var need: Int,
    var isCompleted: Boolean = false,
    var isFavorite: Boolean = false,
)
