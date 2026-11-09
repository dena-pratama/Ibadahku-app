package com.ibadahku.app.data.model

enum class TaskCategory(val displayName: String, val icon: String) {
    QURAN("Al-Qur'an", "📖"),
    SHOLAWAT("Sholawat", "✨"),
    DZIKIR("Dzikir", "📿"),
    SHOLAT_SUNNAH("Sholat Sunnah", "🕌"),
    PUASA("Puasa", "🌙"),
    SEDEKAH("Sedekah", "🤝"),
    LAINNYA("Lainnya", "📌")
}

enum class TaskType {
    CHECKBOX,
    COUNTER
}

enum class RecurrenceRule {
    DAILY,
    WEEKLY,
    ONCE
}

data class TodoTask(
    val id: String,
    val title: String,
    val subtitle: String = "",
    val category: TaskCategory,
    val type: TaskType = TaskType.CHECKBOX,
    val targetCount: Int = 1,
    val currentCount: Int = 0,
    val isCompleted: Boolean = false,
    val recurrence: RecurrenceRule = RecurrenceRule.DAILY,
    val note: String = "",
    val dateCreated: Long = System.currentTimeMillis()
)
