package com.example.eggventure.model.creature

import androidx.annotation.DrawableRes
import com.example.eggventure.R

data class Creature(
    val id: Int,
    val name: String,
    val type: CreatureType,
    @DrawableRes val imageResId: Int,
    var rarity: Rarity = Rarity.COMMON, // Default placeholder
)

object CreatureDatabase : CreatureDataInterface {
    private val allCreatures = listOf(
        Creature(1, "Echoes", CreatureType.REGULAR, R.drawable.creature_stand1),
        Creature(2, "D4C", CreatureType.SHADOW, R.drawable.creature_stand2),
        Creature(3, "SCR", CreatureType.SHADOW, R.drawable.creature_stand3),
        Creature(4, "Killer Queen", CreatureType.RADIANT, R.drawable.creature_stand4),
        Creature(5, "Star Platinum", CreatureType.RADIANT, R.drawable.creature_stand5),
        Creature(6, "GER", CreatureType.REGULAR, R.drawable.creature_stand6),
        Creature(7, "Weather Report", CreatureType.RADIANT, R.drawable.creature_stand7),
        Creature(8, "The World", CreatureType.REGULAR, R.drawable.creature_stand8),
        Creature(9, "Chariot Baby", CreatureType.SHADOW, R.drawable.creature_stand9),
        Creature(10, "Heavens Door", CreatureType.RADIANT, R.drawable.creature_stand10),
    )

    override fun getAllCreatures() = allCreatures

    override fun getById(id: Int) = allCreatures.find { it.id == id }

    override fun getByName(name: String): Creature? {
        return allCreatures.find { it.name.equals(name, ignoreCase = true) }
    }
}
