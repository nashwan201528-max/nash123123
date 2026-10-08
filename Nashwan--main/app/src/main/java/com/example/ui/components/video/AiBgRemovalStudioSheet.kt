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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiBgRemovalSettings
import com.example.data.model.BgReplacementType
import com.example.data.model.VirtualBackgroundItem
import com.example.data.model.VirtualBackgroundLibrary
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiBgRemovalStudioSheet(
    settings: AiBgRemovalSettings,
    isProcessing: Boolean,
    onDismiss: () -> Unit,
    onUpdateSettings: (AiBgRemovalSettings) -> Unit,
    onGenerateCustomBg: (String) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var localSettings by remember(settings) { mutableStateOf(settings) }
    var promptInput by remember { mutableStateOf("") }

    val rimColors = listOf(
        0xFF06B6D4 to "أزرق نيون",
        0xFFF59E0B to "توهج دافئ",
        0xFFEC4899 to "وردي سينمائي",
        0xFF8B5CF6 to "بنفسجي",
        0xFF10B981 to "زمردي",
        0xFFFFFFFF to "أبيض طبيعي"
    )

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
            // 1. Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(StudioEmerald.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.ContentCut, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "استوديو عزل وتبديل الخلفيات AI ✂️",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "عزل الذكاء الاصطناعي للأجسام بدون كروما خضراء",
                            fontSize = 10.sp,
                            color = StudioEmerald
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // 2. Master Toggle Card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (localSettings.isEnabled) StudioEmerald.copy(alpha = 0.15f) else StudioSurfaceVariant
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (localSettings.isEnabled) StudioEmerald else StudioSurfaceBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "تفعيل العزل التلقائي (Green-Screen Free)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "دقة العزل العصبي: ${(localSettings.aiSegmentationConfidence * 100).toInt()}% بدقة خصلات الشعر",
                            fontSize = 9.sp,
                            color = if (localSettings.isEnabled) StudioEmerald else StudioTextMuted
                        )
                    }

                    Switch(
                        checked = localSettings.isEnabled,
                        onCheckedChange = {
                            localSettings = localSettings.copy(isEnabled = it)
                            onUpdateSettings(localSettings)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = StudioEmerald,
                            checkedTrackColor = StudioEmerald.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier.testTag("studio_bg_master_switch")
                    )
                }
            }

            // 3. Replacement Mode Cards
            Text(text = "اختر وضع استبدال الخلفية:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextSecondary)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BgReplacementType.values().forEach { type ->
                    val isSelected = localSettings.replacementType == type
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                localSettings = localSettings.copy(replacementType = type, isEnabled = true)
                                onUpdateSettings(localSettings)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) StudioEmerald.copy(alpha = 0.25f) else StudioDark
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioEmerald else StudioSurfaceBorder
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(text = type.iconEmoji, fontSize = 16.sp)
                            Text(
                                text = type.titleAr.substringBefore(" "),
                                fontSize = 9.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) StudioEmerald else StudioTextPrimary
                            )
                        }
                    }
                }
            }

            // 4. Virtual Sets Grid (for Virtual Sets / Solid Gradients)
            if (localSettings.replacementType == BgReplacementType.VIRTUAL_STUDIO || localSettings.replacementType == BgReplacementType.SOLID_GRADIENT) {
                Text(text = "اختر المشهد الافتراضي:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(VirtualBackgroundLibrary.presets) { bgItem ->
                        val isSelected = localSettings.selectedBgItem.id == bgItem.id
                        val gradColors = bgItem.previewGradientColors.map { Color(it) }

                        Card(
                            modifier = Modifier
                                .width(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    localSettings = localSettings.copy(selectedBgItem = bgItem)
                                    onUpdateSettings(localSettings)
                                },
                            colors = CardDefaults.cardColors(containerColor = StudioDark),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isSelected) 2.dp else 1.dp,
                                if (isSelected) StudioEmerald else StudioSurfaceBorder
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(6.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Brush.linearGradient(gradColors)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = bgItem.iconEmoji, fontSize = 20.sp)
                                }
                                Text(
                                    text = bgItem.titleAr,
                                    fontSize = 9.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioEmerald else StudioTextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = bgItem.categoryAr,
                                    fontSize = 8.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }
                    }
                }
            }

            // 5. Blur Bokeh Control
            if (localSettings.replacementType == BgReplacementType.BLUR_BOKEH) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "🌫️ شدة التمويه البؤري (Blur):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            Text(text = "${(localSettings.blurIntensity * 100).toInt()}%", fontSize = 10.sp, color = StudioEmerald)
                        }
                        Slider(
                            value = localSettings.blurIntensity,
                            onValueChange = {
                                localSettings = localSettings.copy(blurIntensity = it)
                                onUpdateSettings(localSettings)
                            },
                            valueRange = 0.1f..1.0f,
                            colors = SliderDefaults.colors(thumbColor = StudioEmerald, activeTrackColor = StudioEmerald)
                        )
                    }
                }
            }

            // 6. AI Prompt Custom Background Generator
            if (localSettings.replacementType == BgReplacementType.AI_CUSTOM) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = StudioDark),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "✨ كتابة وصف لخلفية مخصصة بالذكاء الاصطناعي:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioViolet
                        )

                        OutlinedTextField(
                            value = promptInput,
                            onValueChange = { promptInput = it },
                            placeholder = { Text("مثال: شاطئ استوائي مع نخيل وشمس ساطعة...", fontSize = 10.sp, color = StudioTextMuted) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioViolet,
                                unfocusedBorderColor = StudioSurfaceBorder,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Quick prompt suggestion pills
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("استوديو بودكاست 🎙️", "شاطئ بالي 🏖️", "مدينة نيون 🌆").forEach { suggestion ->
                                Surface(
                                    color = StudioViolet.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier
                                        .clickable { promptInput = suggestion }
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(text = suggestion, fontSize = 9.sp, color = StudioViolet)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (promptInput.isNotBlank()) onGenerateCustomBg(promptInput)
                            },
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("جاري توليد وضبط المشهد...", fontSize = 11.sp, color = Color.White)
                            } else {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("توليد وتطبيق الخلفية بالذكاء الاصطناعي ✨", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            // 7. Lighting & Edge Refinement (Rim Lighting & Soft Edges)
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDark),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                            Text(text = "إضاءة الحواف السينمائية (Rim Light):", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                        }
                        Switch(
                            checked = localSettings.isRimLightingEnabled,
                            onCheckedChange = {
                                localSettings = localSettings.copy(isRimLightingEnabled = it)
                                onUpdateSettings(localSettings)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = StudioCyan, checkedTrackColor = StudioCyan.copy(alpha = 0.35f))
                        )
                    }

                    if (localSettings.isRimLightingEnabled) {
                        // Color pills
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(rimColors) { (hex, label) ->
                                val isSelected = localSettings.rimLightColorHex == hex
                                Surface(
                                    color = if (isSelected) Color(hex).copy(alpha = 0.3f) else StudioDarkElevated,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(hex) else StudioSurfaceBorder),
                                    modifier = Modifier
                                        .clickable {
                                            localSettings = localSettings.copy(rimLightColorHex = hex)
                                            onUpdateSettings(localSettings)
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(modifier = Modifier.size(10.dp).background(Color(hex), CircleShape))
                                        Text(text = label, fontSize = 9.sp, color = if (isSelected) Color(hex) else StudioTextMuted)
                                    }
                                }
                            }
                        }

                        // Intensity slider
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "شدة توهج الحواف:", fontSize = 9.sp, color = StudioTextMuted)
                            Text(text = "${(localSettings.rimLightIntensity * 100).toInt()}%", fontSize = 9.sp, color = StudioCyan)
                        }
                        Slider(
                            value = localSettings.rimLightIntensity,
                            onValueChange = {
                                localSettings = localSettings.copy(rimLightIntensity = it)
                                onUpdateSettings(localSettings)
                            },
                            valueRange = 0.1f..1.0f,
                            colors = SliderDefaults.colors(thumbColor = StudioCyan, activeTrackColor = StudioCyan)
                        )
                    }

                    // Edge Softness / Feathering
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "نعومة وانسيابية الحواف (Edge Softness):", fontSize = 9.sp, color = StudioTextMuted)
                        Text(text = "${(localSettings.edgeFeathering * 100).toInt()}%", fontSize = 9.sp, color = StudioEmerald)
                    }
                    Slider(
                        value = localSettings.edgeFeathering,
                        onValueChange = {
                            localSettings = localSettings.copy(edgeFeathering = it)
                            onUpdateSettings(localSettings)
                        },
                        valueRange = 0.1f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = StudioEmerald, activeTrackColor = StudioEmerald)
                    )
                }
            }

            // 8. Bottom Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onReset,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = StudioTextMuted, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إعادة ضبط ↺", fontSize = 11.sp, color = StudioTextMuted)
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioEmerald),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(2f)
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("اعتماد وتطبيق العزل ✨", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }
    }
}
