package com.example.eggventure.viewmodel.stats

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eggventure.utils.DependencyProvider

/**
 * Factory class for creating instances of [StatsViewModel].
 *
 * This factory is responsible for providing the necessary dependencies to the StatsViewModel.
 *
 * @property context The application context used to access resources and services.
 */
class StatsFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DependencyProvider.provideStats(context) as T
    }
}
