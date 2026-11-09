package com.ibadahku.app.data.model

import java.time.LocalDate
import java.time.LocalTime

enum class PrayerType(val displayName: String, val arabicName: String) {
    IMSAK("Imsak", "الإمساك"),
    SUBUH("Subuh", "الفجر"),
    TERBIT("Terbit", "الشروق"),
    DZUHUR("Dzuhur", "الظهر"),
    ASHAR("Ashar", "العصر"),
    MAGHRIB("Maghrib", "المغرب"),
    ISYA("Isya", "العشاء")
}

enum class PrayerStatus {
    DONE,       // Sudah
    MISSED,     // Terlewat
    PENDING     // Belum
}

enum class ReminderMode(val label: String) {
    ADZAN_FULL("Adzan Penuh"),
    SHORT_NOTIF("Notifikasi Singkat"),
    VIBRATE_ONLY("Getar Saja"),
    SILENT("Senyap"),
    DISABLED("Nonaktif")
}

data class PrayerScheduleItem(
    val type: PrayerType,
    val time: LocalTime,
    val timeFormatted: String, // e.g. "04:32"
    val isCurrent: Boolean = false,
    val isNext: Boolean = false,
    val status: PrayerStatus = PrayerStatus.PENDING,
    val reminderMode: ReminderMode = ReminderMode.ADZAN_FULL
)

data class DayPrayerSchedule(
    val date: LocalDate,
    val dateHijriFormatted: String, // e.g. "17 Jumadil Awal 1448 H"
    val dateGregorianFormatted: String, // e.g. "Rabu, 8 Nov 2026"
    val city: String,
    val items: List<PrayerScheduleItem>,
    val nextPrayer: PrayerScheduleItem?,
    val remainingTimeFormatted: String // e.g. "1j 12m lagi"
)
