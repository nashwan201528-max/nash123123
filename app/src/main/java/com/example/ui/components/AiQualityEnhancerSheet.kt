package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.MotionPhotosAuto
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun AiQualityEnhancerSheet(
    onDismiss: () -> Unit,
    isProcessing: Boolean,
    onAutoMasterClick: () -> Unit,
    onUpscaleClick: (scaleFactor: Float) -> Unit,
    onDenoiseClick: (strength: Float) -> Unit,
    onSharpenClick: (strength: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var denoiseSlider by remember { mutableFloatStateOf(1.0f) }
    var sharpenSlider by remember { mutableFloatStateOf(1.6f) }
    var selectedUpscaleIndex by remember { mutableIntStateOf(1) } // 0: 1.5x (1080p), 1: 2x (2K), 2: 4x (4K Ultra)

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
                .padding(bottom = 36.dp)
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
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "معزز الجودة",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "تحسين الجودة بالذكاء الاصطناعي 🚀",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "ترقية الدقة، إزالة الضوضاء وشحذ التفاصيل",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            if (isProcessing) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioSurface),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            color = StudioCyan,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 3.dp
                        )
                        Column {
                            Text(
                                text = "جاري المعالجة بالذكاء الاصطناعي...",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan
                            )
                            Text(
                                text = "يتم تطبيق خوارزميات التعلم العميق وتحسين البكسلات.",
                                fontSize = 10.sp,
                                color = StudioTextMuted
                            )
                        }
                    }
                }
            }

            // 1. One-Tap AI Auto Master Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Brush.horizontalGradient(listOf(StudioCyan, StudioViolet))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isProcessing) { onAutoMasterClick() }
                    .testTag("ai_auto_master_btn")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
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
                                .size(40.dp)
                                .background(StudioCyan.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "التحسين التلقائي",
                                tint = StudioCyan,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "التحسين التلقائي الشامل (AI Auto Master)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                Box(
                                    modifier = Modifier
                                        .background(StudioEmerald.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                ) {
                                    Text("بضغطة واحدة", fontSize = 9.sp, color = StudioEmerald, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "يصحح التعريض، يزيل الضبابية، يعزز التباين والحدة تلقائياً",
                                fontSize = 10.sp,
                                color = StudioTextSecondary
                            )
                        }
                    }
                    Button(
                        onClick = onAutoMasterClick,
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("تطبيق ✨", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // 2. AI Upscaling & Super Resolution Section
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HighQuality,
                            contentDescription = "ترقية الدقة",
                            tint = StudioViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "ترقية الدقة الفائقة (AI Super-Resolution Upscaling)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }

                    Text(
                        text = "اختر معامل مضاعفة الدقة لإعادة بناء تفاصيل الصورة بجودة كريستالية:",
                        fontSize = 10.sp,
                        color = StudioTextSecondary
                    )

                    val upscaleOptions = listOf(
                        Triple("1.5x HD", 1.5f, "1080p FHD"),
                        Triple("2x 2K QHD", 2.0f, "موصى به"),
                        Triple("4x 4K UHD", 3.0f, "أعلى وضوح")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        upscaleOptions.forEachIndexed { index, option ->
                            val isSelected = selectedUpscaleIndex == index
                            Surface(
                                color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioViolet else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedUpscaleIndex = index }
                                    .testTag("upscale_opt_$index")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = option.first,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) StudioViolet else StudioTextPrimary
                                    )
                                    Text(
                                        text = option.third,
                                        fontSize = 9.sp,
                                        color = if (isSelected) StudioCyanLight else StudioTextMuted
                                    )
                                }
                            }
                        }
                    }

                    Button(
                        onClick = { onUpscaleClick(upscaleOptions[selectedUpscaleIndex].second) },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_upscale_btn")
                    ) {
                        Text(
                            text = "ترقية الدقة إلى ${upscaleOptions[selectedUpscaleIndex].first} 🔍",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // 3. AI Denoise & Noise Reduction Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "إزالة الضوضاء",
                                tint = StudioEmerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "تقليل الضوضاء والتحبب (AI Noise Reduction)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        }

                        Text(
                            text = "${(denoiseSlider * 100).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioEmerald
                        )
                    }

                    Text(
                        text = "فلتر حواف متقدم يزيل التشويش الليلي و ISO العالي مع الحفاظ على وضوح المعالم.",
                        fontSize = 10.sp,
                        color = StudioTextSecondary
                    )

                    Slider(
                        value = denoiseSlider,
                        onValueChange = { denoiseSlider = it },
                        valueRange = 0.3f..2.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioEmerald,
                            activeTrackColor = StudioEmerald,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { onDenoiseClick(denoiseSlider) },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioEmerald),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_denoise_btn")
                    ) {
                        Text(
                            text = "تطبيق إزالة الضوضاء والتحبب 🧼",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark
                        )
                    }
                }
            }

            // 4. AI Sharpening & Micro-Contrast Card
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MotionPhotosAuto,
                                contentDescription = "تحسين الحدة",
                                tint = StudioAmber,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "تحسين الحدة والوضوح (AI Sharpening & Clarity)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        }

                        Text(
                            text = "${(sharpenSlider * 50).toInt()}%",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioAmber
                        )
                    }

                    Text(
                        text = "تقنية قناع اللابلاسيان Unsharp Mask لإبراز تفاصيل العيون والشعر والملامح الدقيقة.",
                        fontSize = 10.sp,
                        color = StudioTextSecondary
                    )

                    Slider(
                        value = sharpenSlider,
                        onValueChange = { sharpenSlider = it },
                        valueRange = 0.5f..3.0f,
                        colors = SliderDefaults.colors(
                            thumbColor = StudioAmber,
                            activeTrackColor = StudioAmber,
                            inactiveTrackColor = StudioSurfaceVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { onSharpenClick(sharpenSlider) },
                        enabled = !isProcessing,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioAmber),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("apply_sharpen_btn")
                    ) {
                        Text(
                            text = "تطبيق تحسين الحدة والتفاصيل 💎",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark
                        )
                    }
                }
            }
        }
    }
}
