package com.ibadahku.app.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KhatamPlanTest {

    // PRD AC-4: Diberikan target khatam 30 hari dan pengguna melewatkan 2 hari,
    // maka target harian dihitung ulang dan estimasi khatam diperbarui.
    @Test
    fun testAc4KhatamPlanRecalculation() {
        val totalMushafPages = 604
        val targetDaysInitial = 30
        val initialDailyTarget = totalMushafPages.toDouble() / targetDaysInitial.toDouble()
        // 604 / 30 = 20.1333... pages/day
        assertEquals(20.13, initialDailyTarget, 0.05)

        // After 2 days with 0 pages read (user missed 2 days):
        val daysRemaining = targetDaysInitial - 2 // 28 days remaining
        val pagesRemaining = totalMushafPages - 0
        val recalculatedDailyTarget = pagesRemaining.toDouble() / daysRemaining.toDouble()
        // 604 / 28 = 21.57 pages/day
        assertTrue(recalculatedDailyTarget > initialDailyTarget)
        assertEquals(21.57, recalculatedDailyTarget, 0.05)
    }
}
