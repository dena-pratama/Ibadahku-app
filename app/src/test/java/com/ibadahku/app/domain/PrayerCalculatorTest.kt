package com.ibadahku.app.domain

import com.ibadahku.app.data.model.AsrMadhab
import com.ibadahku.app.data.model.CalculationMethod
import com.ibadahku.app.data.model.PrayerSettings
import com.ibadahku.app.data.model.PrayerType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class PrayerCalculatorTest {

    @Test
    fun testJakartaScheduleGeneration() {
        val testDate = LocalDate.of(2026, 11, 8)
        val settings = PrayerSettings(
            method = CalculationMethod.KEMENAG,
            cityName = "Jakarta, Indonesia",
            latitude = -6.2088,
            longitude = 106.8456,
            timeZoneOffsetHours = 7.0
        )

        val schedule = PrayerCalculator.calculateSchedule(testDate, settings)

        assertEquals("Jakarta, Indonesia", schedule.city)
        assertEquals(5, schedule.items.size)

        val subuh = schedule.items.find { it.type == PrayerType.SUBUH }
        val dzuhur = schedule.items.find { it.type == PrayerType.DZUHUR }
        val ashar = schedule.items.find { it.type == PrayerType.ASHAR }
        val maghrib = schedule.items.find { it.type == PrayerType.MAGHRIB }
        val isya = schedule.items.find { it.type == PrayerType.ISYA }

        assertNotNull(subuh)
        assertNotNull(dzuhur)
        assertNotNull(ashar)
        assertNotNull(maghrib)
        assertNotNull(isya)

        // Subuh in Jakarta is typically ~04:10 - 04:45
        assertTrue("Subuh hour: ${subuh!!.time.hour}", subuh.time.hour == 4)
        // Dzuhur in Jakarta is typically ~11:40 - 12:05
        assertTrue("Dzuhur hour: ${dzuhur!!.time.hour}", dzuhur.time.hour in 11..12)
        // Ashar is typically ~14:50 - 15:30 depending on season
        assertTrue("Ashar hour: ${ashar!!.time.hour}", ashar.time.hour in 14..15)
        // Maghrib is typically ~17:40 - 18:15
        assertTrue("Maghrib hour: ${maghrib!!.time.hour}", maghrib.time.hour in 17..18)
        // Isya is typically ~18:55 - 19:30
        assertTrue("Isya hour: ${isya!!.time.hour}", isya.time.hour == 19)
    }

    @Test
    fun testHanafiAsrLaterThanShafii() {
        val testDate = LocalDate.of(2026, 11, 8)
        val shafiiSettings = PrayerSettings(asrMadhab = AsrMadhab.SHAFI_JUMHUR)
        val hanafiSettings = PrayerSettings(asrMadhab = AsrMadhab.HANAFI)

        val shafiiSchedule = PrayerCalculator.calculateSchedule(testDate, shafiiSettings)
        val hanafiSchedule = PrayerCalculator.calculateSchedule(testDate, hanafiSettings)

        val shafiiAshar = shafiiSchedule.items.find { it.type == PrayerType.ASHAR }!!.time
        val hanafiAshar = hanafiSchedule.items.find { it.type == PrayerType.ASHAR }!!.time

        assertTrue(
            "Hanafi ashar ($hanafiAshar) must be later than Shafi'i ashar ($shafiiAshar)",
            hanafiAshar.isAfter(shafiiAshar)
        )
    }

    @Test
    fun testHijriDateConversion() {
        val testDate = LocalDate.of(2026, 11, 8)
        val hijri = PrayerCalculator.convertToHijri(testDate, 0)
        assertTrue(hijri.contains("H"))
        assertTrue(hijri.isNotEmpty())
    }
}
