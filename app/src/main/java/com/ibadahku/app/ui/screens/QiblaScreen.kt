package com.ibadahku.app.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.engine.CompassSensorManager
import com.ibadahku.app.ui.components.CompassDialView
import com.ibadahku.app.ui.theme.AccentGreenSuccess
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary

@Composable
fun QiblaScreen(
    compassSensorManager: CompassSensorManager,
    onBackClick: () -> Unit
) {
    val compassState by compassSensorManager.compassState.collectAsState()
    val scrollState = rememberScrollState()

    DisposableEffect(Unit) {
        compassSensorManager.startListening()
        onDispose {
            compassSensorManager.stopListening()
        }
    }

    var manualRotationSlider by remember { mutableFloatStateOf(compassState.azimuthDegrees) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
                text = "Arah Kiblat",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.size(40.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Circular Compass Dial with Kaaba Icon
        CompassDialView(
            azimuthDegrees = compassState.azimuthDegrees,
            qiblaBearingDegrees = compassState.qiblaBearingDegrees,
            isAligned = compassState.isAligned
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Bold Degrees Display (e.g. 295°)
        Text(
            text = "${compassState.qiblaBearingDegrees.toInt()}°",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = if (compassState.isAligned) AccentGreenSuccess else MaterialTheme.colorScheme.onBackground,
                fontSize = 42.sp
            )
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Cardinal Text: "Arah ke Barat Daya"
        Text(
            text = if (compassState.isAligned) "Tepat Menghadap Kiblat! ✨" else compassState.cardinalDirection,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (compassState.isAligned) FontWeight.Bold else FontWeight.Medium,
                color = if (compassState.isAligned) AccentGreenSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Status Card: "Akurasi ± 3°" & "GPS aktif 🟢"
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (compassState.isAligned) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (compassState.isAligned) Icons.Default.CheckCircle else Icons.Default.GpsFixed,
                            contentDescription = null,
                            tint = if (compassState.isAligned) AccentGreenSuccess else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Akurasi ± 3°",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "Jarak ke Ka'bah: ${compassState.distanceKm.toInt()} km",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(Color(0xFF2E7D32), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "GPS aktif",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Calibration Hint if accuracy is low or sensor needs motion
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.WarningAmber,
                    contentDescription = null,
                    tint = Color(0xFFD4A359),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Bila arah kurang pas, gerakkan HP membentuk angka 8 dan jauhkan dari benda logam/magnet.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    )
                )
            }
        }

        // Simulator Slider (useful for testing on emulator or manual inspection)
        Spacer(modifier = Modifier.height(18.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Simulasi Putar Kompas (Emulator):",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
                Text(
                    text = "${manualRotationSlider.toInt()}°",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }

            Slider(
                value = manualRotationSlider,
                onValueChange = {
                    manualRotationSlider = it
                    compassSensorManager.setSimulatedAzimuth(it)
                },
                valueRange = 0f..360f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
