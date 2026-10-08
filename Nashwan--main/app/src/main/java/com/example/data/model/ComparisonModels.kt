package com.example.data.model

enum class ComparisonMode(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val iconEmoji: String,
    val descriptionAr: String
) {
    SPLIT_SLIDER(
        id = "split_slider",
        titleAr = "شريط منزلق (Wipe Slider)",
        titleEn = "Split Wipe",
        iconEmoji = "↔️",
        descriptionAr = "شريط تفاعلي قابل للسحب يميناً ويساراً لمقارنة تأثير الفلاتر والألوان بدقة متناهية."
    ),
    SIDE_BY_SIDE(
        id = "side_by_side",
        titleAr = "جنباً إلى جنب (Dual View)",
        titleEn = "Side by Side",
        iconEmoji = "👥",
        descriptionAr = "عرض كادرين متزامنين جنباً إلى جنب لرؤية اللقطة الأصلية والمعدلة معاً في نفس اللحظة."
    ),
    HORIZONTAL_SPLIT(
        id = "horizontal_split",
        titleAr = "تقسيم أفقي (Top / Bottom)",
        titleEn = "Horizontal Split",
        iconEmoji = "↕️",
        descriptionAr = "مقارنة النصف العلوي الأصلي مع النصف السفلي المعدل بتدرج بصري نقي."
    ),
    HOLD_TO_PREVIEW(
        id = "hold_to_preview",
        titleAr = "الضغط للمعاينة (Hold to Compare)",
        titleEn = "Hold to View",
        iconEmoji = "👆",
        descriptionAr = "اضغط مطولاً لمعاينة الفيديو الأصلي الخام بدون أي تعديلات، ثم ارفع إصبعك للمعدل."
    )
}

data class DualComparisonSettings(
    val isEnabled: Boolean = false,
    val mode: ComparisonMode = ComparisonMode.SPLIT_SLIDER,
    val splitPosition: Float = 0.5f, // 0.0 to 1.0 (50% center by default)
    val isComparingOriginalRaw: Boolean = false, // When true (e.g. during hold), show 100% raw
    val showParameterDiffBadge: Boolean = true,
    val showDividerGlow: Boolean = true,
    val isLiveZoomEnabled: Boolean = false,
    val beforeLabelAr: String = "قبل (الأصلي الخام)",
    val afterLabelAr: String = "بعد (تعديل الألوان والفلاتر)"
)
