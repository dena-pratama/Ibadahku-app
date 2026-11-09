package com.ibadahku.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ibadahku.app.data.repository.AppPreferencesRepository
import com.ibadahku.app.engine.PrayerAlarmScheduler
import com.ibadahku.app.receiver.PrayerAlarmReceiver

class IbadahkuApp : Application() {

    lateinit var prefsRepository: AppPreferencesRepository
    lateinit var alarmScheduler: PrayerAlarmScheduler

    override fun onCreate() {
        super.onCreate()
        prefsRepository = AppPreferencesRepository(this)
        alarmScheduler = PrayerAlarmScheduler(this)

        createNotificationChannels()

        // Schedule initial 48-hour prayer alarms
        val settings = prefsRepository.loadSettings()
        if (settings.prayerNotificationEnabled) {
            alarmScheduler.scheduleAlarms(settings)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val adzanChannel = NotificationChannel(
                PrayerAlarmReceiver.CHANNEL_ID,
                "Adzan & Pengingat Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi adzan dan pengingat waktu sholat 5 waktu"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500)
            }

            notificationManager.createNotificationChannel(adzanChannel)
        }
    }
}
