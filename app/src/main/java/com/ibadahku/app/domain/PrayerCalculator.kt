package com.ibadahku.app.domain

import com.ibadahku.app.data.model.AsrMadhab
import com.ibadahku.app.data.model.CalculationMethod
import com.ibadahku.app.data.model.DayPrayerSchedule
import com.ibadahku.app.data.model.PrayerScheduleItem
import com.ibadahku.app.data.model.PrayerSettings
import com.ibadahku.app.data.model.PrayerStatus
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.data.model.ReminderMode
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

object PrayerCalculator {

    private fun d2r(d: Double): Double = Math.toRadians(d)
    private fun r2d(r: Double): Double = Math.toDegrees(r)

    private fun julianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = Math.floor(y / 100.0)
        val b = 2 - a + Math.floor(a / 4.0)
        return Math.floor(365.25 * (y + 4716)) + Math.floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private data class SunPosition(val declination: Double, val equationOfTime: Double)

    private fun sunPosition(jd: Double): SunPosition {
        val d = jd - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(d2r(g)) + 0.020 * sin(d2r(2 * g)))

        val e = 23.439 - 0.00000036 * d
        val ra = r2d(Math.atan2(cos(d2r(e)) * sin(d2r(l)), cos(d2r(l)))) / 15.0
        val fixedRa = fixHour(ra)

