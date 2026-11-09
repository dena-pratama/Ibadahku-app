package com.ibadahku.app.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object PrayerSchedule : Screen("prayer_schedule")
    object Tadarus : Screen("tadarus")
    object QuranReader : Screen("quran_reader/{surahNumber}") {
        fun createRoute(surahNumber: Int) = "quran_reader/$surahNumber"
    }
    object Todo : Screen("todo")
    object Tasbih : Screen("tasbih/{taskId}") {
        fun createRoute(taskId: String) = "tasbih/$taskId"
    }
    object Qibla : Screen("qibla")
    object Settings : Screen("settings")
    object ReliabilityCheck : Screen("reliability_check")
    object QadhaTracker : Screen("qadha_tracker")
}
