package com.example.eggventure.model.creature

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "creatures")
@TypeConverters(RarityConverter::class)
data class CreatureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creatureName: String,
    val rarity: Rarity,
    val imageResId: Int,
    val hatchedAt: Long = System.currentTimeMillis(),
)
