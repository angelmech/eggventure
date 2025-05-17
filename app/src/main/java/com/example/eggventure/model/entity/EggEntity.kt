package com.example.eggventure.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "eggs")
data class EggEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creatureName: String,
    val rarity: String,
    val hatchedAt: Long = System.currentTimeMillis(),
)
