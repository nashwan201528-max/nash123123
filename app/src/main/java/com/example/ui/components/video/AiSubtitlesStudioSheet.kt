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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Translate
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubtitleAnimation
import com.example.data.model.SubtitleFontFamily
import com.example.data.model.SubtitleLanguage
import com.example.data.model.SubtitleLine
import com.example.data.model.SubtitlePosition
import com.example.data.model.SubtitleStyleSettings
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
fun AiSubtitlesStudioSheet(
    subtitles: List<SubtitleLine>,
    styleSettings: SubtitleStyleSettings,
    selectedLanguage: SubtitleLanguage,
    isGenerating: Boolean,
    onDismiss: () -> Unit,
    onGenerateSubtitles: (prompt: String) -> Unit,
    onTranslateSubtitles: (SubtitleLanguage) -> Unit,
    onLanguageChange: (SubtitleLanguage) -> Unit,
    onUpdateStyleSettings: (SubtitleStyleSettings) -> Unit,
    onAddSubtitleLine: (SubtitleLine) -> Unit,
    onUpdateSubtitleLine: (Int, SubtitleLine) -> Unit,
    onRemoveSubtitleLine: (Int) -> Unit,
    onClearAllSubtitles: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Auto Generate, 1: Style & Fonts, 2: Edit Lines
    var customContextInput by remember { mutableStateOf("") }
    var newLineText by remember { mutableStateOf("") }
    var newLineStartSec by remember { mutableStateOf("0.0") }
    var newLineEndSec by remember { mutableStateOf("3.0") }

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
                            imageVector = Icons.Default.Subtitles,
                            contentDescription = "الترجمة التلقائية",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "أستوديو الترجمة التلقائية بالذكاء الاصطناعي (AI Subtitles)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "تحويل الصوت لنصوص • لغات متعددة • تخصيص الخط والألوان",
                            fontSize = 10.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Top Tab Selector
            val studioTabs = listOf(
                Pair("⚡ التوليد واللغات", 0),
                Pair("🎨 تصميم وتنسيق الخط", 1),
                Pair("📝 تعديل النصوص (${subtitles.size})", 2)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(studioTabs) { tab ->
                    val isSelected = selectedTab == tab.second
                    Surface(
                        color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioCyan else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .clickable { selectedTab = tab.second }
                            .testTag("subtitle_tab_${tab.second}")
                    ) {
                        Text(
                            text = tab.first,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            // Interactive Live Preview Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDark),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("معاينة حية لتصميم الترجمة:", fontSize = 10.sp, color = StudioTextSecondary)
                        Surface(
                            color = StudioCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "${selectedLanguage.flagEmoji} ${selectedLanguage.nameAr}",
                                fontSize = 10.sp,
                                color = StudioCyan,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Render Subtitle preview according to current styleSettings
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(65.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Brush.verticalGradient(listOf(Color(0xFF1E293B), Color(0xFF0F172A)))),
                        contentAlignment = when (styleSettings.position) {
                            SubtitlePosition.TOP -> Alignment.TopCenter
                            SubtitlePosition.CENTER -> Alignment.Center
                            SubtitlePosition.BOTTOM -> Alignment.BottomCenter
                        }
                    ) {
                        val previewText = subtitles.firstOrNull()?.textAr ?: "سر المونتاج الاحترافي بالذكاء الاصطناعي! ✨"
                        Surface(
                            color = if (styleSettings.hasBackgroundBox) Color(styleSettings.bgColorHex) else Color.Transparent,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(
                                text = previewText,
                                color = Color(styleSettings.textColorHex),
                                fontSize = (styleSettings.fontSizeSp * 0.75f).sp,
                                fontWeight = if (styleSettings.isBold) FontWeight.ExtraBold else FontWeight.Normal,
                                fontStyle = if (styleSettings.isItalic) FontStyle.Italic else FontStyle.Normal,
                                fontFamily = when (styleSettings.fontFamily) {
                                    SubtitleFontFamily.TECH_MONOSPACE -> FontFamily.Monospace
                                    SubtitleFontFamily.ELEGANT_SERIF -> FontFamily.Serif
                                    else -> FontFamily.Default
                                },
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Tab 0: Generation & Multi-Language
            if (selectedTab == 0) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "1. اختر لغة التعرف على الصوت والترجمة (Language):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    // Language Grid / Carousel
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(SubtitleLanguage.values()) { lang ->
                            val isSelected = selectedLanguage == lang
                            Surface(
                                color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                shape = RoundedCornerShape(10.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioCyan else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .clickable { onLanguageChange(lang) }
                                    .testTag("sub_lang_${lang.code}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(lang.flagEmoji, fontSize = 14.sp)
                                    Column {
                                        Text(
                                            text = lang.nameAr,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) StudioCyan else StudioTextPrimary
                                        )
                                        Text(lang.nameNative, fontSize = 9.sp, color = StudioTextMuted)
                                    }
                                }
                            }
                        }
                    }

                    // Context input (optional)
                    OutlinedTextField(
                        value = customContextInput,
                        onValueChange = { customContextInput = it },
                        placeholder = { Text("أدخل موضوع الفيديو أو كلمات مفتاحية لتوليد كابشن متوافق...", color = StudioTextMuted, fontSize = 11.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioCyan,
                            unfocusedBorderColor = StudioSurfaceBorder,
                            focusedTextColor = StudioTextPrimary,
                            unfocusedTextColor = StudioTextPrimary,
                            focusedContainerColor = StudioSurface,
                            unfocusedContainerColor = StudioSurface
                        ),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 2
                    )

                    // Generate Subtitles Button
                    Button(
                        onClick = { onGenerateSubtitles(customContextInput) },
                        enabled = !isGenerating,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("generate_subtitles_btn")
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(color = StudioDark, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("جاري التعرف وتوليد الترجمة التلقائية...", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("توليد ومزامنة الترجمة التلقائية بالذكاء الاصطناعي ⚡", color = StudioDark, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Fast Translation Section if subtitles exist
                    if (subtitles.isNotEmpty()) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = StudioSurface),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = StudioViolet, modifier = Modifier.size(16.dp))
                                    Text("ترجمة فورية لكافة الأسطر بلغات أخرى:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                                }

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val targetOptions = listOf(
                                        SubtitleLanguage.ENGLISH,
                                        SubtitleLanguage.ARABIC,
                                        SubtitleLanguage.FRENCH,
                                        SubtitleLanguage.SPANISH,
                                        SubtitleLanguage.TURKISH,
                                        SubtitleLanguage.GERMAN,
                                        SubtitleLanguage.JAPANESE
                                    )
                                    items(targetOptions) { targetLang ->
                                        Surface(
                                            color = StudioSurfaceVariant,
                                            shape = RoundedCornerShape(6.dp),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                                            modifier = Modifier
                                                .clickable(enabled = !isGenerating) { onTranslateSubtitles(targetLang) }
                                                .testTag("translate_to_${targetLang.code}")
                                        ) {
                                            Text(
                                                text = "${targetLang.flagEmoji} ترجمة لـ ${targetLang.nameAr}",
                                                fontSize = 10.sp,
                                                color = StudioViolet,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Tab 1: Styling, Fonts & Colors Customization
            if (selectedTab == 1) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // 1. Font Family Selector
                    Text("1. نوع ونمط الخط (Font Style):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        SubtitleFontFamily.values().forEach { font ->
                            val isSelected = styleSettings.fontFamily == font
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioSurface
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioCyan else StudioSurfaceBorder
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onUpdateStyleSettings(styleSettings.copy(fontFamily = font)) }
                                    .testTag("font_family_${font.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = font.labelAr,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StudioCyan else StudioTextPrimary
                                    )
                                    if (isSelected) {
                                        Text("محدد ✓", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // 2. Text Color Palette
                    Text("2. لون النص الأساسي (Text Color):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                    val colorsPalette = listOf(
                        Pair("أبيض ناصع", 0xFFFFFFFF),
                        Pair("أصفر نيون", 0xFFFACC15),
                        Pair("سماوي لامع", 0xFF22D3EE),
                        Pair("وردي فوشيا", 0xFFF43F5E),
                        Pair("أخضر نيون", 0xFF4ADE80),
                        Pair("برتقالي جذاب", 0xFFFB923C),
                        Pair("بنفسجي", 0xFFA855F7)
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(colorsPalette) { (name, colorHex) ->
                            val isSelected = styleSettings.textColorHex == colorHex
                            Surface(
                                color = Color(colorHex),
                                shape = CircleShape,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 2.5.dp else 1.dp,
                                    if (isSelected) Color.White else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .size(34.dp)
                                    .clickable { onUpdateStyleSettings(styleSettings.copy(textColorHex = colorHex)) }
                                    .testTag("color_pick_${colorHex}")
                            ) {}
                        }
                    }

                    // 3. Font Size Slider
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("حجم الخط (Font Size):", fontSize = 11.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                                Text("${styleSettings.fontSizeSp.toInt()} sp", fontSize = 11.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
                            }
                            Slider(
                                value = styleSettings.fontSizeSp,
                                onValueChange = { onUpdateStyleSettings(styleSettings.copy(fontSizeSp = it)) },
                                valueRange = 14f..36f,
                                colors = SliderDefaults.colors(
                                    thumbColor = StudioCyan,
                                    activeTrackColor = StudioCyan,
                                    inactiveTrackColor = StudioSurfaceVariant
                                ),
                                modifier = Modifier.height(20.dp)
                            )
                        }
                    }

                    // 4. Background Box Style & Opacity
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("خلفية مستطيلة للنص (Background Box)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                                Text("تزيد وضوح وقراءة النص فوق المشاهد المعقدة", fontSize = 9.sp, color = StudioTextMuted)
                            }
                            Switch(
                                checked = styleSettings.hasBackgroundBox,
                                onCheckedChange = { onUpdateStyleSettings(styleSettings.copy(hasBackgroundBox = it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = StudioCyan)
                            )
                        }
                    }

                    // 5. Position on Screen (Top, Center, Bottom)
                    Text("3. موضع الترجمة على الشاشة:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SubtitlePosition.values().forEach { pos ->
                            val isSelected = styleSettings.position == pos
                            Surface(
                                color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioCyan else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onUpdateStyleSettings(styleSettings.copy(position = pos)) }
                            ) {
                                Text(
                                    text = pos.labelAr,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioCyan else StudioTextPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Tab 2: Manual Line Editor
            if (selectedTab == 2) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Add new subtitle line row
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("إضافة سطر ترجمة مخصص يدوي:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            OutlinedTextField(
                                value = newLineText,
                                onValueChange = { newLineText = it },
                                placeholder = { Text("اكتب نص السطر...", color = StudioTextMuted, fontSize = 11.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = StudioCyan,
                                    unfocusedBorderColor = StudioSurfaceBorder,
                                    focusedTextColor = StudioTextPrimary,
                                    unfocusedTextColor = StudioTextPrimary,
                                    focusedContainerColor = StudioDark,
                                    unfocusedContainerColor = StudioDark
                                ),
                                singleLine = true
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = newLineStartSec,
                                    onValueChange = { newLineStartSec = it },
                                    label = { Text("البداية (ث)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                OutlinedTextField(
                                    value = newLineEndSec,
                                    onValueChange = { newLineEndSec = it },
                                    label = { Text("النهاية (ث)") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true
                                )
                                Button(
                                    onClick = {
                                        if (newLineText.isNotBlank()) {
                                            val s = newLineStartSec.toFloatOrNull() ?: 0f
                                            val e = newLineEndSec.toFloatOrNull() ?: (s + 3f)
                                            onAddSubtitleLine(SubtitleLine(s, e, newLineText.trim()))
                                            newLineText = ""
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("إضافة +", color = StudioDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    // List of existing lines
                    if (subtitles.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("أسطر الترجمة المزامنة (${subtitles.size}):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            Text(
                                text = "مسح الكل 🗑️",
                                fontSize = 10.sp,
                                color = StudioRose,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onClearAllSubtitles() }
                            )
                        }

                        subtitles.forEachIndexed { index, sub ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = StudioSurface),
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
                                        Surface(
                                            color = StudioCyan.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "${String.format("%.1f", sub.startTimeSec)}ث - ${String.format("%.1f", sub.endTimeSec)}ث",
                                                fontSize = 9.sp,
                                                color = StudioCyan,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                        Text(
                                            text = sub.textAr,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StudioTextPrimary,
                                            maxLines = 2
                                        )
                                    }

                                    IconButton(
                                        onClick = { onRemoveSubtitleLine(index) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف السطر", tint = StudioRose, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
