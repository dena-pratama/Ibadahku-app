package com.ibadahku.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class QiblaCalculatorTest {

    // PRD AC-6: 10 test locations, difference <= 0.5°
    private val testCities = listOf(
        Triple("Jakarta", Pair(-6.2088, 106.8456), 295.17),
        Triple("Bandung", Pair(-6.9175, 107.6191), 295.14),
        Triple("Medan", Pair(3.5952, 98.6722), 292.74),
        Triple("Makassar", Pair(-5.1477, 119.4327), 292.48),
        Triple("Jayapura", Pair(-2.5916, 140.6690), 291.33),
        Triple("Sydney", Pair(-33.8688, 151.2093), 277.50),
        Triple("London", Pair(51.5074, -0.1278), 118.98),
        Triple("New York", Pair(40.7128, -74.0060), 58.48),
        Triple("Tokyo", Pair(35.6762, 139.6503), 293.02),
        Triple("Johannesburg", Pair(-26.2041, 28.0473), 14.59)
    )

    @Test
    fun testAc6QiblaBearingAccuracyFor10Cities() {
        for ((cityName, coords, referenceBearing) in testCities) {
            val calculatedBearing = QiblaCalculator.calculateBearing(coords.first, coords.second)
            val diff = abs(calculatedBearing - referenceBearing)
            assertTrue(
                "City $cityName failed accuracy: calculated=$calculatedBearing, reference=$referenceBearing, diff=$diff",
                diff <= 0.5
            )
        }
    }

    @Test
    fun testDistanceToMakkah() {
        // Distance from Jakarta to Makkah is ~7900-8000 km
        val distJakarta = QiblaCalculator.calculateDistanceKm(-6.2088, 106.8456)
        assertTrue(distJakarta in 7800.0..8100.0)
    }

    @Test
    fun testCardinalDirection() {
        val cardinal = QiblaCalculator.getCardinalDirection(295.0)
        assertEquals("Arah ke Barat Daya", cardinal)
    }
}
