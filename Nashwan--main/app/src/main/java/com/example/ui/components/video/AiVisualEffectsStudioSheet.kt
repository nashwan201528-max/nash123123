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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShutterSpeed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ColorGradingState
import com.example.data.model.ComparisonMode
import com.example.data.model.FilterPreset
import com.example.data.model.FilterRepository
import com.example.data.model.TransitionCategory
import com.example.data.model.TransitionCustomization
import com.example.data.model.TransitionEasingCurve
import com.example.data.model.VideoClip
import com.example.data.model.VideoTransitionType
import com.example.data.model.VisualFxParameters
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
fun AiVisualEffectsStudioSheet(
    selectedFilter: FilterPreset,
    filterIntensity: Float,
    colorGradingState: ColorGradingState,
    visualFxParams: VisualFxParameters,
    transitionCustomization: TransitionCustomization,
    isDualComparisonEnabled: Boolean,
    comparisonSplitPosition: Float,
    targetClipIndex: Int,
    clips: List<VideoClip>,
    onFilterChanged: (FilterPreset) -> Unit,
    onFilterIntensityChanged: (Float) -> Unit,
    onColorGradingChanged: (ColorGradingState) -> Unit,
    onVisualFxChanged: (VisualFxParameters) -> Unit,
    onTransitionChanged: (VideoTransitionType, Long) -> Unit,
    onApplyTransitionToAll: (VideoTransitionType, Long) -> Unit,
    onToggleDualComparison: () -> Unit,
    onComparisonSplitChanged: (Float) -> Unit,
    onAiAutoMasterEnhance: () -> Unit,
    onAiPromptColorGrading: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("تغيير وتدريج الألوان 🎨", "فلاتر جمالية ✨", "تأثيرات سينمائية 🎞️", "انتقالات سلسة ⚡", "تلوين بالـ AI 🤖")

    var promptText by remember { mutableStateOf("") }

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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                Brush.linearGradient(listOf(StudioCyan, StudioMagenta)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column {
                        Text(
                            text = "استوديو التأثيرات البصرية الذكية والتلوين",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "تعديل الألوان، فلاتر سينمائية، وانتقالات مدعومة بالذكاء الاصطناعي",
                            fontSize = 11.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Quick AI Auto-Master & Dual Comparison Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onAiAutoMasterEnhance,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("ai_auto_master_btn")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = StudioDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تحسين ذكي تلقائي ✨", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioDark)
                }

                Surface(
                    color = if (isDualComparisonEnabled) StudioMagenta.copy(alpha = 0.2f) else StudioSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isDualComparisonEnabled) StudioMagenta else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onToggleDualComparison() }
                        .testTag("toggle_compare_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Compare,
                            contentDescription = null,
                            tint = if (isDualComparisonEnabled) StudioMagenta else StudioTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isDualComparisonEnabled) "مقارنة مفعلة" else "قبل / بعد ⚖️",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDualComparisonEnabled) StudioMagenta else StudioTextPrimary
                        )
                    }
                }
            }

            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = StudioSurface,
                contentColor = StudioCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = StudioCyan
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) StudioCyan else StudioTextMuted
                            )
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                0 -> ColorGradingTabContent(
                    state = colorGradingState,
                    onStateChange = onColorGradingChanged
                )
                1 -> AestheticFiltersTabContent(
                    selectedFilter = selectedFilter,
                    filterIntensity = filterIntensity,
                    onFilterSelect = onFilterChanged,
                    onIntensityChange = onFilterIntensityChanged
                )
                2 -> CinematicFxTabContent(
                    params = visualFxParams,
                    onParamsChange = onVisualFxChanged
                )
                3 -> SmoothTransitionsTabContent(
                    targetClipIndex = targetClipIndex,
                    clips = clips,
                    customization = transitionCustomization,
                    onApplyTransition = onTransitionChanged,
                    onApplyToAll = onApplyTransitionToAll
                )
                4 -> AiPromptColorTabContent(
                    promptText = promptText,
                    onPromptChange = { promptText = it },
                    onSubmitPrompt = { onAiPromptColorGrading(promptText) }
                )
            }
        }
    }
}

