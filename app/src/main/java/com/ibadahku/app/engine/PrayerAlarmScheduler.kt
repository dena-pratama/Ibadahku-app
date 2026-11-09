package com.ibadahku.app.engine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ibadahku.app.data.model.PrayerSettings
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.domain.PrayerCalculator
import com.ibadahku.app.receiver.PrayerAlarmReceiver
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class PrayerAlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /**
     * Schedules prayer alarms for the next 48 hours according to PRD 8.2
     */
    fun scheduleAlarms(settings: PrayerSettings) {
        if (!settings.prayerNotificationEnabled) return

        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        val scheduleToday = PrayerCalculator.calculateSchedule(today, settings)
        val scheduleTomorrow = PrayerCalculator.calculateSchedule(tomorrow, settings)

        val allItems = scheduleToday.items.map { today to it } + scheduleTomorrow.items.map { tomorrow to it }

        val now = LocalDateTime.now()

        allItems.forEach { (date, item) ->
            if (item.type != PrayerType.IMSAK && item.type != PrayerType.TERBIT) {
                val prayerDateTime = LocalDateTime.of(date, item.time)
                if (prayerDateTime.isAfter(now)) {
                    scheduleSingleAlarm(item.type, prayerDateTime, settings.superAndalAlarm)
                }

                if (settings.prePrayerReminderEnabled && settings.prePrayerMinutes > 0) {
                    val preDateTime = prayerDateTime.minusMinutes(settings.prePrayerMinutes.toLong())
                    if (preDateTime.isAfter(now)) {
                        schedulePrePrayerAlarm(item.type, preDateTime, settings.prePrayerMinutes)
                    }
                }
            }
        }
    }

    private fun schedulePrePrayerAlarm(type: PrayerType, dateTime: LocalDateTime, minutesBefore: Int) {
        val triggerEpochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_PRE_PRAYER_ALARM
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_NAME, type.displayName)
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TYPE, type.name)
            putExtra(PrayerAlarmReceiver.EXTRA_MINUTES_BEFORE, minutesBefore)
        }

        val requestCode = 5000 + type.ordinal * 100 + dateTime.dayOfYear
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerEpochMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    triggerEpochMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun scheduleSingleAlarm(type: PrayerType, dateTime: LocalDateTime, superAndal: Boolean) {
        val triggerEpochMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_NAME, type.displayName)
            putExtra(PrayerAlarmReceiver.EXTRA_PRAYER_TYPE, type.name)
        }

        val requestCode = type.ordinal * 100 + dateTime.dayOfYear
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (superAndal && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val showIntent = Intent(context, com.ibadahku.app.MainActivity::class.java)
                    val showPendingIntent = PendingIntent.getActivity(
                        context,
                        requestCode,
                        showIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(triggerEpochMillis, showPendingIntent),
                        pendingIntent
                    )
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerEpochMillis,
                        pendingIntent
                    )
                }
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    triggerEpochMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Exact alarm permission not granted yet
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                triggerEpochMillis,
                pendingIntent
            )
        }
    }

    fun cancelAllAlarms() {
        PrayerType.values().forEach { type ->
            val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
                action = PrayerAlarmReceiver.ACTION_PRAYER_ALARM
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                type.ordinal,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }
}
