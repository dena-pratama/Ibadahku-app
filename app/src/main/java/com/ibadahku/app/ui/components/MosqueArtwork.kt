package com.ibadahku.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

/**
 * Premium architectural Islamic mosque vector artwork designed specifically for
 * prayer cards, headers, and hero banners. Provides layered atmospheric depth,
 * multi-tier onion domes, minarets with balconies, crescent finials, glowing stars,
 * and illuminated horseshoe arches.
 */
@Composable
fun HeroMosqueCardArtwork(
    modifier: Modifier = Modifier,
    isDark: Boolean = true
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Colors tailored for dark vs light card themes
        val skyStarColor = if (isDark) Color(0xFFF9E79F) else Color(0xFF3B6B52)
        val moonGlowColor = if (isDark) Color(0x33FAD7A0) else Color(0x222D5A43)
        val moonCrescentColor = if (isDark) Color(0xFFFDEBD0) else Color(0xFF2D5A43)

        val farLayerColor = if (isDark) Color(0x403D7255) else Color(0x253B6B52)
        val midLayerColor = if (isDark) Color(0x80528B6A) else Color(0x452D5A43)
        val nearLayerColor = if (isDark) Color(0xD970A887) else Color(0x701E4430)
        val windowGlowColor = if (isDark) Color(0xCCFDEBD0) else Color(0x40FFFFFF)

        // -------------------------------------------------------------
        // 1. SKY ELEMENTS: Golden Crescent Moon & Twinkling Islamic Stars
        // -------------------------------------------------------------
        val moonCx = w * 0.88f
        val moonCy = h * 0.22f
        val moonRadius = h * 0.12f

        // Ambient halo glow behind crescent moon
        drawCircle(
            color = moonGlowColor,
            radius = moonRadius * 1.8f,
            center = Offset(moonCx, moonCy)
        )

        // Golden Crescent Moon
        drawCrescentMoon(
            cx = moonCx,
            cy = moonCy,
            radius = moonRadius,
            color = moonCrescentColor
        )

        // Islamic geometric 8-point & 4-point radiant stars
        drawIslamicStar(cx = w * 0.58f, cy = h * 0.18f, radius = 5.5f, color = skyStarColor.copy(alpha = 0.85f))
        drawIslamicStar(cx = w * 0.74f, cy = h * 0.12f, radius = 4.2f, color = skyStarColor.copy(alpha = 0.75f))
        drawIslamicStar(cx = w * 0.96f, cy = h * 0.32f, radius = 3.8f, color = skyStarColor.copy(alpha = 0.70f))
        drawIslamicStar(cx = w * 0.67f, cy = h * 0.28f, radius = 3.2f, color = skyStarColor.copy(alpha = 0.60f))

        // -------------------------------------------------------------
        // 2. LAYER 1 (Farthest): Soft Desert Dunes & Distant Skyline
        // -------------------------------------------------------------
        val dunePath = Path().apply {
            moveTo(w * 0.38f, h)
            cubicTo(
                w * 0.52f, h * 0.72f,
                w * 0.75f, h * 0.85f,
                w, h * 0.68f
            )
            lineTo(w, h)
            close()
        }
        drawPath(dunePath, farLayerColor)

        // Distant small minaret on the far hill
        val farMinaretX = w * 0.52f
        val farMinaretW = 7f
        val farMinaretH = h * 0.38f
        drawRect(
            color = farLayerColor,
            topLeft = Offset(farMinaretX, h * 0.82f - farMinaretH),
            size = Size(farMinaretW, farMinaretH)
        )
        drawSpire(
            cx = farMinaretX + farMinaretW / 2f,
            baseY = h * 0.82f - farMinaretH,
            width = 11f,
            height = 18f,
            color = farLayerColor
        )

        // Distant soft dome
        drawOnionDome(
            cx = w * 0.60f,
            baseY = h * 0.84f,
            radius = 16f,
            color = farLayerColor,
            hasCrescent = false
        )

        // -------------------------------------------------------------
        // 3. LAYER 2 (Midground): Mosque Secondary Massing & Back Domes
        // -------------------------------------------------------------
        val midBaseY = h * 0.92f
        val midMosqueX = w * 0.66f

        // Back dome left
        drawOnionDome(
            cx = midMosqueX - 28f,
            baseY = midBaseY - h * 0.18f,
            radius = 22f,
            color = midLayerColor,
            hasCrescent = false
        )

        // Back dome right
        drawOnionDome(
            cx = midMosqueX + 46f,
            baseY = midBaseY - h * 0.18f,
            radius = 20f,
            color = midLayerColor,
            hasCrescent = false
        )

        // -------------------------------------------------------------
        // 4. LAYER 3 (Foreground): Grand Ottoman Mosque Architecture
        // -------------------------------------------------------------
        val mosqueCx = w * 0.74f
        val mosqueBaseY = h * 0.96f
        val mainWallWidth = w * 0.34f
        val mainWallHeight = h * 0.32f
        val mainWallLeft = mosqueCx - mainWallWidth / 2f

        // Main Sanctuary Wall
        drawRoundRect(
            color = nearLayerColor,
            topLeft = Offset(mainWallLeft, mosqueBaseY - mainWallHeight),
            size = Size(mainWallWidth, mainWallHeight),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // Decorative Cornice / Molding
        drawRect(
            color = nearLayerColor,
            topLeft = Offset(mainWallLeft - 4f, mosqueBaseY - mainWallHeight - 3f),
            size = Size(mainWallWidth + 8f, 4f)
        )

        // Grand Central Onion Dome (Large & Beautifully Contoured)
        val mainDomeRadius = mainWallWidth * 0.40f
        drawOnionDome(
            cx = mosqueCx,
            baseY = mosqueBaseY - mainWallHeight - 3f,
            radius = mainDomeRadius,
            color = nearLayerColor,
            hasCrescent = true,
            crescentColor = skyStarColor
        )

        // Flanking Front Domes (Symmetrical Left & Right)
        val flankRadius = mainDomeRadius * 0.58f
        drawOnionDome(
            cx = mosqueCx - mainDomeRadius * 0.85f,
            baseY = mosqueBaseY - mainWallHeight - 1f,
            radius = flankRadius,
            color = nearLayerColor,
            hasCrescent = false
        )
        drawOnionDome(
            cx = mosqueCx + mainDomeRadius * 0.85f,
            baseY = mosqueBaseY - mainWallHeight - 1f,
            radius = flankRadius,
            color = nearLayerColor,
            hasCrescent = false
        )

        // Illuminated Horseshoe Arch Colonnade & Portals
        val archCount = 3
        val totalArchSpan = mainWallWidth * 0.75f
        val archWidth = totalArchSpan / archCount * 0.65f
        val archSpacing = totalArchSpan / (archCount - 1)
        val archStartX = mosqueCx - totalArchSpan / 2f + (totalArchSpan / archCount - archWidth) / 2f
        val archH = mainWallHeight * 0.62f

        for (i in 0 until archCount) {
            val ax = archStartX + i * archSpacing
            drawHorseshoeArch(
                left = ax,
                bottom = mosqueBaseY,
                width = archWidth,
                height = if (i == 1) archH * 1.15f else archH,
                color = windowGlowColor
            )
        }

        // -------------------------------------------------------------
        // 5. MINARETS: Left & Right Towers with Double Balconies & Spires
        // -------------------------------------------------------------
        val minaretW = 11f
        val leftMinX = mainWallLeft - minaretW * 1.3f
        val rightMinX = mainWallLeft + mainWallWidth + minaretW * 0.3f

        // Tall Left Minaret (Iconic height)
        drawArchitecturalMinaret(
            x = leftMinX,
            baseY = mosqueBaseY,
            width = minaretW,
            height = h * 0.68f,
            color = nearLayerColor,
            crescentColor = skyStarColor
        )

        // Tall Right Minaret
        drawArchitecturalMinaret(
            x = rightMinX,
            baseY = mosqueBaseY,
            width = minaretW,
            height = h * 0.62f,
            color = nearLayerColor,
            crescentColor = skyStarColor
        )

        // Foreground landscape foundation dune (grounds the building)
        val groundHill = Path().apply {
            moveTo(w * 0.45f, h)
            cubicTo(
                w * 0.62f, h * 0.90f,
                w * 0.82f, h * 0.94f,
                w, h * 0.88f
            )
            lineTo(w, h)
            close()
        }
        drawPath(groundHill, nearLayerColor.copy(alpha = nearLayerColor.alpha * 0.85f))
    }
}

