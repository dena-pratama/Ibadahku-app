package com.ibadahku.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.ui.components.MosqueSilhouette
import com.ibadahku.app.ui.theme.AccentGold
import com.ibadahku.app.ui.theme.CreamBackground
import com.ibadahku.app.ui.theme.DarkForestGreen
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import com.ibadahku.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(1500)
        onSplashFinished()
    }

    val rotationAnim = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        rotationAnim.animateTo(
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CreamBackground,
                        Color(0xFFF2F6F3),
                        Color(0xFFE2EBE4)
                    )
                )
            )
            .clickable { onSplashFinished() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Crescent Moon & Mosque Graphic
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {
                // Crescent Moon (Golden)
                Canvas(modifier = Modifier.size(54.dp).align(Alignment.TopCenter)) {
                    val r = size.minDimension / 2f
                    val moonPath = Path().apply {
                        val cx = center.x
                        val cy = center.y
                        moveTo(cx, cy - r)
                        cubicTo(cx + r * 1.1f, cy - r * 0.5f, cx + r * 1.1f, cy + r * 0.5f, cx, cy + r)
                        cubicTo(cx + r * 0.4f, cy + r * 0.4f, cx + r * 0.4f, cy - r * 0.4f, cx, cy - r)
                        close()
                    }
                    drawPath(moonPath, color = AccentGold)
                }

                // Grand Mosque Graphic
                com.ibadahku.app.ui.components.GrandSplashMosque(
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.BottomCenter)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Ibadahku",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    letterSpacing = 0.5.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Dekat dengan Allah,\nSetiap Hari",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    color = TextSecondary,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(72.dp))

            // Subtle Loading Spinner
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = ForestGreen.copy(alpha = 0.7f),
                strokeWidth = 2.5.dp
            )
        }
    }
}
