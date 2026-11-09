package com.ibadahku.app.data.model

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameLatin: String,
    val translationId: String,
    val totalAyat: Int,
    val startPage: Int,
    val revelationType: String // Makkiyyah / Madaniyyah
)

data class Ayah(
    val number: Int,
    val numberInSurah: Int,
    val textArabic: String,
    val textLatin: String,
    val translationId: String,
    val page: Int,
    val juz: Int
)

data class QuranBookmark(
    val id: String,
    val surahNumber: Int,
    val surahName: String,
    val ayahNumber: Int,
    val pageNumber: Int,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class TadarusProgress(
    val pagesReadToday: Int = 5,
    val targetPagesPerDay: Int = 10,
    val totalPagesRead: Int = 5,
    val totalMushafPages: Int = 30, // or 604
    val lastSurahName: String = "Al-Baqarah",
    val lastPage: Int = 5,
    val lastAyah: Int = 25,
    val streakDays: Int = 7,
    val targetDaysTotal: Int = 30
)

data class KhatamPlan(
    val id: String,
    val targetDays: Int, // 30, 60, 90, 180, 365
    val startDateTimestamp: Long,
    val dailyPages: Double,
    val remainingPages: Int,
    val estimatedFinishDate: String
)
