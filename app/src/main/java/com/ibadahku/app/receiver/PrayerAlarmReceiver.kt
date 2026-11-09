package com.ibadahku.app.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ibadahku.app.MainActivity
import com.ibadahku.app.data.model.PrayerStatus
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.data.repository.AppPreferencesRepository
import com.ibadahku.app.service.PrayerAlarmService
import java.time.LocalDate

class PrayerAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_PRAYER_ALARM = "com.ibadahku.app.ACTION_PRAYER_ALARM"
        const val ACTION_PRE_PRAYER_ALARM = "com.ibadahku.app.ACTION_PRE_PRAYER_ALARM"
        const val ACTION_DISMISS_ALARM = "com.ibadahku.app.ACTION_DISMISS_ALARM"
        const val ACTION_MARK_PRAYED = "com.ibadahku.app.ACTION_MARK_PRAYED"
        const val ACTION_SNOOZE_ALARM = "com.ibadahku.app.ACTION_SNOOZE_ALARM"

        const val EXTRA_PRAYER_NAME = "extra_prayer_name"
        const val EXTRA_PRAYER_TYPE = "extra_prayer_type"
        const val EXTRA_MINUTES_BEFORE = "extra_minutes_before"

        const val CHANNEL_ID = "channel_adzan_prayer"
        const val CHANNEL_PRE_PRAYER_ID = "channel_pre_prayer"
        const val NOTIFICATION_ID = 1001
        const val NOTIFICATION_PRE_ID = 1002
    }

    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra(EXTRA_PRAYER_NAME) ?: "Sholat"
        val prayerTypeName = intent.getStringExtra(EXTRA_PRAYER_TYPE)

        when (intent.action) {
            ACTION_PRE_PRAYER_ALARM -> {
                val minutesBefore = intent.getIntExtra(EXTRA_MINUTES_BEFORE, 15)
                showPrePrayerNotification(context, prayerName, minutesBefore)
            }
            ACTION_PRAYER_ALARM -> {
                showPrayerNotification(context, prayerName, prayerTypeName)
                // Start Foreground Service to play adzan
                val serviceIntent = Intent(context, PrayerAlarmService::class.java).apply {
                    putExtra(EXTRA_PRAYER_NAME, prayerName)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
            ACTION_MARK_PRAYED -> {
                stopAlarmAndService(context)
                prayerTypeName?.let {
                    try {
                        val type = PrayerType.valueOf(it)
                        val repo = AppPreferencesRepository(context)
                        repo.setPrayerStatus(LocalDate.now(), type, PrayerStatus.DONE)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            ACTION_SNOOZE_ALARM -> {
                stopAlarmAndService(context)
                // Schedule reminder in 10 minutes
            }
            ACTION_DISMISS_ALARM -> {
                stopAlarmAndService(context)
            }
        }
    }

    private fun stopAlarmAndService(context: Context) {
        val serviceIntent = Intent(context, PrayerAlarmService::class.java)
        context.stopService(serviceIntent)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun showPrayerNotification(context: Context, prayerName: String, prayerTypeName: String?) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Adzan & Pengingat Sholat",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi suara adzan tepat waktu masuk sholat"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 200, 500)
                setSound(
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 1: Sudah Sholat
        val markPrayedIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_MARK_PRAYED
            putExtra(EXTRA_PRAYER_TYPE, prayerTypeName)
        }
        val markPrayedPendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            markPrayedIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 2: Tunda 10 Menit
        val snoozeIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_SNOOZE_ALARM
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 3: Hentikan Suara
        val dismissIntent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = ACTION_DISMISS_ALARM
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            3,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Waktu Sholat $prayerName Telah Tiba")
            .setContentText("Mari tunaikan sholat $prayerName tepat waktu.")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .addAction(0, "Sudah Sholat", markPrayedPendingIntent)
            .addAction(0, "Tunda 10m", snoozePendingIntent)
            .addAction(0, "Hentikan", dismissPendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun showPrePrayerNotification(context: Context, prayerName: String, minutesBefore: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_PRE_PRAYER_ID,
                "Pengingat Pra-Waktu Sholat",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Pengingat persiapan wudhu sebelum adzan tiba"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            10,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_PRE_PRAYER_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("Persiapan Menuju Waktu $prayerName")
            .setContentText("$minutesBefore menit lagi adzan $prayerName berkumandang. Mari bersiap mengambil wudhu ✨")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(openAppPendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_PRE_ID, notification)
    }
}
