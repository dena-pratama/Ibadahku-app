package com.ibadahku.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ibadahku.app.data.model.RecurrenceRule
import com.ibadahku.app.data.model.TaskCategory
import com.ibadahku.app.data.model.TaskType
import com.ibadahku.app.data.model.TodoTask
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary

@Composable
fun AddTaskDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onTaskCreated: (TodoTask) -> Unit
) {
    if (!visible) return

    var title by remember { mutableStateOf("") }
    var subtitle by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.DZIKIR) }
    var isCounter by remember { mutableStateOf(false) }
    var targetCountText by remember { mutableStateOf("100") }

    // Preset templates (PRD TO-02)
    val templates = remember {
        listOf(
            Triple("Baca Al-Kahfi", "Sunnah hari Jumat", TaskCategory.QURAN),
            Triple("Baca Al-Mulk", "Sebelum tidur malam", TaskCategory.QURAN),
            Triple("Sholawat 100x", "Keutamaan shalawat atas Nabi", TaskCategory.SHOLAWAT),
            Triple("Istighfar 100x", "Memohon ampunan Allah", TaskCategory.DZIKIR),
            Triple("Dzikir Petang", "100x perlindungan sore hari", TaskCategory.DZIKIR),
            Triple("Sholat Dhuha", "2 - 4 Rakaat", TaskCategory.SHOLAT_SUNNAH),
            Triple("Sholat Tahajud", "Sepertiga malam terakhir", TaskCategory.SHOLAT_SUNNAH),
            Triple("Puasa Sunnah", "Senin / Kamis", TaskCategory.PUASA)
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tambah Amalan",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Template Satu Ketukan:",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Template Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    templates.take(3).forEach { (tTitle, tSub, tCat) ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .clickable {
                                    title = tTitle
                                    subtitle = tSub
                                    selectedCategory = tCat
                                    if (tTitle.contains("100x")) {
                                        isCounter = true
                                        targetCountText = "100"
                                    }
                                }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = tTitle,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama Amalan") },
                    placeholder = { Text("Misal: Baca Al-Waqi'ah") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Subtitle Input
                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Catatan / Target") },
                    placeholder = { Text("Misal: 100x atau Minimal 1 juz") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Counter Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gunakan Tasbih Digital?",
                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (!isCounter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                                .clickable { isCounter = false }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Centang",
                                color = if (!isCounter) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isCounter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
                                .clickable { isCounter = true }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "Tasbih",
                                color = if (isCounter) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val count = targetCountText.toIntOrNull() ?: 100
                            onTaskCreated(
                                TodoTask(
                                    id = System.currentTimeMillis().toString(),
                                    title = title.trim(),
                                    subtitle = subtitle.trim(),
                                    category = selectedCategory,
                                    type = if (isCounter) TaskType.COUNTER else TaskType.CHECKBOX,
                                    targetCount = if (isCounter) count else 1,
                                    currentCount = 0,
                                    isCompleted = false,
                                    recurrence = RecurrenceRule.DAILY
                                )
                            )
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = title.isNotBlank()
                ) {
                    Text(
                        text = "Simpan Amalan",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
