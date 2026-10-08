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
import androidx.compose.material.icons.filled.BlurOn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
fun AiBgRemovalPanel(
    settings: AiBgRemovalSettings,
    isProcessing: Boolean,
    onToggleEnabled: () -> Unit,
    onSelectType: (BgReplacementType) -> Unit,
    onSelectBackground: (VirtualBackgroundItem) -> Unit,
    onSetBlur: (Float) -> Unit,
    onSetEdgeFeathering: (Float) -> Unit,
    onToggleRimLight: () -> Unit,
    onGenerateCustomBg: (String) -> Unit,
    onOpenStudioSheet: () -> Unit,
    onResetSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customPromptInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header with Master Switch & Open Studio Sheet
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
                        .background(StudioEmerald.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        tint = StudioEmerald,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        text = "عزل واستبدال الخلفية AI ✂️",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = if (settings.isEnabled) "عزل ذكي بدون كروما (${(settings.aiSegmentationConfidence * 100).toInt()}%)" else "عزل العناصر وتبديل الخلفيات",
                        fontSize = 9.sp,
                        color = if (settings.isEnabled) StudioEmerald else StudioTextMuted
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onOpenStudioSheet,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmerald),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.testTag("open_bg_studio_sheet_btn")
                ) {
                    Icon(imageVector = Icons.Default.Tune, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("الاستوديو 🎛️", fontSize = 10.sp, color = StudioEmerald, fontWeight = FontWeight.Bold)
                }

                Switch(
                    checked = settings.isEnabled,
                    onCheckedChange = { onToggleEnabled() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = StudioEmerald,
                        checkedTrackColor = StudioEmerald.copy(alpha = 0.35f),
                        uncheckedThumbColor = StudioTextMuted,
                        uncheckedTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.testTag("bg_removal_master_switch")
                )
            }
        }

        // 2. Mode Selector Cards
        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(BgReplacementType.values()) { type ->
                val isSelected = settings.replacementType == type && settings.isEnabled
                Card(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            if (!settings.isEnabled) onToggleEnabled()
                            onSelectType(type)
                        }
                        .testTag("bg_type_${type.id}"),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) StudioEmerald.copy(alpha = 0.25f) else StudioDarkElevated
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioEmerald else StudioSurfaceBorder
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = type.iconEmoji, fontSize = 12.sp)
                        Text(
                            text = type.titleAr.substringBefore(" "),
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioEmerald else StudioTextPrimary
                        )
                    }
                }
            }
        }

        // 3. Dynamic Controls based on selected mode
        AnimatedVisibility(visible = settings.isEnabled) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (settings.replacementType) {
                    BgReplacementType.BLUR_BOKEH -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "🌫️ شدة التمويه السينمائي (Bokeh): ${(settings.blurIntensity * 100).toInt()}%",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = StudioTextPrimary
                                    )
                                    Text(
                                        text = if (settings.blurIntensity > 0.8f) "طمس فائق" else "طمس طبيعي",
                                        fontSize = 9.sp,
                                        color = StudioCyan
                                    )
                                }
                                Slider(
                                    value = settings.blurIntensity,
                                    onValueChange = onSetBlur,
                                    valueRange = 0.1f..1.0f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = StudioEmerald,
                                        activeTrackColor = StudioEmerald,
                                        inactiveTrackColor = StudioSurfaceVariant
                                    ),
                                    modifier = Modifier.fillMaxWidth().height(22.dp)
                                )
                            }
                        }
                    }

                    BgReplacementType.VIRTUAL_STUDIO, BgReplacementType.SOLID_GRADIENT -> {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "اختر الخلفية الافتراضية المراد الدمج معها:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextSecondary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(VirtualBackgroundLibrary.presets) { bgItem ->
                                    val isBgSelected = settings.selectedBgItem.id == bgItem.id
                                    val gradColors = bgItem.previewGradientColors.map { Color(it) }

                                    Card(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { onSelectBackground(bgItem) }
                                            .testTag("bg_item_${bgItem.id}"),
                                        shape = RoundedCornerShape(10.dp),
                                        border = androidx.compose.foundation.BorderStroke(
                                            if (isBgSelected) 2.dp else 1.dp,
                                            if (isBgSelected) StudioEmerald else StudioSurfaceBorder
                                        ),
                                        colors = CardDefaults.cardColors(containerColor = StudioDarkElevated)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(6.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(48.dp, 32.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(Brush.linearGradient(gradColors)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(text = bgItem.iconEmoji, fontSize = 14.sp)
                                            }
                                            Text(
                                                text = bgItem.titleAr.take(12),
                                                fontSize = 9.sp,
                                                fontWeight = if (isBgSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isBgSelected) StudioEmerald else StudioTextPrimary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BgReplacementType.AI_CUSTOM -> {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "✨ صمم خلفية ذكاء اصطناعي مخصصة (AI Studio Generator):",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioViolet
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = customPromptInput,
                                        onValueChange = { customPromptInput = it },
                                        placeholder = { Text("مثال: استوديو أخبار نيون مستقبلي...", fontSize = 9.sp, color = StudioTextMuted) },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = StudioViolet,
                                            unfocusedBorderColor = StudioSurfaceBorder,
                                            focusedTextColor = StudioTextPrimary,
                                            unfocusedTextColor = StudioTextPrimary
                                        ),
                                        modifier = Modifier.weight(1f).height(44.dp)
                                    )

                                    Button(
                                        onClick = {
                                            if (customPromptInput.isNotBlank()) {
                                                onGenerateCustomBg(customPromptInput)
                                            }
                                        },
                                        enabled = !isProcessing,
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(44.dp).testTag("generate_custom_ai_bg_btn")
                                    ) {
                                        if (isProcessing) {
                                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                        } else {
                                            Text("توليد 🔮", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    BgReplacementType.TRANSPARENT_CUTOUT -> {
                        Surface(
                            color = StudioDarkElevated,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text("🔲", fontSize = 16.sp)
                                Text(
                                    text = "تم تفريغ الخلفية بنمط قناة ألفا (Alpha Transparent) جاهز للتركيب.",
                                    fontSize = 10.sp,
                                    color = StudioEmerald
                                )
                            }
                        }
                    }
                }

                // Quick Rim Light & Feathering Pills
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (settings.isRimLightingEnabled) StudioCyan.copy(alpha = 0.2f) else StudioDarkElevated,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (settings.isRimLightingEnabled) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { onToggleRimLight() }
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("💡", fontSize = 10.sp)
                            Text(
                                text = if (settings.isRimLightingEnabled) "إضاءة الحواف (Rim Light): نشطة" else "إضاءة الحواف: معطلة",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (settings.isRimLightingEnabled) StudioCyan else StudioTextMuted
                            )
                        }
                    }

                    Text(
                        text = "نعومة الحواف: ${(settings.edgeFeathering * 100).toInt()}% 🎯",
                        fontSize = 9.sp,
                        color = StudioTextSecondary
                    )
                }
            }
        }
    }
}
