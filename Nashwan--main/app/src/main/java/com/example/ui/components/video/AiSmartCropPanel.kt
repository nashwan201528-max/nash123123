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
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AspectRatioType
import com.example.data.model.SmartCropSettings
import com.example.data.model.TrackingSubjectMode
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

@Composable
fun AiSmartCropPanel(
    selectedRatio: AspectRatioType,
    smartCropSettings: SmartCropSettings,
    isAnalyzing: Boolean,
    onSelectRatio: (AspectRatioType) -> Unit,
    onSelectTrackingMode: (TrackingSubjectMode) -> Unit,
    onToggleAutoTracking: () -> Unit,
    onOpenFullSheet: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & Open Full Studio Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.Crop, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                Text(
                    text = "القص الذكي وتوسيط العنصر بالذكاء الاصطناعي (AI Auto-Reframe):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = onOpenFullSheet,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("open_smart_crop_sheet_btn")
                ) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("استوديو التأطير 🎯", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
                }

                Surface(
                    color = StudioRose.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.clickable { onReset() }
                ) {
                    Text(
                        text = "الأصلي ↺",
                        fontSize = 10.sp,
                        color = StudioRose,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // 2. Aspect Ratio Presets Chips
        val ratioPresets = listOf(
            Pair(AspectRatioType.STORY_9_16, "9:16 تيك توك وريلز 📱"),
            Pair(AspectRatioType.SQUARE_1_1, "1:1 مربع منشور 🔲"),
            Pair(AspectRatioType.PORTRAIT_4_5, "4:5 طولي انستغرام 📸"),
            Pair(AspectRatioType.CINEMA_16_9, "16:9 سينمائي يوتيوب 🎬"),
            Pair(AspectRatioType.ULTRAWIDE_21_9, "21:9 شاشة عريضة 🖥️")
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(ratioPresets) { (ratio, label) ->
                val isSelected = selectedRatio == ratio
                Surface(
                    color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .clickable(enabled = !isAnalyzing) { onSelectRatio(ratio) }
                        .testTag("smart_ratio_${ratio.name}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary
                        )
                        if (isSelected && isAnalyzing) {
                            CircularProgressIndicator(color = StudioCyan, modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp)
                        }
                    }
                }
            }
        }

        // 3. Subject Tracking Mode Picker Bar
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(StudioViolet.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.CenterFocusStrong, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = smartCropSettings.trackingMode.titleAr,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "دقة التوسيط: ${(smartCropSettings.detectedSubjectConfidence * 100).toInt()}% • النعومة: ${(smartCropSettings.trackingSmoothness * 100).toInt()}%",
                            fontSize = 9.sp,
                            color = StudioEmerald
                        )
                    }
                }

                Switch(
                    checked = smartCropSettings.isAiAutoTrackingEnabled,
                    onCheckedChange = { onToggleAutoTracking() },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = StudioCyan),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
