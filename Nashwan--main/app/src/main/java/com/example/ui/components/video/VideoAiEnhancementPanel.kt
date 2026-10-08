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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioViolet
import com.example.ui.viewmodel.VideoEditorUiState

@Composable
fun VideoAiEnhancementPanel(
    state: VideoEditorUiState,
    onAutoMasterClick: () -> Unit,
    onUpscaleClick: (Float) -> Unit,
    onDenoiseClick: (Float) -> Unit,
    onSharpenClick: (Float) -> Unit,
    onOpenSheetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var denoiseVal by remember { mutableFloatStateOf(if (state.aiDenoiseLevel > 0f) state.aiDenoiseLevel else 0.9f) }
    var sharpenVal by remember { mutableFloatStateOf(if (state.aiSharpenLevel > 0f) state.aiSharpenLevel else 1.5f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & AI Status Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "تحسين الفيديو AI",
                    tint = StudioCyan,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "أدوات تحسين جودة الفيديو بالذكاء الاصطناعي",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Surface(
                color = if (state.isAiAutoEnhanced) StudioEmerald.copy(alpha = 0.2f) else StudioSurfaceVariant,
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(
                    0.5.dp,
                    if (state.isAiAutoEnhanced) StudioEmerald else StudioSurfaceBorder
                )
            ) {
                Text(
                    text = if (state.isAiAutoEnhanced) "✓ مُحسن تلقائياً" else state.aiUpscaleLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (state.isAiAutoEnhanced) StudioEmerald else StudioCyan,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // 2. Quick 1-Tap Auto Master Button
        Surface(
            color = StudioDarkElevated,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Brush.horizontalGradient(listOf(StudioCyan, StudioViolet))),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !state.isAiProcessing) { onAutoMasterClick() }
                .testTag("video_auto_master_btn")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(StudioCyan.copy(alpha = 0.2f), RoundedCornerShape(6.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                    }
                    Column {
                        Text(
                            text = "الماسترينغ الذكي الشامل (AI Auto Master)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "تحسين تباين الإطارات، الحدة وتقليل التحبب دفعة واحدة",
                            fontSize = 9.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
                Button(
                    onClick = onAutoMasterClick,
                    enabled = !state.isAiProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("تطبيق ✨", color = StudioDark, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 3. Upscaling Quick Selection Row
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "ترقية الدقة الفائقة (AI Upscaling):",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = StudioViolet
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val upscales = listOf(
                    Pair("1080p FHD (1.5x)", 1.5f),
                    Pair("2K QHD (2.0x)", 2.0f),
                    Pair("4K UHD (3.0x)", 3.0f)
                )
                upscales.forEach { item ->
                    val isSelected = state.aiUpscaleLabel.startsWith(item.first.take(3))
                    Surface(
                        color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioViolet else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = !state.isAiProcessing) { onUpscaleClick(item.second) }
                            .testTag("video_upscale_${item.second}")
                    ) {
                        Text(
                            text = item.first,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) StudioViolet else StudioTextPrimary,
                            modifier = Modifier.padding(vertical = 7.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // 4. Sliders for Denoise & Sharpening
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Denoise Column
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioSurfaceBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("إزالة الضوضاء 🧼", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioEmerald)
                        Text("${(denoiseVal * 100).toInt()}%", fontSize = 10.sp, color = StudioEmerald, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = denoiseVal,
                        onValueChange = { denoiseVal = it },
                        valueRange = 0.3f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioEmerald,
                            activeTrackColor = StudioEmerald,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                    Button(
                        onClick = { onDenoiseClick(denoiseVal) },
                        enabled = !state.isAiProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioEmerald),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 2.dp)
                    ) {
                        Text("تطبيق الفلتر", fontSize = 9.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sharpening Column
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioSurfaceBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("تحسين الحدة 💎", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioAmber)
                        Text("${(sharpenVal * 50).toInt()}%", fontSize = 10.sp, color = StudioAmber, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = sharpenVal,
                        onValueChange = { sharpenVal = it },
                        valueRange = 0.5f..3.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.height(20.dp)
                    )
                    Button(
                        onClick = { onSharpenClick(sharpenVal) },
                        enabled = !state.isAiProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 2.dp)
                    ) {
                        Text("تطبيق الشحذ", fontSize = 9.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
