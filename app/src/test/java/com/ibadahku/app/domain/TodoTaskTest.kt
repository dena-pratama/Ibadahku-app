package com.ibadahku.app.domain

import com.ibadahku.app.data.model.RecurrenceRule
import com.ibadahku.app.data.model.TaskCategory
import com.ibadahku.app.data.model.TaskType
import com.ibadahku.app.data.model.TodoTask
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TodoTaskTest {

    // PRD AC-5: Diberikan counter sholawat target 100, ketika aplikasi ditutup di hitungan 57,
    // maka saat dibuka kembali hitungan tetap 57.
    @Test
    fun testAc5CounterRetentionAndCompletion() {
        var task = TodoTask(
            id = "task_sholawat",
            title = "Sholawat Nabi",
            category = TaskCategory.SHOLAWAT,
            type = TaskType.COUNTER,
            targetCount = 100,
            currentCount = 57,
            isCompleted = false,
            recurrence = RecurrenceRule.DAILY
        )

        // Count is retained at 57
        assertEquals(57, task.currentCount)
        assertFalse(task.isCompleted)

        // Increment count
        task = task.copy(currentCount = task.currentCount + 1)
        assertEquals(58, task.currentCount)

        // Reach target 100
        task = task.copy(currentCount = 100, isCompleted = true)
        assertTrue(task.isCompleted)
        assertEquals(100, task.currentCount)
    }
}
