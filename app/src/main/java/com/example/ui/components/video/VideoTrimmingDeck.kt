package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTrimmingDeck(
    clips: List<VideoClip>,
    selectedClipIndex: Int,
    currentTimeMs: Long,
    onSelectClip: (Int) -> Unit,
    onTrimChange: (clipIndex: Int, startMs: Long, endMs: Long) -> Unit,
    onAdjustStartTrim: (clipIndex: Int, deltaMs: Long) -> Unit,
    onAdjustEndTrim: (clipIndex: Int, deltaMs: Long) -> Unit,
    onResetTrim: (clipIndex: Int) -> Unit,
    onApplyPreset: (clipIndex: Int, preset: String) -> Unit,
    onSplitClip: () -> Unit,
    onPreviewTrimmed: (clipIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val activeClip = clips.getOrNull(selectedClipIndex) ?: return

    var activeTab by remember { mutableStateOf(0) } // 0: ضبط بصري ومقابض, 1: ضبط دقيق بالثواني

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioSurface)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Clips Carousel Header (تبديل المقطع الحالي)
        if (clips.size > 1) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(clips.indices.toList()) { index ->
                    val clip = clips[index]
                    val isSelected = index == selectedClipIndex
                    Surface(
                        color = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { onSelectClip(index) }
                            .testTag("trim_select_clip_$index")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(Color(clip.colorHex), CircleShape)
                            )
                            Text(
                                text = clip.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StudioCyan else StudioTextPrimary
                            )
                            Text(
                                text = "${formatSeconds(clip.trimmedDurationMs)}ث",
                                fontSize = 10.sp,
                                color = StudioTextMuted
                            )
                        }
                    }
                }
            }
        }

        // 2. Main Clip Overview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(StudioCyan.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "مقطع ${selectedClipIndex + 1}/${clips.size}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan
                            )
                        }
                        Text(
                            text = activeClip.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Preview Trimmed segment
                        Surface(
                            color = StudioEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmerald.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { onPreviewTrimmed(selectedClipIndex) }
                                .testTag("preview_trim_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "معاينة",
                                    tint = StudioEmerald,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "معاينة القص",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioEmerald
                                )
                            }
                        }

                        // Split button
                        Surface(
                            color = StudioMagenta.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioMagenta.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { onSplitClip() }
                                .testTag("split_clip_trim_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.CallSplit,
                                    contentDescription = "تقسيم",
                                    tint = StudioMagenta,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "تقسيم ✂️",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioMagenta
                                )
                            }
                        }
                    }
                }

                // 3. Filmstrip Visual Trimmer Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioSurface)
                        .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(8.dp))
                ) {
                    // Simulated Filmstrip Frames Background
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val frameCount = 7
                        for (i in 0 until frameCount) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .border(0.5.dp, StudioSurfaceBorder.copy(alpha = 0.3f))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(activeClip.colorHex).copy(alpha = 0.15f + (i % 2) * 0.08f),
                                                StudioDark.copy(alpha = 0.6f)
                                            )
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎬 ${i + 1}",
                                    fontSize = 9.sp,
                                    color = StudioTextMuted.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }

                    // Trimmed Out Left Area Overlay
                    val leftRatio = (activeClip.startTrimMs.toFloat() / activeClip.durationMs.toFloat()).coerceIn(0f, 1f)
                    val rightRatio = (activeClip.endTrimMs.toFloat() / activeClip.durationMs.toFloat()).coerceIn(0f, 1f)

                    if (leftRatio > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(leftRatio)
                                .align(Alignment.CenterStart)
                                .background(Color.Black.copy(alpha = 0.65f))
                        )
                    }

                    // Active Selected Segment Highlight Glow
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(rightRatio - leftRatio)
                            .align(Alignment.CenterStart)
                            .padding(start = (leftRatio * 300).dp.coerceAtLeast(0.dp)) // subtle placement
                            .border(2.dp, StudioAmber, RoundedCornerShape(4.dp))
                    )

                    // Interactive Dual-Thumb Range Slider Overlay
                    RangeSlider(
                        value = activeClip.startTrimMs.toFloat()..activeClip.endTrimMs.toFloat(),
                        onValueChange = { range ->
                            onTrimChange(
                                selectedClipIndex,
                                range.start.toLong(),
                                range.endInclusive.toLong()
                            )
                        },
                        valueRange = 0f..activeClip.durationMs.toFloat(),
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioCyan,
                            inactiveTrackColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .testTag("trim_range_slider")
                    )
                }

                // Time Duration Info Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "البداية: ${formatTimeExact(activeClip.startTrimMs)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = StudioAmber
                    )

                    Surface(
                        color = StudioSurfaceVariant,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "المدة الصافية: ${formatTimeExact(activeClip.trimmedDurationMs)} (الأصلي: ${formatSeconds(activeClip.durationMs)}ث)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioCyanLight,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "النهاية: ${formatTimeExact(activeClip.endTrimMs)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = StudioAmber
                    )
                }
            }
        }

        // 4. Precision Control Mode Switcher (ضبط دقيق أو قوالب سريعة)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = if (activeTab == 0) StudioCyan.copy(alpha = 0.2f) else StudioDarkElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (activeTab == 0) StudioCyan else StudioSurfaceBorder
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { activeTab = 0 }
                    .testTag("trim_tab_precision")
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "ضبط دقيق",
                        tint = if (activeTab == 0) StudioCyan else StudioTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "الضبط الدقيق (أجزاء الثانية)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 0) StudioCyan else StudioTextSecondary
                    )
                }
            }

            Surface(
                color = if (activeTab == 1) StudioCyan.copy(alpha = 0.2f) else StudioDarkElevated,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (activeTab == 1) StudioCyan else StudioSurfaceBorder
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { activeTab = 1 }
                    .testTag("trim_tab_presets")
            ) {
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = "قوالب سريعة",
                        tint = if (activeTab == 1) StudioCyan else StudioTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "قص سريع جاهز",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (activeTab == 1) StudioCyan else StudioTextSecondary
                    )
                }
            }
        }

        // 5. Tab Content: Precision Nudge Buttons vs Quick Presets
        if (activeTab == 0) {
            // Fine-tuning Dual Columns (Start Point & End Point)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start Point Card (نقطة البداية)
                TrimNudgeColumn(
                    title = "نقطة البداية (IN)",
                    currentTimeFormatted = formatTimeExact(activeClip.startTrimMs),
                    onNudge = { delta -> onAdjustStartTrim(selectedClipIndex, delta) },
                    onSetAtCurrentPlayhead = {
                        onTrimChange(
                            selectedClipIndex,
                            currentTimeMs.coerceIn(0L, activeClip.endTrimMs - 200L),
                            activeClip.endTrimMs
                        )
                    },
                    colorAccent = StudioAmber,
                    modifier = Modifier.weight(1f)
                )

                // End Point Card (نقطة النهاية)
                TrimNudgeColumn(
                    title = "نقطة النهاية (OUT)",
                    currentTimeFormatted = formatTimeExact(activeClip.endTrimMs),
                    onNudge = { delta -> onAdjustEndTrim(selectedClipIndex, delta) },
                    onSetAtCurrentPlayhead = {
                        onTrimChange(
                            selectedClipIndex,
                            activeClip.startTrimMs,
                            currentTimeMs.coerceIn(activeClip.startTrimMs + 200L, activeClip.durationMs)
                        )
                    },
                    colorAccent = StudioCyan,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            // Quick Trimming Presets
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioDarkElevated, RoundedCornerShape(10.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "اختر قالب اقتصاص جاهز للمقطع:",
                    fontSize = 11.sp,
                    color = StudioTextSecondary,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PresetButton(
                        label = "أول 3 ثوانٍ ⚡",
                        onClick = { onApplyPreset(selectedClipIndex, "FIRST_3S") },
                        modifier = Modifier.weight(1f)
                    )
                    PresetButton(
                        label = "أول 5 ثوانٍ ⏱️",
                        onClick = { onApplyPreset(selectedClipIndex, "FIRST_5S") },
                        modifier = Modifier.weight(1f)
                    )
                    PresetButton(
                        label = "المنتصف 50% 🎯",
                        onClick = { onApplyPreset(selectedClipIndex, "MIDDLE_HALF") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PresetButton(
                        label = "آخر 3 ثوانٍ 🏁",
                        onClick = { onApplyPreset(selectedClipIndex, "LAST_3S") },
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        color = StudioRose.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioRose.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onResetTrim(selectedClipIndex) }
                            .testTag("reset_trim_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "استعادة الكامل",
                                tint = StudioRose,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "إعادة ضبط (الكامل)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioRose
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrimNudgeColumn(
    title: String,
    currentTimeFormatted: String,
    onNudge: (deltaMs: Long) -> Unit,
    onSetAtCurrentPlayhead: () -> Unit,
    colorAccent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = colorAccent
            )

            Text(
                text = currentTimeFormatted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                color = StudioTextPrimary
            )

            // Nudge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NudgeButton(label = "-1s", onClick = { onNudge(-1000L) })
                NudgeButton(label = "-0.1s", onClick = { onNudge(-100L) })
                NudgeButton(label = "+0.1s", onClick = { onNudge(100L) })
                NudgeButton(label = "+1s", onClick = { onNudge(1000L) })
            }

            // Set at Playhead Button
            Surface(
                color = colorAccent.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, colorAccent.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSetAtCurrentPlayhead() }
            ) {
                Text(
                    text = "📍 تعيين عند المؤشر",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorAccent,
                    modifier = Modifier.padding(vertical = 5.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun NudgeButton(
    label: String,
    onClick: () -> Unit
) {
    Surface(
        color = StudioSurfaceVariant,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioSurfaceBorder),
        modifier = Modifier
            .clickable { onClick() }
            .testTag("nudge_$label")
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun PresetButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = StudioSurfaceVariant,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary,
            modifier = Modifier.padding(vertical = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

private fun formatTimeExact(ms: Long): String {
    val totalSec = ms / 1000f
    val minutes = (totalSec / 60).toInt()
    val seconds = totalSec % 60
    return String.format(Locale.US, "%02d:%05.2f", minutes, seconds)
}

private fun formatSeconds(ms: Long): String {
    return String.format(Locale.US, "%.1f", ms / 1000f)
}
