package com.example.eggventure.model.creature

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "creatures")
@TypeConverters(RarityConverter::class, CreatureTypeConverter::class)
data class CreatureEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creatureName: String,
    val type: CreatureType,
    val imageResId: Int,
    val rarity: Rarity,
    val hatchedAt: Long = System.currentTimeMillis(),
)
