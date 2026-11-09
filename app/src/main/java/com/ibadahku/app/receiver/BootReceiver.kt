package com.ibadahku.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ibadahku.app.data.repository.AppPreferencesRepository
import com.ibadahku.app.engine.PrayerAlarmScheduler

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_TIMEZONE_CHANGED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_MY_PACKAGE_REPLACED
        ) {
            val repo = AppPreferencesRepository(context)
            val settings = repo.loadSettings()
            val scheduler = PrayerAlarmScheduler(context)
            scheduler.scheduleAlarms(settings)
        }
    }
}
