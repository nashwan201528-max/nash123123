package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ExportedMediaEntity
import com.example.data.local.ProjectEntity
import com.example.data.model.AiMontagePlan
import com.example.data.repository.AiMontageRepository
import com.example.data.repository.MediaExportRepository
import com.example.data.repository.ProjectRepository
import com.example.utils.MediaSaver
import com.example.utils.MontageTemplate
import com.example.utils.SampleMediaProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AiMontageUiState(
    val promptInput: String = "مونتاج سياحي شيق لأجمل المعالم السياحية مع موسيقى حماسية",
    val selectedPlatform: String = "TikTok & Reels (9:16)",
    val selectedDurationSec: Int = 20,
    val isGenerating: Boolean = false,
    val generatedPlan: AiMontagePlan? = null,
    val activeTemplate: MontageTemplate? = null,
    val isExporting: Boolean = false,
    val exportProgress: Float = 0f,
    val lastExportedItem: ExportedMediaEntity? = null,
    val statusMessage: String? = null
)

class AiMontageViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val projectRepo = ProjectRepository(db.projectDao())
    private val exportRepo = MediaExportRepository(db.exportDao())
    private val aiRepo = AiMontageRepository()

    private val _uiState = MutableStateFlow(AiMontageUiState())
    val uiState: StateFlow<AiMontageUiState> = _uiState.asStateFlow()

    init {
        // Generate an initial plan right away for immediate preview
        generateMontagePlan("رحلة سياحية ساحرة بين الطبيعة والغروب")
    }

    fun setPrompt(value: String) {
        _uiState.update { it.copy(promptInput = value) }
    }

    fun setPlatform(platform: String) {
        _uiState.update { it.copy(selectedPlatform = platform) }
    }

    fun setDuration(sec: Int) {
        _uiState.update { it.copy(selectedDurationSec = sec) }
    }

    fun applyTemplate(template: MontageTemplate) {
        _uiState.update {
            it.copy(
                activeTemplate = template,
                promptInput = template.descriptionAr,
                selectedPlatform = if (template.aspectRatio == "9:16") "TikTok & Reels (9:16)" else "YouTube & TV (16:9)",
                selectedDurationSec = template.durationSec
            )
        }
        generateMontagePlan(template.descriptionAr)
    }

    fun generateMontagePlan(customPrompt: String? = null) {
        val query = customPrompt ?: _uiState.value.promptInput
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true) }
            val plan = aiRepo.generateMontagePlan(
                topicPrompt = query,
                platform = _uiState.value.selectedPlatform,
                durationSec = _uiState.value.selectedDurationSec
            )
            _uiState.update {
                it.copy(
                    isGenerating = false,
                    generatedPlan = plan,
                    statusMessage = "تم توليد خطة المونتاج الذكي والمشاهد بنجاح! 🪄🎬"
                )
            }
        }
    }

    fun exportMontage() {
        val plan = _uiState.value.generatedPlan ?: return
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isExporting = true, exportProgress = 0.1f) }

            for (p in 20..100 step 20) {
                delay(200)
                _uiState.update { it.copy(exportProgress = p / 100f) }
            }

            val (uri, path) = MediaSaver.saveVideoExport(
                context = getApplication(),
                title = plan.titleAr,
                durationSec = plan.estimatedTotalDurationSec
            )

            val exportEntity = ExportedMediaEntity(
                title = plan.titleAr,
                mediaType = "MONTAGE",
                filePath = path.ifEmpty { uri.toString() },
                mimeType = "video/mp4",
                resolutionLabel = "1080p FHD 60fps",
                fileSizeBytes = 1024L * 1024L * 24L,
                durationSec = plan.estimatedTotalDurationSec,
                appliedFilterName = "مونتاج ذكي مخصص",
                isAiEnhanced = true
            )

            exportRepo.recordExport(exportEntity)

            projectRepo.saveOrUpdateProject(
                ProjectEntity(
                    title = plan.titleAr,
                    mediaType = "MONTAGE",
                    thumbnailUri = path.ifEmpty { uri?.toString() },
                    durationMs = (plan.estimatedTotalDurationSec * 1000L),
                    aspectRatio = plan.targetPlatformAr,
                    notes = plan.moodAr
                )
            )

            _uiState.update {
                it.copy(
                    isExporting = false,
                    lastExportedItem = exportEntity,
                    statusMessage = "تم تصدير وتحميل المونتاج الذكي بنجاح! 🎉"
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
}
