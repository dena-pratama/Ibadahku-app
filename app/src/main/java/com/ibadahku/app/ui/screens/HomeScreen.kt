package com.ibadahku.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.data.model.DayPrayerSchedule
import com.ibadahku.app.data.model.TodoTask
import com.ibadahku.app.ui.components.CircularProgressRing
import com.ibadahku.app.ui.components.MosqueSilhouette
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.DarkForestGreen
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextMuted
import com.ibadahku.app.ui.theme.TextSecondary
import java.time.LocalTime

@Composable
fun HomeScreen(
    prayerSchedule: DayPrayerSchedule,
    tasks: List<TodoTask>,
    userName: String = "Fulan",
    onNavigateToSchedule: () -> Unit,
    onNavigateToTadarus: () -> Unit,
    onNavigateToTodo: () -> Unit,
    onNavigateToQibla: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToQadha: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Determine greeting based on current time
    val currentHour = LocalTime.now().hour
    val greetingTime = when (currentHour) {
        in 3..10 -> "Selamat pagi"
        in 11..14 -> "Selamat siang"
        in 15..17 -> "Selamat sore"
        else -> "Selamat malam"
    }

    val displayName = userName.ifBlank { "Muslim" }

    val nextPrayer = prayerSchedule.nextPrayer ?: prayerSchedule.items.firstOrNull()
    val nextPrayerName = nextPrayer?.type?.displayName ?: "Dzuhur"
    val nextPrayerTime = nextPrayer?.timeFormatted ?: "12:01"
    val remainingCountdown = if (prayerSchedule.remainingTimeFormatted.isNotEmpty()) {
        " · ${prayerSchedule.remainingTimeFormatted}"
    } else {
        " · 1j 12m lagi"
    }

    val completedTasksCount = tasks.count { it.isCompleted }
    val totalTasksCount = tasks.size.coerceAtLeast(1)
    val progressFraction = completedTasksCount.toFloat() / totalTasksCount.toFloat()
    val progressPercent = (progressFraction * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Greeting Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Assalamu'alaikum 👋",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                )
                Text(
                    text = "$greetingTime, $displayName!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 18.sp
                    )
                )
            }

            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Pengaturan",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // The Animated Mosque Prayer Card (matching prototype in prototipe_animasi_kartu_waktu_shalat_android.html)
        com.ibadahku.app.ui.components.AnimatedMosquePrayerCard(
            currentPrayerType = nextPrayer?.type ?: com.ibadahku.app.data.model.PrayerType.DZUHUR,
            prayerName = nextPrayerName,
            prayerTime = nextPrayerTime,
            countdownText = if (prayerSchedule.remainingTimeFormatted.isNotEmpty()) {
                "Menuju $nextPrayerName dalam ${prayerSchedule.remainingTimeFormatted}"
            } else {
                "Menuju $nextPrayerName dalam 01:14:22"
            },
            onNavigateToSchedule = onNavigateToSchedule
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Quick Actions 3-Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionButton(
                icon = Icons.Default.MenuBook,
                title = "Tadarus\nQur'an",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToTadarus
            )
            QuickActionButton(
                icon = Icons.Outlined.Checklist,
                title = "To-Do\nIbadah",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToTodo
            )
            QuickActionButton(
                icon = Icons.Default.NearMe,
                title = "Arah\nKiblat",
                modifier = Modifier.weight(1f),
                onClick = onNavigateToQibla
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Progress Ibadah Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Progress Ibadah",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            )

            Row(
                modifier = Modifier.clickable { onNavigateToTodo() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lihat Detail",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Progress Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToTodo() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Circular Ring (e.g. 70%)
                CircularProgressRing(
                    progress = progressFraction,
                    size = 76.dp,
                    strokeWidth = 7.dp,
                    progressColor = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "$progressPercent%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.width(18.dp))

                Column {
                    Text(
                        text = "$completedTasksCount dari $totalTasksCount",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "Amalan Hari Ini",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Checklist preview (first two completed tasks)
                    val completedTasks = tasks.filter { it.isCompleted }.take(2)
                    if (completedTasks.isNotEmpty()) {
                        completedTasks.forEach { task ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Belum ada amalan tercatat",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Qadha Sholat Quick Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToQadha() },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Pelacak Qadha Sholat",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "Catat & bayar utang sholat fardhu",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(108.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    lineHeight = 14.sp
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
