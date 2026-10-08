package com.example.data.model

enum class SubtitleLanguage(val code: String, val nameAr: String, val nameNative: String, val flagEmoji: String) {
    ARABIC("ar", "العربية", "العربية", "🇸🇦"),
    ENGLISH("en", "الإنجليزية", "English", "🇺🇸"),
    FRENCH("fr", "الفرنسية", "Français", "🇫🇷"),
    SPANISH("es", "الإسبانية", "Español", "🇪🇸"),
    GERMAN("de", "الألمانية", "Deutsch", "🇩🇪"),
    TURKISH("tr", "التركية", "Türkçe", "🇹🇷"),
    JAPANESE("ja", "اليابانية", "日本語", "🇯🇵"),
    CHINESE("zh", "الصينية", "中文", "🇨🇳"),
    HINDI("hi", "الهندية", "हिन्दी", "🇮🇳"),
    URDU("ur", "الأردية", "اردو", "🇵🇰")
}

enum class SubtitleFontFamily(val id: String, val labelAr: String) {
    VIRAL_REELS("viral_reels", "عريض تيك توك وريلز (Viral Bold) 🔥"),
    CINEMATIC_KUFI("kufi", "كوفي سينمائي (Cinematic Kufi) 🎬"),
    MODERN_SANS("modern_sans", "نسخ عصري ناعم (Modern Sans) ✨"),
    ELEGANT_SERIF("serif", "كلاسيكي أنيق (Classic Serif) 📜"),
    TECH_MONOSPACE("mono", "سايبربانك رقمي (Tech Mono) ⚡")
}

enum class SubtitlePosition(val labelAr: String) {
    BOTTOM("أسفل الكادر (افتراضي)"),
    CENTER("منتصف الكادر (ريلز تيك توك)"),
    TOP("أعلى الكادر (عناوين)")
}

enum class SubtitleAnimation(val labelAr: String, val iconEmoji: String) {
    KARAOKE_GLOW("كاريوكي متوهج", "✨"),
    POP_BOUNCE("وثب حركي ديناميكي", "⚡"),
    FADE_IN("تلاشي سينمائي ناعم", "🌫️"),
    STATIC("ثابت فائق الوضوح", "🎯")
}

data class SubtitleStyleSettings(
    val fontFamily: SubtitleFontFamily = SubtitleFontFamily.VIRAL_REELS,
    val fontSizeSp: Float = 22f,
    val textColorHex: Long = 0xFFFFFFFF,
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val hasBackgroundBox: Boolean = true,
    val bgColorHex: Long = 0xAA000000,
    val highlightWordColorHex: Long = 0xFFFACC15, // Golden yellow karaoke highlight
    val position: SubtitlePosition = SubtitlePosition.BOTTOM,
    val animation: SubtitleAnimation = SubtitleAnimation.KARAOKE_GLOW,
    val hasTextShadow: Boolean = true
)

data class SubtitleLine(
    val startTimeSec: Float,
    val endTimeSec: Float,
    val textAr: String,
    val id: String = "sub_${System.currentTimeMillis()}_${(0..999).random()}"
)
