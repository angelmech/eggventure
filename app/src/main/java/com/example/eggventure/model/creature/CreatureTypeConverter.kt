package com.example.eggventure.model.creature

import androidx.room.TypeConverter

class CreatureTypeConverter {
    @TypeConverter
    fun fromType(type: CreatureType): String = type.name

    @TypeConverter
    fun toType(value: String): CreatureType = CreatureType.valueOf(value)
}
