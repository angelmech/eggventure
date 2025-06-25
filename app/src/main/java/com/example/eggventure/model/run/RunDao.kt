package com.example.eggventure.model.run

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Insert
    suspend fun insertRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY date DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    // get the last run
    @Query("SELECT * FROM runs ORDER BY date DESC LIMIT 1")
    suspend fun getLastRun(): RunEntity?
}
