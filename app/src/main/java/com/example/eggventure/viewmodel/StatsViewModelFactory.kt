package com.example.eggventure.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.model.run.RunRepository
import com.example.eggventure.viewmodel.stepcounter.StepCounter

class StatsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependencyProvider.provideStats(context) as T
    }
}
