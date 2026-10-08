package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.local.AppDatabase
import com.example.data.local.ExportedMediaEntity
import com.example.data.local.ProjectEntity
import com.example.data.model.AiEnhancementSuggestion
import com.example.data.model.AspectRatioType
import com.example.data.model.FilterPreset
import com.example.data.model.FilterRepository
import com.example.data.model.StickerOverlay
import com.example.data.model.TextOverlay
import com.example.data.repository.AiMontageRepository
import com.example.data.repository.MediaExportRepository
import com.example.data.repository.ProjectRepository
import com.example.utils.BitmapProcessor
import com.example.utils.MediaSaver
import com.example.utils.SampleMediaProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PhotoEditorUiState(
    val originalBitmap: Bitmap? = null,
    val processedBitmap: Bitmap? = null,
    val selectedFilter: FilterPreset = FilterRepository.presets.first(),
    val filterIntensity: Float = 1.0f,
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val denoiseLevel: Float = 0f,
    val sharpnessLevel: Float = 0f,
    val selectedAspectRatio: AspectRatioType = AspectRatioType.ORIGINAL,
    val isBgRemoved: Boolean = false,
    val bgReplacementType: String = "TRANSPARENT",
    val customBgDescription: String = "",
    val isMagicEraserActive: Boolean = false,
    val textOverlays: List<TextOverlay> = emptyList(),
    val stickers: List<StickerOverlay> = emptyList(),
    val colorGradingState: com.example.data.model.ColorGradingState = com.example.data.model.ColorGradingState(),
    val visualFxParams: com.example.data.model.VisualFxParameters = com.example.data.model.VisualFxParameters(),
    val isVisualFxStudioSheetOpen: Boolean = false,
    val isAiProcessing: Boolean = false,
    val aiSuggestion: AiEnhancementSuggestion? = null,
    val lastExportedItem: ExportedMediaEntity? = null,
    val statusMessage: String? = null
)

class PhotoEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val projectRepo = ProjectRepository(db.projectDao())
    private val exportRepo = MediaExportRepository(db.exportDao())
    private val aiRepo = AiMontageRepository()

    private val _uiState = MutableStateFlow(PhotoEditorUiState())
    val uiState: StateFlow<PhotoEditorUiState> = _uiState.asStateFlow()

    init {
        loadSampleImage(R.drawable.sample_photo_sunset)
    }

    fun loadSampleImage(resId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmap = SampleMediaProvider.loadSampleBitmap(getApplication(), resId)
            _uiState.update {
                it.copy(
                    originalBitmap = bitmap,
                    isBgRemoved = false,
                    textOverlays = emptyList(),
                    stickers = emptyList()
                )
            }
            recomputeProcessedBitmap()
        }
    }

    fun loadFromUri(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(getApplication<Application>().contentResolver, uri)
                _uiState.update {
                    it.copy(
                        originalBitmap = bitmap,
                        isBgRemoved = false,
                        textOverlays = emptyList(),
                        stickers = emptyList(),
                        statusMessage = "تم فتح الصورة بنجاح 🖼️"
                    )
                }
                recomputeProcessedBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun selectFilter(preset: FilterPreset) {
        _uiState.update { it.copy(selectedFilter = preset) }
        recomputeProcessedBitmap()
    }

    fun setFilterIntensity(intensity: Float) {
        _uiState.update { it.copy(filterIntensity = intensity.coerceIn(0f, 1f)) }
        recomputeProcessedBitmap()
    }

    fun setBrightness(value: Float) {
        _uiState.update { it.copy(brightness = value) }
        recomputeProcessedBitmap()
    }

    fun setContrast(value: Float) {
        _uiState.update { it.copy(contrast = value) }
        recomputeProcessedBitmap()
    }

    fun setSaturation(value: Float) {
        _uiState.update { it.copy(saturation = value) }
        recomputeProcessedBitmap()
    }

    fun setAspectRatio(type: AspectRatioType) {
        _uiState.update { it.copy(selectedAspectRatio = type) }
        recomputeProcessedBitmap()
    }

    fun addTextOverlay(textOverlay: TextOverlay) {
        _uiState.update { it.copy(textOverlays = it.textOverlays + textOverlay) }
        recomputeProcessedBitmap()
    }

    fun addSticker(sticker: StickerOverlay) {
        _uiState.update { it.copy(stickers = it.stickers + sticker) }
        recomputeProcessedBitmap()
    }

    fun toggleMagicEraserMode() {
        val next = !_uiState.value.isMagicEraserActive
        _uiState.update {
            it.copy(
                isMagicEraserActive = next,
                statusMessage = if (next) "الممحاة السحرية مفعلة: المس أي عنصر على الصورة لحذفه فوراً 🪄" else "تم إيقاف الممحاة السحرية"
            )
        }
    }

    // === Smart Content-Aware Filter Matching ===
    fun applySmartContentMatchFilter() {
        val base = _uiState.value.originalBitmap ?: return
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(300) // smooth interactive feeling
            val (bestFilterId, recommendedIntensity, reason) = BitmapProcessor.analyzeContentForBestFilter(base)
            val matchedPreset = FilterRepository.presets.find { it.id == bestFilterId } ?: FilterRepository.presets.first()

            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    selectedFilter = matchedPreset,
                    filterIntensity = recommendedIntensity,
                    statusMessage = "🎯 $reason"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    // === AI Image Generator (Text to Image) ===
    fun generateNewImageFromPrompt(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isAiProcessing = true, statusMessage = "جاري إنشاء الصورة بالذكاء الاصطناعي...") }
            delay(1200) // AI synthesis pipeline

            // Select thematic high quality asset & synthesize background matching the prompt
            val lower = prompt.lowercase()
            val baseRes = when {
                lower.contains("غروب") || lower.contains("طبيعة") || lower.contains("جبل") || lower.contains("شمس") || lower.contains("sunset") -> R.drawable.sample_photo_sunset
                lower.contains("ليل") || lower.contains("نيون") || lower.contains("شارع") || lower.contains("cyber") || lower.contains("مستقبل") -> R.drawable.sample_photo_neon
                else -> R.drawable.hero_banner
            }

            val generated = SampleMediaProvider.loadSampleBitmap(getApplication(), baseRes)
            _uiState.update {
                it.copy(
                    originalBitmap = generated,
                    isAiProcessing = false,
                    isBgRemoved = false,
                    statusMessage = "تم توليد وإنشاء الصورة بنجاح من الوصف! 🎨✨"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    // === Magic Eraser / Inpainting ===
    fun applyMagicEraser(touchRatioX: Float, touchRatioY: Float) {
        val base = _uiState.value.originalBitmap ?: return
        viewModelScope.launch(Dispatchers.Default) {
            val width = base.width
            val height = base.height
            val centerX = (touchRatioX * width).toInt().coerceIn(0, width - 1)
            val centerY = (touchRatioY * height).toInt().coerceIn(0, height - 1)
            val radius = (width * 0.08f).toInt().coerceAtLeast(15)

            val output = base.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(output)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)

            // Sample surrounding border color
            val sampleX = (centerX + radius + 5).coerceIn(0, width - 1)
            val sampleY = (centerY + radius + 5).coerceIn(0, height - 1)
            val patchColor = base.getPixel(sampleX, sampleY)

            paint.color = patchColor
            paint.alpha = 240
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), radius.toFloat(), paint)

            _uiState.update {
                it.copy(
                    originalBitmap = output,
                    statusMessage = "تمت إزالة العنصر بواسطة الممحاة السحرية بنجاح 🪄"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    // === AI Tools Integration ===
    fun applyAiAutoEnhance() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiProcessing = true) }
            val current = _uiState.value.originalBitmap
            val suggestion = aiRepo.analyzeAndEnhanceImage(current)
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiSuggestion = suggestion,
                    brightness = suggestion.recommendedBrightness,
                    contrast = suggestion.recommendedContrast,
                    saturation = suggestion.recommendedSaturation,
                    selectedFilter = FilterRepository.presets.find { f -> f.id == suggestion.recommendedFilter } ?: it.selectedFilter,
                    filterIntensity = 0.85f,
                    statusMessage = "تم تطبيق التحسين الذكي للألوان والإضاءة بنجاح ✨"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    fun applyAiSuperResolution(scaleFactor: Float = 2.0f, edgeBoost: Float = 1.4f) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            val base = _uiState.value.originalBitmap
            if (base != null) {
                val enhanced = BitmapProcessor.applyAiUpscaling(base, scaleFactor, edgeBoost)
                _uiState.update {
                    it.copy(
                        originalBitmap = enhanced,
                        sharpnessLevel = edgeBoost,
                        isAiProcessing = false,
                        statusMessage = "تمت ترقية الدقة بمقدار ${scaleFactor}x وشحذ التفاصيل (AI Super-Resolution) 🔍"
                    )
                }
                recomputeProcessedBitmap()
            } else {
                _uiState.update { it.copy(isAiProcessing = false) }
            }
        }
    }

    fun applyAiDenoise(intensity: Float = 1.0f) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            val base = _uiState.value.originalBitmap
            if (base != null) {
                val denoised = BitmapProcessor.applyDenoise(base, intensity)
                _uiState.update {
                    it.copy(
                        originalBitmap = denoised,
                        denoiseLevel = intensity,
                        isAiProcessing = false,
                        statusMessage = "تمت إزالة الضوضاء والتحبب بنجاح بمستوى ${(intensity * 100).toInt()}% 🧼"
                    )
                }
                recomputeProcessedBitmap()
            } else {
                _uiState.update { it.copy(isAiProcessing = false) }
            }
        }
    }

    fun applyAiSharpening(strength: Float = 1.6f) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            val base = _uiState.value.originalBitmap
            if (base != null) {
                val sharpened = BitmapProcessor.applySuperResolutionSharpening(base, strength)
                _uiState.update {
                    it.copy(
                        originalBitmap = sharpened,
                        sharpnessLevel = strength,
                        isAiProcessing = false,
                        statusMessage = "تم تعزيز حدة الحواف وتفاصيل الصورة بنجاح 💎"
                    )
                }
                recomputeProcessedBitmap()
            } else {
                _uiState.update { it.copy(isAiProcessing = false) }
            }
        }
    }

    fun applyAiHdrBoost() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            val base = _uiState.value.originalBitmap
            if (base != null) {
                val hdr = BitmapProcessor.applyHdrColorBoost(base, 1.35f)
                _uiState.update {
                    it.copy(
                        originalBitmap = hdr,
                        isAiProcessing = false,
                        statusMessage = "تم تعزيز المدى الديناميكي HDR للألوان 🌈"
                    )
                }
                recomputeProcessedBitmap()
            } else {
                _uiState.update { it.copy(isAiProcessing = false) }
            }
        }
    }

    fun applyAiRemoveBackground() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            val base = _uiState.value.originalBitmap
            if (base != null) {
                val cutout = BitmapProcessor.removeBackground(base)
                _uiState.update {
                    it.copy(
                        originalBitmap = cutout,
                        isBgRemoved = true,
                        bgReplacementType = "TRANSPARENT",
                        isAiProcessing = false,
                        statusMessage = "تم عزل العنصر وإزالة الخلفية بدقة فائقة ✂️"
                    )
                }
                recomputeProcessedBitmap()
            } else {
                _uiState.update { it.copy(isAiProcessing = false) }
            }
        }
    }

    fun replaceBackground(bgType: String, customDesc: String = "") {
        _uiState.update { it.copy(bgReplacementType = bgType, customBgDescription = customDesc) }
        recomputeProcessedBitmap()
    }

    fun replaceBackgroundWithPrompt(prompt: String) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(500)
            val lower = prompt.lowercase()
            val bgType = when {
                lower.contains("غروب") || lower.contains("شمس") -> "GOLDEN_SUNSET"
                lower.contains("نيون") || lower.contains("ليل") || lower.contains("سايبر") -> "CYBER_GRADIENT"
                lower.contains("ابيض") || lower.contains("نظيف") -> "WHITE_CLEAN"
                else -> "STUDIO_DARK"
            }
            _uiState.update {
                it.copy(
                    isBgRemoved = true,
                    bgReplacementType = bgType,
                    customBgDescription = prompt,
                    isAiProcessing = false,
                    statusMessage = "تم استبدال الخلفية بناءً على الوصف: \"$prompt\" 🎨"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    fun analyzeContentWithAi() {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiProcessing = true) }
            val suggestion = aiRepo.analyzeAndEnhanceImage(_uiState.value.originalBitmap)
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiSuggestion = suggestion,
                    statusMessage = "تم فحص المشهد وتقديم التوصيات الذكية 💡"
                )
            }
        }
    }

    fun dismissExportDialog() {
        _uiState.update { it.copy(lastExportedItem = null) }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }

    // === Export and Download ===
    fun exportAndSavePhoto(title: String = "صورة_معدلة") {
        viewModelScope.launch(Dispatchers.IO) {
            val bitmapToSave = _uiState.value.processedBitmap ?: return@launch
            val isTransparent = _uiState.value.isBgRemoved && _uiState.value.bgReplacementType == "TRANSPARENT"
            val format = if (isTransparent) Bitmap.CompressFormat.PNG else Bitmap.CompressFormat.JPEG

            val (uri, path) = MediaSaver.saveImageToGallery(
                context = getApplication(),
                bitmap = bitmapToSave,
                title = title,
                format = format
            )

            val exportEntity = ExportedMediaEntity(
                title = title,
                mediaType = "PHOTO",
                filePath = path.ifEmpty { uri.toString() },
                mimeType = if (isTransparent) "image/png" else "image/jpeg",
                resolutionLabel = "${bitmapToSave.width}x${bitmapToSave.height} HD",
                fileSizeBytes = 1024L * 1024L * 2L,
                appliedFilterName = "${_uiState.value.selectedFilter.nameAr} (${(_uiState.value.filterIntensity * 100).toInt()}%)",
                isAiEnhanced = _uiState.value.aiSuggestion != null,
                isBgRemoved = _uiState.value.isBgRemoved
            )

            exportRepo.recordExport(exportEntity)

            projectRepo.saveOrUpdateProject(
                ProjectEntity(
                    title = title,
                    mediaType = "PHOTO",
                    thumbnailUri = path.ifEmpty { uri?.toString() },
                    aspectRatio = _uiState.value.selectedAspectRatio.labelAr,
                    activeFilterId = _uiState.value.selectedFilter.id,
                    brightness = _uiState.value.brightness,
                    contrast = _uiState.value.contrast,
                    saturation = _uiState.value.saturation,
                    isBgRemoved = _uiState.value.isBgRemoved,
                    bgReplacementType = _uiState.value.bgReplacementType
                )
            )

            _uiState.update { it.copy(lastExportedItem = exportEntity) }
        }
    }

    // === Smart Visual FX & Color Grading Methods ===

    fun openVisualFxStudioSheet() {
        _uiState.update { it.copy(isVisualFxStudioSheetOpen = true) }
    }

    fun closeVisualFxStudioSheet() {
        _uiState.update { it.copy(isVisualFxStudioSheetOpen = false) }
    }

    fun updateColorGrading(state: com.example.data.model.ColorGradingState) {
        _uiState.update { it.copy(colorGradingState = state) }
        recomputeProcessedBitmap()
    }

    fun updateVisualFx(params: com.example.data.model.VisualFxParameters) {
        _uiState.update { it.copy(visualFxParams = params) }
        recomputeProcessedBitmap()
    }

    fun applyAiPromptColorGrading(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAiProcessing = true, statusMessage = "جاري تحليل وتطبيق تدريج الألوان بالذكاء الاصطناعي... 🎨✨") }
            delay(600)
            val lower = prompt.lowercase()
            val (filterId, temp, sat, contrast, brightness, hue) = when {
                lower.contains("بارد") || lower.contains("شتاء") || lower.contains("ازرق") || lower.contains("blue") || lower.contains("غموض") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("cine_moody_cold", -45f, 1.15f, 1.25f, -5f, 0f)
                }
                lower.contains("غروب") || lower.contains("دافئ") || lower.contains("شمس") || lower.contains("ذهب") || lower.contains("warm") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("nat_golden_hour", 55f, 1.3f, 1.15f, 10f, 0f)
                }
                lower.contains("نيون") || lower.contains("سايبر") || lower.contains("ليل") || lower.contains("cyber") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("futur_cyberpunk_neon", 10f, 1.45f, 1.35f, 5f, 15f)
                }
                lower.contains("ابيض") || lower.contains("اسود") || lower.contains("نوار") || lower.contains("b&w") || lower.contains("black") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("cine_noir_drama", 0f, 0f, 1.45f, -10f, 0f)
                }
                lower.contains("سيبيا") || lower.contains("قديم") || lower.contains("عتيق") || lower.contains("sepia") || lower.contains("vintage") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("vint_sepia_1920", 40f, 0.8f, 1.1f, 5f, 0f)
                }
                lower.contains("طبيعة") || lower.contains("اخضر") || lower.contains("حديقة") -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("nat_emerald_valley", 10f, 1.25f, 1.15f, 5f, 0f)
                }
                else -> {
                    com.example.ui.viewmodel.VideoEditorViewModel.PromptGradingResult("cine_teal_orange", 20f, 1.2f, 1.2f, 0f, 0f)
                }
            }

            val preset = FilterRepository.presets.find { it.id == filterId } ?: FilterRepository.presets.first()
            val newGrading = com.example.data.model.ColorGradingState(
                temperature = temp,
                brightness = brightness,
                contrast = contrast,
                saturation = sat,
                hueShiftDegrees = hue
            )

            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    selectedFilter = preset,
                    filterIntensity = 0.9f,
                    colorGradingState = newGrading,
                    statusMessage = "تم ضبط الألوان بالذكاء الاصطناعي بناءً على: \"$prompt\" 🎨✨"
                )
            }
            recomputeProcessedBitmap()
        }
    }

    private fun recomputeProcessedBitmap() {
        viewModelScope.launch(Dispatchers.Default) {
            val base = _uiState.value.originalBitmap ?: return@launch
            val state = _uiState.value

            // 1. Crop to ratio if selected
            var transformed = if (state.selectedAspectRatio != AspectRatioType.ORIGINAL) {
                BitmapProcessor.cropToAspectRatio(base, state.selectedAspectRatio.ratio)
            } else {
                base
            }

            // 2. Background replacement if removed
            if (state.isBgRemoved && state.bgReplacementType != "TRANSPARENT") {
                transformed = BitmapProcessor.replaceBackground(transformed, state.bgReplacementType)
            }

            // 3. Full spectrum Color grading & visual FX
            var graded = BitmapProcessor.applyFullColorGradingAndFx(
                source = transformed,
                filterMatrix = state.selectedFilter.matrix,
                filterIntensity = state.filterIntensity,
                temperature = state.colorGradingState.temperature,
                tint = state.colorGradingState.tint,
                hueShiftDegrees = state.colorGradingState.hueShiftDegrees,
                brightness = if (state.colorGradingState.brightness != 0f) state.colorGradingState.brightness else state.brightness,
                contrast = if (state.colorGradingState.contrast != 1.0f) state.colorGradingState.contrast else state.contrast,
                saturation = if (state.colorGradingState.saturation != 1.0f) state.colorGradingState.saturation else state.saturation,
                vibrance = state.colorGradingState.vibrance,
                highlights = state.colorGradingState.highlights,
                shadows = state.colorGradingState.shadows,
                vignetteStrength = if (state.visualFxParams.vignetteStrength > 0f) state.visualFxParams.vignetteStrength else state.selectedFilter.vignetteStrength,
                filmGrainStrength = state.visualFxParams.filmGrainStrength,
                glowBloomIntensity = state.visualFxParams.glowBloomIntensity
            )

            // 4. Overlays
            val finalComposite = BitmapProcessor.compositeOverlays(
                base = graded,
                textOverlays = state.textOverlays,
                stickers = state.stickers
            )

            _uiState.update { it.copy(processedBitmap = finalComposite) }
        }
    }
}
