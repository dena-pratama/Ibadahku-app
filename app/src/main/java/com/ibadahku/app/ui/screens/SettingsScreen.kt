package com.ibadahku.app.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ibadahku.app.R
import com.ibadahku.app.data.model.PrayerSettings
import com.ibadahku.app.engine.AudioPlayerManager
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.DarkForestGreen
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary

@Composable
fun SettingsScreen(
    settings: PrayerSettings,
    appTheme: String = "system",
    onSelectTheme: (String) -> Unit = {},
    onBackClick: () -> Unit,
    onUpdateSettings: (PrayerSettings) -> Unit,
    onNavigateToReliabilityCheck: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showNameDialog by remember { mutableStateOf(false) }
    var showSoundDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }

    val audioPlayer = remember { AudioPlayerManager(context) }
    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop()
        }
    }

    // Audio file picker launcher
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                // Ignore if not supported by content provider
            }
            val fileName = getAudioFileName(context, uri)
            onUpdateSettings(
                settings.copy(
                    adzanSoundType = "CUSTOM_FILE",
                    adzanSoundName = fileName,
                    adzanCustomUri = uri.toString(),
                    adzanCustomFileName = fileName
                )
            )
        }
    }

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
                .padding(top = 8.dp, bottom = 20.dp),
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
                text = "Pengaturan",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.size(40.dp))
        }

        // Section: Profil Pengguna (Sapaan Personal)
        Text(
            text = "Profil Pengguna",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .clickable { showNameDialog = true },
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = settings.userName.ifBlank { "Fulan" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Sapaan: Assalamu'alaikum, Selamat siang, ${settings.userName.ifBlank { "Fulan" }}!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            ),
                            maxLines = 1
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Ubah",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
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

        Spacer(modifier = Modifier.height(20.dp))

        // Section: Pengingat Sholat
        Text(
            text = "Pengingat Sholat",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                // Master Switch Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pengingat Sholat",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 15.sp
                        )
                    )

                    Switch(
                        checked = settings.prayerNotificationEnabled,
                        onCheckedChange = {
                            onUpdateSettings(settings.copy(prayerNotificationEnabled = it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Row: Suara Adzan & Pengingat (Bawaan / File Kustom)
                val soundBadge = if (settings.adzanSoundType == "CUSTOM_FILE") {
                    "File: ${settings.adzanSoundName}"
                } else {
                    settings.adzanSoundName
                }

                SettingNavigationRow(
                    label = "Suara Adzan & Pengingat",
                    value = soundBadge,
                    onClick = { showSoundDialog = true }
                )

                // Row: Pengingat Pra-Waktu (Persiapan Sebelum Adzan)
                SettingNavigationRow(
                    label = "Pengingat Pra-Waktu",
                    value = if (settings.prePrayerReminderEnabled) "${settings.prePrayerMinutes} mnt sebelum adzan" else "Nonaktif",
                    onClick = {
                        val nextMin = when (settings.prePrayerMinutes) {
                            5 -> 10
                            10 -> 15
                            15 -> 20
                            20 -> 30
                            else -> 5
                        }
                        onUpdateSettings(
                            settings.copy(
                                prePrayerReminderEnabled = true,
                                prePrayerMinutes = nextMin
                            )
                        )
                    }
                )

                // Row: Mode Hening -> Jeda Notifikasi 30 menit >
                SettingNavigationRow(
                    label = "Mode Hening",
                    value = "Jeda Notifikasi ${settings.silenceModeDelayMinutes} menit",
                    onClick = {
                        val nextDelay = when (settings.silenceModeDelayMinutes) {
                            15 -> 30
                            30 -> 45
                            45 -> 60
                            else -> 15
                        }
                        onUpdateSettings(settings.copy(silenceModeDelayMinutes = nextDelay))
                    }
                )

                // Row: Cek Keandalan Pengingat (PRD ON-03)
                SettingNavigationRow(
                    label = "Cek Keandalan Pengingat",
                    value = "Cek Baterai & Alarm",
                    highlight = true,
                    onClick = onNavigateToReliabilityCheck
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section: Umum
        Text(
            text = "Umum",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 15.sp
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                // Tema Aplikasi
                val themeLabel = when (appTheme) {
                    "dark" -> "Gelap"
                    "light" -> "Terang"
                    else -> "Ikuti Sistem"
                }

                SettingIconRow(
                    icon = if (appTheme == "dark") Icons.Default.DarkMode else Icons.Default.LightMode,
                    title = "Tema Aplikasi",
                    value = themeLabel,
                    onClick = { showThemeDialog = true }
                )

                // Bahasa
                SettingIconRow(
                    icon = Icons.Default.Language,
                    title = "Bahasa",
                    value = "Indonesia",
                    onClick = { /* Change language */ }
                )

                // Metode Perhitungan
                SettingIconRow(
                    icon = Icons.Default.Calculate,
                    title = "Metode Perhitungan",
                    value = settings.method.title.substringBefore(" ("),
                    onClick = {
                        val methods = com.ibadahku.app.data.model.CalculationMethod.values()
                        val nextIdx = (settings.method.ordinal + 1) % methods.size
                        onUpdateSettings(settings.copy(method = methods[nextIdx]))
                    }
                )

                // Tentang Aplikasi
                SettingIconRow(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    value = "v1.0 (Offline-First)",
                    onClick = { /* Show about */ }
                )

                // Bagi Aplikasi
                SettingIconRow(
                    icon = Icons.Default.Share,
                    title = "Bagi Aplikasi",
                    value = "",
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Gunakan Ibadahku - Aplikasi Pengingat Sholat, Tadarus Qur'an, To-Do Ibadah & Kompas Kiblat tanpa iklan!"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Bagikan Ibadahku"))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))
    }

    // 1. Edit Name Dialog
    if (showNameDialog) {
        var tempName by remember { mutableStateOf(settings.userName) }

        Dialog(onDismissRequest = { showNameDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = "Atur Nama Pengguna",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Nama ini akan ditampilkan pada sapaan Beranda setiap kali Anda membuka aplikasi.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Nama Panggilan") },
                        placeholder = { Text("Contoh: Fulan, Ahmad, Aisyah") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Live Preview Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "👀 Preview Sapaan:\n\"Assalamu'alaikum 👋\nSelamat siang, ${tempName.ifBlank { "Muslim" }}!\"",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showNameDialog = false }) {
                            Text("Batal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val cleanName = tempName.trim().ifEmpty { "Fulan" }
                                onUpdateSettings(settings.copy(userName = cleanName))
                                showNameDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Simpan Nama", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 2. Custom Adzan & Sound Dialog (Presets vs Custom Audio File)
    if (showSoundDialog) {
        var selectedTab by remember { mutableStateOf(if (settings.adzanSoundType == "CUSTOM_FILE") 1 else 0) }
        var previewingSoundId by remember { mutableStateOf<String?>(null) }

        val presetSounds = listOf(
            Triple("adzan_nabawi", "Masjid Nabawi (Madinah)", R.raw.adzan_nabawi),
            Triple("adzan_subuh", "Adzan Subuh Khas (Tatswib)", R.raw.adzan_subuh),
            Triple("adzan_ringtone", "Nada Bip / Notifikasi Pendek", R.raw.adzan_ringtone)
        )

        Dialog(onDismissRequest = {
            audioPlayer.stop()
            showSoundDialog = false
        }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Suara Adzan & Pengingat",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Gunakan rekaman adzan bawaan aplikasi atau pilih file audio sendiri dari memori HP.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tab Switcher: [ Bawaan Aplikasi ] [ Pilih File ]
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = {
                                audioPlayer.stop()
                                previewingSoundId = null
                                selectedTab = 0
                            },
                            text = { Text("Bawaan App", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                audioPlayer.stop()
                                previewingSoundId = null
                                selectedTab = 1
                            },
                            text = { Text("Pilih di File", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (selectedTab == 0) {
                        // TAB 1: Bawaan Aplikasi
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            presetSounds.forEach { (id, name, resId) ->
                                val isSelected = (settings.adzanSoundType == "PRESET" && settings.adzanSoundId == id)
                                val isPlayingThis = (previewingSoundId == id)

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                                        .border(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(12.dp)
                                        )
                                        .clickable {
                                            audioPlayer.stop()
                                            previewingSoundId = null
                                            onUpdateSettings(
                                                settings.copy(
                                                    adzanSoundType = "PRESET",
                                                    adzanSoundId = id,
                                                    adzanSoundName = name
                                                )
                                            )
                                        }
                                        .padding(horizontal = 12.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                audioPlayer.stop()
                                                previewingSoundId = null
                                                onUpdateSettings(
                                                    settings.copy(
                                                        adzanSoundType = "PRESET",
                                                        adzanSoundId = id,
                                                        adzanSoundName = name
                                                    )
                                                )
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                                fontSize = 13.sp
                                            )
                                        )
                                    }

                                    // Preview / Test Button
                                    IconButton(
                                        onClick = {
                                            if (isPlayingThis) {
                                                audioPlayer.stop()
                                                previewingSoundId = null
                                            } else {
                                                previewingSoundId = id
                                                audioPlayer.playPreview(soundResId = resId, seconds = 8) {
                                                    previewingSoundId = null
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .size(34.dp)
                                            .background(
                                                if (isPlayingThis) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                                CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = if (isPlayingThis) Icons.Default.Stop else Icons.Default.PlayArrow,
                                            contentDescription = "Dengarkan",
                                            tint = if (isPlayingThis) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // TAB 2: Pilih di File (Custom Audio)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (settings.adzanSoundType == "CUSTOM_FILE" && settings.adzanCustomFileName.isNotBlank()) {
                                // Currently selected custom file card
                                val isPlayingCustom = (previewingSoundId == "custom_file")

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer)
                                        .border(1.5.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                                        .padding(14.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "File Audio Aktif:",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                                Text(
                                                    text = settings.adzanCustomFileName,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                        fontSize = 13.sp
                                                    ),
                                                    maxLines = 1
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = {
                                                if (isPlayingCustom) {
                                                    audioPlayer.stop()
                                                    previewingSoundId = null
                                                } else {
                                                    val uri = Uri.parse(settings.adzanCustomUri)
                                                    previewingSoundId = "custom_file"
                                                    audioPlayer.playPreview(uri = uri, seconds = 10) {
                                                        previewingSoundId = null
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        ) {
                                            Icon(
                                                imageVector = if (isPlayingCustom) Icons.Default.Stop else Icons.Default.PlayArrow,
                                                contentDescription = "Tes Audio",
                                                tint = MaterialTheme.colorScheme.onPrimary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                                        .padding(14.dp)
                                ) {
                                    Text(
                                        text = "Belum ada file audio yang dipilih. Anda dapat memilih rekaman suara adzan kesukaan Anda dalam format MP3, WAV, atau AAC.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp
                                        )
                                    )
                                }
                            }

                            // Button: Open File Picker
                            OutlinedButton(
                                onClick = {
                                    audioPlayer.stop()
                                    previewingSoundId = null
                                    audioPickerLauncher.launch("audio/*")
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FolderOpen,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (settings.adzanSoundType == "CUSTOM_FILE" && settings.adzanCustomFileName.isNotBlank()) {
                                        "Ganti File Audio Lain..."
                                    } else {
                                        "Buka File Audio di HP (.mp3 / .wav)..."
                                    },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                audioPlayer.stop()
                                showSoundDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Selesai", color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    // 3. Theme Selection Dialog
    if (showThemeDialog) {
        val themeOptions = listOf(
            Triple("light", "Mode Terang", "Tampilan cerah nuansa sage klasik"),
            Triple("dark", "Mode Gelap", "Tampilan malam nyaman untuk mata"),
            Triple("system", "Ikuti Sistem", "Otomatis menyesuaikan mode perangkat")
        )

        Dialog(onDismissRequest = { showThemeDialog = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text(
                        text = "Pilih Tema Aplikasi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 17.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        themeOptions.forEach { (key, title, desc) ->
                            val isSelected = (appTheme == key)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable {
                                        onSelectTheme(key)
                                        showThemeDialog = false
                                    }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = {
                                        onSelectTheme(key)
                                        showThemeDialog = false
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showThemeDialog = false }) {
                            Text("Tutup", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun getAudioFileName(context: Context, uri: Uri): String {
    var name = "Audio Kustom"
    try {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    name = it.getString(nameIndex)
                }
            }
        }
    } catch (e: Exception) {
        name = uri.lastPathSegment ?: "Audio Kustom"
    }
    return name
}

@Composable
fun SettingNavigationRow(
    label: String,
    value: String,
    highlight: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
                color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp
            ),
            modifier = Modifier.weight(1f)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                ),
                maxLines = 1
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = if (highlight) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}

@Composable
fun SettingIconRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(11.dp)
            )
        }
    }
}
