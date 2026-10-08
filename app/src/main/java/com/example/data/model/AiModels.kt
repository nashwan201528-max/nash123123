package com.example.data.model

data class AiEnhancementSuggestion(
    val titleAr: String,
    val descriptionAr: String,
    val recommendedFilter: String,
    val recommendedBrightness: Float,
    val recommendedContrast: Float,
    val recommendedSaturation: Float,
    val creativeCaptionAr: String,
    val hashtagsAr: List<String>
)

data class AiMontageScene(
    val sceneNumber: Int,
    val sceneTitleAr: String,
    val suggestedDurationSec: Int,
    val visualDescriptionAr: String,
    val voiceoverOrTextAr: String,
    val transitionEffectAr: String,
    val soundEffectAr: String
)

data class AiMontagePlan(
    val titleAr: String,
    val targetPlatformAr: String,
    val moodAr: String,
    val estimatedTotalDurationSec: Int,
    val musicVibeAr: String,
    val scenes: List<AiMontageScene>,
    val fullScriptAr: String
)

data class AiVideoScriptResult(
    val viralHookAr: String,
    val bodyPointsAr: List<String>,
    val callToActionAr: String,
    val autoSubtitlesAr: List<SubtitleLine>
)
