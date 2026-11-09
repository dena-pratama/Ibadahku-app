package com.ibadahku.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.data.model.PrayerType
import kotlin.math.cos
import kotlin.math.sin

/**
 * Visual Theme specification for each of the 5 Islamic prayers,
 * mirroring the animated prototype in prototipe_animasi_kartu_waktu_shalat_android.html.
 */
data class PrayerVisualTheme(
    val prayerType: PrayerType,
    val name: String,
    val arabicName: String,
    val badgeText: String,
    val sunnahHint: String,
    val skyColors: List<Color>,
    val horizonGlowColor: Color,
    val horizonGlowOuterColor: Color,
    val starsOpacity: Float,
    val showMoon: Boolean,
    val moonXRatio: Float,
    val moonYRatio: Float,
    val showSun: Boolean,
    val sunXRatio: Float,
    val sunYRatio: Float,
    val cloudsOpacity: Float,
    val windowsBrightness: Float,
    val bgSilhouetteColor: Color,
    val midSilhouetteColor: Color,
    val frontSilhouetteColor: Color,
    val defaultInfo: String
)

object PrayerThemeRegistry {
    fun getTheme(type: PrayerType): PrayerVisualTheme {
        return when (type) {
            PrayerType.SUBUH, PrayerType.IMSAK -> PrayerVisualTheme(
                prayerType = PrayerType.SUBUH,
                name = "Subuh",
                arabicName = "الصُّبْح",
                badgeText = "WAKTU SAAT INI • FAJAR",
                sunnahHint = "Sunnah: Qabliyah Subuh 2 Rakaat",
                skyColors = listOf(
                    Color(0xFF090D26), // Zenith Night
                    Color(0xFF1E1B4B), // Deep Indigo
                    Color(0xFF6D28D9), // Fajr Violet
                    Color(0xFFF59E0B), // Dawn Amber
                    Color(0xFFFEF08A)  // Horizon Gold
                ),
                horizonGlowColor = Color(0xCCFEF08A),
                horizonGlowOuterColor = Color(0x73F97316),
                starsOpacity = 0.85f,
                showMoon = true,
                moonXRatio = 0.74f,
                moonYRatio = 0.14f,
                showSun = true,
                sunXRatio = 0.44f,
                sunYRatio = 0.74f,
                cloudsOpacity = 0.60f,
                windowsBrightness = 1.0f,
                bgSilhouetteColor = Color(0xFF2E1065),
                midSilhouetteColor = Color(0xFF1E1B4B),
                frontSilhouetteColor = Color(0xFF0F172A),
                defaultInfo = "Imsak 04:22"
            )

            PrayerType.DZUHUR, PrayerType.TERBIT -> PrayerVisualTheme(
                prayerType = PrayerType.DZUHUR,
                name = "Dzuhur",
                arabicName = "الظُّهْر",
                badgeText = "SIANG HARI • CERAH",
                sunnahHint = "Sunnah: Rawatib Qabliyah & Ba'diyah",
                skyColors = listOf(
                    Color(0xFF0369A1), // Clear Sky Blue
                    Color(0xFF0284C7),
                    Color(0xFF38BDF8), // Midday Cyan
                    Color(0xFFBAE6FD)  // Bright Horizon
                ),
                horizonGlowColor = Color(0xB3FFFFFF),
                horizonGlowOuterColor = Color(0x66BAE6FD),
                starsOpacity = 0.0f,
                showMoon = false,
                moonXRatio = 0.74f,
                moonYRatio = 0.14f,
                showSun = true,
                sunXRatio = 0.68f,
                sunYRatio = 0.15f,
                cloudsOpacity = 0.85f,
                windowsBrightness = 0.20f,
                bgSilhouetteColor = Color(0xFF075985),
                midSilhouetteColor = Color(0xFF0C4A6E),
                frontSilhouetteColor = Color(0xFF082F49),
                defaultInfo = "Zenith Matahari"
            )

            PrayerType.ASHAR -> PrayerVisualTheme(
                prayerType = PrayerType.ASHAR,
                name = "Ashar",
                arabicName = "العَصْر",
                badgeText = "SORE HARI • TEDUH",
                sunnahHint = "Keutamaan: Menjaga Shalat Wustha",
                skyColors = listOf(
                    Color(0xFF1E3A8A), // Afternoon Deep Blue
                    Color(0xFF2563EB),
                    Color(0xFFF59E0B), // Golden Hour
                    Color(0xFFFDE68A)
                ),
                horizonGlowColor = Color(0xA6FDE047),
                horizonGlowOuterColor = Color(0x59F59E0B),
                starsOpacity = 0.0f,
                showMoon = false,
                moonXRatio = 0.74f,
                moonYRatio = 0.14f,
                showSun = true,
                sunXRatio = 0.26f,
                sunYRatio = 0.38f,
                cloudsOpacity = 0.70f,
                windowsBrightness = 0.40f,
                bgSilhouetteColor = Color(0xFF1E3A8A),
                midSilhouetteColor = Color(0xFF172554),
                frontSilhouetteColor = Color(0xFF0F172A),
                defaultInfo = "Bayangan 1x"
            )

            PrayerType.MAGHRIB -> PrayerVisualTheme(
                prayerType = PrayerType.MAGHRIB,
                name = "Maghrib",
                arabicName = "المَغْرِب",
                badgeText = "SENJA • MATAHARI TERBENAM",
                sunnahHint = "Sunnah: Ba'diyah Maghrib 2 Rakaat",
                skyColors = listOf(
                    Color(0xFF0F172A), // Dusk Navy
                    Color(0xFF4C1D95), // Violet
                    Color(0xFF9D174D), // Crimson Dusk
                    Color(0xFFEA580C), // Sunset Orange
                    Color(0xFFFACC15)  // Horizon Yellow
                ),
                horizonGlowColor = Color(0xCCFB923C),
                horizonGlowOuterColor = Color(0x73E11D48),
                starsOpacity = 0.45f,
                showMoon = true,
                moonXRatio = 0.76f,
                moonYRatio = 0.22f,
                showSun = true,
                sunXRatio = 0.34f,
                sunYRatio = 0.76f,
                cloudsOpacity = 0.65f,
                windowsBrightness = 1.0f,
                bgSilhouetteColor = Color(0xFF3B0764),
                midSilhouetteColor = Color(0xFF2E1065),
                frontSilhouetteColor = Color(0xFF090D16),
                defaultInfo = "Ghuruub Maghrib"
            )

            PrayerType.ISYA -> PrayerVisualTheme(
                prayerType = PrayerType.ISYA,
                name = "Isya",
                arabicName = "العِشَاء",
                badgeText = "MALAM HARI • TENANG",
                sunnahHint = "Sunnah: Ba'diyah Isya & Witir",
                skyColors = listOf(
                    Color(0xFF020617), // Midnight Slate
                    Color(0xFF090D26),
                    Color(0xFF1E1B4B), // Starlight Indigo
                    Color(0xFF312E81)
                ),
                horizonGlowColor = Color(0x596366F1),
                horizonGlowOuterColor = Color(0x331E1B4B),
                starsOpacity = 1.0f,
                showMoon = true,
                moonXRatio = 0.70f,
                moonYRatio = 0.12f,
                showSun = false,
                sunXRatio = 0.50f,
                sunYRatio = 0.90f,
                cloudsOpacity = 0.35f,
                windowsBrightness = 1.0f,
                bgSilhouetteColor = Color(0xFF1E1B4B),
                midSilhouetteColor = Color(0xFF0F172A),
                frontSilhouetteColor = Color(0xFF020617),
                defaultInfo = "Sepertiga Malam"
            )
        }
    }
}

