package com.example.eggventure.model.creature

import androidx.room.TypeConverter

class RarityConverter {
    @TypeConverter
    fun fromRarity(rarity: Rarity): String = rarity.name

    @TypeConverter
    fun toRarity(value: String): Rarity = Rarity.valueOf(value)
}
