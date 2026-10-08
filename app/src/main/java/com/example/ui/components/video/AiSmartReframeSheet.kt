package com.example.ui.components.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AspectRatioType
import com.example.data.model.SmartCropSettings
import com.example.data.model.TrackingSubjectMode
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
fun AiSmartReframeSheet(
    settings: SmartCropSettings,
    isAnalyzing: Boolean,
    onDismiss: () -> Unit,
    onApplySmartCrop: (AspectRatioType, TrackingSubjectMode) -> Unit,
    onUpdateSettings: (SmartCropSettings) -> Unit,
    onResetSmartCrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedRatio by remember { mutableStateOf(settings.targetRatio) }
    var selectedMode by remember { mutableStateOf(settings.trackingMode) }

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
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = "القص الذكي والتوسيط",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "استوديو القص والتأطير الذكي (AI Auto-Reframe)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "توسيط العنصر المتحدث آلياً • ملاءمة أبعاد TikTok و Reels واليوتيوب",
                            fontSize = 10.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Live Simulated Subject Tracking Viewport
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDark),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = selectedMode.iconEmoji, fontSize = 16.sp)
                            Text(
                                text = "محاكاة تتبع الكادر الحية (${selectedRatio.labelAr})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                        }

                        Surface(
                            color = StudioEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "دقة التعرف: ${(settings.detectedSubjectConfidence * 100).toInt()}% ✓",
                                fontSize = 9.sp,
                                color = StudioEmerald,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Interactive Bounding Box Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0F172A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw full frame background grid
                            val boxW = when (selectedRatio) {
                                AspectRatioType.STORY_9_16 -> h * (9f / 16f)
                                AspectRatioType.SQUARE_1_1 -> h * 0.9f
                                AspectRatioType.PORTRAIT_4_5 -> h * (4f / 5f)
                                AspectRatioType.CINEMA_16_9 -> w * 0.9f
                                else -> w * 0.75f
                            }.coerceAtMost(w)

                            val boxH = when (selectedRatio) {
                                AspectRatioType.CINEMA_16_9 -> (boxW / (16f / 9f)).coerceAtMost(h)
                                else -> h * 0.92f
                            }

                            val left = (w - boxW) / 2f
                            val top = (h - boxH) / 2f

                            // Draw Crop Window
                            drawRoundRect(
                                color = StudioCyan.copy(alpha = 0.35f),
                                topLeft = Offset(left, top),
                                size = Size(boxW, boxH),
                                cornerRadius = CornerRadius(12f, 12f),
                                style = Stroke(width = 3.dp.toPx())
                            )

                            // Draw Subject Focal Tracking Box
                            val subjectSize = 46.dp.toPx()
                            val subjectCenterX = left + (boxW * settings.focalOffsetX)
                            val subjectCenterY = top + (boxH * settings.focalOffsetY)

                            drawCircle(
                                color = StudioCyan,
                                radius = 6.dp.toPx(),
                                center = Offset(subjectCenterX, subjectCenterY)
                            )

                            drawRoundRect(
                                color = StudioViolet,
                                topLeft = Offset(subjectCenterX - subjectSize / 2, subjectCenterY - subjectSize / 2),
                                size = Size(subjectSize, subjectSize),
                                cornerRadius = CornerRadius(8f, 8f),
                                style = Stroke(width = 2.dp.toPx())
                            )
                        }

                        Surface(
                            color = StudioDark.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp)
                        ) {
                            Text(
                                text = "🎯 [AI Focal Subject Centered: (${(settings.focalOffsetX * 100).toInt()}%, ${(settings.focalOffsetY * 100).toInt()}%)]",
                                fontSize = 9.sp,
                                color = StudioCyan,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            // 1. Aspect Ratio Target Selector
            Text("1. اختر نسبة أبعاد الكادر المطلوبة (Aspect Ratio):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            val ratioOptions = listOf(
                Pair(AspectRatioType.STORY_9_16, "9:16 ريلز وتيك توك 📱"),
                Pair(AspectRatioType.SQUARE_1_1, "1:1 مربع 🔲"),
                Pair(AspectRatioType.PORTRAIT_4_5, "4:5 طولي 📸"),
                Pair(AspectRatioType.CINEMA_16_9, "16:9 يوتيوب 🎬"),
                Pair(AspectRatioType.ULTRAWIDE_21_9, "21:9 شاشة عريضة 🖥️")
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(ratioOptions) { (ratio, label) ->
                    val isSelected = selectedRatio == ratio
                    Surface(
                        color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable {
                                selectedRatio = ratio
                                onApplySmartCrop(ratio, selectedMode)
                            }
                            .testTag("sheet_ratio_${ratio.name}")
                    ) {
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // 2. Tracking Mode Selector
            Text("2. نمط تتبع العنصر الذكي (Subject Tracking Mode):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TrackingSubjectMode.values().forEach { mode ->
                    val isSelected = selectedMode == mode
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) StudioViolet.copy(alpha = 0.2f) else StudioSurface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioViolet else StudioSurfaceBorder
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedMode = mode
                                onApplySmartCrop(selectedRatio, mode)
                            }
                            .testTag("tracking_mode_${mode.id}")
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = mode.iconEmoji, fontSize = 20.sp)
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = mode.titleAr,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) StudioViolet else StudioTextPrimary
                                )
                                Text(
                                    text = mode.descriptionAr,
                                    fontSize = 9.sp,
                                    color = StudioTextMuted
                                )
                            }
                            if (isSelected) {
                                Text("نشط ✓", fontSize = 10.sp, color = StudioViolet, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // 3. Fine-Tune Sliders (Smoothness & Center Offset)
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Smoothness Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("نعومة حركة الكاميرا (Pan Smoothness):", fontSize = 10.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                        Text("${(settings.trackingSmoothness * 100).toInt()}%", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = settings.trackingSmoothness,
                        onValueChange = { onUpdateSettings(settings.copy(trackingSmoothness = it)) },
                        valueRange = 0.3f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = StudioCyan, activeTrackColor = StudioCyan, inactiveTrackColor = StudioSurfaceVariant),
                        modifier = Modifier.height(20.dp)
                    )

                    // X-Axis Focal Offset
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("تعديل موضع المركز الأفقي (X-Axis):", fontSize = 10.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                        Text("${(settings.focalOffsetX * 100).toInt()}%", fontSize = 10.sp, color = StudioViolet, fontWeight = FontWeight.Bold)
                    }
                    Slider(
                        value = settings.focalOffsetX,
                        onValueChange = { onUpdateSettings(settings.copy(focalOffsetX = it)) },
                        valueRange = 0.1f..0.9f,
                        colors = SliderDefaults.colors(thumbColor = StudioViolet, activeTrackColor = StudioViolet, inactiveTrackColor = StudioSurfaceVariant),
                        modifier = Modifier.height(20.dp)
                    )
                }
            }

            // Action Button
            Button(
                onClick = { onApplySmartCrop(selectedRatio, selectedMode) },
                enabled = !isAnalyzing,
                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("confirm_smart_crop_btn")
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = StudioDark, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("جاري التحليل والتوسيط...", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioDark, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تطبيق وتوسيط الكادر بالذكاء الاصطناعي 🎯", color = StudioDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
