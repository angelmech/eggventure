package com.example.eggventure.model.creature

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "creatures")
data class CreatureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creatureName: String,
    val rarity: String,
    val hatchedAt: Long = System.currentTimeMillis(),
)
