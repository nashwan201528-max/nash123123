package com.example.ui.screens.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AspectRatioType
import com.example.data.model.AudioTrack
import com.example.ui.components.ExportSuccessDialog
import com.example.ui.components.FilterThumbnailRow
import com.example.ui.components.TextStickerEditorDialog
import com.example.ui.components.TopStudioAppBar
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
import com.example.ui.theme.StudioViolet
import com.example.ui.theme.TimelineClip
import com.example.ui.theme.TimelinePlayhead
import com.example.ui.theme.TimelineTrack
import com.example.ui.viewmodel.VideoEditorViewModel
import com.example.utils.SampleMediaProvider

private enum class VideoToolMode {
    TRIM_SPLIT,
    VISUAL_FX,
    BG_REMOVAL,
    SMART_CROP,
    TRANSITIONS,
    DUAL_COMPARISON,
    AI_QUALITY,
    SPEED_RAMP,
    FILTERS,
    AUDIO_TRACKS,
    AI_SUBTITLES
}

@Composable
fun VideoEditorScreen(
    viewModel: VideoEditorViewModel,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var activeToolMode by remember { mutableStateOf(VideoToolMode.TRIM_SPLIT) }
    var showTextStickerDialog by remember { mutableStateOf(false) }
    var showRenameDraftDialog by remember { mutableStateOf(false) }
    var renameDraftInput by remember { mutableStateOf("") }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopStudioAppBar(
                title = "محرر ومونتاج الفيديو",
                subtitle = state.projectTitle,
                onBackClick = onBackClick,
                onSaveDraftClick = { viewModel.saveDraft() },
                onCloudExportClick = { viewModel.openCloudStorageSheet() },
                onAiMagicClick = { viewModel.generateAiAutoSubtitles() },
                onExportClick = { viewModel.exportAndDownloadVideo(state.projectTitle, "1080p FHD 60fps") },
                exportButtonText = if (state.isExporting) "جاري التصدير..." else "تصدير الفيديو"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = StudioDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(StudioDark),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Room Draft Status & Rename Bar
            Surface(
                color = StudioDarkElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                renameDraftInput = state.projectTitle
                                showRenameDraftDialog = true
                            }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "📁 ${state.projectTitle}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Surface(
                            color = if (state.isDraftSaved) StudioEmerald.copy(alpha = 0.2f) else StudioAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (state.isDraftSaved) "✓ مسودة محفوظة (Room)" else "• مسودة غير محفوظة",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isDraftSaved) StudioEmerald else StudioAmber,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(
                            color = StudioCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .clickable { viewModel.saveDraft() }
                                .testTag("quick_save_draft_pill")
                        ) {
                            Text(
                                text = "حفظ المسودة 💾",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            color = StudioSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier
                                .clickable { viewModel.createNewProject() }
                                .testTag("new_project_draft_pill")
                        ) {
                            Text(
                                text = "مشروع جديد ➕",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Video Player Viewport
            val sampleRes = when (state.selectedClipIndex % 3) {
                0 -> R.drawable.sample_photo_sunset
                1 -> R.drawable.sample_photo_neon
                else -> R.drawable.hero_banner
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.1f)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(StudioDarkElevated)
                    .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (state.dualComparisonSettings.isEnabled) {
                    // Dual Comparison Live Viewport (Before / After)
                    com.example.ui.components.video.DualComparisonViewport(
                        sampleDrawableRes = sampleRes,
                        filterPreset = state.selectedFilter,
                        filterIntensity = state.filterIntensity,
                        isAiAutoEnhanced = state.isAiAutoEnhanced,
                        aiSharpenLevel = state.aiSharpenLevel,
                        settings = state.dualComparisonSettings,
                        onUpdateSplitPosition = { viewModel.setDualComparisonSplitPosition(it) },
                        onSetMode = { viewModel.setDualComparisonMode(it) },
                        onSetComparingRaw = { viewModel.setComparingOriginalRaw(it) },
                        onCloseComparison = { viewModel.toggleDualComparison() }
                    )
                } else {
                    // Regular Video Player Viewport
                    val activeClip = state.currentActiveClip

                    // Background replacement layer when AI BG Removal is active
                    if (state.bgRemovalSettings.isEnabled) {
                        val bgColors = state.bgRemovalSettings.selectedBgItem.previewGradientColors.map { Color(it) }
                        when (state.bgRemovalSettings.replacementType) {
                            com.example.data.model.BgReplacementType.VIRTUAL_STUDIO,
                            com.example.data.model.BgReplacementType.SOLID_GRADIENT,
                            com.example.data.model.BgReplacementType.AI_CUSTOM -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Brush.linearGradient(bgColors))
                                )
                            }
                            com.example.data.model.BgReplacementType.TRANSPARENT_CUTOUT -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(StudioDarkElevated)
                                )
                            }
                            com.example.data.model.BgReplacementType.BLUR_BOKEH -> {
                                Image(
                                    painter = painterResource(id = sampleRes),
                                    contentDescription = "خلفية مموهة",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alpha = 0.4f
                                )
                            }
                        }
                    }

                    Image(
                        painter = painterResource(id = sampleRes),
                        contentDescription = "عرض الفيديو المباشر",
                        modifier = Modifier
                            .fillMaxSize()
                            .then(
                                if (state.bgRemovalSettings.isEnabled && state.bgRemovalSettings.isRimLightingEnabled) {
                                    Modifier.border(
                                        (2.5f * state.bgRemovalSettings.rimLightIntensity).dp,
                                        Color(state.bgRemovalSettings.rimLightColorHex).copy(alpha = 0.7f),
                                        RoundedCornerShape(8.dp)
                                    )
                                } else Modifier
                            ),
                        contentScale = ContentScale.Crop,
                        alpha = if (state.bgRemovalSettings.isEnabled && state.bgRemovalSettings.replacementType == com.example.data.model.BgReplacementType.BLUR_BOKEH) 0.88f else 1.0f
                    )

                    // Aspect ratio mask frame
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                                )
                            )
                    )

                    // Live Active Subtitle Overlay with Custom Style & Font
                    val activeSec = state.currentTimeMs / 1000f
                    val activeSubtitle = state.subtitles.find { activeSec in it.startTimeSec..it.endTimeSec }
                    if (activeSubtitle != null) {
                        val style = state.subtitleStyleSettings
                        val align = when (style.position) {
                            com.example.data.model.SubtitlePosition.TOP -> Alignment.TopCenter
                            com.example.data.model.SubtitlePosition.CENTER -> Alignment.Center
                            com.example.data.model.SubtitlePosition.BOTTOM -> Alignment.BottomCenter
                        }
                        val padBottom = if (style.position == com.example.data.model.SubtitlePosition.BOTTOM) 52.dp else 12.dp
                        val padTop = if (style.position == com.example.data.model.SubtitlePosition.TOP) 44.dp else 12.dp

                        Box(
                            modifier = Modifier
                                .align(align)
                                .padding(bottom = padBottom, top = padTop)
                                .padding(horizontal = 16.dp)
                                .then(
                                    if (style.hasBackgroundBox) {
                                        Modifier
                                            .background(Color(style.bgColorHex), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 12.dp, vertical = 5.dp)
                                    } else {
                                        Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    }
                                )
                        ) {
                            Text(
                                text = activeSubtitle.textAr,
                                color = Color(style.textColorHex),
                                fontWeight = if (style.isBold) FontWeight.ExtraBold else FontWeight.Normal,
                                fontStyle = if (style.isItalic) FontStyle.Italic else FontStyle.Normal,
                                fontFamily = when (style.fontFamily) {
                                    com.example.data.model.SubtitleFontFamily.TECH_MONOSPACE -> androidx.compose.ui.text.font.FontFamily.Monospace
                                    com.example.data.model.SubtitleFontFamily.ELEGANT_SERIF -> androidx.compose.ui.text.font.FontFamily.Serif
                                    else -> androidx.compose.ui.text.font.FontFamily.Default
                                },
                                fontSize = (style.fontSizeSp * 0.72f).sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // AI Subject Tracking Indicator Box
                    if (state.smartCropSettings.isAiAutoTrackingEnabled && state.selectedAspectRatio != AspectRatioType.ORIGINAL) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(72.dp)
                                .border(1.5.dp, Brush.linearGradient(listOf(StudioCyan, StudioViolet)), RoundedCornerShape(8.dp))
                        ) {
                            Surface(
                                color = StudioDark.copy(alpha = 0.85f),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.align(Alignment.TopCenter).padding(top = 2.dp)
                            ) {
                                Text(
                                    text = "🎯 AI ${(state.smartCropSettings.detectedSubjectConfidence * 100).toInt()}%",
                                    fontSize = 8.sp,
                                    color = StudioCyan,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }

                    // Top Media Info & Quick Compare Toggle Button
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = StudioDark.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${state.selectedAspectRatio.labelAr} | ${state.selectedFilter.nameAr}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            activeClip?.let {
                                Surface(
                                    color = Color(it.colorHex).copy(alpha = 0.85f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${it.speed}x سرعة",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Quick Dual Comparison Toggle Pill
                        Surface(
                            color = StudioDark.copy(alpha = 0.88f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .clickable {
                                    viewModel.toggleDualComparison()
                                    activeToolMode = VideoToolMode.DUAL_COMPARISON
                                }
                                .testTag("quick_dual_comparison_pill")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("⚖️", fontSize = 11.sp)
                                Text(
                                    text = "مقارنة قبل/بعد",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioCyan
                                )
                            }
                        }
                    }

                    // Play / Pause Large Center Trigger
                    IconButton(
                        onClick = { viewModel.togglePlayPause() },
                        modifier = Modifier
                            .size(54.dp)
                            .background(StudioDark.copy(alpha = 0.7f), CircleShape)
                            .border(2.dp, StudioCyan, CircleShape)
                            .testTag("video_play_pause_btn")
                    ) {
                        Icon(
                            imageVector = if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "تشغيل أو إيقاف",
                            tint = StudioCyan,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Floating Action for adding Text / Subtitle
                    IconButton(
                        onClick = { showTextStickerDialog = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .size(38.dp)
                            .background(StudioSurface, CircleShape)
                            .border(1.dp, StudioCyan, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "إضافة نص للفيديو",
                            tint = StudioCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Export progress banner overlay
                if (state.isExporting) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(StudioDark.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                progress = { state.exportProgress },
                                color = StudioCyan,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = "جاري تصدير الفيديو بجودة فائقة ${(state.exportProgress * 100).toInt()}%",
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        }
                    }
                }
            }

            // Interactive Multi-Track Visual Timeline
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioDarkElevated)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Timecode & Track details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currentSec = state.currentTimeMs / 1000
                    val totalSec = state.totalDurationMs / 1000
                    Text(
                        text = String.format("⏱️ %02d:%02d / %02d:%02d", currentSec / 60, currentSec % 60, totalSec / 60, totalSec % 60),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )

                    Text(
                        text = "${state.clips.size} مقاطع مدمجة",
                        fontSize = 11.sp,
                        color = StudioTextMuted
                    )
                }

                // Multi-clip timeline blocks
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TimelineTrack)
                        .padding(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.clips.forEachIndexed { index, clip ->
                            val isSelected = index == state.selectedClipIndex
                            val weight = (clip.trimmedDurationMs.toFloat() / state.totalDurationMs.toFloat()).coerceAtLeast(0.1f)

                            Box(
                                modifier = Modifier
                                    .weight(weight)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(clip.colorHex).copy(alpha = if (isSelected) 1f else 0.6f))
                                    .border(
                                        width = if (isSelected) 2.dp else 0.dp,
                                        color = if (isSelected) StudioCyan else Color.Transparent,
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .clickable { viewModel.selectClip(index) }
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = clip.title,
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }

                            // Interactive Transition Node Button between adjacent clips
                            if (index < state.clips.size - 1) {
                                val transition = clip.transitionToNext
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (transition != null) StudioViolet else StudioDarkElevated
                                        )
                                        .border(
                                            0.5.dp,
                                            if (transition != null) StudioCyanLight else StudioSurfaceBorder,
                                            CircleShape
                                        )
                                        .clickable { viewModel.openTransitionPicker(index) }
                                        .testTag("timeline_transition_btn_$index"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (transition != null) {
                                        Text(
                                            text = transition.type.iconEmoji,
                                            fontSize = 9.sp
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "إضافة انتقال",
                                            tint = StudioCyan,
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Scrubber Playhead Line
                    val playheadRatio = (state.currentTimeMs.toFloat() / state.totalDurationMs.toFloat()).coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(3.dp)
                            .align(Alignment.CenterStart)
                            .background(TimelinePlayhead, RoundedCornerShape(2.dp))
                    )
                }

                // Timeline Scrubber Slider
                Slider(
                    value = state.currentTimeMs.toFloat(),
                    onValueChange = { viewModel.seekTo(it.toLong()) },
                    valueRange = 0f..state.totalDurationMs.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }

            // Bottom Video Toolbar Deck
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioSurface)
            ) {
                // Tool selection tabs (Scrollable Row)
                androidx.compose.foundation.lazy.LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        VideoToolTab(
                            title = "قص وتقسيم",
                            icon = Icons.Default.ContentCut,
                            isSelected = activeToolMode == VideoToolMode.TRIM_SPLIT,
                            onClick = { activeToolMode = VideoToolMode.TRIM_SPLIT }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "تأثيرات وتلوين AI 🎨",
                            icon = Icons.Default.ColorLens,
                            isSelected = activeToolMode == VideoToolMode.VISUAL_FX,
                            onClick = {
                                activeToolMode = VideoToolMode.VISUAL_FX
                                viewModel.openVisualFxStudioSheet()
                            }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "عزل الخلفية AI ✂️",
                            icon = Icons.Default.ContentCut,
                            isSelected = activeToolMode == VideoToolMode.BG_REMOVAL,
                            onClick = { activeToolMode = VideoToolMode.BG_REMOVAL }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "تأطير وقص AI 🎯",
                            icon = Icons.Default.Crop,
                            isSelected = activeToolMode == VideoToolMode.SMART_CROP,
                            onClick = { activeToolMode = VideoToolMode.SMART_CROP }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "انتقالات AI 🔄",
                            icon = Icons.Default.Sync,
                            isSelected = activeToolMode == VideoToolMode.TRANSITIONS,
                            onClick = { activeToolMode = VideoToolMode.TRANSITIONS }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "مقارنة قبل/بعد ⚖️",
                            icon = Icons.Default.Compare,
                            isSelected = activeToolMode == VideoToolMode.DUAL_COMPARISON,
                            onClick = {
                                activeToolMode = VideoToolMode.DUAL_COMPARISON
                                if (!state.dualComparisonSettings.isEnabled) {
                                    viewModel.toggleDualComparison()
                                }
                            }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "تحسين AI 🚀",
                            icon = Icons.Default.AutoAwesome,
                            isSelected = activeToolMode == VideoToolMode.AI_QUALITY,
                            onClick = { activeToolMode = VideoToolMode.AI_QUALITY }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "السرعة",
                            icon = Icons.Default.Speed,
                            isSelected = activeToolMode == VideoToolMode.SPEED_RAMP,
                            onClick = { activeToolMode = VideoToolMode.SPEED_RAMP }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "20 فلتر LUT",
                            icon = Icons.Default.ColorLens,
                            isSelected = activeToolMode == VideoToolMode.FILTERS,
                            onClick = { activeToolMode = VideoToolMode.FILTERS }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "الصوت والموسيقى",
                            icon = Icons.Default.MusicNote,
                            isSelected = activeToolMode == VideoToolMode.AUDIO_TRACKS,
                            onClick = { activeToolMode = VideoToolMode.AUDIO_TRACKS }
                        )
                    }
                    item {
                        VideoToolTab(
                            title = "ترجمة الذكاء",
                            icon = Icons.Default.Subtitles,
                            isSelected = activeToolMode == VideoToolMode.AI_SUBTITLES,
                            onClick = { activeToolMode = VideoToolMode.AI_SUBTITLES }
                        )
                    }
                }

                // Tool Content Panels
                when (activeToolMode) {
                    VideoToolMode.TRIM_SPLIT -> {
                        com.example.ui.components.video.VideoTrimmingDeck(
                            clips = state.clips,
                            selectedClipIndex = state.selectedClipIndex,
                            currentTimeMs = state.currentTimeMs,
                            onSelectClip = { viewModel.selectClip(it) },
                            onTrimChange = { clipIndex, startMs, endMs ->
                                viewModel.updateClipTrim(clipIndex, startMs, endMs)
                            },
                            onAdjustStartTrim = { clipIndex, deltaMs ->
                                viewModel.adjustStartTrim(clipIndex, deltaMs)
                            },
                            onAdjustEndTrim = { clipIndex, deltaMs ->
                                viewModel.adjustEndTrim(clipIndex, deltaMs)
                            },
                            onResetTrim = { clipIndex ->
                                viewModel.resetClipTrim(clipIndex)
                            },
                            onApplyPreset = { clipIndex, preset ->
                                viewModel.applyTrimPreset(clipIndex, preset)
                            },
                            onSplitClip = {
                                viewModel.splitCurrentClip()
                            },
                            onPreviewTrimmed = { clipIndex ->
                                viewModel.previewTrimmedClip(clipIndex)
                            }
                        )
                    }

                    VideoToolMode.VISUAL_FX -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "استوديو التأثيرات البصرية وتدريج الألوان 🎨",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "ضبط الألوان، فلاتر جمالية، وتأثيرات سينمائية وانتقالات ذكية",
                                    fontSize = 10.sp,
                                    color = StudioTextMuted
                                )
                            }
                            Button(
                                onClick = { viewModel.openVisualFxStudioSheet() },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("فتح الاستوديو ✨", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    VideoToolMode.BG_REMOVAL -> {
                        com.example.ui.components.video.AiBgRemovalPanel(
                            settings = state.bgRemovalSettings,
                            isProcessing = state.isAiProcessing,
                            onToggleEnabled = { viewModel.toggleBgRemoval() },
                            onSelectType = { viewModel.setBgReplacementType(it) },
                            onSelectBackground = { viewModel.selectVirtualBackground(it) },
                            onSetBlur = { viewModel.setBgBlurIntensity(it) },
                            onSetEdgeFeathering = { viewModel.setBgEdgeFeathering(it) },
                            onToggleRimLight = { viewModel.toggleBgRimLight() },
                            onGenerateCustomBg = { viewModel.generateAiCustomBackground(it) },
                            onOpenStudioSheet = { viewModel.openBgRemovalSheet() },
                            onResetSettings = { viewModel.resetBgRemoval() }
                        )
                    }

                    VideoToolMode.SMART_CROP -> {
                        com.example.ui.components.video.AiSmartCropPanel(
                            selectedRatio = state.selectedAspectRatio,
                            smartCropSettings = state.smartCropSettings,
                            isAnalyzing = state.isAnalyzingSmartCrop,
                            onSelectRatio = { viewModel.applySmartCrop(it) },
                            onSelectTrackingMode = { viewModel.applySmartCrop(state.selectedAspectRatio, it) },
                            onToggleAutoTracking = { viewModel.toggleAiAutoTracking() },
                            onOpenFullSheet = { viewModel.openSmartCropSheet() },
                            onReset = { viewModel.resetSmartCrop() }
                        )
                    }

                    VideoToolMode.TRANSITIONS -> {
                        com.example.ui.components.video.VideoTransitionsPanel(
                            clips = state.clips,
                            selectedClipIndex = state.selectedClipIndex,
                            onOpenTransitionPicker = { viewModel.openTransitionPicker(it) },
                            onQuickApplyPresetToAll = { viewModel.applyTransitionToAllClips(it) },
                            onRemoveAllTransitions = { viewModel.removeAllTransitions() }
                        )
                    }

                    VideoToolMode.DUAL_COMPARISON -> {
                        com.example.ui.components.video.DualComparisonPanel(
                            settings = state.dualComparisonSettings,
                            selectedFilter = state.selectedFilter,
                            filterIntensity = state.filterIntensity,
                            isAiAutoEnhanced = state.isAiAutoEnhanced,
                            aiSharpenLevel = state.aiSharpenLevel,
                            onToggleComparison = { viewModel.toggleDualComparison() },
                            onSelectMode = { viewModel.setDualComparisonMode(it) },
                            onSetSplitPosition = { viewModel.setDualComparisonSplitPosition(it) },
                            onSelectFilter = { viewModel.selectFilter(it) },
                            onResetSplit = { viewModel.resetDualComparisonSplit() }
                        )
                    }

                    VideoToolMode.AI_QUALITY -> {
                        com.example.ui.components.video.VideoAiEnhancementPanel(
                            state = state,
                            onAutoMasterClick = { viewModel.applyAiVideoAutoMaster() },
                            onUpscaleClick = { viewModel.applyAiVideoUpscaling(it) },
                            onDenoiseClick = { viewModel.applyAiVideoDenoise(it) },
                            onSharpenClick = { viewModel.applyAiVideoSharpening(it) },
                            onOpenSheetClick = { viewModel.openAiQualitySheet() }
                        )
                    }

                    VideoToolMode.SPEED_RAMP -> {
                        com.example.ui.components.video.VideoSpeedControlDeck(
                            clips = state.clips,
                            selectedClipIndex = state.selectedClipIndex,
                            onSelectClip = { viewModel.selectClip(it) },
                            onSetSpeed = { clipIndex, speed -> viewModel.setClipSpeed(clipIndex, speed) },
                            onAdjustSpeedDelta = { clipIndex, delta -> viewModel.adjustClipSpeedByDelta(clipIndex, delta) },
                            onApplySpeedToAll = { speed -> viewModel.applySpeedToAllClips(speed) },
                            onApplySpeedCurve = { clipIndex, curve -> viewModel.applySpeedCurveToClip(clipIndex, curve) },
                            onToggleKeepPitch = { clipIndex -> viewModel.toggleKeepAudioPitch(clipIndex) },
                            onResetSpeed = { clipIndex -> viewModel.resetClipSpeed(clipIndex) }
                        )
                    }

                    VideoToolMode.FILTERS -> {
                        FilterThumbnailRow(
                            selectedFilterId = state.selectedFilter.id,
                            onFilterSelected = { viewModel.selectFilter(it) }
                        )
                    }

                    VideoToolMode.AUDIO_TRACKS -> {
                        com.example.ui.components.video.AudioSuitePanel(
                            selectedAudioTrack = state.selectedAudioTrack,
                            voiceoverTracks = state.voiceoverTracks,
                            mixerSettings = state.audioMixerSettings,
                            onOpenAudioSuiteSheet = { viewModel.openAudioSuiteSheet(it) },
                            onSelectAudioTrack = { viewModel.selectAudioTrack(it) },
                            onToggleMusicMute = { viewModel.toggleAudioTrackMute() },
                            onOpenBeatSync = { viewModel.openBeatSyncSheet() }
                        )
                    }

                    VideoToolMode.AI_SUBTITLES -> {
                        com.example.ui.components.video.AiSubtitlesPanel(
                            subtitles = state.subtitles,
                            styleSettings = state.subtitleStyleSettings,
                            selectedLanguage = state.selectedSubtitleLanguage,
                            isGenerating = state.isGeneratingSubtitles,
                            onOpenStudioSheet = { viewModel.openSubtitleStudioSheet() },
                            onQuickGenerate = { viewModel.generateAutoSubtitles() },
                            onQuickChangeColor = { colorHex ->
                                viewModel.updateSubtitleStyle(state.subtitleStyleSettings.copy(textColorHex = colorHex))
                            }
                        )
                    }
                }
            }
        }
    }

    // Text & Sticker Dialog
    if (showTextStickerDialog) {
        TextStickerEditorDialog(
            onDismiss = { showTextStickerDialog = false },
            onAddTextOverlay = { viewModel.addTextOverlay(it) },
            onAddSticker = { viewModel.addSticker(it) }
        )
    }

    // Rename Draft Dialog
    if (showRenameDraftDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showRenameDraftDialog = false },
            title = {
                Text(
                    text = "تعديل اسم مسودة المشروع 📝",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            },
            text = {
                androidx.compose.material3.OutlinedTextField(
                    value = renameDraftInput,
                    onValueChange = { renameDraftInput = it },
                    placeholder = { Text("اكتب اسم المشروع...", color = StudioTextMuted) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = StudioCyan,
                        unfocusedBorderColor = StudioSurfaceBorder,
                        focusedTextColor = StudioTextPrimary,
                        unfocusedTextColor = StudioTextPrimary,
                        focusedContainerColor = StudioSurface,
                        unfocusedContainerColor = StudioSurface
                    ),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (renameDraftInput.isNotBlank()) {
                            viewModel.setProjectTitle(renameDraftInput.trim())
                            viewModel.saveDraft(renameDraftInput.trim())
                        }
                        showRenameDraftDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan)
                ) {
                    Text("حفظ الاسم", color = StudioDark, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { showRenameDraftDialog = false }) {
                    Text("إلغاء", color = StudioTextMuted)
                }
            },
            containerColor = StudioSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }

    // AI Smart Visual Effects & Color Grading Studio Sheet
    if (state.isVisualFxStudioSheetOpen) {
        com.example.ui.components.video.AiVisualEffectsStudioSheet(
            selectedFilter = state.selectedFilter,
            filterIntensity = state.filterIntensity,
            colorGradingState = state.colorGradingState,
            visualFxParams = state.visualFxParams,
            transitionCustomization = state.transitionCustomization,
            isDualComparisonEnabled = state.dualComparisonSettings.isEnabled,
            comparisonSplitPosition = state.dualComparisonSettings.splitPosition,
            targetClipIndex = state.selectedClipIndex,
            clips = state.clips,
            onFilterChanged = { viewModel.selectFilter(it) },
            onFilterIntensityChanged = { viewModel.setFilterIntensity(it) },
            onColorGradingChanged = { viewModel.updateColorGrading(it) },
            onVisualFxChanged = { viewModel.updateVisualFx(it) },
            onTransitionChanged = { type, durationMs -> viewModel.setClipTransition(state.selectedClipIndex, type, durationMs) },
            onApplyTransitionToAll = { type, durationMs -> viewModel.applyTransitionToAllClips(type, durationMs) },
            onToggleDualComparison = { viewModel.toggleDualComparison() },
            onComparisonSplitChanged = { viewModel.setDualComparisonSplitPosition(it) },
            onAiAutoMasterEnhance = { viewModel.applyAiVideoAutoMaster() },
            onAiPromptColorGrading = { viewModel.applyAiPromptColorGrading(it) },
            onDismiss = { viewModel.closeVisualFxStudioSheet() }
        )
    }

    // AI Quality Enhancer Bottom Sheet
    if (state.isAiQualitySheetOpen) {
        com.example.ui.components.AiQualityEnhancerSheet(
            onDismiss = { viewModel.closeAiQualitySheet() },
            isProcessing = state.isAiProcessing,
            onAutoMasterClick = { viewModel.applyAiVideoAutoMaster() },
            onUpscaleClick = { viewModel.applyAiVideoUpscaling(it) },
            onDenoiseClick = { viewModel.applyAiVideoDenoise(it) },
            onSharpenClick = { viewModel.applyAiVideoSharpening(it) }
        )
    }

    // Video Transition Selector Bottom Sheet
    if (state.isTransitionSheetOpen) {
        com.example.ui.components.video.VideoTransitionSelectorSheet(
            targetClipIndex = state.activeTransitionTargetClipIndex,
            clips = state.clips,
            onDismiss = { viewModel.closeTransitionPicker() },
            onApplyTransition = { clipIndex, type, durationMs ->
                viewModel.setClipTransition(clipIndex, type, durationMs)
            },
            onApplyToAll = { type, durationMs ->
                viewModel.applyTransitionToAllClips(type, durationMs)
            },
            onRemoveTransition = { clipIndex ->
                viewModel.removeClipTransition(clipIndex)
            }
        )
    }

    // Audio Suite Bottom Sheet
    if (state.isAudioSuiteSheetOpen) {
        com.example.ui.components.video.AudioSuiteBottomSheet(
            selectedTrack = state.selectedAudioTrack,
            voiceoverTracks = state.voiceoverTracks,
            mixerSettings = state.audioMixerSettings,
            isRecording = state.isRecordingVoiceover,
            recordingDurationSec = state.recordingVoiceoverDurationSec,
            recordingAmplitudes = state.recordingAmplitudes,
            currentTimelineMs = state.currentTimeMs,
            initialTab = state.selectedAudioSuiteTab,
            onDismiss = { viewModel.closeAudioSuiteSheet() },
            onSelectAudioTrack = { viewModel.selectAudioTrack(it) },
            onSetAudioTrackVolume = { viewModel.setAudioTrackVolume(it) },
            onToggleAudioTrackMute = { viewModel.toggleAudioTrackMute() },
            onStartVoiceoverRecording = { viewModel.startVoiceoverRecording() },
            onStopVoiceoverRecording = { viewModel.stopVoiceoverRecording() },
            onCancelVoiceoverRecording = { viewModel.cancelVoiceoverRecording() },
            onRemoveVoiceoverTrack = { viewModel.removeVoiceoverTrack(it) },
            onToggleVoiceoverMute = { viewModel.toggleVoiceoverMute(it) },
            onToggleVoiceoverAiDenoise = { viewModel.toggleVoiceoverAiDenoise(it) },
            onUpdateMixerSettings = { viewModel.updateMixerSettings(it) },
            onInsertSoundEffect = { viewModel.insertSoundEffect(it) }
        )
    }

    // AI Subtitles Studio Bottom Sheet
    if (state.isSubtitleStudioSheetOpen) {
        com.example.ui.components.video.AiSubtitlesStudioSheet(
            subtitles = state.subtitles,
            styleSettings = state.subtitleStyleSettings,
            selectedLanguage = state.selectedSubtitleLanguage,
            isGenerating = state.isGeneratingSubtitles,
            onDismiss = { viewModel.closeSubtitleStudioSheet() },
            onGenerateSubtitles = { viewModel.generateAutoSubtitles(it) },
            onTranslateSubtitles = { viewModel.translateSubtitles(it) },
            onLanguageChange = { viewModel.setSubtitleLanguage(it) },
            onUpdateStyleSettings = { viewModel.updateSubtitleStyle(it) },
            onAddSubtitleLine = { viewModel.addSubtitleLine(it) },
            onUpdateSubtitleLine = { index, line -> viewModel.updateSubtitleLine(index, line) },
            onRemoveSubtitleLine = { viewModel.removeSubtitleLine(it) },
            onClearAllSubtitles = { viewModel.clearAllSubtitles() }
        )
    }

    // AI Background Removal Studio Bottom Sheet
    if (state.isBgRemovalSheetOpen) {
        com.example.ui.components.video.AiBgRemovalStudioSheet(
            settings = state.bgRemovalSettings,
            isProcessing = state.isAiProcessing,
            onDismiss = { viewModel.closeBgRemovalSheet() },
            onUpdateSettings = { viewModel.updateBgRemovalSettings(it) },
            onGenerateCustomBg = { viewModel.generateAiCustomBackground(it) },
            onReset = { viewModel.resetBgRemoval() }
        )
    }

    // AI Smart Reframe Studio Bottom Sheet
    if (state.isSmartCropSheetOpen) {
        com.example.ui.components.video.AiSmartReframeSheet(
            settings = state.smartCropSettings,
            isAnalyzing = state.isAnalyzingSmartCrop,
            onDismiss = { viewModel.closeSmartCropSheet() },
            onApplySmartCrop = { ratio, mode -> viewModel.applySmartCrop(ratio, mode) },
            onUpdateSettings = { viewModel.updateSmartCropSettings(it) },
            onResetSmartCrop = { viewModel.resetSmartCrop() }
        )
    }

    // AI Music Beat Sync Bottom Sheet
    if (state.isBeatSyncSheetOpen) {
        com.example.ui.components.video.AiBeatSyncSheet(
            audioTrack = state.selectedAudioTrack,
            clips = state.clips,
            settings = state.beatSyncSettings,
            lastResult = state.lastBeatSyncResult,
            isAnalyzing = state.isAnalyzingBeatSync,
            onDismiss = { viewModel.closeBeatSyncSheet() },
            onSyncClips = { mode, transitions -> viewModel.syncClipsToAudioBeat(mode, transitions) }
        )
    }

    // Firebase Cloud Export & Sync Bottom Sheet
    if (state.isCloudStorageSheetOpen) {
        com.example.ui.components.video.CloudExportBottomSheet(
            projectTitle = state.projectTitle,
            durationMs = state.totalDurationMs,
            syncStatus = state.cloudSyncStatus,
            cloudProjects = state.cloudProjectsList,
            onDismiss = { viewModel.closeCloudStorageSheet() },
            onStartCloudExport = { resolution -> viewModel.exportToFirebaseCloudStorage(resolution) }
        )
    }

    // Export Success Dialog
    state.lastExportedItem?.let { item ->
        ExportSuccessDialog(
            exportItem = item,
            onDismiss = { viewModel.dismissExportDialog() }
        )
    }
}

@Composable
private fun VideoToolTab(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) StudioCyan.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) StudioCyan else StudioTextMuted,
            modifier = Modifier.size(15.dp)
        )
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) StudioCyan else StudioTextMuted
        )
    }
}
