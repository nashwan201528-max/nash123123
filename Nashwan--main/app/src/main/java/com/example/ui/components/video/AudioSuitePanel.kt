package com.example.ui.components.video

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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioMixerSettings
import com.example.data.model.AudioTrack
import com.example.data.model.VoiceoverTrack
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
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
import com.example.utils.SampleMediaProvider

@Composable
fun AudioSuitePanel(
    selectedAudioTrack: AudioTrack?,
    voiceoverTracks: List<VoiceoverTrack>,
    mixerSettings: AudioMixerSettings,
    onOpenAudioSuiteSheet: (tabIndex: Int) -> Unit,
    onSelectAudioTrack: (AudioTrack?) -> Unit,
    onToggleMusicMute: () -> Unit,
    onOpenBeatSync: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & Open Full Studio Button & AI Beat Sync
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(16.dp))
                Text(
                    text = "الصوتيات والموسيقى والميكروفون:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // AI Beat Sync Button
                Button(
                    onClick = onOpenBeatSync,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("open_beat_sync_btn")
                ) {
                    Text("مزامنة الإيقاع ⚡🎵", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onOpenAudioSuiteSheet(2) }, // Open Mixer tab
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("open_audio_mixer_btn")
                ) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("الخلاط 🎛️", fontSize = 10.sp, color = StudioViolet, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 2. Quick Action Modules: Record Voiceover & Music & SFX
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Voiceover Quick Launcher Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioRose.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenAudioSuiteSheet(1) }
                    .testTag("quick_record_voiceover_btn")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(StudioRose.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = StudioRose, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = "تسجيل تعليق صوتي 🎙️",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = if (voiceoverTracks.isNotEmpty()) "${voiceoverTracks.size} تسجيلات مضافة" else "المس لبدء التسجيل",
                            fontSize = 9.sp,
                            color = if (voiceoverTracks.isNotEmpty()) StudioEmerald else StudioTextMuted
                        )
                    }
                }
            }

            // SFX Quick Launcher Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenAudioSuiteSheet(3) }
                    .testTag("quick_sfx_library_btn")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(StudioCyan.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🔊", fontSize = 16.sp)
                    }
                    Column {
                        Text(
                            text = "مكتبة المؤثرات SFX",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "8 مؤثرات سينمائية",
                            fontSize = 9.sp,
                            color = StudioCyan
                        )
                    }
                }
            }
        }

        // 3. Quick Music Track Selector Carousel
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(SampleMediaProvider.sampleAudioTracks) { track ->
                val isSelected = selectedAudioTrack?.id == track.id
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSelectAudioTrack(if (isSelected) null else track) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) StudioViolet.copy(alpha = 0.3f) else StudioSurfaceVariant
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioViolet else StudioSurfaceBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = if (isSelected) StudioViolet else StudioTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text(
                                text = track.titleAr,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) StudioViolet else StudioTextPrimary,
                                maxLines = 1
                            )
                            Text(
                                text = "${track.categoryAr} • ${track.durationSec}ث",
                                fontSize = 9.sp,
                                color = StudioTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
