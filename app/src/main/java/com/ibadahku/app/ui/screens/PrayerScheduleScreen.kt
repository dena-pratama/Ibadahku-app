package com.ibadahku.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.data.model.DayPrayerSchedule
import com.ibadahku.app.data.model.PrayerScheduleItem
import com.ibadahku.app.data.model.PrayerStatus
import com.ibadahku.app.data.model.PrayerType
import com.ibadahku.app.data.model.ReminderMode
import com.ibadahku.app.ui.components.HeroMosqueCardArtwork
import com.ibadahku.app.ui.theme.AccentBadgeBackground
import com.ibadahku.app.ui.theme.AccentBadgeText
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.DarkForestGreen
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary

@Composable
fun PrayerScheduleScreen(
    schedule: DayPrayerSchedule,
    onBackClick: () -> Unit,
    onToggleReminder: (PrayerType) -> Unit,
    onTogglePrayerStatus: (PrayerType) -> Unit,
    onNavigateToQadha: () -> Unit = {},
    onNavigateToTadarus: () -> Unit = {},
    qadhaTotal: Int = 0,
    completedTasksCount: Int = 0,
    quranPagesRead: Int = 5
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(0) } // 0: Jadwal Sholat, 1: Riwayat & Statistik

    val fardhuItems = schedule.items.filter {
        it.type in listOf(
            PrayerType.SUBUH,
            PrayerType.DZUHUR,
            PrayerType.ASHAR,
            PrayerType.MAGHRIB,
            PrayerType.ISYA
        )
    }
    val completedFardhuCount = fardhuItems.count { it.status == PrayerStatus.DONE }
    val totalFardhuCount = fardhuItems.size.coerceAtLeast(5)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Kembali",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = if (selectedTab == 0) "Jadwal Sholat" else "Riwayat & Statistik",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp
                )
            )

            IconButton(
                onClick = { /* Pick date */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Pilih Tanggal",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 1. Redesigned Premium Islamic Header Card with Mosque Artwork
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp)),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF163C2A),
                                Color(0xFF22513B),
                                Color(0xFF2E654C)
                            )
                        )
                    )
            ) {
                // Subtle mosque silhouette background
                HeroMosqueCardArtwork(
                    modifier = Modifier
                        .matchParentSize()
                        .padding(top = 8.dp),
                    isDark = true
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Dates & Location Badge
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0x33FBBF24))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "📍 ${schedule.city}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFFFEF08A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = schedule.dateGregorianFormatted,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "🌙 ${schedule.dateHijriFormatted}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFD1FAE5),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    // Right: Circular Sholat Progress Ring
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.14f))
                            .border(1.5.dp, Color(0xFFFDE047).copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$completedFardhuCount/$totalFardhuCount",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "Selesai",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFFFEF08A),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. Segmented Pill Tab Switcher with animated selection
        val tab0Bg by animateColorAsState(
            targetValue = if (selectedTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent,
            animationSpec = tween(250),
            label = "tab0Bg"
        )
        val tab0Text by animateColorAsState(
            targetValue = if (selectedTab == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            animationSpec = tween(250),
            label = "tab0Text"
        )
        val tab1Bg by animateColorAsState(
            targetValue = if (selectedTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent,
            animationSpec = tween(250),
            label = "tab1Bg"
        )
        val tab1Text by animateColorAsState(
            targetValue = if (selectedTab == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            animationSpec = tween(250),
            label = "tab1Text"
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tab0Bg)
                    .clickable { selectedTab = 0 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Jadwal Hari Ini",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = tab0Text,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tab1Bg)
                    .clickable { selectedTab = 1 }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Riwayat & Statistik",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = tab1Text,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 13.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                if (targetState > initialState) {
                    (slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { it / 3 } + fadeIn(tween(280)))
                        .togetherWith(slideOutHorizontally(tween(220, easing = FastOutSlowInEasing)) { -it / 3 } + fadeOut(tween(200)))
                } else {
                    (slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { -it / 3 } + fadeIn(tween(280)))
                        .togetherWith(slideOutHorizontally(tween(220, easing = FastOutSlowInEasing)) { it / 3 } + fadeOut(tween(200)))
                }.using(SizeTransform(clip = false))
            },
            label = "PrayerScheduleTabAnim"
        ) { currentTab ->
            if (currentTab == 0) {
                // ================== TAB 0: JADWAL SHOLAT ==================
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        schedule.items.forEach { item ->
                            ImprovedPrayerTimeCard(
                                item = item,
                                onToggleReminder = { onToggleReminder(item.type) },
                                onToggleStatus = { onTogglePrayerStatus(item.type) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Clean Modern Bottom Banner (Overlapping bug removed!)
                    val nextItem = schedule.nextPrayer ?: schedule.items.firstOrNull()
                    val nextName = nextItem?.type?.displayName ?: "Dzuhur"
                    val remaining = if (schedule.remainingTimeFormatted.isNotEmpty()) schedule.remainingTimeFormatted else "1 jam 12 menit"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(AccentBadgeBackground, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = null,
                                    tint = AccentBadgeText,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column {
                                Text(
                                    text = "$nextName dalam $remaining",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Siapkan diri untuk menyambut panggilan-Nya tepat waktu",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
            // ================== TAB 1: RIWAYAT & STATISTIK ==================
            // Card 1: 7-Day Weekly Consistency Tracker
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Konsistensi Sholat Mingguan",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = "7 Hari Terakhir",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "88% Sangat Baik",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 7-Day Bar Chart
                    val weekDays = listOf(
                        Triple("Sen", 5, true),
                        Triple("Sel", 5, false),
                        Triple("Rab", 4, false),
                        Triple("Kam", 5, false),
                        Triple("Jum", 5, false),
                        Triple("Sab", 4, false),
                        Triple("Min", 5, false)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        weekDays.forEach { (day, done, isToday) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = "$done/5",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 10.sp,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                                    )
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Vertical Bar
                                Box(
                                    modifier = Modifier
                                        .width(28.dp)
                                        .height(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.BottomCenter
                                ) {
                                    val barHeightRatio = done / 5.0f
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(barHeightRatio)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (isToday) MaterialTheme.colorScheme.primary else Color(0xFF689F82)
                                            )
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card 2: Tadarus & To-Do Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Ringkasan Amalan Harian",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row Tadarus
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onNavigateToTadarus() }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Tadarus Al-Qur'an",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "$quranPagesRead Halaman dibaca hari ini",
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
                            modifier = Modifier.size(13.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Row To-Do & Dzikir
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(MaterialTheme.colorScheme.surface, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Checklist,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "To-Do & Dzikir Harian",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "$completedTasksCount Amalan tuntas hari ini",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Tercatat",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Card 3: Pelacak Qadha Sholat
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigateToQadha() },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFFFEF3C7), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Pelacak Qadha Sholat",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            )
                            Text(
                                text = if (qadhaTotal > 0) "Sisa $qadhaTotal utang sholat perlu di-qadha" else "Alhamdulillah, tidak ada utang sholat",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (qadhaTotal > 0) Color(0xFFD97706) else MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Buka",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ImprovedPrayerTimeCard(
    item: PrayerScheduleItem,
    onToggleReminder: () -> Unit,
    onToggleStatus: () -> Unit
) {
    val isHighlighted = item.isCurrent
    val isDone = (item.status == PrayerStatus.DONE)
    val isReminderActive = (item.reminderMode != ReminderMode.DISABLED)

    // Animated container and border colors
    val cardBgColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
            isDone -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(250),
        label = "cardBgColor"
    )

    val cardBorderColor by animateColorAsState(
        targetValue = when {
            isHighlighted -> Color(0xFFF59E0B).copy(alpha = 0.7f)
            isDone -> ForestGreen.copy(alpha = 0.35f)
            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
        },
        animationSpec = tween(250),
        label = "cardBorderColor"
    )

    val arabicName = when (item.type) {
        PrayerType.SUBUH -> "الصُّبْح"
        PrayerType.TERBIT -> "الشُّرُوق"
        PrayerType.DZUHUR -> "الظُّهْر"
        PrayerType.ASHAR -> "العَصْر"
        PrayerType.MAGHRIB -> "المَغْرِب"
        PrayerType.ISYA -> "العِشَاء"
        PrayerType.IMSAK -> "الإمْسَاك"
    }

    val icon: ImageVector = when (item.type) {
        PrayerType.SUBUH -> Icons.Outlined.Bedtime
        PrayerType.TERBIT -> Icons.Outlined.WbSunny
        PrayerType.DZUHUR -> Icons.Default.WbSunny
        PrayerType.ASHAR -> Icons.Outlined.WbSunny
        PrayerType.MAGHRIB -> Icons.Outlined.Bedtime
        PrayerType.ISYA -> Icons.Outlined.Bedtime
        PrayerType.IMSAK -> Icons.Outlined.Bedtime
    }

    // Infinite breathing glow for active prayer pill
    val infiniteTransition = rememberInfiniteTransition(label = "ActivePrayerPulse")
    val liveDotAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "DotPulse"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isHighlighted) 1.2.dp else 1.dp,
                color = cardBorderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Icon in 38dp Circle
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        when {
                            isHighlighted -> Color(0xFFFEF3C7)
                            isDone -> ForestGreen.copy(alpha = 0.12f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = item.type.displayName,
                    tint = when {
                        isHighlighted -> Color(0xFFD97706)
                        isDone -> ForestGreen
                        else -> MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 2. Prayer Name & Status Subtitle (Uniform 2-line height for all items!)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.type.displayName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = arabicName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isHighlighted) Color(0xFFB45309) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        ),
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Consistent subtitle across all cards (avoids any height jump!)
                if (isHighlighted) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD97706).copy(alpha = liveDotAlpha))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sedang Berjalan",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFD97706),
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                } else if (isDone) {
                    Text(
                        text = "Sudah Dikerjakan ✓",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ForestGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                } else {
                    Text(
                        text = "Belum Dikerjakan",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f),
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 3. Right: Time + Bell Button + Checkmark Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = item.timeFormatted,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isHighlighted) Color(0xFF92400E) else MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    )
                )

                // Bell / Notification toggle icon
                val bellTint by animateColorAsState(
                    targetValue = if (isReminderActive) ForestGreen else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f),
                    animationSpec = tween(200),
                    label = "BellTint"
                )
                IconButton(
                    onClick = onToggleReminder,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isReminderActive) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                        contentDescription = "Pengingat",
                        tint = bellTint,
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Check button for "Sudah Sholat"
                val checkBgColor by animateColorAsState(
                    targetValue = if (isDone) ForestGreen else MaterialTheme.colorScheme.surfaceVariant,
                    animationSpec = tween(250),
                    label = "CheckBg"
                )
                val checkIconTint by animateColorAsState(
                    targetValue = if (isDone) Color.White else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    animationSpec = tween(250),
                    label = "CheckTint"
                )
                IconButton(
                    onClick = onToggleStatus,
                    modifier = Modifier
                        .size(32.dp)
                        .background(color = checkBgColor, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = if (isDone) "Sudah Sholat" else "Tandai Sholat",
                        tint = checkIconTint,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}