/**
 * Centered Grand Mosque artwork for Splash Screen
 */
@Composable
fun GrandSplashMosque(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val domeGreenDark = Color(0xFF1B4332)
        val domeGreenMid = Color(0xFF2D6A4F)
        val wallCream = Color(0xFFE8F0EA)
        val goldAccent = Color(0xFFD4AF37)

        val cx = w / 2f
        val baseY = h * 0.78f
        val complexW = w * 0.76f

        // Base wall
        val wallW = complexW * 0.75f
        val wallH = h * 0.28f
        val wallLeft = cx - wallW / 2f

        drawRoundRect(
            color = wallCream,
            topLeft = Offset(wallLeft, baseY - wallH),
            size = Size(wallW, wallH),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // Central Grand Dome
        val mainDomeRadius = wallW * 0.28f
        drawOnionDome(
            cx = cx,
            baseY = baseY - wallH,
            radius = mainDomeRadius,
            color = domeGreenDark,
            hasCrescent = true,
            crescentColor = goldAccent
        )

        // Twin Side Domes
        val sideDomeRadius = mainDomeRadius * 0.68f
        drawOnionDome(
            cx = cx - mainDomeRadius * 0.95f,
            baseY = baseY - wallH,
            radius = sideDomeRadius,
            color = domeGreenMid,
            hasCrescent = false
        )
        drawOnionDome(
            cx = cx + mainDomeRadius * 0.95f,
            baseY = baseY - wallH,
            radius = sideDomeRadius,
            color = domeGreenMid,
            hasCrescent = false
        )

        // Arched Portals
        val archW = wallW * 0.16f
        val archH = wallH * 0.55f
        drawHorseshoeArch(
            left = cx - archW / 2f,
            bottom = baseY,
            width = archW,
            height = archH * 1.2f,
            color = domeGreenDark
        )
        drawHorseshoeArch(
            left = cx - archW * 1.8f,
            bottom = baseY,
            width = archW * 0.8f,
            height = archH,
            color = domeGreenMid
        )
        drawHorseshoeArch(
            left = cx + archW * 1.0f,
            bottom = baseY,
            width = archW * 0.8f,
            height = archH,
            color = domeGreenMid
        )

        // Minarets
        val minW = 16f
        drawArchitecturalMinaret(
            x = wallLeft - minW * 1.2f,
            baseY = baseY,
            width = minW,
            height = h * 0.60f,
            color = wallCream,
            crescentColor = goldAccent
        )
        drawArchitecturalMinaret(
            x = wallLeft + wallW + minW * 0.2f,
            baseY = baseY,
            width = minW,
            height = h * 0.60f,
            color = wallCream,
            crescentColor = goldAccent
        )
    }
}

