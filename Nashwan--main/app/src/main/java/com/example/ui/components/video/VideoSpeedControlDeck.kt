package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SpeedCurvePreset
import com.example.data.model.VideoClip
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
fun VideoSpeedControlDeck(
    clips: List<VideoClip>,
    selectedClipIndex: Int,
    onSelectClip: (Int) -> Unit,
    onSetSpeed: (clipIndex: Int, speed: Float) -> Unit,
    onAdjustSpeedDelta: (clipIndex: Int, delta: Float) -> Unit,
    onApplySpeedToAll: (Float) -> Unit,
    onApplySpeedCurve: (clipIndex: Int, SpeedCurvePreset) -> Unit,
    onToggleKeepPitch: (clipIndex: Int) -> Unit,
    onResetSpeed: (clipIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeClip = clips.getOrNull(selectedClipIndex) ?: return
    val originalDurationSec = (activeClip.endTrimMs - activeClip.startTrimMs) / 1000f
    val speedAdjustedDurationSec = activeClip.trimmedDurationMs / 1000f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & Clip Selection Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.Speed, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                Text(
                    text = "التحكم في سرعة تشغيل المقاطع (Speed & Ramping):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Surface(
                color = StudioRose.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.clickable { onResetSpeed(selectedClipIndex) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = StudioRose, modifier = Modifier.size(11.dp))
                    Text("إعادة ضبط 1.0x", fontSize = 9.sp, color = StudioRose, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Clip Switcher Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(clips.indices.toList()) { index ->
                val clip = clips[index]
                val isSelected = index == selectedClipIndex
                Surface(
                    color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioDarkElevated,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .clickable { onSelectClip(index) }
                        .testTag("speed_clip_chip_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = clip.title,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary
                        )
                        Surface(
                            color = if (clip.speed != 1.0f) StudioAmber else StudioSurfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${String.format("%.1f", clip.speed)}x",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (clip.speed != 1.0f) StudioDark else StudioTextMuted,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }
        }

        // 2. Speed Value & Duration Calculation Card
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "سرعة ${activeClip.title}:",
                            fontSize = 10.sp,
                            color = StudioTextSecondary
                        )
                        Text(
                            text = "${String.format("%.2f", activeClip.speed)}x",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (activeClip.speed < 1.0f) StudioAmber else if (activeClip.speed > 1.0f) StudioCyan else StudioTextPrimary
                        )
                    }

                    // Stepper Buttons (-0.1x / +0.1x)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { onAdjustSpeedDelta(selectedClipIndex, -0.1f) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(StudioSurfaceVariant, RoundedCornerShape(8.dp))
                        ) {
                            Icon(imageVector = Icons.Default.Remove, contentDescription = "تقليل السرعة", tint = StudioTextPrimary, modifier = Modifier.size(16.dp))
                        }

                        IconButton(
                            onClick = { onAdjustSpeedDelta(selectedClipIndex, +0.1f) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(StudioSurfaceVariant, RoundedCornerShape(8.dp))
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "زيادة السرعة", tint = StudioCyan, modifier = Modifier.size(16.dp))
                        }
                    }

                    // Duration Comparison Badge
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "المدة الأصلية: ${String.format("%.1f", originalDurationSec)}ث",
                            fontSize = 9.sp,
                            color = StudioTextMuted
                        )
                        Text(
                            text = "المدة الجديدة: ${String.format("%.1f", speedAdjustedDurationSec)}ث",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioEmerald
                        )
                    }
                }

                // Continuous Precision Speed Slider (0.1x to 8.0x)
                Slider(
                    value = activeClip.speed,
                    onValueChange = { onSetSpeed(selectedClipIndex, it) },
                    valueRange = 0.1f..8.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth().height(24.dp)
                )
            }
        }

        // 3. Quick Speed Presets Bar
        val speedPresets = listOf(0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 2.0f, 3.0f, 5.0f, 8.0f)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(speedPresets) { speedVal ->
                val isSelected = (activeClip.speed - speedVal).let { it in -0.05f..0.05f }
                Surface(
                    color = if (isSelected) StudioCyan else StudioSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .clickable { onSetSpeed(selectedClipIndex, speedVal) }
                        .testTag("speed_preset_${speedVal}")
                ) {
                    Text(
                        text = "${speedVal}x",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) StudioDark else StudioTextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // 4. Kinetic Speed Curves / Ramps (منحنيات السرعة الحركية للمونتاج)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.Timeline, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(15.dp))
                Text("منحنيات التسارع الحركي (Speed Curves & Ramp):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SpeedCurvePreset.values()) { preset ->
                    val isSelected = activeClip.speedCurve == preset
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioViolet else StudioSurfaceBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .clickable { onApplySpeedCurve(selectedClipIndex, preset) }
                            .testTag("speed_curve_${preset.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(preset.iconEmoji, fontSize = 14.sp)
                                Text(
                                    text = preset.titleAr,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) StudioViolet else StudioTextPrimary
                                )
                            }

                            // Render miniature curve chart
                            SpeedMiniCurveChart(
                                points = preset.curvePoints,
                                isSelected = isSelected,
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(26.dp)
                            )
                        }
                    }
                }
            }
        }

        // 5. Advanced Pitch & Apply To All Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Keep pitch toggle
            Surface(
                color = StudioSurfaceVariant,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier
                    .weight(1.2f)
                    .clickable { onToggleKeepPitch(selectedClipIndex) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = StudioAmber, modifier = Modifier.size(14.dp))
                        Text("نبرة الصوت الطبيعية", fontSize = 9.sp, color = StudioTextPrimary)
                    }
                    Switch(
                        checked = activeClip.keepAudioPitch,
                        onCheckedChange = { onToggleKeepPitch(selectedClipIndex) },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = StudioAmber),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Apply To All Clips Button
            Button(
                onClick = { onApplySpeedToAll(activeClip.speed) },
                colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                modifier = Modifier.weight(1f).testTag("apply_speed_all_btn")
            ) {
                Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(13.dp))
                Spacer(modifier = Modifier.width(3.dp))
                Text("تطبيق للكل ✨", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SpeedMiniCurveChart(
    points: List<Float>,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(StudioDark)
    ) {
        if (points.isEmpty()) return@Canvas

        val maxVal = (points.maxOrNull() ?: 1f).coerceAtLeast(3f)
        val minVal = (points.minOrNull() ?: 0.2f).coerceAtMost(0.3f)
        val range = (maxVal - minVal).coerceAtLeast(1f)

        val path = Path()
        val stepX = size.width / (points.size - 1)

        points.forEachIndexed { i, p ->
            val x = i * stepX
            val normalizedY = 1f - ((p - minVal) / range).coerceIn(0.05f, 0.95f)
            val y = normalizedY * size.height

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        drawPath(
            path = path,
            color = if (isSelected) StudioViolet else StudioCyan,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
