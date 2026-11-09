package com.ibadahku.app.domain

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object QiblaCalculator {

    // Ka'bah coordinates according to PRD 6.6
    const val MAKKAH_LATITUDE = 21.4225
    const val MAKKAH_LONGITUDE = 39.8262

    private fun toRad(deg: Double): Double = Math.toRadians(deg)
    private fun toDeg(rad: Double): Double = Math.toDegrees(rad)

    /**
     * Calculates the great-circle bearing towards the Ka'bah
     * θ = atan2( sin Δλ · cos φ2 , cos φ1 · sin φ2 − sin φ1 · cos φ2 · cos Δλ )
     * Returns bearing normalized to 0..360 degrees
     */
    fun calculateBearing(userLat: Double, userLng: Double): Double {
        val phi1 = toRad(userLat)
        val phi2 = toRad(MAKKAH_LATITUDE)
        val deltaLambda = toRad(MAKKAH_LONGITUDE - userLng)

        val y = sin(deltaLambda) * cos(phi2)
        val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(deltaLambda)
        val bearingRad = atan2(y, x)
        var bearingDeg = toDeg(bearingRad)

        // Normalize to 0..360
        bearingDeg = (bearingDeg + 360.0) % 360.0
        return bearingDeg
    }

    /**
     * Calculates great-circle distance to Makkah in kilometers
     */
    fun calculateDistanceKm(userLat: Double, userLng: Double): Double {
        val earthRadiusKm = 6371.0
        val dLat = toRad(MAKKAH_LATITUDE - userLat)
        val dLon = toRad(MAKKAH_LONGITUDE - userLng)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(toRad(userLat)) * cos(toRad(MAKKAH_LATITUDE)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadiusKm * c
    }

    /**
     * Gets Indonesian description of cardinal direction for a given bearing angle
     */
    fun getCardinalDirection(bearingDeg: Double): String {
        val b = (bearingDeg + 360.0) % 360.0
        return when {
            b >= 337.5 || b < 22.5 -> "Arah ke Utara"
            b >= 22.5 && b < 67.5 -> "Arah ke Timur Laut"
            b >= 67.5 && b < 112.5 -> "Arah ke Timur"
            b >= 112.5 && b < 157.5 -> "Arah ke Tenggara"
            b >= 157.5 && b < 202.5 -> "Arah ke Selatan"
            b >= 202.5 && b < 247.5 -> "Arah ke Barat Daya"
            b >= 247.5 && b < 292.5 -> "Arah ke Barat"
            else -> "Arah ke Barat Daya" // In Indonesia typically 295° (Barat Laut / Barat - Barat Daya)
        }
    }
}
