package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioViolet
import com.example.ui.viewmodel.PhotoEditorUiState

@Composable
fun PhotoAiQualityPanel(
    state: PhotoEditorUiState,
    onAutoMasterClick: () -> Unit,
    onUpscaleClick: (Float) -> Unit,
    onDenoiseClick: (Float) -> Unit,
    onSharpenClick: (Float) -> Unit,
    onHdrBoostClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var denoiseVal by remember { mutableFloatStateOf(if (state.denoiseLevel > 0f) state.denoiseLevel else 1.0f) }
    var sharpenVal by remember { mutableFloatStateOf(if (state.sharpnessLevel > 0f) state.sharpnessLevel else 1.6f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Quick 1-Tap AI Master Card
        Surface(
            color = StudioDarkElevated,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Brush.horizontalGradient(listOf(StudioCyan, StudioViolet))),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !state.isAiProcessing) { onAutoMasterClick() }
                .testTag("photo_auto_master_btn")
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
                            text = "تحسين شامل تلقائي (AI Auto Enhance)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "ضبط الألوان، التباين، وإزالة التشويش فوراً",
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

        // Upscale presets row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val upscaleOptions = listOf(
                Pair("ترقية 1.5x HD", 1.5f),
                Pair("ترقية 2x 2K 🔍", 2.0f),
                Pair("ترقية 4x 4K 🚀", 3.0f),
                Pair("تعزيز HDR 🌈", 0f)
            )

            upscaleOptions.forEach { opt ->
                Surface(
                    color = StudioSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = !state.isAiProcessing) {
                            if (opt.second == 0f) onHdrBoostClick() else onUpscaleClick(opt.second)
                        }
                        .testTag("photo_upscale_${opt.second}")
                ) {
                    Text(
                        text = opt.first,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (opt.second == 0f) StudioAmber else StudioViolet,
                        modifier = Modifier.padding(vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // Denoise & Sharpening Sliders Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Denoise Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioSurfaceBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
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
                        Text("تطبيق التنعيم", fontSize = 9.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sharpen Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(0.5.dp, StudioSurfaceBorder),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("شحذ الحواف 💎", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioAmber)
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