/**
 * Backward-compatible alias for existing MosqueSilhouette usages
 */
@Composable
fun MosqueSilhouette(
    modifier: Modifier = Modifier,
    tint: Color = Color(0x33FFFFFF),
    accentTint: Color = Color(0x55FFFFFF)
) {
    HeroMosqueCardArtwork(
        modifier = modifier,
        isDark = tint.alpha < 0.5f && (tint.red > 0.5f || tint == Color(0x22FFFFFF))
    )
}

// -----------------------------------------------------------------------------
// HELPER CANVAS DRAWING FUNCTIONS
// -----------------------------------------------------------------------------

private fun DrawScope.drawCrescentMoon(
    cx: Float,
    cy: Float,
    radius: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(cx, cy - radius)
        // Outer arc
        cubicTo(
            cx + radius * 1.15f, cy - radius * 0.55f,
            cx + radius * 1.15f, cy + radius * 0.55f,
            cx, cy + radius
        )
        // Inner carved crescent arc
        cubicTo(
            cx + radius * 0.42f, cy + radius * 0.45f,
            cx + radius * 0.42f, cy - radius * 0.45f,
            cx, cy - radius
        )
        close()
    }
    drawPath(path, color)
}

private fun DrawScope.drawOnionDome(
    cx: Float,
    baseY: Float,
    radius: Float,
    color: Color,
    hasCrescent: Boolean = false,
    crescentColor: Color = Color.White
) {
    val peakHeight = radius * 1.52f
    val topPeakY = baseY - peakHeight

    val domePath = Path().apply {
        moveTo(cx - radius, baseY)
        // Beautiful pointed ogival onion silhouette curve
        cubicTo(
            cx - radius * 1.18f, baseY - radius * 0.52f,
            cx - radius * 0.45f, topPeakY + radius * 0.40f,
            cx, topPeakY
        )
        cubicTo(
            cx + radius * 0.45f, topPeakY + radius * 0.40f,
            cx + radius * 1.18f, baseY - radius * 0.52f,
            cx + radius, baseY
        )
        close()
    }
    drawPath(domePath, color)

    // Golden Crescent finial on the pinnacle tip
    if (hasCrescent) {
        val spireLen = radius * 0.38f
        drawLine(
            color = crescentColor,
            start = Offset(cx, topPeakY),
            end = Offset(cx, topPeakY - spireLen),
            strokeWidth = 2.2f
        )
        // Crescent ring on top
        drawCircle(
            color = crescentColor,
            radius = radius * 0.12f,
            center = Offset(cx, topPeakY - spireLen - radius * 0.10f)
        )
    }
}

