package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ExportedMediaEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.ProjectJsonConverters
import com.example.data.model.AiVideoScriptResult
import com.example.data.model.AspectRatioType
import com.example.data.model.AudioMixerSettings
import com.example.data.model.AudioTrack
import com.example.data.model.BeatIntervalMode
import com.example.data.model.AiBgRemovalSettings
import com.example.data.model.BeatMarker
import com.example.data.model.BeatSyncResult
import com.example.data.model.BeatSyncSettings
import com.example.data.model.BgReplacementType
import com.example.data.model.CloudExportResult
import com.example.data.model.CloudProjectItem
import com.example.data.model.CloudSyncStatus
import com.example.data.model.ComparisonMode
import com.example.data.model.DualComparisonSettings
import com.example.data.model.FilterPreset
import com.example.data.model.FilterRepository
import com.example.data.model.VirtualBackgroundItem
import com.example.data.model.VirtualBackgroundLibrary
import com.example.data.repository.AiBgRemovalEngine
import com.example.data.model.SmartCropSettings
import com.example.data.model.SoundEffectItem
import com.example.data.model.SpeedCurvePreset
import com.example.data.model.StickerOverlay
import com.example.data.model.SubtitleLanguage
import com.example.data.model.SubtitleLine
import com.example.data.model.SubtitleStyleSettings
import com.example.data.model.TextOverlay
import com.example.data.model.TrackingSubjectMode
import com.example.data.model.VideoClip
import com.example.data.model.VideoTransition
import com.example.data.model.VideoTransitionType
import com.example.data.model.VoiceoverTrack
import com.example.data.repository.AiBeatSyncEngine
import com.example.data.repository.AiMontageRepository
import com.example.data.repository.AiSmartReframeEngine
import com.example.data.repository.AiSpeechToTextService
import com.example.data.repository.FirebaseCloudExportRepository
import com.example.data.repository.MediaExportRepository
import com.example.data.repository.ProjectRepository
import com.example.utils.MediaSaver
import com.example.utils.SampleMediaProvider
import com.example.utils.VoiceoverRecorderHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VideoEditorUiState(
    val currentDraftId: Long = 0L,
    val projectTitle: String = "مشروع فيديو سينمائي",
    val clips: List<VideoClip> = SampleMediaProvider.getSampleVideoClips(),
    val selectedClipIndex: Int = 0,
    val isPlaying: Boolean = false,
    val currentTimeMs: Long = 0L,
    val selectedFilter: FilterPreset = FilterRepository.presets.first(),
    val filterIntensity: Float = 1.0f,
    val selectedAspectRatio: AspectRatioType = AspectRatioType.STORY_9_16,
    val selectedAudioTrack: AudioTrack? = SampleMediaProvider.sampleAudioTracks.first(),
    val voiceoverTracks: List<VoiceoverTrack> = emptyList(),
    val audioMixerSettings: AudioMixerSettings = AudioMixerSettings(),
    val isRecordingVoiceover: Boolean = false,
    val recordingVoiceoverDurationSec: Float = 0f,
    val recordingAmplitudes: List<Float> = emptyList(),
    val isAudioSuiteSheetOpen: Boolean = false,
    val selectedAudioSuiteTab: Int = 0,
    val textOverlays: List<TextOverlay> = emptyList(),
    val stickers: List<StickerOverlay> = emptyList(),
    val subtitles: List<SubtitleLine> = emptyList(),
    val subtitleStyleSettings: SubtitleStyleSettings = SubtitleStyleSettings(),
    val selectedSubtitleLanguage: SubtitleLanguage = SubtitleLanguage.ARABIC,
    val isSubtitleStudioSheetOpen: Boolean = false,
    val isGeneratingSubtitles: Boolean = false,
    val smartCropSettings: SmartCropSettings = SmartCropSettings(),
    val isSmartCropSheetOpen: Boolean = false,
    val isAnalyzingSmartCrop: Boolean = false,
    val beatSyncSettings: BeatSyncSettings = BeatSyncSettings(),
    val isBeatSyncSheetOpen: Boolean = false,
    val isAnalyzingBeatSync: Boolean = false,
    val lastBeatSyncResult: BeatSyncResult? = null,
    val cloudSyncStatus: CloudSyncStatus = CloudSyncStatus(),
    val cloudProjectsList: List<CloudProjectItem> = emptyList(),
    val isCloudStorageSheetOpen: Boolean = false,
    val isExporting: Boolean = false,
    val exportProgress: Float = 0f,
    val isAiProcessing: Boolean = false,
    val aiScriptResult: AiVideoScriptResult? = null,
    val isAiQualitySheetOpen: Boolean = false,
    val isTransitionSheetOpen: Boolean = false,
    val activeTransitionTargetClipIndex: Int = 0,
    val dualComparisonSettings: DualComparisonSettings = DualComparisonSettings(),
    val bgRemovalSettings: AiBgRemovalSettings = AiBgRemovalSettings(),
    val isBgRemovalSheetOpen: Boolean = false,
    val colorGradingState: com.example.data.model.ColorGradingState = com.example.data.model.ColorGradingState(),
    val visualFxParams: com.example.data.model.VisualFxParameters = com.example.data.model.VisualFxParameters(),
    val isVisualFxStudioSheetOpen: Boolean = false,
    val transitionCustomization: com.example.data.model.TransitionCustomization = com.example.data.model.TransitionCustomization(),
    val aiUpscaleLabel: String = "1080p FHD",
    val aiDenoiseLevel: Float = 0f,
    val aiSharpenLevel: Float = 0f,
    val isAiAutoEnhanced: Boolean = false,
    val lastExportedItem: ExportedMediaEntity? = null,
    val isDraftSaved: Boolean = false,
    val statusMessage: String? = null
) {
    val totalDurationMs: Long
        get() = clips.sumOf { it.trimmedDurationMs }.coerceAtLeast(1000L)

    val currentActiveClip: VideoClip?
        get() = clips.getOrNull(selectedClipIndex)
}

class VideoEditorViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val projectRepo = ProjectRepository(db.projectDao())
    private val exportRepo = MediaExportRepository(db.exportDao())
    private val aiRepo = AiMontageRepository()
    private val speechToTextService = AiSpeechToTextService()
    private val firebaseCloudRepo = FirebaseCloudExportRepository(application)
    private val smartReframeEngine = AiSmartReframeEngine()
    private val beatSyncEngine = AiBeatSyncEngine()
    private val bgRemovalEngine = AiBgRemovalEngine()

    private val _uiState = MutableStateFlow(VideoEditorUiState())
    val uiState: StateFlow<VideoEditorUiState> = _uiState.asStateFlow()

    private val voiceoverHelper = VoiceoverRecorderHelper(application)

    init {
        startTimelinePlaybackLoop()
        observeVoiceoverHelper()
    }

    private fun observeVoiceoverHelper() {
        viewModelScope.launch {
            voiceoverHelper.isRecording.collect { isRec ->
                _uiState.update { it.copy(isRecordingVoiceover = isRec) }
            }
        }
        viewModelScope.launch {
            voiceoverHelper.recordingDurationSec.collect { dur ->
                _uiState.update { it.copy(recordingVoiceoverDurationSec = dur) }
            }
        }
        viewModelScope.launch {
            voiceoverHelper.liveAmplitudes.collect { amps ->
                _uiState.update { it.copy(recordingAmplitudes = amps) }
            }
        }
    }

    // === Audio Suite Management ===

    fun openAudioSuiteSheet(tabIndex: Int = 0) {
        _uiState.update {
            it.copy(
                isAudioSuiteSheetOpen = true,
                selectedAudioSuiteTab = tabIndex.coerceIn(0, 3)
            )
        }
    }

    fun closeAudioSuiteSheet() {
        if (_uiState.value.isRecordingVoiceover) {
            cancelVoiceoverRecording()
        }
        _uiState.update { it.copy(isAudioSuiteSheetOpen = false) }
    }

    fun selectAudioTrack(track: AudioTrack?) {
        _uiState.update {
            it.copy(
                selectedAudioTrack = track,
                isDraftSaved = false,
                statusMessage = if (track != null) "تم اختيار مقطع الموسيقى: ${track.titleAr} 🎵" else "تمت إزالة موسيقى الخلفية"
            )
        }
        autoSaveDraft()
    }

    fun setAudioTrackVolume(volume: Float) {
        _uiState.update { state ->
            val updated = state.selectedAudioTrack?.copy(volume = volume.coerceIn(0f, 2f))
            state.copy(selectedAudioTrack = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun toggleAudioTrackMute() {
        _uiState.update { state ->
            val track = state.selectedAudioTrack ?: return@update state
            val updated = track.copy(isMuted = !track.isMuted)
            state.copy(selectedAudioTrack = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun startVoiceoverRecording() {
        val currentTimelineMs = _uiState.value.currentTimeMs
        voiceoverHelper.startRecording(currentTimelineMs, viewModelScope)
        _uiState.update { it.copy(isPlaying = false, statusMessage = "جاري تسجيل التعليق الصوتي بالميكروفون... 🎙️") }
    }

    fun stopVoiceoverRecording() {
        val currentTimelineMs = _uiState.value.currentTimeMs
        val recordedTrack = voiceoverHelper.stopRecording(currentTimelineMs)
        if (recordedTrack != null) {
            _uiState.update { state ->
                state.copy(
                    voiceoverTracks = state.voiceoverTracks + recordedTrack,
                    isDraftSaved = false,
                    statusMessage = "تم حفظ وإضافة التعليق الصوتي إلى الخط الزمني بنجاح! 🎙️✨"
                )
            }
            autoSaveDraft()
        }
    }

    fun cancelVoiceoverRecording() {
        voiceoverHelper.cancelRecording()
        _uiState.update { it.copy(statusMessage = "تم إلغاء التسجيل الصوتي") }
    }

    fun removeVoiceoverTrack(id: String) {
        _uiState.update { state ->
            state.copy(
                voiceoverTracks = state.voiceoverTracks.filterNot { it.id == id },
                isDraftSaved = false,
                statusMessage = "تم حذف التعليق الصوتي 🗑️"
            )
        }
        autoSaveDraft()
    }

    fun toggleVoiceoverMute(id: String) {
        _uiState.update { state ->
            val updated = state.voiceoverTracks.map {
                if (it.id == id) it.copy(isMuted = !it.isMuted) else it
            }
            state.copy(voiceoverTracks = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun toggleVoiceoverAiDenoise(id: String) {
        _uiState.update { state ->
            val updated = state.voiceoverTracks.map {
                if (it.id == id) it.copy(isAiDenoised = !it.isAiDenoised) else it
            }
            state.copy(
                voiceoverTracks = updated,
                isDraftSaved = false,
                statusMessage = "تم تبديل تقنية تنقية الصوت بالذكاء الاصطناعي (AI Denoise) 🧼"
            )
        }
        autoSaveDraft()
    }

    fun updateMixerSettings(settings: AudioMixerSettings) {
        _uiState.update { it.copy(audioMixerSettings = settings, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun insertSoundEffect(sfx: SoundEffectItem) {
        _uiState.update {
            it.copy(
                statusMessage = "تم إدراج المؤثر الصوتي: ${sfx.nameAr} ${sfx.iconEmoji} عند التوقيت ${_uiState.value.currentTimeMs / 1000f}ث 🔊"
            )
        }
    }

    // === AI Speech-to-Text & Subtitles Suite ===

    fun openSubtitleStudioSheet() {
        _uiState.update { it.copy(isSubtitleStudioSheetOpen = true) }
    }

    fun closeSubtitleStudioSheet() {
        _uiState.update { it.copy(isSubtitleStudioSheetOpen = false) }
    }

    fun setSubtitleLanguage(lang: SubtitleLanguage) {
        _uiState.update { it.copy(selectedSubtitleLanguage = lang) }
    }

    fun updateSubtitleStyle(settings: SubtitleStyleSettings) {
        _uiState.update { it.copy(subtitleStyleSettings = settings, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun generateAutoSubtitles(contextPrompt: String = "") {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingSubtitles = true, isAiProcessing = true, statusMessage = "جاري التعرف على الصوت وتوليد الترجمة التلقائية بالذكاء الاصطناعي... 🤖🎙️") }
            try {
                val prompt = contextPrompt.ifBlank {
                    val clipsDesc = _uiState.value.clips.joinToString { it.title }
                    val voDesc = if (_uiState.value.voiceoverTracks.isNotEmpty()) "يوجد تعليق صوتي مضاف" else ""
                    "${_uiState.value.projectTitle} - مقاطع: $clipsDesc $voDesc"
                }
                val totalSec = (_uiState.value.totalDurationMs / 1000f).coerceAtLeast(5f)
                val generatedSubtitles = speechToTextService.transcribeAudioToSubtitles(
                    audioContextDescription = prompt,
                    targetLanguage = _uiState.value.selectedSubtitleLanguage,
                    totalDurationSec = totalSec
                )
                _uiState.update {
                    it.copy(
                        subtitles = generatedSubtitles,
                        isGeneratingSubtitles = false,
                        isAiProcessing = false,
                        isDraftSaved = false,
                        statusMessage = "تم توليد ${generatedSubtitles.size} أسطر ترجمة تلقائية متزامنة بدقة! ✨📝"
                    )
                }
                autoSaveDraft()
            } catch (e: Exception) {
                _uiState.update { it.copy(isGeneratingSubtitles = false, isAiProcessing = false, statusMessage = "حدث خطأ أثناء التوليد، تم استخدام نموذج الترجمة الاحتياطي.") }
            }
        }
    }

    fun translateSubtitles(targetLanguage: SubtitleLanguage) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGeneratingSubtitles = true, isAiProcessing = true, statusMessage = "جاري ترجمة النصوص إلى ${targetLanguage.nameAr}... 🌐") }
            try {
                val translated = speechToTextService.translateSubtitles(
                    subtitles = _uiState.value.subtitles,
                    targetLanguage = targetLanguage
                )
                _uiState.update {
                    it.copy(
                        subtitles = translated,
                        selectedSubtitleLanguage = targetLanguage,
                        isGeneratingSubtitles = false,
                        isAiProcessing = false,
                        isDraftSaved = false,
                        statusMessage = "تمت ترجمة الكابشن بالكامل إلى ${targetLanguage.nameAr} ${targetLanguage.flagEmoji}"
                    )
                }
                autoSaveDraft()
            } catch (e: Exception) {
                _uiState.update { it.copy(isGeneratingSubtitles = false, isAiProcessing = false) }
            }
        }
    }

    fun addSubtitleLine(line: SubtitleLine) {
        _uiState.update { state ->
            val updated = (state.subtitles + line).sortedBy { it.startTimeSec }
            state.copy(subtitles = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun updateSubtitleLine(index: Int, line: SubtitleLine) {
        _uiState.update { state ->
            val updated = state.subtitles.toMutableList()
            if (index in updated.indices) {
                updated[index] = line
            }
            state.copy(subtitles = updated.sortedBy { it.startTimeSec }, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun removeSubtitleLine(index: Int) {
        _uiState.update { state ->
            val updated = state.subtitles.toMutableList()
            if (index in updated.indices) {
                updated.removeAt(index)
            }
            state.copy(subtitles = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun clearAllSubtitles() {
        _uiState.update { it.copy(subtitles = emptyList(), isDraftSaved = false, statusMessage = "تم مسح جميع أسطر الترجمة") }
        autoSaveDraft()
    }

    private fun startTimelinePlaybackLoop() {
        viewModelScope.launch {
            while (true) {
                if (_uiState.value.isPlaying && !_uiState.value.isExporting) {
                    val current = _uiState.value.currentTimeMs
                    val total = _uiState.value.totalDurationMs
                    val next = if (current + 100L >= total) 0L else current + 100L
                    _uiState.update { it.copy(currentTimeMs = next) }
                }
                delay(100)
            }
        }
    }

    fun setProjectTitle(title: String) {
        _uiState.update { it.copy(projectTitle = title, isDraftSaved = false) }
    }

    fun togglePlayPause() {
        _uiState.update { it.copy(isPlaying = !it.isPlaying) }
    }

    fun seekTo(timeMs: Long) {
        _uiState.update { it.copy(currentTimeMs = timeMs.coerceIn(0L, it.totalDurationMs)) }
    }

    fun selectClip(index: Int) {
        _uiState.update { it.copy(selectedClipIndex = index.coerceIn(0, it.clips.size - 1)) }
    }

    fun updateClipTrim(clipIndex: Int, startTrimMs: Long, endTrimMs: Long) {
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                val clip = updated[clipIndex]
                val safeStart = startTrimMs.coerceIn(0L, clip.durationMs - 200L)
                val safeEnd = endTrimMs.coerceIn(safeStart + 200L, clip.durationMs)
                updated[clipIndex] = clip.copy(
                    startTrimMs = safeStart,
                    endTrimMs = safeEnd
                )
            }
            state.copy(clips = updated, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun adjustStartTrim(clipIndex: Int, deltaMs: Long) {
        val clip = _uiState.value.clips.getOrNull(clipIndex) ?: return
        val newStart = (clip.startTrimMs + deltaMs).coerceIn(0L, clip.endTrimMs - 200L)
        updateClipTrim(clipIndex, newStart, clip.endTrimMs)
        seekTo(newStart)
    }

    fun adjustEndTrim(clipIndex: Int, deltaMs: Long) {
        val clip = _uiState.value.clips.getOrNull(clipIndex) ?: return
        val newEnd = (clip.endTrimMs + deltaMs).coerceIn(clip.startTrimMs + 200L, clip.durationMs)
        updateClipTrim(clipIndex, clip.startTrimMs, newEnd)
        seekTo(newEnd)
    }

    fun resetClipTrim(clipIndex: Int) {
        val clip = _uiState.value.clips.getOrNull(clipIndex) ?: return
        updateClipTrim(clipIndex, 0L, clip.durationMs)
        _uiState.update { it.copy(statusMessage = "تمت إعادة ضبط المقطع إلى طوله الكامل ↺") }
    }

    fun applyTrimPreset(clipIndex: Int, presetType: String) {
        val clip = _uiState.value.clips.getOrNull(clipIndex) ?: return
        when (presetType) {
            "FIRST_3S" -> updateClipTrim(clipIndex, 0L, 3000L.coerceAtMost(clip.durationMs))
            "FIRST_5S" -> updateClipTrim(clipIndex, 0L, 5000L.coerceAtMost(clip.durationMs))
            "MIDDLE_HALF" -> {
                val quarter = clip.durationMs / 4
                updateClipTrim(clipIndex, quarter, clip.durationMs - quarter)
            }
            "LAST_3S" -> {
                val start = (clip.durationMs - 3000L).coerceAtLeast(0L)
                updateClipTrim(clipIndex, start, clip.durationMs)
            }
        }
        _uiState.update { it.copy(statusMessage = "تم تطبيق قالب القص السريع بنجاح ✂️") }
    }

    fun previewTrimmedClip(clipIndex: Int) {
        val clip = _uiState.value.clips.getOrNull(clipIndex) ?: return
        _uiState.update {
            it.copy(
                currentTimeMs = clip.startTrimMs,
                isPlaying = true,
                statusMessage = "معاينة الجزء المقصوص للمقطع... ▶️"
            )
        }
    }

    // === Speed Control & Curve Ramping ===

    fun setClipSpeed(clipIndex: Int, speed: Float, keepPitch: Boolean = true) {
        val safeSpeed = speed.coerceIn(0.1f, 10.0f)
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                updated[clipIndex] = updated[clipIndex].copy(
                    speed = safeSpeed,
                    keepAudioPitch = keepPitch,
                    speedCurve = if (safeSpeed == 1.0f) SpeedCurvePreset.CONSTANT else updated[clipIndex].speedCurve
                )
            }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تم ضبط سرعة المقطع ${clipIndex + 1} إلى ${String.format("%.2f", safeSpeed)}x ⏱️"
            )
        }
        autoSaveDraft()
    }

    fun adjustClipSpeedByDelta(clipIndex: Int, delta: Float) {
        val currentSpeed = _uiState.value.clips.getOrNull(clipIndex)?.speed ?: 1.0f
        setClipSpeed(clipIndex, (currentSpeed + delta).coerceIn(0.1f, 10.0f))
    }

    fun applySpeedToAllClips(speed: Float) {
        val safeSpeed = speed.coerceIn(0.1f, 10.0f)
        _uiState.update { state ->
            val updated = state.clips.map { it.copy(speed = safeSpeed) }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تم تطبيق سرعة ${String.format("%.2f", safeSpeed)}x على كافة مقاطع الفيديو ✨"
            )
        }
        autoSaveDraft()
    }

    fun applySpeedCurveToClip(clipIndex: Int, curvePreset: SpeedCurvePreset) {
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                val averageCurveSpeed = (curvePreset.curvePoints.sum() / curvePreset.curvePoints.size).coerceIn(0.2f, 5.0f)
                updated[clipIndex] = updated[clipIndex].copy(
                    speedCurve = curvePreset,
                    speed = averageCurveSpeed
                )
            }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تم تطبيق منحنى سرعة ${curvePreset.titleAr} ${curvePreset.iconEmoji}"
            )
        }
        autoSaveDraft()
    }

    fun toggleKeepAudioPitch(clipIndex: Int) {
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                val current = updated[clipIndex]
                updated[clipIndex] = current.copy(keepAudioPitch = !current.keepAudioPitch)
            }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تم تبديل خيار الحفاظ على نبرة الصوت الطبيعية 🎵"
            )
        }
        autoSaveDraft()
    }

    fun resetClipSpeed(clipIndex: Int) {
        setClipSpeed(clipIndex, 1.0f)
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                updated[clipIndex] = updated[clipIndex].copy(speedCurve = SpeedCurvePreset.CONSTANT, speed = 1.0f)
            }
            state.copy(clips = updated, isDraftSaved = false, statusMessage = "تمت إعادة ضبط سرعة المقطع للوضع الطبيعي 1.0x ↺")
        }
        autoSaveDraft()
    }

    fun selectFilter(filter: FilterPreset) {
        _uiState.update { it.copy(selectedFilter = filter, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun setFilterIntensity(intensity: Float) {
        _uiState.update { it.copy(filterIntensity = intensity.coerceIn(0f, 1f), isDraftSaved = false) }
    }

    // === Smart Visual FX & Color Grading Studio Suite ===

    fun openVisualFxStudioSheet() {
        _uiState.update { it.copy(isVisualFxStudioSheetOpen = true) }
    }

    fun closeVisualFxStudioSheet() {
        _uiState.update { it.copy(isVisualFxStudioSheetOpen = false) }
    }

    fun updateColorGrading(state: com.example.data.model.ColorGradingState) {
        _uiState.update { it.copy(colorGradingState = state, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun updateVisualFx(params: com.example.data.model.VisualFxParameters) {
        _uiState.update { it.copy(visualFxParams = params, isDraftSaved = false) }
        autoSaveDraft()
    }

    data class PromptGradingResult(
        val filterId: String,
        val temp: Float,
        val sat: Float,
        val contrast: Float,
        val brightness: Float,
        val hue: Float
    )

    fun applyAiPromptColorGrading(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isAiProcessing = true, statusMessage = "جاري تحليل الوصف وتوليد تدريج الألوان بالذكاء الاصطناعي... 🎨🤖") }
            delay(700)
            val lower = prompt.lowercase()
            val result = when {
                lower.contains("بارد") || lower.contains("شتاء") || lower.contains("ازرق") || lower.contains("blue") || lower.contains("غموض") -> {
                    PromptGradingResult("cine_moody_cold", -45f, 1.15f, 1.25f, -5f, 0f)
                }
                lower.contains("غروب") || lower.contains("دافئ") || lower.contains("شمس") || lower.contains("ذهب") || lower.contains("warm") -> {
                    PromptGradingResult("nat_golden_hour", 55f, 1.3f, 1.15f, 10f, 0f)
                }
                lower.contains("نيون") || lower.contains("سايبر") || lower.contains("ليل") || lower.contains("cyber") -> {
                    PromptGradingResult("futur_cyberpunk_neon", 10f, 1.45f, 1.35f, 5f, 15f)
                }
                lower.contains("ابيض") || lower.contains("اسود") || lower.contains("نوار") || lower.contains("b&w") || lower.contains("black") -> {
                    PromptGradingResult("cine_noir_drama", 0f, 0f, 1.45f, -10f, 0f)
                }
                lower.contains("سيبيا") || lower.contains("قديم") || lower.contains("عتيق") || lower.contains("sepia") || lower.contains("vintage") -> {
                    PromptGradingResult("vint_sepia_1920", 40f, 0.8f, 1.1f, 5f, 0f)
                }
                lower.contains("طبيعة") || lower.contains("اخضر") || lower.contains("حديقة") -> {
                    PromptGradingResult("nat_emerald_valley", 10f, 1.25f, 1.15f, 5f, 0f)
                }
                else -> {
                    PromptGradingResult("cine_teal_orange", 20f, 1.2f, 1.2f, 0f, 0f)
                }
            }

            val preset = FilterRepository.presets.find { it.id == result.filterId } ?: FilterRepository.presets.first()
            val newGrading = com.example.data.model.ColorGradingState(
                temperature = result.temp,
                brightness = result.brightness,
                contrast = result.contrast,
                saturation = result.sat,
                hueShiftDegrees = result.hue
            )

            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    selectedFilter = preset,
                    filterIntensity = 0.9f,
                    colorGradingState = newGrading,
                    isDraftSaved = false,
                    statusMessage = "تم توليد وضبط تدريج الألوان بالذكاء الاصطناعي بناءً على: \"$prompt\" ✨"
                )
            }
            autoSaveDraft()
        }
    }

    // === Applying Comprehensive Video Template ===

    fun applyComprehensiveTemplate(template: com.example.data.model.ComprehensiveVideoTemplate) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAiProcessing = true,
                    statusMessage = "جاري تكييف وتطبيق قالب \"${template.titleAr}\"... 🎬⚡"
                )
            }
            delay(400)

            val newClips = template.scenes.mapIndexed { index, scene ->
                VideoClip(
                    id = "template_clip_${index + 1}",
                    title = scene.titleAr,
                    durationMs = scene.durationMs,
                    startTrimMs = 0L,
                    endTrimMs = scene.durationMs,
                    speed = 1.0f,
                    colorHex = scene.colorHex,
                    transitionToNext = scene.transitionToNext?.let { VideoTransition(it, 800L) },
                    filterName = template.titleAr
                )
            }

            val matchingFilter = FilterRepository.presets.find { it.id == template.recommendedFilterId }
                ?: FilterRepository.presets.first()

            val matchingAudio = SampleMediaProvider.sampleAudioTracks.find {
                it.titleAr.contains(template.musicTrackTitleAr, ignoreCase = true) || template.musicTrackTitleAr.contains(it.titleAr, ignoreCase = true)
            } ?: SampleMediaProvider.sampleAudioTracks.first()

            val templateOverlays = template.scenes.mapNotNull { scene ->
                scene.textOverlay?.let { text ->
                    com.example.data.model.TextOverlay(
                        id = "text_${System.currentTimeMillis()}_${scene.titleAr.hashCode()}",
                        text = text,
                        xPosRatio = 0.5f,
                        yPosRatio = 0.8f,
                        textColorHex = 0xFFFFFFFF,
                        bgColorHex = 0xAA000000,
                        fontSizeSp = 22f,
                        isBold = true
                    )
                }
            }

            _uiState.update {
                it.copy(
                    projectTitle = template.titleAr,
                    clips = newClips,
                    selectedAspectRatio = template.defaultAspectRatio,
                    selectedFilter = matchingFilter,
                    filterIntensity = 0.95f,
                    selectedAudioTrack = matchingAudio,
                    textOverlays = templateOverlays,
                    selectedClipIndex = 0,
                    currentTimeMs = 0L,
                    isPlaying = false,
                    isAiProcessing = false,
                    isDraftSaved = false,
                    statusMessage = "تم تطبيق قالب \"${template.titleAr}\" بنجاح! 🎬✨"
                )
            }
            autoSaveDraft()
        }
    }

    // === Dual Comparison Preview (Before vs After) ===

    fun toggleDualComparison() {
        _uiState.update { state ->
            val current = state.dualComparisonSettings
            val newState = current.copy(isEnabled = !current.isEnabled)
            state.copy(
                dualComparisonSettings = newState,
                statusMessage = if (newState.isEnabled) "تم تفعيل وضع المعاينة المزدوجة (قبل / بعد) ⚖️" else "تم إيقاف وضع المقارنة"
            )
        }
    }

    fun setDualComparisonMode(mode: ComparisonMode) {
        _uiState.update { state ->
            state.copy(
                dualComparisonSettings = state.dualComparisonSettings.copy(mode = mode, isEnabled = true),
                statusMessage = "نمط المقارنة: ${mode.titleAr} ${mode.iconEmoji}"
            )
        }
    }

    fun setDualComparisonSplitPosition(position: Float) {
        _uiState.update { state ->
            state.copy(
                dualComparisonSettings = state.dualComparisonSettings.copy(
                    splitPosition = position.coerceIn(0.05f, 0.95f)
                )
            )
        }
    }

    fun setComparingOriginalRaw(isRaw: Boolean) {
        _uiState.update { state ->
            state.copy(
                dualComparisonSettings = state.dualComparisonSettings.copy(isComparingOriginalRaw = isRaw)
            )
        }
    }

    fun updateDualComparisonSettings(settings: DualComparisonSettings) {
        _uiState.update { state ->
            state.copy(dualComparisonSettings = settings)
        }
    }

    fun resetDualComparisonSplit() {
        _uiState.update { state ->
            state.copy(
                dualComparisonSettings = state.dualComparisonSettings.copy(splitPosition = 0.5f),
                statusMessage = "تمت إعادة توسيط شريط المقارنة 50/50 ↺"
            )
        }
    }

    // === AI Background Removal & Virtual Sets (Green-Screen Free) ===

    fun openBgRemovalSheet() {
        _uiState.update { it.copy(isBgRemovalSheetOpen = true) }
    }

    fun closeBgRemovalSheet() {
        _uiState.update { it.copy(isBgRemovalSheetOpen = false) }
    }

    fun toggleBgRemoval() {
        _uiState.update { state ->
            val current = state.bgRemovalSettings
            val newState = current.copy(isEnabled = !current.isEnabled)
            state.copy(
                bgRemovalSettings = newState,
                isDraftSaved = false,
                statusMessage = if (newState.isEnabled) "تم تفعيل عزل واستبدال الخلفية بالذكاء الاصطناعي ✂️✨" else "تم إيقاف عزل الخلفية"
            )
        }
        autoSaveDraft()
    }

    fun setBgReplacementType(type: BgReplacementType) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAiProcessing = true,
                    statusMessage = "جاري عزل وتكييف المشهد بنمط ${type.titleAr}... 🧠✂️"
                )
            }
            val activeClipTitle = _uiState.value.currentActiveClip?.title ?: _uiState.value.projectTitle
            val settings = bgRemovalEngine.analyzeAndSegmentSubject(
                clipTitle = activeClipTitle,
                targetType = type,
                currentSettings = _uiState.value.bgRemovalSettings
            )
            _uiState.update {
                it.copy(
                    bgRemovalSettings = settings,
                    isAiProcessing = false,
                    isDraftSaved = false,
                    statusMessage = "تم تطبيق ${type.titleAr} بدقة عزل ${(settings.aiSegmentationConfidence * 100).toInt()}% ✨"
                )
            }
            autoSaveDraft()
        }
    }

    fun selectVirtualBackground(bgItem: VirtualBackgroundItem) {
        _uiState.update { state ->
            state.copy(
                bgRemovalSettings = state.bgRemovalSettings.copy(
                    selectedBgItem = bgItem,
                    isEnabled = true,
                    replacementType = if (state.bgRemovalSettings.replacementType == BgReplacementType.BLUR_BOKEH) BgReplacementType.VIRTUAL_STUDIO else state.bgRemovalSettings.replacementType
                ),
                isDraftSaved = false,
                statusMessage = "تم تطبيق خلفية: ${bgItem.titleAr} ${bgItem.iconEmoji}"
            )
        }
        autoSaveDraft()
    }

    fun setBgBlurIntensity(blur: Float) {
        _uiState.update { state ->
            state.copy(
                bgRemovalSettings = state.bgRemovalSettings.copy(blurIntensity = blur.coerceIn(0.1f, 1f)),
                isDraftSaved = false
            )
        }
    }

    fun setBgEdgeFeathering(feather: Float) {
        _uiState.update { state ->
            state.copy(
                bgRemovalSettings = state.bgRemovalSettings.copy(edgeFeathering = feather.coerceIn(0.1f, 1f)),
                isDraftSaved = false
            )
        }
    }

    fun toggleBgRimLight() {
        _uiState.update { state ->
            val current = state.bgRemovalSettings
            state.copy(
                bgRemovalSettings = current.copy(isRimLightingEnabled = !current.isRimLightingEnabled),
                isDraftSaved = false,
                statusMessage = if (!current.isRimLightingEnabled) "تم تفعيل إضاءة الحواف السينمائية (Rim Light) 💡" else "تم إيقاف إضاءة الحواف"
            )
        }
        autoSaveDraft()
    }

    fun updateBgRemovalSettings(settings: AiBgRemovalSettings) {
        _uiState.update { state ->
            state.copy(bgRemovalSettings = settings, isDraftSaved = false)
        }
        autoSaveDraft()
    }

    fun generateAiCustomBackground(prompt: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAiProcessing = true,
                    statusMessage = "جاري تصميم وضبط إضاءة الخلفية المخصصة بالذكاء الاصطناعي... 🎨✨"
                )
            }
            val newBg = bgRemovalEngine.generateCustomAiBackground(prompt)
            _uiState.update { state ->
                state.copy(
                    isAiProcessing = false,
                    bgRemovalSettings = state.bgRemovalSettings.copy(
                        selectedBgItem = newBg,
                        replacementType = BgReplacementType.AI_CUSTOM,
                        isEnabled = true,
                        customAiPrompt = prompt
                    ),
                    isDraftSaved = false,
                    statusMessage = "تم توليد وتطبيق الخلفية: ${newBg.titleAr} ✨"
                )
            }
            autoSaveDraft()
        }
    }

    fun resetBgRemoval() {
        _uiState.update { state ->
            state.copy(
                bgRemovalSettings = AiBgRemovalSettings(),
                isDraftSaved = false,
                statusMessage = "تمت إعادة ضبط إعدادات عزل الخلفية للوضع الافتراضي ↺"
            )
        }
        autoSaveDraft()
    }

    // === AI Smart Cropping & Auto-Reframe ===

    fun openSmartCropSheet() {
        _uiState.update { it.copy(isSmartCropSheetOpen = true) }
    }

    fun closeSmartCropSheet() {
        _uiState.update { it.copy(isSmartCropSheetOpen = false) }
    }

    fun applySmartCrop(
        targetRatio: AspectRatioType,
        trackingMode: TrackingSubjectMode = _uiState.value.smartCropSettings.trackingMode
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isAnalyzingSmartCrop = true,
                    isAiProcessing = true,
                    statusMessage = "جاري الكشف عن العنصر وتوسيط الكادر بنسبة ${targetRatio.labelAr} بالذكاء الاصطناعي... 🎯📱"
                )
            }

            val activeClipTitle = _uiState.value.currentActiveClip?.title ?: _uiState.value.projectTitle
            val settings = smartReframeEngine.analyzeAndAutoReframe(
                clipTitle = activeClipTitle,
                targetRatio = targetRatio,
                trackingMode = trackingMode
            )

            _uiState.update {
                it.copy(
                    selectedAspectRatio = targetRatio,
                    smartCropSettings = settings,
                    isAnalyzingSmartCrop = false,
                    isAiProcessing = false,
                    isDraftSaved = false,
                    statusMessage = "تم توسيط الكادر وتطبيق القص الذكي بدقة ${(settings.detectedSubjectConfidence * 100).toInt()}% ✨🎯"
                )
            }
            autoSaveDraft()
        }
    }

    fun updateSmartCropSettings(settings: SmartCropSettings) {
        _uiState.update {
            it.copy(
                selectedAspectRatio = settings.targetRatio,
                smartCropSettings = settings,
                isDraftSaved = false
            )
        }
        autoSaveDraft()
    }

    fun toggleAiAutoTracking() {
        _uiState.update { state ->
            val current = state.smartCropSettings
            state.copy(
                smartCropSettings = current.copy(isAiAutoTrackingEnabled = !current.isAiAutoTrackingEnabled),
                isDraftSaved = false,
                statusMessage = if (!current.isAiAutoTrackingEnabled) "تم تفعيل التتبع التلقائي للعنصر بالذكاء الاصطناعي 🎯" else "تم إيقاف التتبع التلقائي"
            )
        }
        autoSaveDraft()
    }

    fun adjustFocalOffset(offsetX: Float, offsetY: Float) {
        _uiState.update { state ->
            val current = state.smartCropSettings
            state.copy(
                smartCropSettings = current.copy(
                    focalOffsetX = offsetX.coerceIn(0f, 1f),
                    focalOffsetY = offsetY.coerceIn(0f, 1f)
                ),
                isDraftSaved = false
            )
        }
        autoSaveDraft()
    }

    fun resetSmartCrop() {
        _uiState.update {
            it.copy(
                selectedAspectRatio = AspectRatioType.ORIGINAL,
                smartCropSettings = SmartCropSettings(),
                isDraftSaved = false,
                statusMessage = "تمت إعادة ضبط أبعاد وقص الفيديو للوضع الأصلي ↺"
            )
        }
        autoSaveDraft()
    }

    // === AI Music Beat Sync & Rhythm Trimming ===

    fun openBeatSyncSheet() {
        val currentTrack = _uiState.value.selectedAudioTrack ?: SampleMediaProvider.sampleAudioTracks.first()
        val markers = beatSyncEngine.generateBeatMarkers(currentTrack, (_uiState.value.totalDurationMs / 1000L).toInt().coerceAtLeast(15))
        _uiState.update {
            it.copy(
                isBeatSyncSheetOpen = true,
                beatSyncSettings = it.beatSyncSettings.copy(
                    bpm = currentTrack.bpm,
                    detectedBeatsCount = markers.size,
                    beatMarkers = markers
                )
            )
        }
    }

    fun closeBeatSyncSheet() {
        _uiState.update { it.copy(isBeatSyncSheetOpen = false) }
    }

    fun syncClipsToAudioBeat(
        intervalMode: BeatIntervalMode = _uiState.value.beatSyncSettings.intervalMode,
        applyTransitions: Boolean = _uiState.value.beatSyncSettings.applyTransitionsOnBeats
    ) {
        viewModelScope.launch {
            val audioTrack = _uiState.value.selectedAudioTrack ?: SampleMediaProvider.sampleAudioTracks.first()
            _uiState.update {
                it.copy(
                    isAnalyzingBeatSync = true,
                    isAiProcessing = true,
                    statusMessage = "جاري تحليل إيقاع \"${audioTrack.titleAr}\" ومزامنة لقطات الفيديو مع ضربات البيس... 🎵⚡"
                )
            }

            val result = beatSyncEngine.syncClipsToBeat(
                clips = _uiState.value.clips,
                audioTrack = audioTrack,
                intervalMode = intervalMode,
                applyDynamicTransitions = applyTransitions
            )

            val markers = beatSyncEngine.generateBeatMarkers(audioTrack, (result.totalDurationMs / 1000L).toInt().coerceAtLeast(10))

            _uiState.update {
                it.copy(
                    clips = result.adjustedClips,
                    beatSyncSettings = it.beatSyncSettings.copy(
                        isBeatSyncActive = true,
                        bpm = audioTrack.bpm,
                        intervalMode = intervalMode,
                        applyTransitionsOnBeats = applyTransitions,
                        detectedBeatsCount = markers.size,
                        beatMarkers = markers
                    ),
                    lastBeatSyncResult = result,
                    isAnalyzingBeatSync = false,
                    isAiProcessing = false,
                    isDraftSaved = false,
                    statusMessage = result.summaryAr
                )
            }
            autoSaveDraft()
        }
    }

    fun updateBeatSyncSettings(settings: BeatSyncSettings) {
        _uiState.update { it.copy(beatSyncSettings = settings, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun selectAspectRatio(ratio: AspectRatioType) {
        applySmartCrop(ratio)
    }

    fun addTextOverlay(textOverlay: TextOverlay) {
        _uiState.update { it.copy(textOverlays = it.textOverlays + textOverlay, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun addSticker(sticker: StickerOverlay) {
        _uiState.update { it.copy(stickers = it.stickers + sticker, isDraftSaved = false) }
        autoSaveDraft()
    }

    fun splitCurrentClip() {
        val currentIndex = _uiState.value.selectedClipIndex
        val clips = _uiState.value.clips.toMutableList()
        if (currentIndex in clips.indices) {
            val original = clips[currentIndex]
            val midPoint = (original.startTrimMs + original.endTrimMs) / 2
            val clipPart1 = original.copy(id = "${original.id}_part1", endTrimMs = midPoint)
            val clipPart2 = original.copy(
                id = "${original.id}_part2",
                title = "${original.title} (مقطع 2)",
                startTrimMs = midPoint,
                colorHex = 0xFFEC4899
            )
            clips.removeAt(currentIndex)
            clips.add(currentIndex, clipPart2)
            clips.add(currentIndex, clipPart1)
            _uiState.update {
                it.copy(
                    clips = clips,
                    isDraftSaved = false,
                    statusMessage = "تم تقسيم المقطع بنجاح ✂️"
                )
            }
            autoSaveDraft()
        }
    }

    // === AI Video Transitions ===

    fun openTransitionPicker(clipIndex: Int) {
        _uiState.update {
            it.copy(
                isTransitionSheetOpen = true,
                activeTransitionTargetClipIndex = clipIndex.coerceIn(0, (it.clips.size - 2).coerceAtLeast(0))
            )
        }
    }

    fun closeTransitionPicker() {
        _uiState.update { it.copy(isTransitionSheetOpen = false) }
    }

    fun setClipTransition(clipIndex: Int, transitionType: VideoTransitionType?, durationMs: Long = 800L) {
        _uiState.update { state ->
            val updated = state.clips.toMutableList()
            if (clipIndex in updated.indices) {
                val transition = transitionType?.let { VideoTransition(it, durationMs) }
                updated[clipIndex] = updated[clipIndex].copy(transitionToNext = transition)
            }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = if (transitionType != null) "تم تطبيق انتقال ${transitionType.nameAr} ${transitionType.iconEmoji}" else "تمت إزالة الانتقال"
            )
        }
        autoSaveDraft()
    }

    fun applyTransitionToAllClips(transitionType: VideoTransitionType, durationMs: Long = 800L) {
        _uiState.update { state ->
            val updated = state.clips.mapIndexed { index, clip ->
                if (index < state.clips.size - 1) {
                    clip.copy(transitionToNext = VideoTransition(transitionType, durationMs))
                } else {
                    clip
                }
            }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تم تطبيق انتقال ${transitionType.nameAr} ${transitionType.iconEmoji} على كافة مقاطع الفيديو ✨"
            )
        }
        autoSaveDraft()
    }

    fun removeClipTransition(clipIndex: Int) {
        setClipTransition(clipIndex, null)
    }

    fun removeAllTransitions() {
        _uiState.update { state ->
            val updated = state.clips.map { it.copy(transitionToNext = null) }
            state.copy(
                clips = updated,
                isDraftSaved = false,
                statusMessage = "تمت إزالة كافة الانتقالات والعودة للقطع المباشر ↺"
            )
        }
        autoSaveDraft()
    }

    // === Draft Management (Room Persistence) ===

    fun saveDraft(customTitle: String? = null, showNotification: Boolean = true) {
        viewModelScope.launch(Dispatchers.IO) {
            val titleToUse = customTitle ?: _uiState.value.projectTitle
            val clipsJson = ProjectJsonConverters.videoClipsToJson(_uiState.value.clips)
            val subtitlesJson = ProjectJsonConverters.subtitlesToJson(_uiState.value.subtitles)
            val textOverlaysJson = ProjectJsonConverters.textOverlaysToJson(_uiState.value.textOverlays)

            val draftEntity = ProjectEntity(
                id = _uiState.value.currentDraftId,
                title = titleToUse,
                mediaType = "VIDEO",
                durationMs = _uiState.value.totalDurationMs,
                aspectRatio = _uiState.value.selectedAspectRatio.labelAr,
                activeFilterId = _uiState.value.selectedFilter.id,
                filterIntensity = _uiState.value.filterIntensity,
                videoClipsJson = clipsJson,
                subtitlesJson = subtitlesJson,
                textOverlaysJson = textOverlaysJson,
                selectedAudioTrackId = _uiState.value.selectedAudioTrack?.id,
                lastModifiedTimestamp = System.currentTimeMillis(),
                notes = "${_uiState.value.clips.size} مقاطع • ${_uiState.value.selectedFilter.nameAr}"
            )

            val savedId = projectRepo.saveOrUpdateProject(draftEntity)
            _uiState.update {
                it.copy(
                    currentDraftId = savedId,
                    projectTitle = titleToUse,
                    isDraftSaved = true,
                    statusMessage = if (showNotification) "تم حفظ مسودة الفيديو في قاعدة البيانات بنجاح 💾" else it.statusMessage
                )
            }
        }
    }

    private fun autoSaveDraft() {
        if (_uiState.value.currentDraftId != 0L) {
            saveDraft(showNotification = false)
        }
    }

    fun loadDraft(project: ProjectEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val restoredClips = ProjectJsonConverters.jsonToVideoClips(project.videoClipsJson).ifEmpty {
                SampleMediaProvider.getSampleVideoClips()
            }
            val restoredSubtitles = ProjectJsonConverters.jsonToSubtitles(project.subtitlesJson)
            val restoredTextOverlays = ProjectJsonConverters.jsonToTextOverlays(project.textOverlaysJson)
            val restoredFilter = FilterRepository.presets.find { it.id == project.activeFilterId }
                ?: FilterRepository.presets.first()
            val restoredAudioTrack = SampleMediaProvider.sampleAudioTracks.find { it.id == project.selectedAudioTrackId }

            _uiState.update {
                it.copy(
                    currentDraftId = project.id,
                    projectTitle = project.title,
                    clips = restoredClips,
                    subtitles = restoredSubtitles,
                    textOverlays = restoredTextOverlays,
                    selectedFilter = restoredFilter,
                    filterIntensity = project.filterIntensity,
                    selectedAudioTrack = restoredAudioTrack,
                    currentTimeMs = 0L,
                    isPlaying = false,
                    isDraftSaved = true,
                    statusMessage = "تم استرجاع مسودة المشروع: \"${project.title}\" بنجاح 📂✨"
                )
            }
        }
    }

    fun createNewProject() {
        _uiState.update {
            VideoEditorUiState(
                currentDraftId = 0L,
                projectTitle = "مشروع فيديو جديد",
                clips = SampleMediaProvider.getSampleVideoClips(),
                subtitles = emptyList(),
                textOverlays = emptyList(),
                isDraftSaved = false,
                statusMessage = "تم فتح مشروع فيديو جديد فارغ 🎬"
            )
        }
    }

    // === AI Video Tools ===

    fun openAiQualitySheet() {
        _uiState.update { it.copy(isAiQualitySheetOpen = true) }
    }

    fun closeAiQualitySheet() {
        _uiState.update { it.copy(isAiQualitySheetOpen = false) }
    }

    fun applyAiVideoAutoMaster() {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(600) // Deep learning simulation pass
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    isAiAutoEnhanced = true,
                    aiSharpenLevel = 1.4f,
                    aiDenoiseLevel = 0.8f,
                    filterIntensity = 0.9f,
                    isDraftSaved = false,
                    statusMessage = "تم تطبيق التحسين الذكي الشامل للفيديو (AI Auto Master) ✨"
                )
            }
            autoSaveDraft()
        }
    }

    fun applyAiVideoUpscaling(scaleFactor: Float) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(800)
            val label = when {
                scaleFactor >= 3.0f -> "4K Ultra HD (3840x2160)"
                scaleFactor >= 2.0f -> "2K QHD (2560x1440)"
                else -> "1080p Full HD (1920x1080)"
            }
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiUpscaleLabel = label,
                    isDraftSaved = false,
                    statusMessage = "تمت ترقية دقة الفيديو إلى $label بتقنية Super-Resolution 🔍"
                )
            }
            autoSaveDraft()
        }
    }

    fun applyAiVideoDenoise(intensity: Float) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(500)
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiDenoiseLevel = intensity,
                    isDraftSaved = false,
                    statusMessage = "تم تفعيل فلتر تقليل الضوضاء والتحبب في الفيديو بمستوى ${(intensity * 100).toInt()}% 🧼"
                )
            }
            autoSaveDraft()
        }
    }

    fun applyAiVideoSharpening(strength: Float) {
        viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(isAiProcessing = true) }
            delay(500)
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiSharpenLevel = strength,
                    isDraftSaved = false,
                    statusMessage = "تم تطبيق شحذ التفاصيل الدقيقة وحواف الإطارات 💎"
                )
            }
            autoSaveDraft()
        }
    }

    fun generateAiAutoSubtitles(prompt: String = "مونتاج فيديو احترافي") {
        viewModelScope.launch {
            _uiState.update { it.copy(isAiProcessing = true) }
            val result = aiRepo.generateAutoSubtitlesAndScript(prompt)
            _uiState.update {
                it.copy(
                    isAiProcessing = false,
                    aiScriptResult = result,
                    subtitles = result.autoSubtitlesAr,
                    isDraftSaved = false,
                    statusMessage = "تم توليد الترجمة والكابشن التلقائي بالذكاء الاصطناعي 📝✨"
                )
            }
            autoSaveDraft()
        }
    }

    fun exportAndDownloadVideo(title: String = "مونتاج_فيديو_احترافي", resolutionLabel: String = "1080p FHD") {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isExporting = true, exportProgress = 0.05f) }

            for (progress in 10..100 step 15) {
                delay(250)
                _uiState.update { it.copy(exportProgress = progress / 100f) }
            }

            val durationSec = (_uiState.value.totalDurationMs / 1000L).toInt().coerceAtLeast(1)

            val (uri, path) = MediaSaver.saveVideoExport(
                context = getApplication(),
                title = title,
                durationSec = durationSec
            )

            val exportEntity = ExportedMediaEntity(
                title = title,
                mediaType = "VIDEO",
                filePath = path.ifEmpty { uri.toString() },
                mimeType = "video/mp4",
                resolutionLabel = resolutionLabel,
                fileSizeBytes = 1024L * 1024L * 18L,
                durationSec = durationSec,
                appliedFilterName = _uiState.value.selectedFilter.nameAr,
                isAiEnhanced = _uiState.value.aiScriptResult != null
            )

            exportRepo.recordExport(exportEntity)

            // Save completed project state to Room DB
            saveDraft(customTitle = title, showNotification = false)

            _uiState.update {
                it.copy(
                    isExporting = false,
                    exportProgress = 1f,
                    lastExportedItem = exportEntity,
                    statusMessage = "تم تصدير وحفظ الفيديو في جهازك بنجاح! 🎬🎉"
                )
            }
        }
    }

    // === Firebase Cloud Storage Export & Multi-Device Sync ===

    fun openCloudStorageSheet() {
        _uiState.update { it.copy(isCloudStorageSheetOpen = true) }
        fetchCloudProjects()
    }

    fun closeCloudStorageSheet() {
        _uiState.update { it.copy(isCloudStorageSheetOpen = false) }
    }

    fun fetchCloudProjects() {
        viewModelScope.launch {
            val list = firebaseCloudRepo.getCloudProjectsList()
            _uiState.update { it.copy(cloudProjectsList = list) }
        }
    }

    fun exportToFirebaseCloudStorage(resolutionLabel: String = "1080p FHD") {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cloudSyncStatus = CloudSyncStatus(
                        isUploading = true,
                        uploadProgress = 0.05f,
                        currentStepLabel = "بدء الاتصال بـ Firebase Cloud Storage... ☁️"
                    ),
                    statusMessage = "جاري رفع ومزامنة الفيديو سحابياً على Firebase... ☁️"
                )
            }

            val currentDraft = ProjectEntity(
                id = _uiState.value.currentDraftId,
                title = _uiState.value.projectTitle,
                mediaType = "VIDEO",
                durationMs = _uiState.value.totalDurationMs,
                aspectRatio = _uiState.value.selectedAspectRatio.labelAr,
                activeFilterId = _uiState.value.selectedFilter.id,
                filterIntensity = _uiState.value.filterIntensity,
                videoClipsJson = ProjectJsonConverters.videoClipsToJson(_uiState.value.clips),
                subtitlesJson = ProjectJsonConverters.subtitlesToJson(_uiState.value.subtitles),
                textOverlaysJson = ProjectJsonConverters.textOverlaysToJson(_uiState.value.textOverlays),
                selectedAudioTrackId = _uiState.value.selectedAudioTrack?.id,
                lastModifiedTimestamp = System.currentTimeMillis()
            )

            val result = firebaseCloudRepo.exportProjectToFirebaseStorage(
                project = currentDraft,
                resolutionLabel = resolutionLabel
            ) { progress, step ->
                _uiState.update { state ->
                    state.copy(
                        cloudSyncStatus = state.cloudSyncStatus.copy(
                            uploadProgress = progress,
                            currentStepLabel = step
                        )
                    )
                }
            }

            _uiState.update { state ->
                state.copy(
                    cloudSyncStatus = CloudSyncStatus(
                        isUploading = false,
                        uploadProgress = 1.0f,
                        currentStepLabel = "تم الرفع السحابي بنجاح! ✨",
                        lastExportResult = result
                    ),
                    statusMessage = "تم تصدير الفيديو وحفظه في مساحة Firebase السحابية لجميع أجهزتك! ☁️🎉"
                )
            }
            fetchCloudProjects()
        }
    }

    fun dismissExportDialog() {
        _uiState.update { it.copy(lastExportedItem = null) }
    }

    fun clearStatusMessage() {
        _uiState.update { it.copy(statusMessage = null) }
    }
}
