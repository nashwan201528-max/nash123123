package com.example.data.model

enum class AudioTrackType {
    MUSIC,
    VOICEOVER,
    SOUND_EFFECT
}

data class SoundEffectItem(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val categoryAr: String,
    val iconEmoji: String,
    val durationSec: Float,
    val descriptionAr: String
)

data class VoiceoverTrack(
    val id: String,
    val title: String = "تعليق صوتي 🎙️",
    val startTimelineMs: Long = 0L,
    val durationMs: Long = 3000L,
    val volume: Float = 1.0f,
    val isMuted: Boolean = false,
    val isAiDenoised: Boolean = true,
    val amplitudes: List<Float> = listOf(0.2f, 0.4f, 0.7f, 0.9f, 0.6f, 0.8f, 0.3f, 0.5f, 0.8f, 0.4f)
)

data class AudioMixerSettings(
    val videoOriginalVolume: Float = 1.0f,
    val isVideoOriginalMuted: Boolean = false,
    val backgroundMusicVolume: Float = 0.85f,
    val isBackgroundMusicMuted: Boolean = false,
    val voiceoverVolume: Float = 1.2f,
    val isVoiceoverMuted: Boolean = false,
    val sfxVolume: Float = 1.0f,
    val isSfxMuted: Boolean = false,
    val isSmartDuckingEnabled: Boolean = true,
    val isFadeInEnabled: Boolean = true,
    val isFadeOutEnabled: Boolean = true
)

object SoundEffectsLibrary {
    val effects = listOf(
        SoundEffectItem(
            id = "sfx_whoosh_fast",
            nameAr = "مسح حركي سريع (Whoosh)",
            nameEn = "Fast Whoosh Transition",
            categoryAr = "انتقالات",
            iconEmoji = "💨",
            durationSec = 0.8f,
            descriptionAr = "مؤثر حركة هوائية سريع جداً مثالي مع انتقالات المشاهد."
        ),
        SoundEffectItem(
            id = "sfx_camera_shutter",
            nameAr = "غالق الكاميرا (Camera Click)",
            nameEn = "DSLR Shutter Click",
            categoryAr = "كاميرا",
            iconEmoji = "📸",
            durationSec = 0.5f,
            descriptionAr = "صوت التقاط صور احترافية لقطات متتالية."
        ),
        SoundEffectItem(
            id = "sfx_impact_boom",
            nameAr = "ضربة سينمائية عميقة (Bass Boom)",
            nameEn = "Cinematic Impact Boom",
            categoryAr = "سينمائي",
            iconEmoji = "💥",
            durationSec = 1.8f,
            descriptionAr = "صوت بيس ضخم للمشاهد الدرامية وبداية العناوين."
        ),
        SoundEffectItem(
            id = "sfx_riser_swell",
            nameAr = "تصاعد تشويقي (Riser Tension)",
            nameEn = "Cinematic Riser Swell",
            categoryAr = "سينمائي",
            iconEmoji = "📈",
            durationSec = 2.5f,
            descriptionAr = "تصاعد نغمي يزيد الترقب قبل اللحظة الحاسمة."
        ),
        SoundEffectItem(
            id = "sfx_pop_click",
            nameAr = "فرقعة وزر منبثق (UI Pop)",
            nameEn = "Modern Pop Bubble",
            categoryAr = "عصري",
            iconEmoji = "🎈",
            durationSec = 0.3f,
            descriptionAr = "مؤثر ظهور الملصقات والنصوص التفاعلية."
        ),
        SoundEffectItem(
            id = "sfx_cyber_glitch",
            nameAr = "تشويش إلكتروني (Cyber Glitch)",
            nameEn = "Digital Data Glitch",
            categoryAr = "سايبر",
            iconEmoji = "⚡",
            durationSec = 0.9f,
            descriptionAr = "مؤثر تشويش رقمي ممتاز مع مقاطع الخيال والتقنية."
        ),
        SoundEffectItem(
            id = "sfx_applause_cheer",
            nameAr = "تصفيق وهتاف (Applause)",
            nameEn = "Crowd Applause",
            categoryAr = "جمهور",
            iconEmoji = "👏",
            durationSec = 3.0f,
            descriptionAr = "تصفيق حار للمسابقات ونهايات الفيديوهات."
        ),
        SoundEffectItem(
            id = "sfx_bell_ding",
            nameAr = "رنين جرس الإشعار (Notification Ding)",
            nameEn = "Success Ding",
            categoryAr = "تنبيهات",
            iconEmoji = "🔔",
            durationSec = 0.6f,
            descriptionAr = "تنبيه لافت للانتباه لدعوة الاشتراك والتفاعل."
        )
    )
}