private fun DrawScope.drawArchitecturalMinaret(
    x: Float,
    baseY: Float,
    width: Float,
    height: Float,
    color: Color,
    crescentColor: Color = Color.White
) {
    val topShaftY = baseY - height
    val cx = x + width / 2f
    val overhang = width * 0.45f

    // Main column shaft (subtly tapered)
    val shaftPath = Path().apply {
        moveTo(x, baseY)
        lineTo(x + width * 0.08f, topShaftY)
        lineTo(x + width * 0.92f, topShaftY)
        lineTo(x + width, baseY)
        close()
    }
    drawPath(shaftPath, color)

    // Lower Balcony (Sherefe 1) at ~60% height
    val b1Y = baseY - height * 0.58f
    drawRoundRect(
        color = color,
        topLeft = Offset(x - overhang, b1Y),
        size = Size(width + overhang * 2f, 5.5f),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Upper Balcony (Sherefe 2) at top shaft
    drawRoundRect(
        color = color,
        topLeft = Offset(x - overhang, topShaftY),
        size = Size(width + overhang * 2f, 6f),
        cornerRadius = CornerRadius(2f, 2f)
    )

    // Conical pointed roof spire
    val spireHeight = width * 2.4f
    drawSpire(
        cx = cx,
        baseY = topShaftY,
        width = width * 1.25f,
        height = spireHeight,
        color = color
    )

    // Finial crescent on minaret spire
    drawLine(
        color = crescentColor,
        start = Offset(cx, topShaftY - spireHeight),
        end = Offset(cx, topShaftY - spireHeight - 6f),
        strokeWidth = 1.8f
    )
    drawCircle(
        color = crescentColor,
        radius = 2.5f,
        center = Offset(cx, topShaftY - spireHeight - 7f)
    )
}

private fun DrawScope.drawSpire(
    cx: Float,
    baseY: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val spirePath = Path().apply {
        moveTo(cx - width / 2f, baseY)
        lineTo(cx, baseY - height)
        lineTo(cx + width / 2f, baseY)
        close()
    }
    drawPath(spirePath, color)
}

private fun DrawScope.drawHorseshoeArch(
    left: Float,
    bottom: Float,
    width: Float,
    height: Float,
    color: Color
) {
    val archR = width / 2f
    val archPath = Path().apply {
        moveTo(left, bottom)
        lineTo(left, bottom - height + archR)
        cubicTo(
            left - 1.2f, bottom - height - 1.5f,
            left + width + 1.2f, bottom - height - 1.5f,
            left + width, bottom - height + archR
        )
        lineTo(left + width, bottom)
        close()
    }
    drawPath(archPath, color)
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
