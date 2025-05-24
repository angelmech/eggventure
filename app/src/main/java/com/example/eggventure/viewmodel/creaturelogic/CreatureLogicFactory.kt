package com.example.eggventure.viewmodel.creaturelogic

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.AppDatabase
import com.example.eggventure.model.creature.CreatureDataInterface
import com.example.eggventure.model.creature.CreatureDatabase
import com.example.eggventure.model.creature.CreatureRepository
import com.example.eggventure.model.creature.CreatureRepositoryImpl
import com.example.eggventure.model.hatchprogress.HatchProgressRepository
import com.example.eggventure.model.hatchprogress.HatchProgressRepositoryImpl
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.model.run.RunRepositoryImpl


class CreatureLogicFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val db = AppDatabase.getDatabase(context)
        val creatureSource: CreatureDataInterface = CreatureDatabase
        val creatureRepository : CreatureRepository =  CreatureRepositoryImpl(db.creatureDao())
        val hatchProgressRepository : HatchProgressRepository = HatchProgressRepositoryImpl(db.hatchProgressDao())

        val hatchEvent = EggHatchEvent(hatchProgressRepository, creatureRepository)


        @Suppress("UNCHECKED_CAST")
        return CreatureLogic(creatureSource, hatchEvent) as T
    }
}
