package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.ui.theme.StudioViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiToolsBottomSheet(
    onDismiss: () -> Unit,
    isProcessing: Boolean,
    onAutoEnhanceClick: () -> Unit,
    onSuperResolutionClick: () -> Unit,
    onDenoiseClick: () -> Unit,
    onHdrBoostClick: () -> Unit,
    onRemoveBackgroundClick: () -> Unit,
    onReplaceBackgroundClick: (String) -> Unit,
    onReplaceBackgroundWithPrompt: ((String) -> Unit)? = null,
    onMagicEraserClick: (() -> Unit)? = null,
    onGenerateNewImageClick: ((String) -> Unit)? = null,
    onAnalyzeContentClick: () -> Unit,
    isBgRemoved: Boolean = false,
    currentBgReplacement: String = "TRANSPARENT"
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var customBgPrompt by remember { mutableStateOf("") }
    var textToImagePrompt by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StudioDarkElevated,
        contentColor = StudioTextPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
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
                                brush = Brush.radialGradient(listOf(StudioCyan, StudioMagenta)),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "الذكاء الاصطناعي",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "أدوات الذكاء الاصطناعي الخارقة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "إنشاء، عزل الخلفية، ممحاة سحرية، ورفع الجودة",
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioCyan
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "إغلاق",
                        tint = StudioTextMuted
                    )
                }
            }

            if (isProcessing) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        CircularProgressIndicator(
                            color = StudioCyan,
                            modifier = Modifier.size(26.dp)
                        )
                        Column {
                            Text(
                                text = "جاري المعالجة بالذكاء الاصطناعي...",
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "يتم توليد وتحسين البكسلات وعزل العناصر بدقة",
                                fontSize = 11.sp,
                                color = StudioTextMuted
                            )
                        }
                    }
                }
            }

            // Section 1: AI Text to Image Generator
            Text(
                text = "🎨 إنشاء صورة جديدة بالذكاء الاصطناعي (Text to Image)",
                style = MaterialTheme.typography.labelLarge,
                color = StudioCyan,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "اكتب وصفاً دقيقاً لما تتخيله وسيقوم الذكاء الاصطناعي بإنشائه:",
                        fontSize = 11.sp,
                        color = StudioTextMuted
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = textToImagePrompt,
                            onValueChange = { textToImagePrompt = it },
                            placeholder = { Text("مثال: منظر طبيعي وقت غروب الشمس على بحيرة...", fontSize = 11.sp, color = StudioTextMuted) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("ai_image_prompt_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = StudioCyan,
                                unfocusedBorderColor = StudioSurfaceBorder,
                                focusedTextColor = StudioTextPrimary,
                                unfocusedTextColor = StudioTextPrimary,
                                focusedContainerColor = StudioDarkElevated,
                                unfocusedContainerColor = StudioDarkElevated
                            ),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                if (textToImagePrompt.isNotBlank() && onGenerateNewImageClick != null) {
                                    onGenerateNewImageClick(textToImagePrompt)
                                }
                            },
                            enabled = textToImagePrompt.isNotBlank() && !isProcessing,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("ai_generate_image_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Create, contentDescription = "توليد", tint = StudioDark, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("توليد", color = StudioDark, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Section 2: Quality & Resolution Enhancement
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "⚡ تحسين الجودة ورفع الدقة الفائقة",
                style = MaterialTheme.typography.labelLarge,
                color = StudioCyan,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AiToolCard(
                    title = "تحسين تلقائي ذكي",
                    subtitle = "تحليل المشهد وضبط التباين والألوان",
                    icon = Icons.Default.AutoAwesome,
                    gradient = listOf(StudioCyan, Color(0xFF0284C7)),
                    onClick = onAutoEnhanceClick,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f),
                    testTag = "ai_auto_enhance_btn"
                )
                AiToolCard(
                    title = "دقة فائقة (Upscale)",
                    subtitle = "شحذ التفاصيل وإزالة التغبيش",
                    icon = Icons.Default.HighQuality,
                    gradient = listOf(StudioViolet, StudioMagenta),
                    onClick = onSuperResolutionClick,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f),
                    testTag = "ai_super_res_btn"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AiToolCard(
                    title = "إزالة الضوضاء (Denoise)",
                    subtitle = "تصفية التحبب الرقمي وتنعيم نقي",
                    icon = Icons.Default.Tune,
                    gradient = listOf(Color(0xFF059669), StudioEmerald),
                    onClick = onDenoiseClick,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f),
                    testTag = "ai_denoise_btn"
                )
                AiToolCard(
                    title = "ألوان HDR ديناميكية",
                    subtitle = "إبراز الظلال وتوسيع المدى اللوني",
                    icon = Icons.Default.ColorLens,
                    gradient = listOf(Color(0xFFD97706), StudioAmber),
                    onClick = onHdrBoostClick,
                    enabled = !isProcessing,
                    modifier = Modifier.weight(1f),
                    testTag = "ai_hdr_boost_btn"
                )
            }

            // Section 3: AI Background Removal & Replacement with Prompts
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "✂️ إزالة وتغيير الخلفية والممحاة السحرية",
                style = MaterialTheme.typography.labelLarge,
                color = StudioMagenta,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(StudioMagenta.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCut,
                                    contentDescription = "قص الخلفية",
                                    tint = StudioMagenta
                                )
                            }
                            Column {
                                Text(
                                    text = if (isBgRemoved) "تم عزل العنصر بنجاح!" else "إزالة الخلفية التلقائية",
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "عزل بضغطة زر وتوليد خلفية جديدة من النص أو شفافة",
                                    fontSize = 11.sp,
                                    color = StudioTextMuted
                                )
                            }
                        }

                        Button(
                            onClick = onRemoveBackgroundClick,
                            enabled = !isProcessing,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isBgRemoved) StudioEmerald else StudioMagenta
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("ai_remove_bg_trigger_btn")
                        ) {
                            Text(
                                text = if (isBgRemoved) "إعادة العزل" else "إزالة فورية",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Write description for new background
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "أو اكتب وصفاً لخلفية جديدة ليتم توليدها:",
                            fontSize = 11.sp,
                            color = StudioCyan,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = customBgPrompt,
                                onValueChange = { customBgPrompt = it },
                                placeholder = { Text("مثال: شاطئ استوائي مع نخيل...", fontSize = 11.sp, color = StudioTextMuted) },
                                modifier = Modifier.weight(1f),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioMagenta,
                                    unfocusedBorderColor = StudioSurfaceBorder,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary,
                                    focusedContainerColor = StudioDarkElevated,
                                    unfocusedContainerColor = StudioDarkElevated
                                ),
                                singleLine = true
                            )

                            Button(
                                onClick = {
                                    if (customBgPrompt.isNotBlank() && onReplaceBackgroundWithPrompt != null) {
                                        onReplaceBackgroundWithPrompt(customBgPrompt)
                                    }
                                },
                                enabled = customBgPrompt.isNotBlank() && !isProcessing,
                                colors = ButtonDefaults.buttonColors(containerColor = StudioMagenta),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Send, contentDescription = "تطبيق", modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Quick background presets
                    Text(
                        text = "خيارات الخلفيات السريعة:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudioTextPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val bgOptions = listOf(
                            Pair("TRANSPARENT", "شفافة 🏁"),
                            Pair("STUDIO_DARK", "استوديو 🌑"),
                            Pair("CYBER_GRADIENT", "نيون 🌆"),
                            Pair("GOLDEN_SUNSET", "غروب 🌅"),
                            Pair("WHITE_CLEAN", "بيضاء ⚪")
                        )

                        bgOptions.forEach { (type, label) ->
                            val isSelected = currentBgReplacement == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant)
                                    .border(
                                        1.dp,
                                        if (isSelected) StudioCyan else StudioSurfaceBorder,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { onReplaceBackgroundClick(type) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioCyan else StudioTextPrimary
                                )
                            }
                        }
                    }

                    // Magic Eraser trigger button
                    if (onMagicEraserClick != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = {
                                onMagicEraserClick()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("activate_magic_eraser_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AutoFixHigh, contentDescription = "ممحاة سحرية", tint = StudioViolet, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("تفعيل الممحاة السحرية لحذف العناصر غير المرغوبة", color = StudioTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Section 4: Content Analysis
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAnalyzeContentClick() }
                    .testTag("ai_analyze_scene_btn"),
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioViolet.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                brush = Brush.linearGradient(listOf(StudioViolet, StudioCyan)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "تحليل المحتوى",
                            tint = Color.White
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "تحليل المحتوى واقتراح الأفكار بالذكاء الاصطناعي",
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "توليد كابشن تريند، هاشتاجات، وتوصيات تعديل مخصصة لصورتك",
                            fontSize = 11.sp,
                            color = StudioTextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AiToolCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = enabled) { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(brush = Brush.linearGradient(gradient), shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = StudioTextPrimary
            )

            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = StudioTextMuted,
                lineHeight = 14.sp
            )
        }
    }
}
