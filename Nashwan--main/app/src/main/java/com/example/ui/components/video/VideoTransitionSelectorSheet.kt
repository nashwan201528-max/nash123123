package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
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
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TransitionCategory
import com.example.data.model.VideoClip
import com.example.data.model.VideoTransitionType
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
fun VideoTransitionSelectorSheet(
    targetClipIndex: Int,
    clips: List<VideoClip>,
    onDismiss: () -> Unit,
    onApplyTransition: (clipIndex: Int, type: VideoTransitionType, durationMs: Long) -> Unit,
    onApplyToAll: (type: VideoTransitionType, durationMs: Long) -> Unit,
    onRemoveTransition: (clipIndex: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentClip = clips.getOrNull(targetClipIndex)
    val nextClip = clips.getOrNull(targetClipIndex + 1)

    var selectedType by remember {
        mutableStateOf(currentClip?.transitionToNext?.type ?: VideoTransitionType.SMOOTH_ZOOM_IN)
    }

    var durationSec by remember {
        mutableFloatStateOf((currentClip?.transitionToNext?.durationMs ?: 800L) / 1000f)
    }

    var selectedCategory by remember { mutableStateOf<TransitionCategory?>(null) }

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
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "انتقالات AI",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "مكتبة الانتقالات بالذكاء الاصطناعي (AI Transitions)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "بين \"${currentClip?.title ?: "المقطع"}\" و \"${nextClip?.title ?: "المقطع التالي"}\"",
                            fontSize = 11.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // 2. Animated Interactive Transition Preview Box
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
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
                            Text(text = selectedType.iconEmoji, fontSize = 16.sp)
                            Text(
                                text = selectedType.nameAr,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        }
                        Surface(
                            color = StudioCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${String.format("%.1f", durationSec)}ثانية",
                                fontSize = 10.sp,
                                color = StudioCyan,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = selectedType.descriptionAr,
                        fontSize = 10.sp,
                        color = StudioTextSecondary
                    )

                    // Simulated live preview canvas
                    TransitionLivePreviewBox(
                        transitionType = selectedType,
                        clipAColor = Color(currentClip?.colorHex ?: 0xFF3B82F6),
                        clipBColor = Color(nextClip?.colorHex ?: 0xFFEC4899),
                        clipATitle = currentClip?.title ?: "مقطع 1",
                        clipBTitle = nextClip?.title ?: "مقطع 2"
                    )
                }
            }

            // 3. Category Filter Chips
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    val isAll = selectedCategory == null
                    Surface(
                        color = if (isAll) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isAll) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier.clickable { selectedCategory = null }
                    ) {
                        Text(
                            text = "الكل (16 انتقال)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isAll) StudioCyan else StudioTextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
                items(TransitionCategory.values()) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier.clickable { selectedCategory = category }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = category.iconEmoji, fontSize = 12.sp)
                            Text(
                                text = category.titleAr,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StudioCyan else StudioTextPrimary
                            )
                        }
                    }
                }
            }

            // 4. Transitions Grid / List
            val displayedTransitions = VideoTransitionType.values().filter {
                selectedCategory == null || it.category == selectedCategory
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                displayedTransitions.chunked(2).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { transition ->
                            val isSelected = selectedType == transition
                            Surface(
                                color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioDarkElevated,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioViolet else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedType = transition }
                                    .testTag("transition_opt_${transition.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(text = transition.iconEmoji, fontSize = 20.sp)
                                    Column {
                                        Text(
                                            text = transition.nameAr,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) StudioViolet else StudioTextPrimary,
                                            maxLines = 1
                                        )
                                        Text(
                                            text = transition.nameEn,
                                            fontSize = 9.sp,
                                            color = StudioTextMuted,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // 5. Duration Slider
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
                            Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = StudioAmber, modifier = Modifier.size(16.dp))
                            Text("مدة الانتقال (Transition Duration):", fontSize = 11.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                        }
                        Text("${String.format("%.1f", durationSec)} ثانية", fontSize = 11.sp, color = StudioAmber, fontWeight = FontWeight.Bold)
                    }

                    Slider(
                        value = durationSec,
                        onValueChange = { durationSec = it },
                        valueRange = 0.2f..2.0f,
                        steps = 17,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 6. Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Apply to this clip
                Button(
                    onClick = {
                        onApplyTransition(
                            targetClipIndex,
                            selectedType,
                            (durationSec * 1000L).toLong()
                        )
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1.4f)
                        .testTag("apply_transition_btn")
                ) {
                    Text("تطبيق الانتقال ✨", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Apply to all clips
                Button(
                    onClick = {
                        onApplyToAll(
                            selectedType,
                            (durationSec * 1000L).toLong()
                        )
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("apply_transition_all_btn")
                ) {
                    Icon(imageVector = Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تطبيق للكل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Remove transition button
                if (currentClip?.transitionToNext != null) {
                    IconButton(
                        onClick = {
                            onRemoveTransition(targetClipIndex)
                            onDismiss()
                        },
                        modifier = Modifier.background(StudioRose.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف الانتقال", tint = StudioRose)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransitionLivePreviewBox(
    transitionType: VideoTransitionType,
    clipAColor: Color,
    clipBColor: Color,
    clipATitle: String,
    clipBTitle: String
) {
    val infiniteTransition = rememberInfiniteTransition(label = "transition_preview")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "trans_progress"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(StudioDark)
            .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(10.dp))
    ) {
        // Render dynamic animation depending on transition type
        when (transitionType) {
            VideoTransitionType.SMOOTH_ZOOM_IN -> {
                val scaleA = 1f + progress * 0.4f
                val alphaA = (1f - progress * 1.5f).coerceIn(0f, 1f)
                val scaleB = 0.7f + progress * 0.3f

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scaleB)
                        .background(clipBColor.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(clipBTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                if (alphaA > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .scale(scaleA)
                            .alpha(alphaA)
                            .background(clipAColor.copy(alpha = 0.9f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(clipATitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            VideoTransitionType.CROSS_DISSOLVE -> {
                val alphaA = (1f - progress).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(clipBColor.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(clipBTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(alphaA)
                        .background(clipAColor.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(clipATitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            VideoTransitionType.SLIDE_LEFT -> {
                Row(modifier = Modifier.fillMaxSize()) {
                    val offsetRatio = progress.coerceIn(0f, 1f)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth((1f - offsetRatio).coerceAtLeast(0.001f))
                            .background(clipAColor.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(clipATitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1)
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .background(clipBColor.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(clipBTitle, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp, maxLines = 1)
                    }
                }
            }

            else -> {
                // Generic Cyber / Morph / Flash
                val flashAlpha = if (progress in 0.4f..0.6f) 0.8f else 0f
                val isSecondHalf = progress > 0.5f

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(if (isSecondHalf) clipBColor.copy(alpha = 0.8f) else clipAColor.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (isSecondHalf) clipBTitle else clipATitle,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                if (flashAlpha > 0f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(flashAlpha)
                            .background(Color.White)
                    )
                }
            }
        }

        // Overlay Badge
        Surface(
            color = Color.Black.copy(alpha = 0.6f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(6.dp)
        ) {
            Text(
                text = "معاينة حية ⚡",
                fontSize = 9.sp,
                color = StudioCyanLight,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}