@Composable
private fun ColorGradingTabContent(
    state: ColorGradingState,
    onStateChange: (ColorGradingState) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("عناصر تدريج الألوان وضبط الإضاءة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            if (state.isModified) {
                Text(
                    text = "إعادة ضبط ↺",
                    fontSize = 11.sp,
                    color = StudioCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onStateChange(ColorGradingState()) }
                )
            }
        }

        // 1. Color Temperature (Warm / Cold)
        ColorSliderRow(
            title = "حرارة اللون (Temperature)",
            subLabel = if (state.temperature > 0) "دافئ ذهبي +${state.temperature.toInt()}" else if (state.temperature < 0) "بارد شتوي ${state.temperature.toInt()}" else "طبيعي 0",
            value = state.temperature,
            valueRange = -100f..100f,
            activeColor = if (state.temperature >= 0) StudioAmber else StudioCyan,
            onValueChange = { onStateChange(state.copy(temperature = it)) }
        )

        // 2. Tint (Green / Magenta)
        ColorSliderRow(
            title = "صبغة التدرج (Tint)",
            subLabel = if (state.tint > 0) "أرجواني +${state.tint.toInt()}" else if (state.tint < 0) "أخضر زمردي ${state.tint.toInt()}" else "متوازن 0",
            value = state.tint,
            valueRange = -100f..100f,
            activeColor = if (state.tint >= 0) StudioMagenta else StudioEmerald,
            onValueChange = { onStateChange(state.copy(tint = it)) }
        )

        // 3. Hue Shift (Color Wheel 0° to 360°)
        ColorSliderRow(
            title = "تدوير أطياف الألوان (Hue Shift)",
            subLabel = "${state.hueShiftDegrees.toInt()}°",
            value = state.hueShiftDegrees,
            valueRange = 0f..360f,
            activeColor = StudioViolet,
            onValueChange = { onStateChange(state.copy(hueShiftDegrees = it)) }
        )

        // 4. Brightness & Exposure (الإضاءة والسطوع)
        ColorSliderRow(
            title = "السطوع والإضاءة (Brightness)",
            subLabel = if (state.brightness > 0) "+${state.brightness.toInt()}" else "${state.brightness.toInt()}",
            value = state.brightness,
            valueRange = -80f..80f,
            activeColor = StudioAmber,
            onValueChange = { onStateChange(state.copy(brightness = it)) }
        )

        // 5. Contrast (التباين)
        ColorSliderRow(
            title = "التباين والعمق (Contrast)",
            subLabel = String.format("%.2f x", state.contrast),
            value = state.contrast,
            valueRange = 0.5f..2.0f,
            activeColor = StudioCyan,
            onValueChange = { onStateChange(state.copy(contrast = it)) }
        )

        // 6. Saturation (التشبع)
        ColorSliderRow(
            title = "تشبع الألوان (Saturation)",
            subLabel = String.format("%.2f x", state.saturation),
            value = state.saturation,
            valueRange = 0.0f..2.5f,
            activeColor = StudioRose,
            onValueChange = { onStateChange(state.copy(saturation = it)) }
        )

        // 7. Highlights & Shadows
        ColorSliderRow(
            title = "الإضاءات العالية (Highlights)",
            subLabel = if (state.highlights > 0) "+${state.highlights.toInt()}" else "${state.highlights.toInt()}",
            value = state.highlights,
            valueRange = -100f..100f,
            activeColor = StudioCyanLight,
            onValueChange = { onStateChange(state.copy(highlights = it)) }
        )

        ColorSliderRow(
            title = "استعادة الظلال (Shadows)",
            subLabel = if (state.shadows > 0) "+${state.shadows.toInt()}" else "${state.shadows.toInt()}",
            value = state.shadows,
            valueRange = -100f..100f,
            activeColor = StudioViolet,
            onValueChange = { onStateChange(state.copy(shadows = it)) }
        )
    }
}