/**
 * Animated Interactive Mosque Prayer Card for Android Jetpack Compose,
 * implementing all celestial motions, breathing dawn horizon glow, twinkling stars,
 * moving clouds, flying birds, and warm glowing mosque windows from the prototype.
 */
@Composable
fun AnimatedMosquePrayerCard(
    currentPrayerType: PrayerType,
    prayerName: String,
    prayerTime: String,
    countdownText: String,
    imsakOrInfo: String = "",
    alarmEnabled: Boolean = true,
    onToggleAlarm: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Optional Manual Override for Testing / Preset Switcher
    var manualOverrideType by remember { mutableStateOf<PrayerType?>(null) }
    val effectiveType = manualOverrideType ?: currentPrayerType
    val theme = remember(effectiveType) { PrayerThemeRegistry.getTheme(effectiveType) }

    val effectivePrayerName = if (manualOverrideType != null) theme.name else prayerName.ifEmpty { theme.name }
    val effectivePrayerTime = if (manualOverrideType != null) {
        when (effectiveType) {
            PrayerType.SUBUH, PrayerType.IMSAK -> "04:32"
            PrayerType.DZUHUR, PrayerType.TERBIT -> "11:58"
            PrayerType.ASHAR -> "14:55"
            PrayerType.MAGHRIB -> "18:02"
            PrayerType.ISYA -> "19:14"
        }
    } else {
        prayerTime.ifEmpty { "04:32" }
    }
    val effectiveCountdownText = if (manualOverrideType != null) {
        when (effectiveType) {
            PrayerType.SUBUH -> "Menuju Subuh dalam 04:32:00"
            PrayerType.DZUHUR -> "Menuju Dzuhur dalam 00:24:18"
            PrayerType.ASHAR -> "Menuju Ashar dalam 02:43:10"
            PrayerType.MAGHRIB -> "Menuju Maghrib dalam 05:48:00"
            PrayerType.ISYA -> "Menuju Isya dalam 07:02:15"
            else -> countdownText
        }
    } else {
        countdownText.ifEmpty { "Menuju sholat dalam 01:14:22" }
    }

    // 1. Infinite Animation Transition for Celestial Cycles
    val infiniteTransition = rememberInfiniteTransition(label = "CardLivingAtmosphere")

    // Slow drifting clouds
    val cloudOffset by infiniteTransition.animateFloat(
        initialValue = -35f,
        targetValue = 35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "CloudDrift"
    )

    // Breathing Horizon Dawn / Dusk Glow
    val horizonGlowScale by infiniteTransition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "HorizonGlowScale"
    )

    // Pulsing Mosque Arched Windows Glow
    val windowGlowPulse by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "WindowGlowPulse"
    )

    // Shimmering Stars (Phase 1 & Phase 2)
    val starTwinkle1 by infiniteTransition.animateFloat(
        initialValue = 0.20f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "StarTwinkle1"
    )

    val starTwinkle2 by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "StarTwinkle2"
    )

    // Flying Birds Silhouette gliding across
    val birdProgress by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 13000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "BirdFlight"
    )

    // Simulated adzan toast toggle
    var showAdzanSimToast by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Quick Prayer Pills Selector (Subuh, Dzuhur, Ashar, Maghrib, Isya)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val presets = listOf(
                PrayerType.SUBUH to "Subuh",
                PrayerType.DZUHUR to "Dzuhur",
                PrayerType.ASHAR to "Ashar",
                PrayerType.MAGHRIB to "Maghrib",
                PrayerType.ISYA to "Isya"
            )

            presets.forEach { (type, label) ->
                val isSelected = (effectiveType == type)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFFD97706) else MaterialTheme.colorScheme.surface
                        )
                        .clickable {
                            manualOverrideType = if (manualOverrideType == type) null else type
                        }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // The Main Animated Mosque Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(290.dp),
            shape = RoundedCornerShape(26.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // 1. Animated Canvas (Sky, Celestial bodies, Horizon Glow, Mosque Silhouettes, Windows)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height
                    val sx = w / 360f
                    val sy = h / 300f

                    // 1.1 Sky Gradient
                    drawRect(brush = Brush.verticalGradient(theme.skyColors))

                    // 1.2 Horizon Glow Breathing
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                theme.horizonGlowColor,
                                theme.horizonGlowOuterColor,
                                Color.Transparent
                            ),
                            center = Offset(w * 0.5f, h * 0.90f),
                            radius = (w * 0.55f) * horizonGlowScale
                        ),
                        center = Offset(w * 0.5f, h * 0.90f),
                        radius = (w * 0.55f) * horizonGlowScale
                    )

                    // 1.3 Stars Layer (Subuh, Maghrib, Isya)
                    if (theme.starsOpacity > 0f) {
                        val baseAlpha = theme.starsOpacity
                        val starColor1 = Color.White.copy(alpha = (baseAlpha * starTwinkle1).coerceIn(0f, 1f))
                        val starColor2 = Color(0xFFFEF08A).copy(alpha = (baseAlpha * starTwinkle2).coerceIn(0f, 1f))

                        drawCircle(color = starColor1, radius = 2.0f * sx, center = Offset(42f * sx, 48f * sy))
                        drawCircle(color = starColor2, radius = 2.4f * sx, center = Offset(88f * sx, 28f * sy))
                        drawCircle(color = starColor1, radius = 1.8f * sx, center = Offset(155f * sx, 42f * sy))
                        drawCircle(color = starColor1, radius = 2.2f * sx, center = Offset(215f * sx, 24f * sy))
                        drawCircle(color = starColor2, radius = 2.0f * sx, center = Offset(268f * sx, 58f * sy))
                        drawCircle(color = starColor1, radius = 2.4f * sx, center = Offset(322f * sx, 35f * sy))
                        drawCircle(color = starColor2, radius = 1.6f * sx, center = Offset(122f * sx, 74f * sy))
                        drawCircle(color = starColor1, radius = 1.9f * sx, center = Offset(64f * sx, 92f * sy))

                        // Morning Star (Bintang Kejora) at Subuh
                        if (effectiveType == PrayerType.SUBUH || effectiveType == PrayerType.IMSAK) {
                            drawIslamicStar(
                                cx = 290f * sx,
                                cy = 72f * sy,
                                radius = 6.5f * sx,
                                color = Color(0xFFFEF9C3).copy(alpha = (0.95f * starTwinkle1).coerceIn(0f, 1f))
                            )
                        }
                    }

                    // 1.4 Celestial Moon
                    if (theme.showMoon) {
                        val moonCx = w * theme.moonXRatio
                        val moonCy = h * theme.moonYRatio
                        val moonR = 18f * sx

                        // Halo glow behind moon
                        drawCircle(
                            color = Color(0x33FEF9C3),
                            radius = moonR * 1.8f,
                            center = Offset(moonCx, moonCy)
                        )
                        drawCrescent(
                            cx = moonCx,
                            cy = moonCy,
                            radius = moonR,
                            color = Color(0xFFFEF9C3)
                        )
                    }

                    // 1.5 Celestial Sun
                    if (theme.showSun) {
                        val sunCx = w * theme.sunXRatio
                        val sunCy = h * theme.sunYRatio
                        val sunR = 20f * sx

                        // Sun Corona / Aura
                        drawCircle(
                            color = Color(0x33FDE047),
                            radius = sunR * 1.9f,
                            center = Offset(sunCx, sunCy)
                        )
                        drawCircle(
                            color = Color(0xFFFEF08A),
                            radius = sunR,
                            center = Offset(sunCx, sunCy)
                        )
                    }

                    // 1.6 Moving Soft Clouds
                    if (theme.cloudsOpacity > 0f) {
                        val cloudAlpha = theme.cloudsOpacity
                        val cColor1 = Color(0x33FDBA74).copy(alpha = 0.25f * cloudAlpha)
                        val cColor2 = Color(0x33C4B5FD).copy(alpha = 0.22f * cloudAlpha)

                        drawOval(
                            color = cColor1,
                            topLeft = Offset(w * 0.12f + cloudOffset, h * 0.45f),
                            size = Size(w * 0.42f, 26f * sy)
                        )
                        drawOval(
                            color = cColor2,
                            topLeft = Offset(w * 0.55f - cloudOffset * 0.7f, h * 0.38f),
                            size = Size(w * 0.48f, 28f * sy)
                        )
                    }

                    // 1.7 Flying Birds
                    if (effectiveType == PrayerType.SUBUH || effectiveType == PrayerType.ASHAR || effectiveType == PrayerType.DZUHUR) {
                        val birdX = (birdProgress * w)
                        val birdY = (h * 0.38f) + sin(birdProgress * Math.PI.toFloat() * 2f) * 12f
                        drawBird(birdX, birdY, 1.0f * sx, Color(0x801E1B4B))
                        drawBird(birdX + 22f * sx, birdY + 8f * sy, 0.75f * sx, Color(0x601E1B4B))
                    }

                    // 1.8 Layer 1: Background Dunes & Horizon
                    val dunePath = Path().apply {
                        moveTo(0f, 262f * sy)
                        quadraticTo(65f * sx, 244f * sy, 135f * sx, 256f * sy)
                        quadraticTo(205f * sx, 268f * sy, 275f * sx, 248f * sy)
                        quadraticTo(318f * sx, 253f * sy, 360f * sx, 258f * sy)
                        lineTo(360f * sx, h)
                        lineTo(0f, h)
                        close()
                    }
                    drawPath(dunePath, theme.bgSilhouetteColor.copy(alpha = 0.75f))

                    // 1.9 Layer 2: Midground Architectural Complex (Left & Right Flanking Minarets & Domes)
                    val midColor = theme.midSilhouetteColor

                    // Left Minaret
                    drawRect(
                        color = midColor,
                        topLeft = Offset(34f * sx, 172f * sy),
                        size = Size(12f * sx, 110f * sy)
                    )
                    drawRoundRect(
                        color = midColor,
                        topLeft = Offset(31f * sx, 168f * sy),
                        size = Size(18f * sx, 4.5f * sy),
                        cornerRadius = CornerRadius(1.5f, 1.5f)
                    )
                    val leftSpire = Path().apply {
                        moveTo(34f * sx, 168f * sy)
                        quadraticTo(40f * sx, 150f * sy, 46f * sx, 168f * sy)
                        close()
                    }
                    drawPath(leftSpire, midColor)
                    drawLine(
                        color = Color(0xFFFDE047),
                        start = Offset(40f * sx, 148f * sy),
                        end = Offset(40f * sx, 156f * sy),
                        strokeWidth = 1.8f
                    )

                    // Right Tall Minaret (Double Balconies)
                    drawRect(
                        color = midColor,
                        topLeft = Offset(308f * sx, 152f * sy),
                        size = Size(14f * sx, 130f * sy)
                    )
                    drawRoundRect(
                        color = midColor,
                        topLeft = Offset(304f * sx, 148f * sy),
                        size = Size(22f * sx, 5f * sy),
                        cornerRadius = CornerRadius(2f, 2f)
                    )
                    drawRect(
                        color = midColor,
                        topLeft = Offset(310f * sx, 132f * sy),
                        size = Size(10f * sx, 16f * sy)
                    )
                    val rightSpire = Path().apply {
                        moveTo(308f * sx, 132f * sy)
                        quadraticTo(315f * sx, 112f * sy, 322f * sx, 132f * sy)
                        close()
                    }
                    drawPath(rightSpire, midColor)
                    drawLine(
                        color = Color(0xFFFDE047),
                        start = Offset(315f * sx, 104f * sy),
                        end = Offset(315f * sx, 115f * sy),
                        strokeWidth = 2f
                    )
                    drawCircle(color = Color(0xFFFDE047), radius = 2.5f * sx, center = Offset(315f * sx, 103f * sy))

                    // Side Companion Domes
                    val leftDome = Path().apply {
                        moveTo(98f * sx, 242f * sy)
                        cubicTo(98f * sx, 212f * sy, 138f * sx, 212f * sy, 138f * sx, 242f * sy)
                        close()
                    }
                    drawPath(leftDome, midColor)

                    val rightDome = Path().apply {
                        moveTo(222f * sx, 242f * sy)
                        cubicTo(222f * sx, 212f * sy, 262f * sx, 212f * sy, 262f * sx, 242f * sy)
                        close()
                    }
                    drawPath(rightDome, midColor)

                    // 1.10 Layer 3: Foreground Grand Central Mosque Silhouette
                    val frontColor = theme.frontSilhouetteColor

                    // Base Building & Terrace
                    drawRect(
                        color = frontColor,
                        topLeft = Offset(0f, 256f * sy),
                        size = Size(w, 60f * sy)
                    )
                    drawRoundRect(
                        color = frontColor,
                        topLeft = Offset(78f * sx, 238f * sy),
                        size = Size(204f * sx, 35f * sy),
                        cornerRadius = CornerRadius(2f, 2f)
                    )

                    // Grand Central Dome
                    val centerDome = Path().apply {
                        moveTo(132f * sx, 240f * sy)
                        cubicTo(132f * sx, 180f * sy, 228f * sx, 180f * sy, 228f * sx, 240f * sy)
                        close()
                    }
                    drawPath(centerDome, frontColor)

                    // Dome Finial & Hilal
                    drawLine(
                        color = Color(0xFFFDE047),
                        start = Offset(180f * sx, 175f * sy),
                        end = Offset(180f * sx, 192f * sy),
                        strokeWidth = 2.2f
                    )
                    drawCircle(
                        color = Color(0xFFFDE047),
                        radius = 3.5f * sx,
                        center = Offset(180f * sx, 173f * sy)
                    )

                    // Inner Twin Minarets
                    drawRect(color = frontColor, topLeft = Offset(72f * sx, 190f * sy), size = Size(9f * sx, 75f * sy))
                    val innerLSpire = Path().apply {
                        moveTo(71f * sx, 190f * sy)
                        quadraticTo(76.5f * sx, 176f * sy, 82f * sx, 190f * sy)
                        close()
                    }
                    drawPath(innerLSpire, frontColor)

                    drawRect(color = frontColor, topLeft = Offset(279f * sx, 190f * sy), size = Size(9f * sx, 75f * sy))
                    val innerRSpire = Path().apply {
                        moveTo(278f * sx, 190f * sy)
                        quadraticTo(283.5f * sx, 176f * sy, 289f * sx, 190f * sy)
                        close()
                    }
                    drawPath(innerRSpire, frontColor)

                    // 1.11 Illuminated Arched Windows (Glowing Mosque Interior)
                    val effectiveGlow = theme.windowsBrightness * windowGlowPulse
                    if (effectiveGlow > 0.05f) {
                        val archGold = Color(0xFFFBBF24).copy(alpha = effectiveGlow)
                        val archLight = Color(0xFFFEF08A).copy(alpha = effectiveGlow)
                        val archWarm = Color(0xFFF59E0B).copy(alpha = effectiveGlow)

                        // Center Grand Arches
                        drawHorseshoeWindow(163f * sx, 246f * sy, 8f * sx, 16f * sy, archGold)
                        drawHorseshoeWindow(176f * sx, 243f * sy, 8f * sx, 19f * sy, archLight)
                        drawHorseshoeWindow(189f * sx, 246f * sy, 8f * sx, 16f * sy, archGold)

                        // Flanking Windows
                        drawHorseshoeWindow(114f * sx, 250f * sy, 6f * sx, 10f * sy, archWarm)
                        drawHorseshoeWindow(126f * sx, 250f * sy, 6f * sx, 10f * sy, archWarm)
                        drawHorseshoeWindow(228f * sx, 250f * sy, 6f * sx, 10f * sy, archWarm)
                        drawHorseshoeWindow(240f * sx, 250f * sy, 6f * sx, 10f * sy, archWarm)

                        // Balcony Lanterns
                        drawRoundRect(
                            color = archLight,
                            topLeft = Offset(313f * sx, 137f * sy),
                            size = Size(4f * sx, 7f * sy),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                        drawRoundRect(
                            color = archGold,
                            topLeft = Offset(38f * sx, 176f * sy),
                            size = Size(4f * sx, 6f * sy),
                            cornerRadius = CornerRadius(2f, 2f)
                        )
                    }

                    // 1.12 Bottom Dark Gradient Vignette for Razor-sharp Text Legibility
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC090D16),
                                Color(0xF2090D16)
                            ),
                            startY = 220f * sy,
                            endY = h
                        ),
                        topLeft = Offset(0f, 220f * sy),
                        size = Size(w, h - 220f * sy)
                    )
                }

                // 2. Foreground Card UI Overlay (Interactive elements, texts, badges, buttons)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top Row: Status Badge Pill + Prayer Name & Arabic + Alarm Bell Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            // Live Pulsing Status Badge
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.Black.copy(alpha = 0.38f))
                                    .padding(horizontal = 9.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Color(0xFFFBBF24), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = theme.badgeText,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFFFEF08A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        letterSpacing = 0.8.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Prayer Name in Latin & Arabic Script side by side
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = effectivePrayerName,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = theme.arabicName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color(0xFFFDE047).copy(alpha = 0.95f),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                )
                            }
                        }

                        // Bell / Alarm Toggle Button (Interactive with Sound simulation toast)
                        IconButton(
                            onClick = {
                                onToggleAlarm()
                                showAdzanSimToast = true
                            },
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (alarmEnabled) Color(0x33FBBF24) else Color.Black.copy(alpha = 0.35f)
                                )
                        ) {
                            Icon(
                                imageVector = if (alarmEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                contentDescription = "Toggle Alarm",
                                tint = if (alarmEnabled) Color(0xFFFDE047) else Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    // Middle Section: Large Prayer Time & Live Countdown Pill
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = effectivePrayerTime,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 44.sp,
                                    letterSpacing = (-0.5).sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .padding(bottom = 8.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black.copy(alpha = 0.32f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "WIB",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFFFEF08A),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Countdown Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.42f))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = effectiveCountdownText,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.95f),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }

                    // Bottom Row: Sunnah Hint, Info & "Lihat Jadwal >" Button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = theme.sunnahHint,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.88f),
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }

                        // Button: Lihat Jadwal >
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .clickable { onNavigateToSchedule() }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Jadwal",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF1E3A8A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color(0xFF1E3A8A),
                                modifier = Modifier.size(9.dp)
                            )
                        }
                    }
                }

                // 3. Adzan Preview Soundwave Toast (Pops down smoothly when alarm toggled)
                androidx.compose.animation.AnimatedVisibility(
                    visible = showAdzanSimToast,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.TopCenter)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xE6090D16)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .background(Color(0x33FBBF24), RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = null,
                                        tint = Color(0xFFFDE047),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Adzan ${theme.name} Berkumandang",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Pengingat otomatis aktif tepat waktu",
                                        color = Color(0xFFFEF08A),
                                        fontSize = 9.sp
                                    )
                                }
                            }

                            IconButton(
                                onClick = { showAdzanSimToast = false },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Tutup",
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// HELPER CANVAS PRIMITIVES
// -----------------------------------------------------------------------------

private fun DrawScope.drawCrescent(
    cx: Float,
    cy: Float,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(cx, cy - radius)
        cubicTo(
            cx + radius * 1.15f, cy - radius * 0.55f,
            cx + radius * 1.15f, cy + radius * 0.55f,
            cx, cy + radius
        )
        cubicTo(
            cx + radius * 0.42f, cy + radius * 0.45f,
            cx + radius * 0.42f, cy - radius * 0.45f,
            cx, cy - radius
        )
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawHorseshoeWindow(
    x: Float,
    y: Float,
    w: Float,
    h: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(x, y + h)
        lineTo(x, y + w * 0.5f)
        quadraticTo(x + w * 0.5f, y - 2f, x + w, y + w * 0.5f)
        lineTo(x + w, y + h)
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawBird(
    x: Float,
    y: Float,
    scale: Float,
    color: Color
) {
    val wingR = 7f * scale
    val path = Path().apply {
        moveTo(x - wingR * 2, y + wingR * 0.4f)
        quadraticTo(x - wingR, y - wingR, x, y)
        quadraticTo(x + wingR, y - wingR, x + wingR * 2, y + wingR * 0.4f)
    }
    drawPath(path, color, style = Stroke(width = 1.6f * scale, cap = StrokeCap.Round))
}

private fun DrawScope.drawIslamicStar(
    cx: Float,
    cy: Float,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45.0) - 22.5)
            val r = if (i % 2 == 0) radius else radius * 0.44f
            val x = (cx + r * cos(angle)).toFloat()
            val y = (cy + r * sin(angle)).toFloat()
            if (i == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
    drawPath(path, color)
}
