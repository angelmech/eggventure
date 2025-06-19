package com.example.eggventure.model.run

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class RunEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val steps: Int, // Schritte des Laufs, Gesamtschritte werden gezählt indem man alle Läufe addiert
    val duration: Long, // Dauer in ms, später in hh:mm:ss umwandeln
    val date: Long,
    val averageSpeed: Float?,  // km/h
    val distanceMeters: Float?, // geschätzte Distanz in km
)
