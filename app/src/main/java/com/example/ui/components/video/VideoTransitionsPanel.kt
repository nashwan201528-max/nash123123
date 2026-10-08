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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun VideoTransitionsPanel(
    clips: List<VideoClip>,
    selectedClipIndex: Int,
    onOpenTransitionPicker: (clipIndex: Int) -> Unit,
    onQuickApplyPresetToAll: (VideoTransitionType) -> Unit,
    onRemoveAllTransitions: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf<TransitionCategory?>(null) }
    val activeClipIndex = if (selectedClipIndex in 0 until (clips.size - 1).coerceAtLeast(1)) selectedClipIndex else 0
    val activeClip = clips.getOrNull(activeClipIndex)
    val nextClip = clips.getOrNull(activeClipIndex + 1)
    val currentTransition = activeClip?.transitionToNext

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & Open Full Preview Studio Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(16.dp))
                Text(
                    text = "مكتبة الانتقالات السينمائية (16 انتقال احترافي):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = { onOpenTransitionPicker(activeClipIndex) },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("open_transition_studio_btn")
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("المعاينة الحية 🎬", fontSize = 10.sp, color = StudioViolet, fontWeight = FontWeight.Bold)
                }

                Surface(
                    color = StudioRose.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable { onRemoveAllTransitions() }
                ) {
                    Text(
                        text = "حذف الكل ✕",
                        fontSize = 10.sp,
                        color = StudioRose,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // 2. Timeline Cuts Map (خريطة نقاط القطع بين المقاطع)
        if (clips.size > 1) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until clips.size - 1) {
                    val clipA = clips[i]
                    val clipB = clips[i + 1]
                    val transition = clipA.transitionToNext
                    val isTarget = i == activeClipIndex

                    item {
                        Surface(
                            color = if (isTarget) StudioViolet.copy(alpha = 0.25f) else StudioDarkElevated,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isTarget) 1.5.dp else 1.dp,
                                if (isTarget) StudioViolet else StudioSurfaceBorder
                            ),
                            modifier = Modifier
                                .clickable { onOpenTransitionPicker(i) }
                                .testTag("cut_point_btn_$i")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = clipA.title,
                                    fontSize = 10.sp,
                                    color = if (isTarget) StudioViolet else StudioTextSecondary,
                                    fontWeight = if (isTarget) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )

                                // Transition Icon Pill
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(
                                            if (transition != null) StudioViolet else StudioSurfaceVariant,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (transition != null) {
                                        Text(text = transition.type.iconEmoji, fontSize = 13.sp)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Add,
                                            contentDescription = "إضافة انتقال",
                                            tint = StudioCyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Text(
                                    text = clipB.title,
                                    fontSize = 10.sp,
                                    color = if (isTarget) StudioViolet else StudioTextSecondary,
                                    fontWeight = if (isTarget) FontWeight.Bold else FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "قم بإضافة أو تقسيم مقاطع الفيديو لتطبيق الانتقالات السينمائية بينها.",
                    fontSize = 11.sp,
                    color = StudioTextMuted,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // 3. Category Filter Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            item {
                val isAll = selectedCategory == null
                Surface(
                    color = if (isAll) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isAll) StudioViolet else StudioSurfaceBorder),
                    modifier = Modifier.clickable { selectedCategory = null }
                ) {
                    Text(
                        text = "الكل (16)",
                        fontSize = 10.sp,
                        fontWeight = if (isAll) FontWeight.Bold else FontWeight.Normal,
                        color = if (isAll) StudioViolet else StudioTextPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            items(TransitionCategory.values()) { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioViolet else StudioSurfaceBorder),
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(cat.iconEmoji, fontSize = 11.sp)
                        Text(
                            text = cat.titleAr,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioViolet else StudioTextPrimary
                        )
                    }
                }
            }
        }

        // 4. One-Tap Transition Selector Grid / Carousel
        val filteredTransitions = VideoTransitionType.values().filter {
            selectedCategory == null || it.category == selectedCategory
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(filteredTransitions) { transitionType ->
                val isCurrent = currentTransition?.type == transitionType
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) StudioViolet.copy(alpha = 0.3f) else StudioSurfaceVariant
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCurrent) StudioViolet else StudioSurfaceBorder
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .clickable {
                            onQuickApplyPresetToAll(transitionType)
                        }
                        .testTag("trans_card_${transitionType.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(text = transitionType.iconEmoji, fontSize = 16.sp)
                        Text(
                            text = transitionType.nameAr,
                            fontSize = 10.sp,
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCurrent) StudioViolet else StudioTextPrimary,
                            maxLines = 1
                        )
                        Text(
                            text = "${transitionType.defaultDurationMs / 1000f}ث",
                            fontSize = 8.sp,
                            color = StudioTextMuted
                        )
                    }
                }
            }
        }
    }
}
