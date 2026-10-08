package com.example.data.model

data class AppStudioSettings(
    // 1. Export Preferences
    val defaultExportResolution: String = "1080p FHD 60fps",
    val defaultVideoBitrate: String = "عالي الجودة (High 35Mbps)",
    val defaultVideoCodec: String = "H.264 (متوافق مع كافة الأجهزة)",
    val autoSaveToGallery: Boolean = true,
    val includeWatermark: Boolean = false,

    // 2. AI Engine & Transcription
    val defaultSubtitleLanguage: SubtitleLanguage = SubtitleLanguage.ARABIC,
    val autoAiVoiceDenoise: Boolean = true,
    val autoAiColorEnhancement: Boolean = false,
    val hardwareAccelerationEnabled: Boolean = true,
    val aiModelPreset: String = "Gemini 2.5 Flash Ultra Studio Engine",

    // 3. Cloud & Firebase Sync
    val autoCloudSyncEnabled: Boolean = true,
    val uploadOnWifiOnly: Boolean = false,
    val cloudBackupRetentionDays: Int = 30,

    // 4. UI & Interface Preferences
    val darkStudioTheme: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val showTimelineAudioWaveform: Boolean = true,
    val autoSaveDraftsIntervalSec: Int = 5,
    val interfaceLanguage: String = "العربية (Arabic)"
)