@Composable
private fun ColorSliderRow(
    title: String,
    subLabel: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    activeColor: Color,
    onValueChange: (Float) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 11.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                Surface(
                    color = activeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = subLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = activeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = activeColor,
                    activeTrackColor = activeColor,
                    inactiveTrackColor = StudioSurfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AestheticFiltersTabContent(
    selectedFilter: FilterPreset,
    filterIntensity: Float,
    onFilterSelect: (FilterPreset) -> Unit,
    onIntensityChange: (Float) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("الكل") }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Intensity Slider
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(selectedFilter.iconEmoji, fontSize = 14.sp)
                        Text(
                            text = "كثافة الفلتر الجمالي: ${selectedFilter.nameAr}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }
                    Text(
                        text = "${(filterIntensity * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )
                }

                Slider(
                    value = filterIntensity,
                    onValueChange = onIntensityChange,
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Filter Categories
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(FilterRepository.categories) { category ->
                val isSelected = category == selectedCategory
                Surface(
                    color = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier.clickable { selectedCategory = category }
                ) {
                    Text(
                        text = category,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) StudioCyan else StudioTextPrimary,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Filter Grid / List
        val filteredList = if (selectedCategory == "الكل") {
            FilterRepository.presets
        } else {
            FilterRepository.presets.filter { it.categoryAr == selectedCategory }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            filteredList.forEach { preset ->
                val isCurrent = preset.id == selectedFilter.id
                Surface(
                    color = if (isCurrent) StudioCyan.copy(alpha = 0.15f) else StudioSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isCurrent) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onFilterSelect(preset) }
                        .testTag("filter_opt_${preset.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(text = preset.iconEmoji, fontSize = 22.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = preset.nameAr,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) StudioCyan else StudioTextPrimary
                                )
                                Surface(
                                    color = StudioSurfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = preset.categoryAr,
                                        fontSize = 9.sp,
                                        color = StudioTextMuted,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = preset.descriptionAr,
                                fontSize = 10.sp,
                                color = StudioTextSecondary,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CinematicFxTabContent(
    params: VisualFxParameters,
    onParamsChange: (VisualFxParameters) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("المؤثرات السينمائية المتقدمة:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
            if (params.isFxActive) {
                Text(
                    text = "إعادة ضبط ↺",
                    fontSize = 11.sp,
                    color = StudioCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onParamsChange(VisualFxParameters()) }
                )
            }
        }

        // 1. Film Grain
        ColorSliderRow(
            title = "تحبب شريط الفيلم (Film Grain 35mm)",
            subLabel = "${(params.filmGrainStrength * 100).toInt()}%",
            value = params.filmGrainStrength,
            valueRange = 0f..1f,
            activeColor = StudioAmber,
            onValueChange = { onParamsChange(params.copy(filmGrainStrength = it)) }
        )

        // 2. Glow & Light Bloom
        ColorSliderRow(
            title = "توهج الإضاءات والبلوم (Glow & Bloom)",
            subLabel = "${(params.glowBloomIntensity * 100).toInt()}%",
            value = params.glowBloomIntensity,
            valueRange = 0f..1f,
            activeColor = StudioMagenta,
            onValueChange = { onParamsChange(params.copy(glowBloomIntensity = it)) }
        )

        // 3. Vignette
        ColorSliderRow(
            title = "تعتيم الحواف السينمائي (Vignette)",
            subLabel = "${(params.vignetteStrength * 100).toInt()}%",
            value = params.vignetteStrength,
            valueRange = 0f..1f,
            activeColor = StudioViolet,
            onValueChange = { onParamsChange(params.copy(vignetteStrength = it)) }
        )

        // 4. Super Sharpening
        ColorSliderRow(
            title = "شحذ التفاصيل الدقيقة (Super Sharpening)",
            subLabel = String.format("%.1f x", params.sharpnessFactor),
            value = params.sharpnessFactor,
            valueRange = 0f..3f,
            activeColor = StudioCyan,
            onValueChange = { onParamsChange(params.copy(sharpnessFactor = it)) }
        )

        // 5. AI Denoise
        ColorSliderRow(
            title = "إزالة وتنعيم الضوضاء الرقمية (AI Denoise)",
            subLabel = "${(params.denoiseFactor * 100).toInt()}%",
            value = params.denoiseFactor,
            valueRange = 0f..1f,
            activeColor = StudioEmerald,
            onValueChange = { onParamsChange(params.copy(denoiseFactor = it)) }
        )
    }
}

@Composable
private fun SmoothTransitionsTabContent(
    targetClipIndex: Int,
    clips: List<VideoClip>,
    customization: TransitionCustomization,
    onApplyTransition: (VideoTransitionType, Long) -> Unit,
    onApplyToAll: (VideoTransitionType, Long) -> Unit
) {
    var selectedType by remember { mutableStateOf(customization.selectedType) }
    var durationSec by remember { mutableFloatStateOf(customization.durationMs / 1000f) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("تأثيرات انتقالية سلسة بين المشاهد:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)

        // Transitions Categories & Items
        VideoTransitionType.values().forEach { transition ->
            val isSelected = transition == selectedType
            Surface(
                color = if (isSelected) StudioViolet.copy(alpha = 0.2f) else StudioSurface,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) StudioViolet else StudioSurfaceBorder
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedType = transition }
                    .testTag("fx_trans_${transition.id}")
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(transition.iconEmoji, fontSize = 20.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(transition.nameAr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isSelected) StudioViolet else StudioTextPrimary)
                        Text(transition.descriptionAr, fontSize = 10.sp, color = StudioTextMuted, maxLines = 1)
                    }
                }
            }
        }

        // Duration Slider
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("مدة الانتقال:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                    Text("${String.format("%.1f", durationSec)} ثانية", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioAmber)
                }

                Slider(
                    value = durationSec,
                    onValueChange = { durationSec = it },
                    valueRange = 0.2f..2.5f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioAmber,
                        activeTrackColor = StudioAmber,
                        inactiveTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { onApplyTransition(selectedType, (durationSec * 1000L).toLong()) },
                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1.3f)
            ) {
                Text("تطبيق الانتقال ✨", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onApplyToAll(selectedType, (durationSec * 1000L).toLong()) },
                colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1.2f)
            ) {
                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("تطبيق للكل", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AiPromptColorTabContent(
    promptText: String,
    onPromptChange: (String) -> Unit,
    onSubmitPrompt: () -> Unit
) {
    val samplePrompts = listOf(
        "ألوان سينمائية دافئة مثل أفلام المغامرات وهوليوود 🎬",
        "أجواء ليلية سايبربانك مع أضواء نيون فوشيا وأزرق كهربائي 🌆",
        "أبيض وأسود عالي التباين بطابع فيلم نوار كلاسيكي 📽️",
        "توهج الساعة الذهبية قبل الغروب مع دفء شاعري 🌅",
        "ألوان طبيعية نقية مع إبراز زرقة البحر وخضرة الواحات 🌿"
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "توليد تدريج ألوان مخصص عبر الذكاء الاصطناعي (Gemini AI):",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary
        )

        OutlinedTextField(
            value = promptText,
            onValueChange = onPromptChange,
            placeholder = { Text("صف المظهر اللوني الذي تريده (مثال: ألوان شتوية باردة وغامضة)", fontSize = 11.sp, color = StudioTextMuted) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = StudioSurface,
                unfocusedContainerColor = StudioSurface,
                focusedBorderColor = StudioMagenta,
                unfocusedBorderColor = StudioSurfaceBorder,
                focusedTextColor = StudioTextPrimary,
                unfocusedTextColor = StudioTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("ai_color_prompt_input")
        )

        Button(
            onClick = onSubmitPrompt,
            enabled = promptText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = StudioMagenta),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("submit_ai_color_prompt_btn")
        ) {
            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("توليد وضبط الألوان بالذكاء الاصطناعي 🎨✨", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }

        Text("أمثلة جاهزة للضغط المباشر:", fontSize = 11.sp, color = StudioTextMuted)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            samplePrompts.forEach { sample ->
                Surface(
                    color = StudioSurface,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPromptChange(sample) }
                ) {
                    Text(
                        text = sample,
                        fontSize = 11.sp,
                        color = StudioTextPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
