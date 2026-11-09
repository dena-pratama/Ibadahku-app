package com.ibadahku.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ibadahku.app.ui.theme.AccentGold
import com.ibadahku.app.ui.theme.ForestGreen
import com.ibadahku.app.ui.theme.SageContainer
import com.ibadahku.app.ui.theme.TextDark
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CompassDialView(
    azimuthDegrees: Float,
    qiblaBearingDegrees: Float,
    isAligned: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedAzimuth by animateFloatAsState(
        targetValue = azimuthDegrees,
        animationSpec = tween(durationMillis = 150),
        label = "azimuth"
    )

    val dialBorderColor by animateColorAsState(
        targetValue = if (isAligned) Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
        animationSpec = tween(durationMillis = 300),
        label = "borderColor"
    )

    val glowColor by animateColorAsState(
        targetValue = if (isAligned) Color(0xFF2E7D32).copy(alpha = 0.15f) else Color.Transparent,
        animationSpec = tween(durationMillis = 300),
        label = "glowColor"
    )

    val tickMajorColor = MaterialTheme.colorScheme.onSurface
    val tickMinorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
    val cardinalColor = MaterialTheme.colorScheme.onSurface

    Box(
        modifier = modifier
            .size(280.dp)
            .shadow(
                elevation = if (isAligned) 16.dp else 6.dp,
                shape = CircleShape,
                ambientColor = if (isAligned) Color(0xFF2E7D32) else Color.Black.copy(alpha = 0.1f)
            )
            .background(MaterialTheme.colorScheme.surface, CircleShape)
            .background(glowColor, CircleShape)
            .border(width = if (isAligned) 3.5.dp else 2.dp, color = dialBorderColor, shape = CircleShape)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Rotating Compass Dial (ticks + cardinal directions)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(-animatedAzimuth),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f
                val cy = size.height / 2f
                val radius = (size.width / 2f) - 10f

                // Draw tick marks around the compass
                for (i in 0 until 60) {
                    val angleDeg = i * 6.0
                    val angleRad = Math.toRadians(angleDeg - 90.0)
                    val isMajor = (i % 15 == 0)
                    val isSemiMajor = (i % 5 == 0)

                    val tickLen = when {
                        isMajor -> 14f
                        isSemiMajor -> 8f
                        else -> 4f
                    }
                    val strokeW = if (isMajor) 2.5f else 1.2f
                    val tickColor = if (isMajor) tickMajorColor else tickMinorColor

                    val startX = (cx + (radius - tickLen) * cos(angleRad)).toFloat()
                    val startY = (cy + (radius - tickLen) * sin(angleRad)).toFloat()
                    val endX = (cx + radius * cos(angleRad)).toFloat()
                    val endY = (cy + radius * sin(angleRad)).toFloat()

                    drawLine(
                        color = tickColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = strokeW
                    )
                }

                // Qibla Pointer Arc / Direction line from center
                val qiblaAngleRad = Math.toRadians(qiblaBearingDegrees.toDouble() - 90.0)
                val qiblaX = (cx + (radius - 22f) * cos(qiblaAngleRad)).toFloat()
                val qiblaY = (cy + (radius - 22f) * sin(qiblaAngleRad)).toFloat()

                // Draw Kaaba target dot / triangle marker on rim
                drawCircle(
                    color = if (isAligned) Color(0xFF2E7D32) else ForestGreen,
                    radius = 6f,
                    center = Offset(qiblaX, qiblaY)
                )
            }

            // Cardinal Labels: U, T, S, B
            // Utara (North)
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 4.dp)
            ) {
                Text(
                    text = "U",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = if (isAligned) Color(0xFF2E7D32) else cardinalColor
                )
            }

            // Timur (East)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = "T",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = cardinalColor
                )
            }

            // Selatan (South)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "S",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = cardinalColor
                )
            }

            // Barat (West)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
            ) {
                Text(
                    text = "B",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = cardinalColor
                )
            }
        }

        // Fixed Top Compass Pointer Triangle (Indicator that phone is pointing at)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 2.dp)
        ) {
            Canvas(modifier = Modifier.size(16.dp, 14.dp)) {
                val triangle = Path().apply {
                    moveTo(size.width / 2f, size.height)
                    lineTo(0f, 0f)
                    lineTo(size.width, 0f)
                    close()
                }
                drawPath(
                    path = triangle,
                    color = if (isAligned) Color(0xFF2E7D32) else ForestGreen
                )
            }
        }

        // Center Ka'bah Icon
        // Rotates towards Qibla direction relative to current device orientation
        val kaabaRotation = (qiblaBearingDegrees - animatedAzimuth)
        Box(
            modifier = Modifier
                .size(68.dp)
                .rotate(kaabaRotation),
            contentAlignment = Alignment.Center
        ) {
            // Ka'bah 3D isometric representation
            KaabaIcon(
                modifier = Modifier.size(54.dp),
                isGlowing = isAligned
            )
        }
    }
}

@Composable
fun KaabaIcon(
    modifier: Modifier = Modifier,
    isGlowing: Boolean = false
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Ka'bah body (dark cubic box)
        val boxWidth = w * 0.72f
        val boxHeight = h * 0.72f
        val left = (w - boxWidth) / 2f
        val top = (h - boxHeight) / 2f

        // Draw shadow/depth
        drawRoundRect(
            color = Color(0xFF1A1A1A),
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
        )

        // Golden Kiswah band across top
        drawRect(
            color = AccentGold,
            topLeft = Offset(left, top + boxHeight * 0.22f),
            size = Size(boxWidth, boxHeight * 0.12f)
        )

        // Golden Door (Bab ar-Rahman)
        drawRoundRect(
            color = AccentGold,
            topLeft = Offset(left + boxWidth * 0.60f, top + boxHeight * 0.42f),
            size = Size(boxWidth * 0.24f, boxHeight * 0.44f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f)
        )

        // Subtle Roof Highlight
        drawRect(
            color = Color(0xFF2C2C2C),
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight * 0.08f)
        )
    }
}
