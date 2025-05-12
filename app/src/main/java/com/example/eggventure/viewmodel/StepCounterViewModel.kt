package com.example.eggventure.viewmodel
/*
import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.eggventure.viewmodel.StepCounterService
import com.example.eggventure.viewmodel.StepCounterServiceBinder

@HiltViewModel
class PersistentStepCounterViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val _stepCount = MutableLiveData<Int>(0)
    val stepCount: LiveData<Int> = _stepCount
    private var stepCounterService: StepCounterService? = null
    private var isServiceBound = false

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StepCounterServiceBinder
            stepCounterService = binder.getService()
            isServiceBound = true
            stepCounterService?.steps?.observeForever { steps ->
                _stepCount.value = steps
            }
        }

        override fun onServiceDisconnected(className: ComponentName) {
            stepCounterService = null
            isServiceBound = false
        }
    }

    fun startStepCounterService() {
        val serviceIntent = Intent(getApplication(), StepCounterService::class.java)
        getApplication<Application>().startForegroundService(serviceIntent)
        Intent(getApplication(), StepCounterService::class.java).also { intent ->
            getApplication<Application>().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    fun stopStepCounterService() {
        if (isServiceBound) {
            getApplication<Application>().unbindService(serviceConnection)
            isServiceBound = false
        }
        val serviceIntent = Intent(getApplication(), StepCounterService::class.java)
        getApplication<Application>().stopService(serviceIntent)
    }

    override fun onCleared() {
        super.onCleared()
        stopStepCounterService()
    }
}*/