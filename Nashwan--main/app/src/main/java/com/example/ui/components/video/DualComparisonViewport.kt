package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComparisonMode
import com.example.data.model.DualComparisonSettings
import com.example.data.model.FilterPreset
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioCyanLight
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioViolet

@Composable
fun DualComparisonViewport(
    sampleDrawableRes: Int,
    filterPreset: FilterPreset,
    filterIntensity: Float,
    isAiAutoEnhanced: Boolean,
    aiSharpenLevel: Float,
    settings: DualComparisonSettings,
    onUpdateSplitPosition: (Float) -> Unit,
    onSetMode: (ComparisonMode) -> Unit,
    onSetComparingRaw: (Boolean) -> Unit,
    onCloseComparison: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Construct ColorMatrix for the "After" (Edited) image
    val editedColorMatrix = remember(filterPreset, filterIntensity, isAiAutoEnhanced) {
        val baseMatrix = filterPreset.matrix
        // Apply intensity interpolation with identity matrix
        val interpolated = FloatArray(20) { index ->
            val identityVal = if (index % 6 == 0) 1f else 0f
            val targetVal = if (index < baseMatrix.size) baseMatrix[index] else identityVal
            identityVal + (targetVal - identityVal) * filterIntensity
        }
        if (isAiAutoEnhanced) {
            // Slight boost in contrast and dynamic warmth for AI enhancement
            interpolated[4] += 8f
            interpolated[9] += 6f
            interpolated[14] += 12f
        }
        ColorMatrix(interpolated)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .background(StudioDarkElevated)
            .border(1.5.dp, StudioCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("dual_comparison_viewport")
    ) {
        when (settings.mode) {
            ComparisonMode.SPLIT_SLIDER -> {
                WipeSplitSliderView(
                    sampleDrawableRes = sampleDrawableRes,
                    editedColorMatrix = editedColorMatrix,
                    splitPosition = settings.splitPosition,
                    filterName = filterPreset.nameAr,
                    onUpdateSplit = onUpdateSplitPosition
                )
            }

            ComparisonMode.SIDE_BY_SIDE -> {
                SideBySideDualView(
                    sampleDrawableRes = sampleDrawableRes,
                    editedColorMatrix = editedColorMatrix,
                    filterName = filterPreset.nameAr
                )
            }

            ComparisonMode.HORIZONTAL_SPLIT -> {
                HorizontalSplitView(
                    sampleDrawableRes = sampleDrawableRes,
                    editedColorMatrix = editedColorMatrix,
                    splitPosition = settings.splitPosition,
                    filterName = filterPreset.nameAr,
                    onUpdateSplit = onUpdateSplitPosition
                )
            }

            ComparisonMode.HOLD_TO_PREVIEW -> {
                HoldToCompareView(
                    sampleDrawableRes = sampleDrawableRes,
                    editedColorMatrix = editedColorMatrix,
                    isComparingRaw = settings.isComparingOriginalRaw,
                    filterName = filterPreset.nameAr,
                    onSetComparingRaw = onSetComparingRaw
                )
            }
        }

        // Top Controls: Mode Switcher & Exit Button
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Comparison Mode Switcher Chips
            Row(
                modifier = Modifier
                    .background(StudioDark.copy(alpha = 0.85f), RoundedCornerShape(20.dp))
                    .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ComparisonMode.values().forEach { mode ->
                    val isSelected = settings.mode == mode
                    Surface(
                        color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else Color.Transparent,
                        shape = RoundedCornerShape(14.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, StudioCyan) else null,
                        modifier = Modifier
                            .clickable { onSetMode(mode) }
                            .padding(horizontal = 6.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "${mode.iconEmoji} ${mode.titleAr.substringBefore(" ")}",
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextMuted
                        )
                    }
                }
            }

            // Close / Exit Comparison Button
            IconButton(
                onClick = onCloseComparison,
                modifier = Modifier
                    .size(28.dp)
                    .background(StudioDark.copy(alpha = 0.85f), CircleShape)
                    .border(1.dp, StudioRose.copy(alpha = 0.5f), CircleShape)
                    .testTag("exit_comparison_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "إغلاق وضع المقارنة",
                    tint = StudioRose,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Bottom Parameter Diff Bar
        if (settings.showParameterDiffBadge) {
            Surface(
                color = StudioDark.copy(alpha = 0.88f),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp, start = 12.dp, end = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "⚖️ الفلتر: ${filterPreset.nameAr} (${(filterIntensity * 100).toInt()}%)",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )
                    Text(text = "•", fontSize = 9.sp, color = StudioTextMuted)
                    Text(
                        text = if (isAiAutoEnhanced) "تحسين AI: مفعل ✨" else "الحدة: ${(aiSharpenLevel * 100).toInt()}%",
                        fontSize = 9.sp,
                        color = if (isAiAutoEnhanced) StudioEmerald else StudioTextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun WipeSplitSliderView(
    sampleDrawableRes: Int,
    editedColorMatrix: ColorMatrix,
    splitPosition: Float,
    filterName: String,
    onUpdateSplit: (Float) -> Unit
) {
    var localSplit by remember(splitPosition) { mutableFloatStateOf(splitPosition) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val newSplit = (localSplit + dragAmount.x / size.width).coerceIn(0.05f, 0.95f)
                    localSplit = newSplit
                    onUpdateSplit(newSplit)
                }
            }
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val splitX = widthPx * localSplit

        // Layer 1: Original Raw Frame (Neutral, Left Side)
        Image(
            painter = painterResource(id = sampleDrawableRes),
            contentDescription = "اللقطة الأصلية قبل التعديل",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Layer 2: Edited Graded Frame (Right Side) clipped using Path
        Image(
            painter = painterResource(id = sampleDrawableRes),
            contentDescription = "اللقطة بعد التعديل والفلاتر",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val clipPath = Path().apply {
                        addRect(Rect(left = splitX, top = 0f, right = size.width, bottom = size.height))
                    }
                    clipPath(clipPath) {
                        this@drawWithContent.drawContent()
                    }
                },
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(editedColorMatrix)
        )

        // Floating Tags: "قبل" on Left, "بعد" on Right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 44.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                color = StudioDark.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber.copy(alpha = 0.6f))
            ) {
                Text(
                    text = "◀ قبل (الأصل)",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioAmber,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Surface(
                color = StudioDark.copy(alpha = 0.85f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan)
            ) {
                Text(
                    text = "بعد (${filterName}) ▶",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioCyan,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // Draggable Glowing Divider Line & Handle
        val splitDp = (maxWidth * localSplit)
        Box(
            modifier = Modifier
                .offset { IntOffset(splitX.toInt() - 16, 0) }
                .fillMaxHeight()
                .width(32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Vertical Line
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(2.5.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(StudioCyan, Color.White, StudioViolet)
                        )
                    )
            )

            // Center Circular Drag Handle Pill
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(StudioDarkElevated, CircleShape)
                    .border(2.dp, StudioCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CompareArrows,
                    contentDescription = "سحب شريط المقارنة",
                    tint = StudioCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SideBySideDualView(
    sampleDrawableRes: Int,
    editedColorMatrix: ColorMatrix,
    filterName: String
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 40.dp, bottom = 32.dp, start = 6.dp, end = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Left Side: Original Raw
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, StudioAmber.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = sampleDrawableRes),
                contentDescription = "الأصل قبل التعديل",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Surface(
                color = StudioDark.copy(alpha = 0.85f),
                shape = RoundedCornerShape(bottomEnd = 8.dp),
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Text(
                    text = "الأصلي (قبل) 📷",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioAmber,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        // Right Side: Edited & Graded
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(1.5.dp, StudioCyan, RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = sampleDrawableRes),
                contentDescription = "بعد التعديل والفلاتر",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(editedColorMatrix)
            )
            Surface(
                color = StudioDark.copy(alpha = 0.85f),
                shape = RoundedCornerShape(bottomStart = 8.dp),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = "المعدل: $filterName ✨",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioCyan,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun HorizontalSplitView(
    sampleDrawableRes: Int,
    editedColorMatrix: ColorMatrix,
    splitPosition: Float,
    filterName: String,
    onUpdateSplit: (Float) -> Unit
) {
    var localSplit by remember(splitPosition) { mutableFloatStateOf(splitPosition) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val newSplit = (localSplit + dragAmount.y / size.height).coerceIn(0.1f, 0.9f)
                    localSplit = newSplit
                    onUpdateSplit(newSplit)
                }
            }
    ) {
        val heightPx = constraints.maxHeight.toFloat()
        val splitY = heightPx * localSplit

        // Layer 1: Original Raw Frame (Top Half)
        Image(
            painter = painterResource(id = sampleDrawableRes),
            contentDescription = "اللقطة الأصلية",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Layer 2: Edited Graded Frame (Bottom Half)
        Image(
            painter = painterResource(id = sampleDrawableRes),
            contentDescription = "اللقطة المعدلة",
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val clipPath = Path().apply {
                        addRect(Rect(left = 0f, top = splitY, right = size.width, bottom = size.height))
                    }
                    clipPath(clipPath) {
                        this@drawWithContent.drawContent()
                    }
                },
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.colorMatrix(editedColorMatrix)
        )

        // Floating Tags: Top = Before, Bottom = After
        Surface(
            color = StudioDark.copy(alpha = 0.85f),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 44.dp, start = 12.dp)
        ) {
            Text(
                text = "الأعلى: قبل (الخام)",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = StudioAmber,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Surface(
            color = StudioDark.copy(alpha = 0.85f),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 36.dp, end = 12.dp)
        ) {
            Text(
                text = "الأسفل: بعد ($filterName)",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = StudioCyan,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        // Horizontal Line & Handle
        Box(
            modifier = Modifier
                .offset { IntOffset(0, splitY.toInt() - 16) }
                .fillMaxWidth()
                .height(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.5.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(StudioAmber, Color.White, StudioCyan)
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(StudioDarkElevated, CircleShape)
                    .border(2.dp, StudioCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CompareArrows,
                    contentDescription = "سحب أفقي",
                    tint = StudioCyan,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun HoldToCompareView(
    sampleDrawableRes: Int,
    editedColorMatrix: ColorMatrix,
    isComparingRaw: Boolean,
    filterName: String,
    onSetComparingRaw: (Boolean) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val showRaw = isComparingRaw || isPressed

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onSetComparingRaw(true)
                        tryAwaitRelease()
                        isPressed = false
                        onSetComparingRaw(false)
                    }
                )
            }
    ) {
        Image(
            painter = painterResource(id = sampleDrawableRes),
            contentDescription = if (showRaw) "الأصل قبل التعديل" else "بعد التعديل",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            colorFilter = if (showRaw) null else ColorFilter.colorMatrix(editedColorMatrix)
        )

        // Center Instruction Badge
        Surface(
            color = if (showRaw) StudioAmber.copy(alpha = 0.9f) else StudioDark.copy(alpha = 0.85f),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (showRaw) StudioAmber else StudioCyan),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = if (showRaw) Color.Black else StudioCyan,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = if (showRaw) "👁️ جاري عرض الفيديو الأصلي الخام (قبل)" else "👆 اضغط مع الاستمرار هنا لمعاينة (قبل)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (showRaw) Color.Black else StudioTextPrimary
                )
            }
        }
    }
}
