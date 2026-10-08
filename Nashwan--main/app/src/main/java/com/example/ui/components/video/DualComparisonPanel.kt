package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Tune
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComparisonMode
import com.example.data.model.DualComparisonSettings
import com.example.data.model.FilterPreset
import com.example.data.model.FilterRepository
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
fun DualComparisonPanel(
    settings: DualComparisonSettings,
    selectedFilter: FilterPreset,
    filterIntensity: Float,
    isAiAutoEnhanced: Boolean,
    aiSharpenLevel: Float,
    onToggleComparison: () -> Unit,
    onSelectMode: (ComparisonMode) -> Unit,
    onSetSplitPosition: (Float) -> Unit,
    onSelectFilter: (FilterPreset) -> Unit,
    onResetSplit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Header & Master Enable Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(StudioCyan.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Compare,
                        contentDescription = null,
                        tint = StudioCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = "المعاينة المزدوجة (قبل / بعد) ⚖️",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = if (settings.isEnabled) "وضع المقارنة الحية نشط" else "انقر للتفعيل والمقارنة الفورية",
                        fontSize = 9.sp,
                        color = if (settings.isEnabled) StudioEmerald else StudioTextMuted
                    )
                }
            }

            Switch(
                checked = settings.isEnabled,
                onCheckedChange = { onToggleComparison() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = StudioCyan,
                    checkedTrackColor = StudioCyan.copy(alpha = 0.35f),
                    uncheckedThumbColor = StudioTextMuted,
                    uncheckedTrackColor = StudioSurfaceVariant
                ),
                modifier = Modifier.testTag("dual_comparison_master_switch")
            )
        }

        // 2. Mode Selector Cards
        Text(
            text = "اختر نمط المقارنة المفضل:",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextSecondary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComparisonMode.values().forEach { mode ->
                val isSelected = settings.mode == mode && settings.isEnabled
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            if (!settings.isEnabled) onToggleComparison()
                            onSelectMode(mode)
                        }
                        .testTag("mode_${mode.id}_card"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioDarkElevated
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = mode.iconEmoji, fontSize = 18.sp)
                        Text(
                            text = mode.titleAr.substringBefore(" "),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        // 3. Slider Position Control (Visible for Split Slider & Horizontal Split)
        if (settings.isEnabled && (settings.mode == ComparisonMode.SPLIT_SLIDER || settings.mode == ComparisonMode.HORIZONTAL_SPLIT)) {
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "موضع خط التقسيم (${(settings.splitPosition * 100).toInt()}%):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf(0.25f to "25%", 0.5f to "50%", 0.75f to "75%").forEach { (pos, label) ->
                                Surface(
                                    color = if (kotlin.math.abs(settings.splitPosition - pos) < 0.05f) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .clickable { onSetSplitPosition(pos) }
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 9.sp,
                                        color = if (kotlin.math.abs(settings.splitPosition - pos) < 0.05f) StudioCyan else StudioTextMuted
                                    )
                                }
                            }
                            IconButton(
                                onClick = onResetSplit,
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = "إعادة التوسيط",
                                    tint = StudioTextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Slider(
                        value = settings.splitPosition,
                        onValueChange = onSetSplitPosition,
                        valueRange = 0.05f..0.95f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioCyan,
                            activeTrackColor = StudioCyan,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                    )
                }
            }
        }

        // 4. Quick Live Filter Switcher Row
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "جرب مقارنة مختلف الفلاتر والتدرجات مباشرة 🎨:",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextSecondary
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(FilterRepository.presets.take(8)) { filter ->
                    val isSelected = selectedFilter.id == filter.id
                    Card(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onSelectFilter(filter) }
                            .testTag("filter_quick_pick_${filter.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyan else StudioSurfaceBorder
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = filter.iconEmoji, fontSize = 12.sp)
                            Text(
                                text = filter.nameAr,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StudioCyan else StudioTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // 5. Diff Inspector Card: Before vs After Breakdown
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "📋 جدول الفروقات البصرية المطبقة:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "🔴 قبل (الأصلي الخام)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StudioAmber)
                        Text(text = "• الفلتر: أصلي خام (Neutral)", fontSize = 9.sp, color = StudioTextMuted)
                        Text(text = "• تباين: طبيعي 1.0x", fontSize = 9.sp, color = StudioTextMuted)
                        Text(text = "• حدة: افتراضية 0%", fontSize = 9.sp, color = StudioTextMuted)
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(48.dp)
                            .background(StudioSurfaceBorder)
                    )

                    Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                        Text(text = "🟢 بعد (المعدل والمحسن)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = StudioCyan)
                        Text(text = "• الفلتر: ${selectedFilter.nameAr} (${(filterIntensity * 100).toInt()}%)", fontSize = 9.sp, color = StudioCyanLight)
                        Text(text = "• تحسين AI: ${if (isAiAutoEnhanced) "مفعل ✨" else "عادي"}", fontSize = 9.sp, color = if (isAiAutoEnhanced) StudioEmerald else StudioTextMuted)
                        Text(text = "• الحدة والوضوح: ${(aiSharpenLevel * 100).toInt()}%", fontSize = 9.sp, color = StudioTextSecondary)
                    }
                }
            }
        }
    }
}
