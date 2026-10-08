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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.OfflineBolt
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioTrack
import com.example.data.model.BeatIntervalMode
import com.example.data.model.BeatSyncResult
import com.example.data.model.BeatSyncSettings
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiBeatSyncSheet(
    audioTrack: AudioTrack?,
    clips: List<VideoClip>,
    settings: BeatSyncSettings,
    lastResult: BeatSyncResult?,
    isAnalyzing: Boolean,
    onDismiss: () -> Unit,
    onSyncClips: (BeatIntervalMode, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedIntervalMode by remember { mutableStateOf(settings.intervalMode) }
    var applyTransitions by remember { mutableStateOf(settings.applyTransitionsOnBeats) }
    val track = audioTrack ?: AudioTrack("track_1", "لحن سينمائي ملحمي", "سينمائي", 60, bpm = 128)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StudioDarkElevated,
        contentColor = StudioTextPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(listOf(StudioAmber, StudioMagenta)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "مزامنة الموسيقى",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "المزامنة الذكية مع إيقاع الموسيقى (AI Beat Sync)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "قص لقطات الفيديو وتزامن الانتقالات مع ضربات البيس تلقائياً",
                            fontSize = 10.sp,
                            color = StudioAmber
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // 2. Active Audio Track & Beat Waveform Visualizer
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDark),
                shape = RoundedCornerShape(14.dp),
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null, tint = StudioAmber, modifier = Modifier.size(16.dp))
                            Text(
                                text = track.titleAr,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary,
                                maxLines = 1
                            )
                        }

                        Surface(
                            color = StudioAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${track.bpm} BPM ⚡",
                                fontSize = 10.sp,
                                color = StudioAmber,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Simulated Audio Beat Waveform Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F172A))
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height
                            val barCount = 48
                            val barWidth = w / (barCount * 1.5f)

                            for (i in 0 until barCount) {
                                val x = i * (w / barCount)
                                val isBeat = (i % 4 == 0)
                                val barHeight = if (isBeat) h * 0.85f else (h * (0.25f + ((i * 17) % 40) / 100f))

                                drawLine(
                                    color = if (isBeat) StudioAmber else StudioCyan.copy(alpha = 0.4f),
                                    start = Offset(x, (h - barHeight) / 2f),
                                    end = Offset(x, (h + barHeight) / 2f),
                                    strokeWidth = if (isBeat) 3.dp.toPx() else 1.5.dp.toPx()
                                )

                                if (isBeat) {
                                    drawCircle(
                                        color = StudioAmber,
                                        radius = 3.dp.toPx(),
                                        center = Offset(x, (h - barHeight) / 2f - 4.dp.toPx())
                                    )
                                }
                            }
                        }

                        Surface(
                            color = StudioDark.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.align(Alignment.BottomEnd).padding(4.dp)
                        ) {
                            Text(
                                text = "تم رصد ${settings.detectedBeatsCount} ضربة بيس متزامنة 🎯",
                                fontSize = 9.sp,
                                color = StudioAmber,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
            }

            // 3. Rhythm Cut Interval Mode Selector
            Text("1. نمط وإيقاع تقطيع المقاطع (Rhythm Pacing Style):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                BeatIntervalMode.values().forEach { mode ->
                    val isSelected = selectedIntervalMode == mode
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) StudioAmber.copy(alpha = 0.2f) else StudioSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioAmber else StudioSurfaceBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedIntervalMode = mode }
                            .testTag("beat_mode_${mode.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = mode.iconEmoji, fontSize = 20.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.titleAr,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) StudioAmber else StudioTextPrimary
                                )
                                Text(
                                    text = mode.subtitleAr,
                                    fontSize = 9.sp,
                                    color = StudioTextMuted
                                )
                            }
                            if (isSelected) {
                                Text("محدد ✓", fontSize = 10.sp, color = StudioAmber, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 4. Dynamic Beat Transitions Toggle
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "إضافة انتقالات وميض وزووم متزامنة مع الإيقاع",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "تطبيق فلاش ضوئي وزووم حركي تلقائي عند كل ضربة بيس للمونتاج",
                            fontSize = 9.sp,
                            color = StudioTextMuted
                        )
                    }
                    Switch(
                        checked = applyTransitions,
                        onCheckedChange = { applyTransitions = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = StudioAmber),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // 5. Result Summary Banner if applied
            lastResult?.let { res ->
                Surface(
                    color = StudioEmerald.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmerald),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(18.dp))
                        Text(
                            text = res.summaryAr,
                            fontSize = 10.sp,
                            color = StudioEmerald,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 6. Action Button
            Button(
                onClick = { onSyncClips(selectedIntervalMode, applyTransitions) },
                enabled = !isAnalyzing,
                colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("apply_beat_sync_btn")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = StudioDark, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("جاري تحليل الإيقاع وتنسيق المقاطع...", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.OfflineBolt, contentDescription = null, tint = StudioDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "قص ومزامنة كافة المقاطع مع إيقاع الموسيقى ⚡🎵",
                        color = StudioDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
