package com.ibadahku.app.data.model

enum class CalculationMethod(val title: String, val fajrAngle: Double, val ishaAngle: Double, val ihtiyathMinutes: Long) {
    KEMENAG("Kemenag RI (Standard Indonesia)", 20.0, 18.0, 2),
    MWL("Muslim World League", 18.0, 17.0, 0),
    UMM_AL_QURA("Umm al-Qura (Makkah)", 18.5, 90.0, 0), // 90 min after maghrib
    EGYPTIAN("Egyptian General Authority", 19.5, 17.5, 0),
    KARACHI("University of Islamic Sciences, Karachi", 18.0, 18.0, 0),
    ISNA("Islamic Society of North America", 15.0, 15.0, 0)
}

enum class AsrMadhab(val title: String) {
    SHAFI_JUMHUR("Syafi'i / Maliki / Hanbali"),
    HANAFI("Hanafi")
}

data class PrayerSettings(
    val userName: String = "Fulan",
    val method: CalculationMethod = CalculationMethod.KEMENAG,
    val asrMadhab: AsrMadhab = AsrMadhab.SHAFI_JUMHUR,
    val cityName: String = "Jakarta, Indonesia",
    val latitude: Double = -6.2088,
    val longitude: Double = 106.8456,
    val timeZoneOffsetHours: Double = 7.0,
    val isAutoLocation: Boolean = true,
    val superAndalAlarm: Boolean = true, // setAlarmClock
    val prayerNotificationEnabled: Boolean = true,
    val adzanSoundType: String = "PRESET", // "PRESET" or "CUSTOM_FILE"
    val adzanSoundId: String = "adzan_nabawi",
    val adzanSoundName: String = "Masjid Nabawi",
    val adzanCustomUri: String = "",
    val adzanCustomFileName: String = "",
    val silenceModeDelayMinutes: Int = 30,
    val volumePercent: Int = 100,
    val fadeInSeconds: Int = 3,
    val hijriAdjustmentDays: Int = 0, // ±2 days
    val adjustSubuhMinutes: Int = 0,
    val adjustDzuhurMinutes: Int = 0,
    val adjustAsharMinutes: Int = 0,
    val adjustMaghribMinutes: Int = 0,
    val adjustIsyaMinutes: Int = 0,
    val prePrayerReminderEnabled: Boolean = true,
    val prePrayerMinutes: Int = 15
)
