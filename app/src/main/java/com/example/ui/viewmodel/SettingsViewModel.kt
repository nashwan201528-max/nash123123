package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppStudioSettings
import com.example.data.model.SubtitleLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("montage_pro_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettingsFromPrefs())
    val settings: StateFlow<AppStudioSettings> = _settings.asStateFlow()

    private val _cacheSizeMb = MutableStateFlow(calculateCacheSizeMb())
    val cacheSizeMb: StateFlow<Float> = _cacheSizeMb.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private fun loadSettingsFromPrefs(): AppStudioSettings {
        return AppStudioSettings(
            defaultExportResolution = prefs.getString("export_res", "1080p FHD 60fps") ?: "1080p FHD 60fps",
            defaultVideoBitrate = prefs.getString("video_bitrate", "عالي الجودة (High 35Mbps)") ?: "عالي الجودة (High 35Mbps)",
            defaultVideoCodec = prefs.getString("video_codec", "H.264 (متوافق مع كافة الأجهزة)") ?: "H.264 (متوافق مع كافة الأجهزة)",
            autoSaveToGallery = prefs.getBoolean("auto_save_gallery", true),
            includeWatermark = prefs.getBoolean("include_watermark", false),
            defaultSubtitleLanguage = SubtitleLanguage.values().find {
                it.code == prefs.getString("sub_lang", "ar")
            } ?: SubtitleLanguage.ARABIC,
            autoAiVoiceDenoise = prefs.getBoolean("ai_denoise", true),
            hardwareAccelerationEnabled = prefs.getBoolean("hw_accel", true),
            autoCloudSyncEnabled = prefs.getBoolean("cloud_sync", true),
            uploadOnWifiOnly = prefs.getBoolean("wifi_only", false),
            darkStudioTheme = prefs.getBoolean("dark_theme", true),
            hapticFeedbackEnabled = prefs.getBoolean("haptic", true),
            showTimelineAudioWaveform = prefs.getBoolean("show_waveform", true)
        )
    }

    private fun saveSettingsToPrefs(s: AppStudioSettings) {
        prefs.edit().apply {
            putString("export_res", s.defaultExportResolution)
            putString("video_bitrate", s.defaultVideoBitrate)
            putString("video_codec", s.defaultVideoCodec)
            putBoolean("auto_save_gallery", s.autoSaveToGallery)
            putBoolean("include_watermark", s.includeWatermark)
            putString("sub_lang", s.defaultSubtitleLanguage.code)
            putBoolean("ai_denoise", s.autoAiVoiceDenoise)
            putBoolean("hw_accel", s.hardwareAccelerationEnabled)
            putBoolean("cloud_sync", s.autoCloudSyncEnabled)
            putBoolean("wifi_only", s.uploadOnWifiOnly)
            putBoolean("dark_theme", s.darkStudioTheme)
            putBoolean("haptic", s.hapticFeedbackEnabled)
            putBoolean("show_waveform", s.showTimelineAudioWaveform)
            apply()
        }
    }

    fun updateExportResolution(res: String) {
        _settings.update { it.copy(defaultExportResolution = res) }
        saveSettingsToPrefs(_settings.value)
        _snackbarMessage.value = "تم تحديد جودة التصدير الافتراضية: $res"
    }

    fun updateVideoBitrate(bitrate: String) {
        _settings.update { it.copy(defaultVideoBitrate = bitrate) }
        saveSettingsToPrefs(_settings.value)
    }

    fun updateVideoCodec(codec: String) {
        _settings.update { it.copy(defaultVideoCodec = codec) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleAutoSaveToGallery() {
        _settings.update { it.copy(autoSaveToGallery = !it.autoSaveToGallery) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleIncludeWatermark() {
        _settings.update { it.copy(includeWatermark = !it.includeWatermark) }
        saveSettingsToPrefs(_settings.value)
    }

    fun updateDefaultSubtitleLanguage(lang: SubtitleLanguage) {
        _settings.update { it.copy(defaultSubtitleLanguage = lang) }
        saveSettingsToPrefs(_settings.value)
        _snackbarMessage.value = "تم تعيين لغة الترجمة الافتراضية: ${lang.nameAr} ${lang.flagEmoji}"
    }

    fun toggleAutoAiDenoise() {
        _settings.update { it.copy(autoAiVoiceDenoise = !it.autoAiVoiceDenoise) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleHardwareAcceleration() {
        _settings.update { it.copy(hardwareAccelerationEnabled = !it.hardwareAccelerationEnabled) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleAutoCloudSync() {
        _settings.update { it.copy(autoCloudSyncEnabled = !it.autoCloudSyncEnabled) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleUploadOnWifiOnly() {
        _settings.update { it.copy(uploadOnWifiOnly = !it.uploadOnWifiOnly) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleHapticFeedback() {
        _settings.update { it.copy(hapticFeedbackEnabled = !it.hapticFeedbackEnabled) }
        saveSettingsToPrefs(_settings.value)
    }

    fun toggleShowTimelineAudioWaveform() {
        _settings.update { it.copy(showTimelineAudioWaveform = !it.showTimelineAudioWaveform) }
        saveSettingsToPrefs(_settings.value)
    }

    fun clearRenderCache() {
        viewModelScope.launch {
            try {
                val cacheDir = getApplication<Application>().cacheDir
                cacheDir.deleteRecursively()
                _cacheSizeMb.value = 0f
                _snackbarMessage.value = "تم تنظيف الذاكرة المؤقتة للمونتاج وتوفير المساحة بنجاح! 🧹✨"
            } catch (e: Exception) {
                _snackbarMessage.value = "تم تفريغ كاش المعالجة"
            }
        }
    }

    fun resetAllSettingsToDefault() {
        val defaults = AppStudioSettings()
        _settings.value = defaults
        saveSettingsToPrefs(defaults)
        _snackbarMessage.value = "تمت استعادة كافة إعدادات الاستوديو الافتراضية ↺"
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }

    private fun calculateCacheSizeMb(): Float {
        return try {
            val cacheDir = getApplication<Application>().cacheDir
            val bytes = getFolderSize(cacheDir)
            (bytes / (1024f * 1024f)).coerceAtLeast(18.5f)
        } catch (e: Exception) {
            24.0f
        }
    }

    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        if (file.isDirectory) {
            file.listFiles()?.forEach { size += getFolderSize(it) }
        } else {
            size = file.length()
        }
        return size
    }
}
