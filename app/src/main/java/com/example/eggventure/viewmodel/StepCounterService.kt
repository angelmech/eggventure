package com.example.eggventure.viewmodel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eggventure.MainActivity // Replace with your MainActivity path
import com.example.eggventure.viewmodel.StepCounterInitializer

class StepCounterService : Service(), android.hardware.SensorEventListener {

    private val _steps = MutableLiveData<Int>(0)
    val steps: LiveData<Int> = _steps
    private var initialStepCount: Int? = null
    private val channelId = "StepCounterServiceChannel"
    private val notificationId = 1
    private val tag = "StepCounterService"
    private lateinit var sensorInitializer: StepCounterInitializer

    override fun onBind(intent: Intent?): IBinder? {
        return StepCounterServiceBinder(this)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        sensorInitializer = StepCounterInitializer(this, this)
        sensorInitializer.initialize()
        Log.i(tag, "Service created and sensor initialized")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(tag, "Service started")
        startForegroundNotification()
        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Step Counter Service"
            val descriptionText = "Keeps track of your steps in the background"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startForegroundNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            // Add flags if needed
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_directions) // Replace with your app icon
            .setContentTitle("Step Counter Active")
            .setContentText("Tracking your steps")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(notificationId, notification)
    }

    override fun onSensorChanged(event: android.hardware.SensorEvent?) {
        if (event?.sensor?.type == android.hardware.Sensor.TYPE_STEP_COUNTER) {
            val currentStepCount = event.values[0].toInt()
            if (initialStepCount == null) {
                initialStepCount = currentStepCount
            }
            _steps.postValue(currentStepCount - (initialStepCount ?: 0))
        }
    }

    override fun onAccuracyChanged(sensor: android.hardware.Sensor?, accuracy: Int) {
        Log.i(tag, "Accuracy changed in Service: $accuracy")
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorInitializer.unregister()
        Log.i(tag, "Service destroyed, listener unregistered")
    }
}

class StepCounterServiceBinder(private val service: StepCounterService) : android.os.Binder() {
    fun getService(): StepCounterService = service
}