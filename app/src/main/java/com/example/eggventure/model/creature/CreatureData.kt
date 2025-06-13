package com.example.eggventure.model.creature

import androidx.annotation.DrawableRes
import com.example.eggventure.R

data class Creature(
    val id: Int,
    val name: String,
    val rarity: Rarity,
    @DrawableRes val imageResId: Int
)

object CreatureDatabase : CreatureDataInterface {
    private val allCreatures = listOf(
        Creature(1, "Echoes", Rarity.COMMON, R.drawable.creature_stand1),
        Creature(2, "D4C", Rarity.EPIC, R.drawable.creature_stand2),
        Creature(3, "SCR", Rarity.MYTHICAL, R.drawable.creature_stand3),
        Creature(4, "Killer Queen", Rarity.RARE, R.drawable.creature_stand4),
        Creature(5, "Star Platinum", Rarity.EPIC, R.drawable.creature_stand5),
        Creature(6, "GER", Rarity.LEGENDARY, R.drawable.creature_stand6),
        Creature(7, "Weather Report", Rarity.EPIC, R.drawable.creature_stand7),
        Creature(8, "Man in the mirror", Rarity.COMMON, R.drawable.creature_stand8),
        Creature(9, "Chariot Baby", Rarity.MYTHICAL, R.drawable.creature_stand9),
        Creature(10, "Heavens Door", Rarity.RARE, R.drawable.creature_stand10),
    )

    override fun getAllCreatures() = allCreatures

    override fun getById(id: Int) = allCreatures.find { it.id == id }

    override fun getByName(name: String): Creature? {
        return allCreatures.find { it.name.equals(name, ignoreCase = true) }
    }
}
