package com.ibadahku.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.ibadahku.app.R
import com.ibadahku.app.data.repository.AppPreferencesRepository
import com.ibadahku.app.engine.AudioPlayerManager
import com.ibadahku.app.receiver.PrayerAlarmReceiver

class PrayerAlarmService : Service() {

    private lateinit var audioPlayerManager: AudioPlayerManager

    override fun onCreate() {
        super.onCreate()
        audioPlayerManager = AudioPlayerManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val prayerName = intent?.getStringExtra(PrayerAlarmReceiver.EXTRA_PRAYER_NAME) ?: "Sholat"

        startAsForeground(prayerName)

        val prefs = AppPreferencesRepository(this)
        val settings = prefs.loadSettings()

        audioPlayerManager.playAdzanFromSettings(
            settings = settings,
            prayerName = prayerName,
            volumePercent = settings.volumePercent,
            fadeInSeconds = settings.fadeInSeconds
        ) {
            stopSelf()
        }

        return START_NOT_STICKY
    }

    private fun startAsForeground(prayerName: String) {
        val channelId = "channel_prayer_service"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pemutaran Adzan",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Adzan $prayerName Berkumandang")
            .setContentText("Memutar suara adzan pengingat sholat...")
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(1002, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(1002, notification)
        }
    }

    override fun onDestroy() {
        audioPlayerManager.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
