package com.example.ui.components.video

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeDown
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AudioMixerSettings
import com.example.data.model.AudioTrack
import com.example.data.model.SoundEffectItem
import com.example.data.model.SoundEffectsLibrary
import com.example.data.model.VoiceoverTrack
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
import com.example.utils.SampleMediaProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSuiteBottomSheet(
    selectedTrack: AudioTrack?,
    voiceoverTracks: List<VoiceoverTrack>,
    mixerSettings: AudioMixerSettings,
    isRecording: Boolean,
    recordingDurationSec: Float,
    recordingAmplitudes: List<Float>,
    currentTimelineMs: Long,
    initialTab: Int = 0,
    onDismiss: () -> Unit,
    onSelectAudioTrack: (AudioTrack?) -> Unit,
    onSetAudioTrackVolume: (Float) -> Unit,
    onToggleAudioTrackMute: () -> Unit,
    onStartVoiceoverRecording: () -> Unit,
    onStopVoiceoverRecording: () -> Unit,
    onCancelVoiceoverRecording: () -> Unit,
    onRemoveVoiceoverTrack: (String) -> Unit,
    onToggleVoiceoverMute: (String) -> Unit,
    onToggleVoiceoverAiDenoise: (String) -> Unit,
    onUpdateMixerSettings: (AudioMixerSettings) -> Unit,
    onInsertSoundEffect: (SoundEffectItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(initialTab) }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onStartVoiceoverRecording()
        }
    }

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
            // Header
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
                                Brush.linearGradient(listOf(StudioMagenta, StudioViolet)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = "أستوديو الصوت",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "أستوديو تحرير ومعالجة الصوتيات (Audio Suite)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "موسيقى خلفية • تسجيل تعليق صوتي • خلاط المسارات",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Audio Suite Tab Switcher
            val tabs = listOf(
                Pair("🎵 الموسيقى", 0),
                Pair("🎙️ التعليق الصوتي", 1),
                Pair("🎛️ الخلاط متعدد المسارات", 2),
                Pair("🔊 المؤثرات SFX", 3)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(tabs) { tab ->
                    val isSelected = selectedTab == tab.second
                    Surface(
                        color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioViolet else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { selectedTab = tab.second }
                            .testTag("audio_tab_${tab.second}")
                    ) {
                        Text(
                            text = tab.first,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioViolet else StudioTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Tab Content
            when (selectedTab) {
                // 0. Background Music Library
                0 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "اختر مقطع موسيقى تصويرية مرخصة بدون حقوق:",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        SampleMediaProvider.sampleAudioTracks.forEach { track ->
                            val isSelected = selectedTrack?.id == track.id
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectAudioTrack(if (isSelected) null else track) }
                                    .testTag("music_item_${track.id}"),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) StudioViolet.copy(alpha = 0.2f) else StudioSurface
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioViolet else StudioSurfaceBorder
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .background(
                                                    if (isSelected) StudioViolet else StudioSurfaceVariant,
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MusicNote,
                                                contentDescription = null,
                                                tint = if (isSelected) Color.White else StudioViolet,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = track.titleAr,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) StudioViolet else StudioTextPrimary
                                            )
                                            Text(
                                                text = "${track.categoryAr} • ${track.durationSec} ثانية",
                                                fontSize = 10.sp,
                                                color = StudioTextMuted
                                            )
                                        }
                                    }

                                    if (isSelected) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            IconButton(
                                                onClick = { onToggleAudioTrackMute() },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (track.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                                    contentDescription = "كتم",
                                                    tint = if (track.isMuted) StudioRose else StudioCyan,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }

                                            Surface(
                                                color = StudioViolet,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "مفعلة ✓",
                                                    fontSize = 10.sp,
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Music Volume Slider if active
                        selectedTrack?.let { track ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("مستوى صوت الموسيقى:", fontSize = 11.sp, color = StudioTextPrimary)
                                        Text("${(track.volume * 100).toInt()}%", fontSize = 11.sp, color = StudioViolet, fontWeight = FontWeight.Bold)
                                    }
                                    Slider(
                                        value = track.volume,
                                        onValueChange = { onSetAudioTrackVolume(it) },
                                        valueRange = 0f..2.0f,
                                        colors = SliderDefaults.colors(
                                            thumbColor = StudioViolet,
                                            activeTrackColor = StudioViolet,
                                            inactiveTrackColor = StudioSurfaceVariant
                                        ),
                                        modifier = Modifier.height(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 1. Voiceover Recording Studio
                1 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Current timestamp locator card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioSurface),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("نقطة إدراج التعليق الصوتي:", fontSize = 10.sp, color = StudioTextSecondary)
                                    Text(
                                        text = "📍 التوقيت الحالي: ${String.format("%.2f", currentTimelineMs / 1000f)} ثانية",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioCyan
                                    )
                                }

                                Surface(
                                    color = StudioEmerald.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(12.dp))
                                        Text("تنقية AI تلقائية", fontSize = 9.sp, color = StudioEmerald, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }

                        // Live Recording Deck
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isRecording) StudioRose else StudioSurfaceBorder
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                if (isRecording) {
                                    Text(
                                        text = "جاري التسجيل: ${String.format("%.1f", recordingDurationSec)} ثانية 🔴",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioRose
                                    )

                                    // Real-time live Waveform Canvas
                                    VoiceoverLiveWaveform(
                                        amplitudes = recordingAmplitudes,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                    )
                                } else {
                                    Text(
                                        text = "المس الزر أدناه لبدء تسجيل صوتك عبر الميكروفون",
                                        fontSize = 11.sp,
                                        color = StudioTextSecondary
                                    )
                                }

                                // Pulsating Record Button
                                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                                val pulseScale by infiniteTransition.animateFloat(
                                    initialValue = 1f,
                                    targetValue = 1.15f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(durationMillis = 600),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "pulse_scale"
                                )

                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .scale(if (isRecording) pulseScale else 1f)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                if (isRecording) listOf(StudioRose, StudioRose) else listOf(StudioCyan, StudioViolet)
                                            )
                                        )
                                        .clickable {
                                            if (isRecording) {
                                                onStopVoiceoverRecording()
                                            } else {
                                                recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                            }
                                        }
                                        .testTag("voiceover_record_toggle_btn"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                                        contentDescription = if (isRecording) "إيقاف التسجيل" else "بدء التسجيل",
                                        tint = Color.White,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }

                                Text(
                                    text = if (isRecording) "اضغط لإيقاف وحفظ التسجيل" else "بدء تسجيل صوتي جديد",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isRecording) StudioRose else StudioTextPrimary
                                )
                            }
                        }

                        // List of recorded Voiceover Tracks
                        if (voiceoverTracks.isNotEmpty()) {
                            Text(
                                text = "التسجيلات الصوتية في المشروع (${voiceoverTracks.size}):",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )

                            voiceoverTracks.forEach { vo ->
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(30.dp)
                                                    .background(StudioCyan.copy(alpha = 0.2f), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                                            }
                                            Column {
                                                Text(vo.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                                                Text(
                                                    text = "يبدأ عند ${(vo.startTimelineMs / 1000f)}ث • المدة: ${(vo.durationMs / 1000f)}ث",
                                                    fontSize = 9.sp,
                                                    color = StudioTextMuted
                                                )
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            // AI Denoise chip
                                            Surface(
                                                color = if (vo.isAiDenoised) StudioEmerald.copy(alpha = 0.2f) else StudioSurfaceVariant,
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.clickable { onToggleVoiceoverAiDenoise(vo.id) }
                                            ) {
                                                Text(
                                                    text = if (vo.isAiDenoised) "AI نقي ✓" else "بدون تنقية",
                                                    fontSize = 9.sp,
                                                    color = if (vo.isAiDenoised) StudioEmerald else StudioTextMuted,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                                )
                                            }

                                            // Mute button
                                            IconButton(onClick = { onToggleVoiceoverMute(vo.id) }, modifier = Modifier.size(28.dp)) {
                                                Icon(
                                                    imageVector = if (vo.isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                                    contentDescription = "كتم",
                                                    tint = if (vo.isMuted) StudioRose else StudioCyan,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            // Delete button
                                            IconButton(onClick = { onRemoveVoiceoverTrack(vo.id) }, modifier = Modifier.size(28.dp)) {
                                                Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = StudioRose, modifier = Modifier.size(16.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 2. Multi-Track Audio Mixer
                2 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "موازنة وخلاط المسارات الصوتية (Multi-Track Mixing Deck):",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        // Channel 1: Original Video Audio
                        AudioMixerChannelCard(
                            channelName = "صوت الفيديو الأصلي (Original Audio)",
                            iconEmoji = "📹",
                            volume = mixerSettings.videoOriginalVolume,
                            isMuted = mixerSettings.isVideoOriginalMuted,
                            accentColor = StudioCyan,
                            onVolumeChange = { onUpdateMixerSettings(mixerSettings.copy(videoOriginalVolume = it)) },
                            onMuteToggle = { onUpdateMixerSettings(mixerSettings.copy(isVideoOriginalMuted = !mixerSettings.isVideoOriginalMuted)) }
                        )

                        // Channel 2: Background Music
                        AudioMixerChannelCard(
                            channelName = "موسيقى الخلفية (Background Music)",
                            iconEmoji = "🎵",
                            volume = mixerSettings.backgroundMusicVolume,
                            isMuted = mixerSettings.isBackgroundMusicMuted,
                            accentColor = StudioViolet,
                            onVolumeChange = { onUpdateMixerSettings(mixerSettings.copy(backgroundMusicVolume = it)) },
                            onMuteToggle = { onUpdateMixerSettings(mixerSettings.copy(isBackgroundMusicMuted = !mixerSettings.isBackgroundMusicMuted)) }
                        )

                        // Channel 3: Voiceover
                        AudioMixerChannelCard(
                            channelName = "التعليق الصوتي والميكروفون (Voiceover)",
                            iconEmoji = "🎙️",
                            volume = mixerSettings.voiceoverVolume,
                            isMuted = mixerSettings.isVoiceoverMuted,
                            accentColor = StudioAmber,
                            onVolumeChange = { onUpdateMixerSettings(mixerSettings.copy(voiceoverVolume = it)) },
                            onMuteToggle = { onUpdateMixerSettings(mixerSettings.copy(isVoiceoverMuted = !mixerSettings.isVoiceoverMuted)) }
                        )

                        // Channel 4: Sound Effects (SFX)
                        AudioMixerChannelCard(
                            channelName = "المؤثرات الصوتية (SFX Sound Effects)",
                            iconEmoji = "🔊",
                            volume = mixerSettings.sfxVolume,
                            isMuted = mixerSettings.isSfxMuted,
                            accentColor = StudioEmerald,
                            onVolumeChange = { onUpdateMixerSettings(mixerSettings.copy(sfxVolume = it)) },
                            onMuteToggle = { onUpdateMixerSettings(mixerSettings.copy(isSfxMuted = !mixerSettings.isSfxMuted)) }
                        )

                        // Smart Audio Ducking Card
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioSurface),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("🦆", fontSize = 18.sp)
                                    Column {
                                        Text(
                                            text = "خفض الموسيقى الذكي (Audio Ducking)",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StudioTextPrimary
                                        )
                                        Text(
                                            text = "يخفض صوت الموسيقى تلقائياً عند وجود تعليق صوتي ليبقى الكلام واضحاً",
                                            fontSize = 9.sp,
                                            color = StudioTextSecondary
                                        )
                                    }
                                }
                                Switch(
                                    checked = mixerSettings.isSmartDuckingEnabled,
                                    onCheckedChange = { onUpdateMixerSettings(mixerSettings.copy(isSmartDuckingEnabled = it)) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = StudioCyan
                                    )
                                )
                            }
                        }
                    }
                }

                // 3. Sound Effects Library (SFX)
                3 -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "مكتبة المؤثرات الصوتية الاحترافية (SFX):",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )

                        SoundEffectsLibrary.effects.chunked(2).forEach { sfxPair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                sfxPair.forEach { sfx ->
                                    Card(
                                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { onInsertSoundEffect(sfx) }
                                            .testTag("sfx_btn_${sfx.id}")
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(sfx.iconEmoji, fontSize = 18.sp)
                                                Surface(
                                                    color = StudioCyan.copy(alpha = 0.15f),
                                                    shape = RoundedCornerShape(4.dp)
                                                ) {
                                                    Text(
                                                        text = "${sfx.durationSec}ث",
                                                        fontSize = 9.sp,
                                                        color = StudioCyan,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Text(
                                                text = sfx.nameAr,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = StudioTextPrimary,
                                                maxLines = 1
                                            )
                                            Text(
                                                text = sfx.descriptionAr,
                                                fontSize = 9.sp,
                                                color = StudioTextMuted,
                                                maxLines = 2
                                            )

                                            Button(
                                                onClick = { onInsertSoundEffect(sfx) },
                                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.fillMaxWidth(),
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 2.dp)
                                            ) {
                                                Text("إدراج عند المؤشر ⚡", fontSize = 9.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }
                                }
                                if (sfxPair.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioMixerChannelCard(
    channelName: String,
    iconEmoji: String,
    volume: Float,
    isMuted: Boolean,
    accentColor: Color,
    onVolumeChange: (Float) -> Unit,
    onMuteToggle: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(iconEmoji, fontSize = 15.sp)
                    Text(channelName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (isMuted) "مكتوم" else "${(volume * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isMuted) StudioRose else accentColor
                    )
                    IconButton(onClick = onMuteToggle, modifier = Modifier.size(28.dp)) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "كتم",
                            tint = if (isMuted) StudioRose else accentColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Slider(
                value = if (isMuted) 0f else volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..2.0f,
                colors = SliderDefaults.colors(
                    thumbColor = accentColor,
                    activeTrackColor = accentColor,
                    inactiveTrackColor = StudioSurfaceVariant
                ),
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Composable
private fun VoiceoverLiveWaveform(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(StudioDark)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val count = amplitudes.size.coerceAtLeast(1)
            val barWidth = (size.width / (count * 1.5f)).coerceIn(3.dp.toPx(), 8.dp.toPx())
            val spacing = barWidth * 0.5f

            amplitudes.forEachIndexed { index, amp ->
                val barHeight = (size.height * amp.coerceIn(0.1f, 1.0f)).coerceAtLeast(4.dp.toPx())
                val x = index * (barWidth + spacing)
                val y = (size.height - barHeight) / 2

                drawRoundRect(
                    color = if (index > count - 5) StudioRose else StudioCyan,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f, 4f)
                )
            }
        }
    }
}
