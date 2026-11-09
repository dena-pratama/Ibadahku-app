package com.ibadahku.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ibadahku.app.data.model.AsrMadhab
import com.ibadahku.app.data.model.CalculationMethod
import com.ibadahku.app.data.model.PrayerSettings
import com.ibadahku.app.data.model.PrayerStatus
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.data.model.ReminderMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class AppPreferencesRepository(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ibadahku_prefs", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<PrayerSettings> = _settingsFlow.asStateFlow()

    private val fardhuPrayers = listOf(PrayerType.SUBUH, PrayerType.DZUHUR, PrayerType.ASHAR, PrayerType.MAGHRIB, PrayerType.ISYA)

    private val _qadhaFlow = MutableStateFlow(loadAllQadha())
    val qadhaFlow: StateFlow<Map<PrayerType, Int>> = _qadhaFlow.asStateFlow()

    private fun loadAllQadha(): Map<PrayerType, Int> {
        return fardhuPrayers.associateWith { type ->
            prefs.getInt("qadha_${type.name}", 0)
        }
    }

    fun getQadhaCount(type: PrayerType): Int {
        return prefs.getInt("qadha_${type.name}", 0)
    }

    fun setQadhaCount(type: PrayerType, count: Int) {
        val safeCount = count.coerceAtLeast(0)
        prefs.edit().putInt("qadha_${type.name}", safeCount).apply()
        _qadhaFlow.value = loadAllQadha()
    }

    fun incrementQadha(type: PrayerType, delta: Int = 1): Int {
        val newCount = (getQadhaCount(type) + delta).coerceAtLeast(0)
        setQadhaCount(type, newCount)
        return newCount
    }

    fun decrementQadha(type: PrayerType): Int {
        val current = getQadhaCount(type)
        if (current <= 0) return 0
        val newCount = (current - 1).coerceAtLeast(0)
        setQadhaCount(type, newCount)
        return newCount
    }

    fun getTotalQadha(): Int {
        return fardhuPrayers.sumOf { getQadhaCount(it) }
    }

    fun loadSettings(): PrayerSettings {
        val methodName = prefs.getString("calc_method", CalculationMethod.KEMENAG.name)
        val method = try {
            CalculationMethod.valueOf(methodName ?: CalculationMethod.KEMENAG.name)
        } catch (e: Exception) {
            CalculationMethod.KEMENAG
        }

        val madhabName = prefs.getString("asr_madhab", AsrMadhab.SHAFI_JUMHUR.name)
        val madhab = try {
            AsrMadhab.valueOf(madhabName ?: AsrMadhab.SHAFI_JUMHUR.name)
        } catch (e: Exception) {
            AsrMadhab.SHAFI_JUMHUR
        }

        return PrayerSettings(
            userName = prefs.getString("user_name", "Fulan") ?: "Fulan",
            method = method,
            asrMadhab = madhab,
            cityName = prefs.getString("city_name", "Jakarta, Indonesia") ?: "Jakarta, Indonesia",
            latitude = prefs.getFloat("latitude", -6.2088f).toDouble(),
            longitude = prefs.getFloat("longitude", 106.8456f).toDouble(),
            timeZoneOffsetHours = prefs.getFloat("tz_offset", 7.0f).toDouble(),
            isAutoLocation = prefs.getBoolean("auto_location", true),
            superAndalAlarm = prefs.getBoolean("super_andal_alarm", true),
            prayerNotificationEnabled = prefs.getBoolean("prayer_notif_enabled", true),
            adzanSoundType = prefs.getString("adzan_sound_type", "PRESET") ?: "PRESET",
            adzanSoundId = prefs.getString("adzan_sound_id", "adzan_nabawi") ?: "adzan_nabawi",
            adzanSoundName = prefs.getString("adzan_sound_name", "Masjid Nabawi") ?: "Masjid Nabawi",
            adzanCustomUri = prefs.getString("adzan_custom_uri", "") ?: "",
            adzanCustomFileName = prefs.getString("adzan_custom_file_name", "") ?: "",
            silenceModeDelayMinutes = prefs.getInt("silence_delay", 30),
            volumePercent = prefs.getInt("alarm_volume", 100),
            fadeInSeconds = prefs.getInt("fade_in_sec", 3),
            hijriAdjustmentDays = prefs.getInt("hijri_adjust", 0),
            adjustSubuhMinutes = prefs.getInt("adj_subuh", 0),
            adjustDzuhurMinutes = prefs.getInt("adj_dzuhur", 0),
            adjustAsharMinutes = prefs.getInt("adj_ashar", 0),
            adjustMaghribMinutes = prefs.getInt("adj_maghrib", 0),
            adjustIsyaMinutes = prefs.getInt("adj_isya", 0),
            prePrayerReminderEnabled = prefs.getBoolean("pre_prayer_enabled", true),
            prePrayerMinutes = prefs.getInt("pre_prayer_min", 15)
        )
    }

    fun updateSettings(newSettings: PrayerSettings) {
        prefs.edit().apply {
            putString("user_name", newSettings.userName)
            putString("calc_method", newSettings.method.name)
            putString("asr_madhab", newSettings.asrMadhab.name)
            putString("city_name", newSettings.cityName)
            putFloat("latitude", newSettings.latitude.toFloat())
            putFloat("longitude", newSettings.longitude.toFloat())
            putFloat("tz_offset", newSettings.timeZoneOffsetHours.toFloat())
            putBoolean("auto_location", newSettings.isAutoLocation)
            putBoolean("super_andal_alarm", newSettings.superAndalAlarm)
            putBoolean("prayer_notif_enabled", newSettings.prayerNotificationEnabled)
            putString("adzan_sound_type", newSettings.adzanSoundType)
            putString("adzan_sound_id", newSettings.adzanSoundId)
            putString("adzan_sound_name", newSettings.adzanSoundName)
            putString("adzan_custom_uri", newSettings.adzanCustomUri)
            putString("adzan_custom_file_name", newSettings.adzanCustomFileName)
            putInt("silence_delay", newSettings.silenceModeDelayMinutes)
            putInt("alarm_volume", newSettings.volumePercent)
            putInt("fade_in_sec", newSettings.fadeInSeconds)
            putInt("hijri_adjust", newSettings.hijriAdjustmentDays)
            putInt("adj_subuh", newSettings.adjustSubuhMinutes)
            putInt("adj_dzuhur", newSettings.adjustDzuhurMinutes)
            putInt("adj_ashar", newSettings.adjustAsharMinutes)
            putInt("adj_maghrib", newSettings.adjustMaghribMinutes)
            putInt("adj_isya", newSettings.adjustIsyaMinutes)
            putBoolean("pre_prayer_enabled", newSettings.prePrayerReminderEnabled)
            putInt("pre_prayer_min", newSettings.prePrayerMinutes)
            apply()
        }
        _settingsFlow.value = newSettings
    }

    // Prayer Status per day
    fun getPrayerStatus(date: LocalDate, type: PrayerType): PrayerStatus {
        val key = "status_${date}_${type.name}"
        val value = prefs.getString(key, PrayerStatus.PENDING.name)
        return try {
            PrayerStatus.valueOf(value ?: PrayerStatus.PENDING.name)
        } catch (e: Exception) {
            PrayerStatus.PENDING
        }
    }

    fun setPrayerStatus(date: LocalDate, type: PrayerType, status: PrayerStatus) {
        val key = "status_${date}_${type.name}"
        prefs.edit().putString(key, status.name).apply()
    }

    // Reminder Mode per prayer
    fun getReminderMode(type: PrayerType): ReminderMode {
        val key = "reminder_mode_${type.name}"
        val value = prefs.getString(key, ReminderMode.ADZAN_FULL.name)
        return try {
            ReminderMode.valueOf(value ?: ReminderMode.ADZAN_FULL.name)
        } catch (e: Exception) {
            ReminderMode.ADZAN_FULL
        }
    }

    fun setReminderMode(type: PrayerType, mode: ReminderMode) {
        val key = "reminder_mode_${type.name}"
        prefs.edit().putString(key, mode.name).apply()
    }

    // App Theme (system, light, dark)
    private val _themeFlow = MutableStateFlow(getAppTheme())
    val themeFlow: StateFlow<String> = _themeFlow.asStateFlow()

    fun getAppTheme(): String = prefs.getString("app_theme", "system") ?: "system"

    fun setAppTheme(theme: String) {
        prefs.edit().putString("app_theme", theme).apply()
        _themeFlow.value = theme
    }

    // App Language (id, en)
    fun getAppLanguage(): String = prefs.getString("app_language", "id") ?: "id"
    fun setAppLanguage(lang: String) = prefs.edit().putString("app_language", lang).apply()

    // First time onboarding completed
    fun isOnboardingDone(): Boolean = prefs.getBoolean("onboarding_done", true)
    fun setOnboardingDone(done: Boolean) = prefs.edit().putBoolean("onboarding_done", done).apply()
}
