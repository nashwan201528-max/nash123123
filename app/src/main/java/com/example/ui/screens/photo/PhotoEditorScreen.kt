package com.example.ui.screens.photo

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatColorReset
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AspectRatioType
import com.example.ui.components.AiToolsBottomSheet
import com.example.ui.components.ExportSuccessDialog
import com.example.ui.components.FilterThumbnailRow
import com.example.ui.components.PhotoAiQualityPanel
import com.example.ui.components.TextStickerEditorDialog
import com.example.ui.components.TopStudioAppBar
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet
import com.example.ui.viewmodel.PhotoEditorViewModel

private enum class PhotoToolMode {
    FILTERS,
    VISUAL_FX,
    AI_QUALITY,
    CROP_RATIO,
    ADJUSTMENTS,
    SAMPLE_PHOTOS
}

@Composable
fun PhotoEditorScreen(
    viewModel: PhotoEditorViewModel,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var showAiSheet by remember { mutableStateOf(false) }
    var showTextStickerDialog by remember { mutableStateOf(false) }
    var activeToolMode by remember { mutableStateOf(PhotoToolMode.FILTERS) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.loadFromUri(uri)
        }
    }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopStudioAppBar(
                title = "محرر الصور الاحترافي",
                subtitle = "20 فلتر + أدوات الذكاء الاصطناعي",
                onBackClick = onBackClick,
                onAiMagicClick = { showAiSheet = true },
                onExportClick = { viewModel.exportAndSavePhoto("صورة_معدلة_${state.selectedFilter.nameAr}") },
                exportButtonText = "حفظ وتحميل"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = StudioDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(StudioDark),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main Viewport / Image Display Canvas with Touch Detection for Magic Eraser
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (state.isBgRemoved && state.bgReplacementType == "TRANSPARENT") {
                            Color(0xFF1E293B)
                        } else {
                            StudioDarkElevated
                        }
                    )
                    .border(
                        width = if (state.isMagicEraserActive) 2.dp else 1.dp,
                        color = if (state.isMagicEraserActive) StudioViolet else StudioSurfaceBorder,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .pointerInput(state.isMagicEraserActive) {
                        if (state.isMagicEraserActive) {
                            detectTapGestures { offset ->
                                val ratioX = offset.x / size.width.toFloat()
                                val ratioY = offset.y / size.height.toFloat()
                                viewModel.applyMagicEraser(ratioX, ratioY)
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (state.processedBitmap != null) {
                    Image(
                        bitmap = state.processedBitmap!!.asImageBitmap(),
                        contentDescription = "الصورة المعدلة",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .testTag("photo_editor_viewport"),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Text(
                        text = "جاري تحميل وتوليد الصورة...",
                        color = StudioTextMuted
                    )
                }

                // Top Badge Overlays for Status
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (state.isMagicEraserActive) {
                        Surface(
                            color = StudioViolet.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "🪄 الممحاة السحرية مفعلة: انقر لإزالة أي عنصر",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "إلغاء",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clickable { viewModel.toggleMagicEraserMode() }
                                )
                            }
                        }
                    }

                    if (state.isBgRemoved) {
                        Surface(
                            color = StudioMagenta.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "✂️ خلفية معزولة",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (state.selectedAspectRatio != AspectRatioType.ORIGINAL) {
                        Surface(
                            color = StudioCyan.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = state.selectedAspectRatio.labelAr,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioDark,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Floating quick action buttons over canvas
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.toggleMagicEraserMode() },
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                if (state.isMagicEraserActive) StudioViolet else StudioSurface,
                                CircleShape
                            )
                            .border(1.dp, StudioViolet, CircleShape)
                            .testTag("floating_eraser_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoFixHigh,
                            contentDescription = "الممحاة السحرية",
                            tint = if (state.isMagicEraserActive) Color.White else StudioViolet,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = { showAiSheet = true },
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                brush = Brush.radialGradient(listOf(StudioViolet, StudioMagenta)),
                                shape = CircleShape
                            )
                            .testTag("floating_ai_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "الذكاء الاصطناعي",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = { showTextStickerDialog = true },
                        modifier = Modifier
                            .size(42.dp)
                            .background(StudioSurface, CircleShape)
                            .border(1.dp, StudioCyan, CircleShape)
                            .testTag("floating_text_sticker_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.TextFields,
                            contentDescription = "إضافة نصوص وملصقات",
                            tint = StudioCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = { photoPickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .size(42.dp)
                            .background(StudioSurface, CircleShape)
                            .border(1.dp, StudioSurfaceBorder, CircleShape)
                            .testTag("floating_change_photo_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "رفع صورة من المعرض",
                            tint = StudioTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Bottom Tools Control Deck
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(StudioSurface)
            ) {
                // Secondary tool selector tabs (Scrollable Row)
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item {
                        ToolTabButton(
                            title = "20 فلتر وتأثير",
                            icon = Icons.Default.ColorLens,
                            isSelected = activeToolMode == PhotoToolMode.FILTERS,
                            onClick = { activeToolMode = PhotoToolMode.FILTERS }
                        )
                    }
                    item {
                        ToolTabButton(
                            title = "تأثيرات وتلوين AI 🎨",
                            icon = Icons.Default.ColorLens,
                            isSelected = activeToolMode == PhotoToolMode.VISUAL_FX,
                            onClick = {
                                activeToolMode = PhotoToolMode.VISUAL_FX
                                viewModel.openVisualFxStudioSheet()
                            }
                        )
                    }
                    item {
                        ToolTabButton(
                            title = "تحسين AI 🚀",
                            icon = Icons.Default.AutoAwesome,
                            isSelected = activeToolMode == PhotoToolMode.AI_QUALITY,
                            onClick = { activeToolMode = PhotoToolMode.AI_QUALITY }
                        )
                    }
                    item {
                        ToolTabButton(
                            title = "القص والنسبة",
                            icon = Icons.Default.AspectRatio,
                            isSelected = activeToolMode == PhotoToolMode.CROP_RATIO,
                            onClick = { activeToolMode = PhotoToolMode.CROP_RATIO }
                        )
                    }
                    item {
                        ToolTabButton(
                            title = "الضبط اليدوي",
                            icon = Icons.Default.Tune,
                            isSelected = activeToolMode == PhotoToolMode.ADJUSTMENTS,
                            onClick = { activeToolMode = PhotoToolMode.ADJUSTMENTS }
                        )
                    }
                    item {
                        ToolTabButton(
                            title = "صور تجريبية",
                            icon = Icons.Default.Collections,
                            isSelected = activeToolMode == PhotoToolMode.SAMPLE_PHOTOS,
                            onClick = { activeToolMode = PhotoToolMode.SAMPLE_PHOTOS }
                        )
                    }
                }

                // Dynamic tool drawer
                when (activeToolMode) {
                    PhotoToolMode.FILTERS -> {
                        FilterThumbnailRow(
                            selectedFilterId = state.selectedFilter.id,
                            onFilterSelected = { viewModel.selectFilter(it) },
                            filterIntensity = state.filterIntensity,
                            onIntensityChange = { viewModel.setFilterIntensity(it) },
                            onSmartMatchClick = { viewModel.applySmartContentMatchFilter() }
                        )
                    }

                    PhotoToolMode.VISUAL_FX -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "استوديو التأثيرات والتلوين الذكي 🎨",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                Text(
                                    text = "تعديل حرارة اللون، التباين، الإضاءة، والمؤثرات السينمائية",
                                    fontSize = 10.sp,
                                    color = StudioTextMuted
                                )
                            }
                            Button(
                                onClick = { viewModel.openVisualFxStudioSheet() },
                                colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("فتح الاستوديو ✨", color = StudioDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    PhotoToolMode.AI_QUALITY -> {
                        PhotoAiQualityPanel(
                            state = state,
                            onAutoMasterClick = { viewModel.applyAiAutoEnhance() },
                            onUpscaleClick = { viewModel.applyAiSuperResolution(it) },
                            onDenoiseClick = { viewModel.applyAiDenoise(it) },
                            onSharpenClick = { viewModel.applyAiSharpening(it) },
                            onHdrBoostClick = { viewModel.applyAiHdrBoost() }
                        )
                    }

                    PhotoToolMode.CROP_RATIO -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "تحديد نسبة الأبعاد واقتصاص الكادر:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(AspectRatioType.values()) { ratio ->
                                    val isSelected = state.selectedAspectRatio == ratio
                                    Card(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable { viewModel.setAspectRatio(ratio) }
                                            .testTag("crop_ratio_${ratio.name}"),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant
                                        ),
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.dp,
                                            if (isSelected) StudioCyan else StudioSurfaceBorder
                                        )
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                text = ratio.labelAr,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) StudioCyan else StudioTextPrimary
                                            )
                                            Text(
                                                text = ratio.iconDescription,
                                                fontSize = 10.sp,
                                                color = StudioTextMuted
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    PhotoToolMode.ADJUSTMENTS -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AdjustmentSliderRow(
                                title = "السطوع (Brightness)",
                                value = state.brightness,
                                valueRange = -60f..60f,
                                onValueChange = { viewModel.setBrightness(it) }
                            )
                            AdjustmentSliderRow(
                                title = "التباين (Contrast)",
                                value = state.contrast,
                                valueRange = 0.5f..1.8f,
                                onValueChange = { viewModel.setContrast(it) }
                            )
                            AdjustmentSliderRow(
                                title = "التشبع اللوني (Saturation)",
                                value = state.saturation,
                                valueRange = 0.0f..2.0f,
                                onValueChange = { viewModel.setSaturation(it) }
                            )
                        }
                    }

                    PhotoToolMode.SAMPLE_PHOTOS -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "اختر صورة تجريبية عالية الجودة:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SamplePhotoChip(
                                    title = "غروب ذهبي 🌅",
                                    resId = R.drawable.sample_photo_sunset,
                                    onClick = { viewModel.loadSampleImage(R.drawable.sample_photo_sunset) },
                                    modifier = Modifier.weight(1f)
                                )
                                SamplePhotoChip(
                                    title = "سايبربانك نيون 🌆",
                                    resId = R.drawable.sample_photo_neon,
                                    onClick = { viewModel.loadSampleImage(R.drawable.sample_photo_neon) },
                                    modifier = Modifier.weight(1f)
                                )
                                SamplePhotoChip(
                                    title = "استوديو الإنتاج 🎬",
                                    resId = R.drawable.hero_banner,
                                    onClick = { viewModel.loadSampleImage(R.drawable.hero_banner) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // AI Smart Visual Effects & Color Grading Studio Sheet
    if (state.isVisualFxStudioSheetOpen) {
        com.example.ui.components.video.AiVisualEffectsStudioSheet(
            selectedFilter = state.selectedFilter,
            filterIntensity = state.filterIntensity,
            colorGradingState = state.colorGradingState,
            visualFxParams = state.visualFxParams,
            transitionCustomization = com.example.data.model.TransitionCustomization(),
            isDualComparisonEnabled = false,
            comparisonSplitPosition = 0.5f,
            targetClipIndex = 0,
            clips = emptyList(),
            onFilterChanged = { viewModel.selectFilter(it) },
            onFilterIntensityChanged = { viewModel.setFilterIntensity(it) },
            onColorGradingChanged = { viewModel.updateColorGrading(it) },
            onVisualFxChanged = { viewModel.updateVisualFx(it) },
            onTransitionChanged = { _, _ -> },
            onApplyTransitionToAll = { _, _ -> },
            onToggleDualComparison = { },
            onComparisonSplitChanged = { },
            onAiAutoMasterEnhance = { viewModel.applyAiAutoEnhance() },
            onAiPromptColorGrading = { viewModel.applyAiPromptColorGrading(it) },
            onDismiss = { viewModel.closeVisualFxStudioSheet() }
        )
    }

    // AI Tools Bottom Sheet
    if (showAiSheet) {
        AiToolsBottomSheet(
            onDismiss = { showAiSheet = false },
            isProcessing = state.isAiProcessing,
            onAutoEnhanceClick = { viewModel.applyAiAutoEnhance() },
            onSuperResolutionClick = { viewModel.applyAiSuperResolution() },
            onDenoiseClick = { viewModel.applyAiDenoise() },
            onHdrBoostClick = { viewModel.applyAiHdrBoost() },
            onRemoveBackgroundClick = { viewModel.applyAiRemoveBackground() },
            onReplaceBackgroundClick = { viewModel.replaceBackground(it) },
            onReplaceBackgroundWithPrompt = { viewModel.replaceBackgroundWithPrompt(it) },
            onMagicEraserClick = { viewModel.toggleMagicEraserMode() },
            onGenerateNewImageClick = { prompt ->
                viewModel.generateNewImageFromPrompt(prompt)
                showAiSheet = false
            },
            onAnalyzeContentClick = { viewModel.analyzeContentWithAi() },
            isBgRemoved = state.isBgRemoved,
            currentBgReplacement = state.bgReplacementType
        )
    }

    // Text & Sticker Dialog
    if (showTextStickerDialog) {
        TextStickerEditorDialog(
            onDismiss = { showTextStickerDialog = false },
            onAddTextOverlay = { viewModel.addTextOverlay(it) },
            onAddSticker = { viewModel.addSticker(it) }
        )
    }

    // Export & Download Success Dialog
    state.lastExportedItem?.let { item ->
        ExportSuccessDialog(
            exportItem = item,
            onDismiss = { viewModel.dismissExportDialog() }
        )
    }
}

@Composable
private fun ToolTabButton(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) StudioCyan.copy(alpha = 0.2f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) StudioCyan else StudioTextMuted,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) StudioCyan else StudioTextMuted
        )
    }
}

@Composable
private fun AdjustmentSliderRow(
    title: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 11.sp, color = StudioTextPrimary)
            Text(text = String.format("%.2f", value), fontSize = 11.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = StudioCyan,
                activeTrackColor = StudioCyan,
                inactiveTrackColor = StudioSurfaceVariant
            ),
            modifier = Modifier.height(28.dp)
        )
    }
}

@Composable
private fun SamplePhotoChip(
    title: String,
    resId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 4.dp)
        )
    }
}
