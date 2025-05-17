package com.example.eggventure.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val steps: Int, // alle Schritte des Laufs
    val duration: Long, // Dauer in ms, später in hh:mm:ss umwandeln
    val date: Long = System.currentTimeMillis(),
    val averageSpeed: Float?,  // km/h
    val distanceMeters: Float?, // geschätzte Distanz in km
)
