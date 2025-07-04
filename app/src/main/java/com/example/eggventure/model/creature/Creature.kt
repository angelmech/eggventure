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
        Creature(1, "Chromastone", CreatureType.RADIANT, R.drawable.radiant1),
        Creature(2, "Heatblast", CreatureType.RADIANT, R.drawable.radiant2),
        Creature(3, "Feedback", CreatureType.RADIANT, R.drawable.radiant3),
        Creature(4, "XLR8", CreatureType.SHADOW, R.drawable.sh4),
        Creature(5, "Upgrade", CreatureType.SHADOW, R.drawable.sh3),
        Creature(6, "Allen X", CreatureType.SHADOW, R.drawable.sh2),
        Creature(7, "Goop", CreatureType.SHADOW, R.drawable.sh1),
        Creature(8, "Diamondhead", CreatureType.REGULAR, R.drawable.r1),
        Creature(9, "Swampfire", CreatureType.REGULAR, R.drawable.r2),
        Creature(10, "Lodestar", CreatureType.REGULAR, R.drawable.r3),

    )

    override fun getAllCreatures() = allCreatures

    override fun getById(id: Int) = allCreatures.find { it.id == id }

    override fun getByName(name: String): Creature? {
        return allCreatures.find { it.name.equals(name, ignoreCase = true) }
    }
}
