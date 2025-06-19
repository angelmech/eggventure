package com.example.eggventure.model.hatchprogress

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing the egg progress in the database.
 * !!! Reset after egg is hatched
 */
@Entity(tableName = "hatch_progress")
data class HatchProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val hatchProgressSteps: Int, // steps accumulated till hatch
    val hatchGoal: Int = 5000, // default hatch goal, can be changed in the future
)