        val declination = r2d(asin(sin(d2r(e)) * sin(d2r(l))))
        val equationOfTime = (q / 15.0) - fixedRa
        return SunPosition(declination, equationOfTime)
    }

    private fun fixAngle(a: Double): Double {
        var angle = a - 360.0 * Math.floor(a / 360.0)
        if (angle < 0) angle += 360.0
        return angle
    }

    private fun fixHour(h: Double): Double {
        var hour = h - 24.0 * Math.floor(h / 24.0)
        if (hour < 0) hour += 24.0
        return hour
    }

    private fun hourToLocalTime(decimalHours: Double): LocalTime {
        var hours = decimalHours
        while (hours < 0) hours += 24.0
        while (hours >= 24) hours -= 24.0
        val h = hours.toInt()
        val m = ((hours - h) * 60).toInt()
        val s = ((((hours - h) * 60) - m) * 60).toInt()
        return LocalTime.of(
            h.coerceIn(0, 23),
            m.coerceIn(0, 59),
            s.coerceIn(0, 59)
        )
    }

    /**
     * Accurate prayer times calculation for a given date, coordinates, and settings
     */
    fun calculateSchedule(
        date: LocalDate,
        settings: PrayerSettings,
        prayerStatuses: Map<PrayerType, PrayerStatus> = emptyMap(),
        reminderModes: Map<PrayerType, ReminderMode> = emptyMap(),
        currentTime: LocalTime = LocalTime.now()
    ): DayPrayerSchedule {
        val jd = julianDay(date.year, date.monthValue, date.dayOfMonth)
        val sun = sunPosition(jd)
        val lat = settings.latitude
        val lng = settings.longitude
        val tz = settings.timeZoneOffsetHours

        // Solar Noon (Dzuhur base)
        val noon = fixHour(12.0 + tz - (lng / 15.0) - sun.equationOfTime)

        // Sunrise & Sunset angle (sun center is -0.833 degrees below horizon)
        val sunriseAngle = 0.833
        val cosSunrise = (-sin(d2r(sunriseAngle)) - sin(d2r(lat)) * sin(d2r(sun.declination))) /
                (cos(d2r(lat)) * cos(d2r(sun.declination)))
        val sunriseDiff = if (cosSunrise in -1.0..1.0) r2d(acos(cosSunrise)) / 15.0 else 6.0

        val sunriseDecimal = noon - sunriseDiff
        val sunsetDecimal = noon + sunriseDiff

        // Fajr (Subuh)
        val fajrAngle = settings.method.fajrAngle
        val cosFajr = (-sin(d2r(fajrAngle)) - sin(d2r(lat)) * sin(d2r(sun.declination))) /
                (cos(d2r(lat)) * cos(d2r(sun.declination)))
        val fajrDiff = if (cosFajr in -1.0..1.0) r2d(acos(cosFajr)) / 15.0 else 6.0
        val fajrDecimal = noon - fajrDiff

        // Isha (Isya)
        val ishaDecimal = if (settings.method == CalculationMethod.UMM_AL_QURA) {
            sunsetDecimal + 1.5 // 90 minutes after sunset
        } else {
            val ishaAngle = settings.method.ishaAngle
            val cosIsha = (-sin(d2r(ishaAngle)) - sin(d2r(lat)) * sin(d2r(sun.declination))) /
                    (cos(d2r(lat)) * cos(d2r(sun.declination)))
            val ishaDiff = if (cosIsha in -1.0..1.0) r2d(acos(cosIsha)) / 15.0 else 6.0
            noon + ishaDiff
        }

        // Asr (shadow ratio = 1 for Shafi'i/Jumhur, 2 for Hanafi)
        val shadowFactor = if (settings.asrMadhab == AsrMadhab.HANAFI) 2.0 else 1.0
        val asrAltitude = r2d(atan(1.0 / (shadowFactor + tan(d2r(abs(lat - sun.declination))))))
        val cosAsr = (sin(d2r(asrAltitude)) - sin(d2r(lat)) * sin(d2r(sun.declination))) /
                (cos(d2r(lat)) * cos(d2r(sun.declination)))
        val asrDiff = if (cosAsr in -1.0..1.0) r2d(acos(cosAsr)) / 15.0 else 3.5
        val asrDecimal = noon + asrDiff

        // Add standard Ihtiyath (safety minutes, default +2 min for Kemenag)
        val ihtiyathMinutes = settings.method.ihtiyathMinutes
        val ihtiyathHours = ihtiyathMinutes / 60.0

        var subuhTime = hourToLocalTime(fajrDecimal + ihtiyathHours).plusMinutes(settings.adjustSubuhMinutes.toLong())
        val terbitTime = hourToLocalTime(sunriseDecimal)
        val imsakTime = subuhTime.minusMinutes(10)
        var dzuhurTime = hourToLocalTime(noon + ihtiyathHours).plusMinutes(settings.adjustDzuhurMinutes.toLong())
        var asharTime = hourToLocalTime(asrDecimal + ihtiyathHours).plusMinutes(settings.adjustAsharMinutes.toLong())
        var maghribTime = hourToLocalTime(sunsetDecimal + ihtiyathHours).plusMinutes(settings.adjustMaghribMinutes.toLong())
        var isyaTime = hourToLocalTime(ishaDecimal + ihtiyathHours).plusMinutes(settings.adjustIsyaMinutes.toLong())

        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val rawItems = listOf(
            PrayerType.SUBUH to subuhTime,
            PrayerType.DZUHUR to dzuhurTime,
            PrayerType.ASHAR to asharTime,
            PrayerType.MAGHRIB to maghribTime,
            PrayerType.ISYA to isyaTime
        )

        // Find current and next prayer
        var activePrayerType: PrayerType? = null
        var nextPrayerType: PrayerType? = null

        val isToday = date.isEqual(LocalDate.now())

        if (isToday) {
            when {
                currentTime.isBefore(subuhTime) -> {
                    nextPrayerType = PrayerType.SUBUH
                    activePrayerType = PrayerType.ISYA
                }
                currentTime.isBefore(dzuhurTime) -> {
                    activePrayerType = PrayerType.SUBUH
                    nextPrayerType = PrayerType.DZUHUR
                }
                currentTime.isBefore(asharTime) -> {
                    activePrayerType = PrayerType.DZUHUR
                    nextPrayerType = PrayerType.ASHAR
                }
                currentTime.isBefore(maghribTime) -> {
                    activePrayerType = PrayerType.ASHAR
                    nextPrayerType = PrayerType.MAGHRIB
                }
                currentTime.isBefore(isyaTime) -> {
                    activePrayerType = PrayerType.MAGHRIB
                    nextPrayerType = PrayerType.ISYA
                }
                else -> {
                    activePrayerType = PrayerType.ISYA
                    nextPrayerType = PrayerType.SUBUH // tomorrow Subuh
                }
            }
        }

        val items = rawItems.map { (type, time) ->
            val status = prayerStatuses[type] ?: if (isToday && currentTime.isAfter(time)) {
                PrayerStatus.PENDING
            } else {
                PrayerStatus.PENDING
            }
            val mode = reminderModes[type] ?: ReminderMode.ADZAN_FULL

            PrayerScheduleItem(
                type = type,
                time = time,
                timeFormatted = time.format(timeFormatter),
                isCurrent = (type == activePrayerType),
                isNext = (type == nextPrayerType),
                status = status,
                reminderMode = mode
            )
        }

        val nextItem = items.find { it.type == nextPrayerType } ?: items.first()

        // Calculate countdown to next prayer
        val remainingFormatted = if (isToday) {
            val targetTime = nextItem.time
            val durationMinutes = if (targetTime.isAfter(currentTime)) {
                Duration.between(currentTime, targetTime).toMinutes()
            } else {
                Duration.between(currentTime, targetTime.plusHours(24)).toMinutes()
            }
            val hours = durationMinutes / 60
            val mins = durationMinutes % 60
            if (hours > 0) "${hours}j ${mins}m lagi" else "${mins}m lagi"
        } else {
            ""
        }

        // Format Gregorian and Hijri Date
        val gregFormatter = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy", Locale("id", "ID"))
        val gregFormatted = date.format(gregFormatter)
        val hijriFormatted = convertToHijri(date, settings.hijriAdjustmentDays)

        return DayPrayerSchedule(
            date = date,
            dateHijriFormatted = hijriFormatted,
            dateGregorianFormatted = gregFormatted,
            city = settings.cityName,
            items = items,
            nextPrayer = nextItem,
            remainingTimeFormatted = remainingFormatted
        )
    }

    /**
     * Hijri calculation algorithm with ±2 days manual correction support
     */
    fun convertToHijri(date: LocalDate, adjustmentDays: Int = 0): String {
        val adjusted = date.plusDays(adjustmentDays.toLong())
        val jd = julianDay(adjusted.year, adjusted.monthValue, adjusted.dayOfMonth)

        val l = jd.toLong() - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val l1 = l - 10631 * n + 354
        val j = (((10985 - l1) / 5316).toInt()) * (((50 * l1) / 17719).toInt()) +
                ((l1 / 5670).toInt()) * (((43 * l1) / 15238).toInt())
        val l2 = l1 - (((30 - j) / 15).toInt()) * (((17719 * j) / 50).toInt()) -
                ((j / 16).toInt()) * (((15238 * j) / 43).toInt()) + 29
        val m = ((24 * l2) / 709).toInt()
        val d = (l2 - ((709 * m) / 24).toInt()).toInt()
        val y = (30 * n + j - 30).toInt()

        val hijriMonths = listOf(
            "Muharram", "Safar", "Rabi'ul Awal", "Rabi'ul Akhir",
            "Jumadil Awal", "Jumadil Akhir", "Rajab", "Sya'ban",
            "Ramadhan", "Syawal", "Dzulqa'dah", "Dzulhijjah"
        )
        val monthName = hijriMonths.getOrElse(m - 1) { "Jumadil Awal" }

        return "$d $monthName $y H"
    }
}
