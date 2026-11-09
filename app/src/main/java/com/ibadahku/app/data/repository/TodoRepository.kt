package com.ibadahku.app.data.repository

import android.content.Context
import com.ibadahku.app.data.model.RecurrenceRule
import com.ibadahku.app.data.model.TaskCategory
import com.ibadahku.app.data.model.TaskType
import com.ibadahku.app.data.model.TodoTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("ibadahku_todo", Context.MODE_PRIVATE)

    private val _tasksFlow = MutableStateFlow(loadInitialTasks())
    val tasksFlow: StateFlow<List<TodoTask>> = _tasksFlow.asStateFlow()

    private fun loadInitialTasks(): List<TodoTask> {
        return listOf(
            TodoTask(
                id = "1",
                title = "Baca Al-Qur'an",
                subtitle = "Minimal 10 halaman",
                category = TaskCategory.QURAN,
                type = TaskType.CHECKBOX,
                targetCount = 10,
                currentCount = 10,
                isCompleted = true,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "2",
                title = "Dzikir Pagi",
                subtitle = "100x",
                category = TaskCategory.DZIKIR,
                type = TaskType.COUNTER,
                targetCount = 100,
                currentCount = 100,
                isCompleted = true,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "3",
                title = "Sholat Dhuha",
                subtitle = "2 - 4 Rakaat",
                category = TaskCategory.SHOLAT_SUNNAH,
                type = TaskType.CHECKBOX,
                targetCount = 1,
                currentCount = 1,
                isCompleted = true,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "4",
                title = "Sholawat Nabi",
                subtitle = "100x",
                category = TaskCategory.SHOLAWAT,
                type = TaskType.COUNTER,
                targetCount = 100,
                currentCount = 45,
                isCompleted = false,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "5",
                title = "Sedekah",
                subtitle = "Ikhlas karena Allah",
                category = TaskCategory.SEDEKAH,
                type = TaskType.CHECKBOX,
                targetCount = 1,
                currentCount = 0,
                isCompleted = false,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "6",
                title = "Baca Al-Mulk",
                subtitle = "Sebelum tidur",
                category = TaskCategory.QURAN,
                type = TaskType.CHECKBOX,
                targetCount = 1,
                currentCount = 0,
                isCompleted = false,
                recurrence = RecurrenceRule.DAILY
            ),
            TodoTask(
                id = "7",
                title = "Istighfar",
                subtitle = "100x memohon ampunan",
                category = TaskCategory.DZIKIR,
                type = TaskType.COUNTER,
                targetCount = 100,
                currentCount = 20,
                isCompleted = false,
                recurrence = RecurrenceRule.DAILY
            )
        )
    }

    fun toggleTask(taskId: String): Boolean {
        var newlyCompleted = false
        val current = _tasksFlow.value.map { task ->
            if (task.id == taskId) {
                val nextState = !task.isCompleted
                if (nextState) newlyCompleted = true
                val newCount = if (nextState) task.targetCount else 0
                task.copy(isCompleted = nextState, currentCount = newCount)
            } else {
                task
            }
        }
        _tasksFlow.value = current
        return newlyCompleted
    }

    fun incrementCounter(taskId: String, amount: Int = 1): Boolean {
        var justFinished = false
        val current = _tasksFlow.value.map { task ->
            if (task.id == taskId) {
                val newCount = (task.currentCount + amount).coerceAtMost(task.targetCount)
                val completed = newCount >= task.targetCount
                if (completed && !task.isCompleted) {
                    justFinished = true
                }
                task.copy(currentCount = newCount, isCompleted = completed)
            } else {
                task
            }
        }
        _tasksFlow.value = current
        return justFinished
    }

    fun resetCounter(taskId: String) {
        val current = _tasksFlow.value.map { task ->
            if (task.id == taskId) {
                task.copy(currentCount = 0, isCompleted = false)
            } else {
                task
            }
        }
        _tasksFlow.value = current
    }

    fun undoCounter(taskId: String) {
        val current = _tasksFlow.value.map { task ->
            if (task.id == taskId) {
                val newCount = (task.currentCount - 1).coerceAtLeast(0)
                task.copy(currentCount = newCount, isCompleted = (newCount >= task.targetCount))
            } else {
                task
            }
        }
        _tasksFlow.value = current
    }

    fun addTask(task: TodoTask) {
        val current = _tasksFlow.value.toMutableList()
        current.add(0, task)
        _tasksFlow.value = current
    }

    fun deleteTask(taskId: String) {
        _tasksFlow.value = _tasksFlow.value.filter { it.id != taskId }
    }
}
