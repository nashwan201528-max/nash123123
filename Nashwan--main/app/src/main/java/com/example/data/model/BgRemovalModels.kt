package com.example.data.model

import com.example.R

enum class BgReplacementType(
    val id: String,
    val titleAr: String,
    val iconEmoji: String,
    val descriptionAr: String
) {
    BLUR_BOKEH(
        id = "blur_bokeh",
        titleAr = "تمويه سينمائي (Bokeh Blur)",
        iconEmoji = "🌫️",
        descriptionAr = "عزل الخلفية الأصلية وطمسها بنعومة لتركيز العدسة على المتحدث تماماً كما في كاميرات DSLR."
    ),
    VIRTUAL_STUDIO(
        id = "virtual_studio",
        titleAr = "استوديوهات ومناظر (Virtual Sets)",
        iconEmoji = "🌆",
        descriptionAr = "استبدال الخلفية بأماكن احترافية مثل استوديوهات البودكاست، المدن المستقبلية، والمكاتب الفاخرة."
    ),
    SOLID_GRADIENT(
        id = "solid_gradient",
        titleAr = "ألوان وتدرجات استوديو (Colors)",
        iconEmoji = "🎨",
        descriptionAr = "وضع خلفية ملونة نقية أو تدرج نيون سينمائي أو كروما خضراء للتصدير المخصص."
    ),
    TRANSPARENT_CUTOUT(
        id = "transparent",
        titleAr = "تفريغ شفاف (Alpha Cutout)",
        iconEmoji = "🔲",
        descriptionAr = "إزالة الخلفية بالكامل وعزل الشخص كعنصر شفاف جاهز للدمج والتصدير."
    ),
    AI_CUSTOM(
        id = "ai_custom",
        titleAr = "توليد بالذكاء الاصطناعي (AI Generated)",
        iconEmoji = "✨",
        descriptionAr = "اكتب أي مكان تتخيله وسيقوم الذكاء الاصطناعي بتصميمه ووضعه كخلفية متناسقة مع إضاءتك."
    )
}

data class VirtualBackgroundItem(
    val id: String,
    val titleAr: String,
    val categoryAr: String,
    val iconEmoji: String,
    val previewGradientColors: List<Long>,
    val drawableRes: Int = R.drawable.sample_photo_neon,
    val descriptionAr: String
)

object VirtualBackgroundLibrary {
    val presets: List<VirtualBackgroundItem> = listOf(
        VirtualBackgroundItem(
            id = "bg_podcast_studio",
            titleAr = "استوديو بودكاست احترافي",
            categoryAr = "استوديوهات",
            iconEmoji = "🎙️",
            previewGradientColors = listOf(0xFF1E1B4B, 0xFF312E81, 0xFF0F172A),
            drawableRes = R.drawable.sample_photo_neon,
            descriptionAr = "جدران عازلة للصوت مع إضاءة نيون دافئة ورفوف كتب عصرية مخصصة لصناع المحتوى."
        ),
        VirtualBackgroundItem(
            id = "bg_cyber_neon",
            titleAr = "مدينة نيون مستقبلية",
            categoryAr = "خيال علمي",
            iconEmoji = "🌆",
            previewGradientColors = listOf(0xFF0F172A, 0xFF06B6D4, 0xFF7C3AED),
            drawableRes = R.drawable.hero_banner,
            descriptionAr = "أفق مدينة ليلية مع ناطحات سحاب متلألئة بأضواء النيون السايبربانك."
        ),
        VirtualBackgroundItem(
            id = "bg_sunset_beach",
            titleAr = "شاطئ المحيط الذهبي",
            categoryAr = "طبيعة",
            iconEmoji = "🌅",
            previewGradientColors = listOf(0xFFF97316, 0xFFF59E0B, 0xFF3B82F6),
            drawableRes = R.drawable.sample_photo_sunset,
            descriptionAr = "غروب شمس ساحر فوق مياه المحيط مع أمواج هادئة وانعكاسات ذهبية دافئة."
        ),
        VirtualBackgroundItem(
            id = "bg_luxury_loft",
            titleAr = "مكتب وشقة فاخرة",
            categoryAr = "استوديوهات",
            iconEmoji = "🏢",
            previewGradientColors = listOf(0xFF27272A, 0xFF3F3F46, 0xFF18181B),
            drawableRes = R.drawable.sample_photo_neon,
            descriptionAr = "تصميم مينيماليست حديث بنوافذ بانورامية واسعة وإضاءة معمارية أنيقة."
        ),
        VirtualBackgroundItem(
            id = "bg_space_galaxy",
            titleAr = "فضاء ومجرات كونية",
            categoryAr = "خيال علمي",
            iconEmoji = "🌌",
            previewGradientColors = listOf(0xFF020617, 0xFF38BDF8, 0xFF4C1D95),
            drawableRes = R.drawable.hero_banner,
            descriptionAr = "سدم ومجرات متلألئة بنجوم بعيدة تمنح الفيديو طابعاً استكشافياً فريداً."
        ),
        VirtualBackgroundItem(
            id = "bg_chroma_green",
            titleAr = "شاشة كروما خضراء 100%",
            categoryAr = "كروما",
            iconEmoji = "🟩",
            previewGradientColors = listOf(0xFF00FF00, 0xFF00DD00, 0xFF00AA00),
            drawableRes = R.drawable.sample_photo_neon,
            descriptionAr = "شاشة خضراء نقية تسهل عمليات المونتاج والقص في برامج المونتاج الأخرى."
        ),
        VirtualBackgroundItem(
            id = "bg_dark_gradient",
            titleAr = "تدرج رمادي سينمائي",
            categoryAr = "بسيط",
            iconEmoji = "🎬",
            previewGradientColors = listOf(0xFF090D16, 0xFF1E293B, 0xFF020617),
            drawableRes = R.drawable.sample_photo_neon,
            descriptionAr = "خلفية داكنة خافتة تعطي تركيزاً بنسبة 100% على ملامح الشخصية المتحدثة."
        ),
        VirtualBackgroundItem(
            id = "bg_warm_amber",
            titleAr = "توهج دافئ ومريح",
            categoryAr = "بسيط",
            iconEmoji = "🕯️",
            previewGradientColors = listOf(0xFF451A03, 0xFF78350F, 0xFF1E1B4B),
            drawableRes = R.drawable.sample_photo_sunset,
            descriptionAr = "إضاءة جانبية ناعمة تحاكي غروب الشمس وتمنح الدفء لدرجات البشرة."
        )
    )
}

data class AiBgRemovalSettings(
    val isEnabled: Boolean = false,
    val replacementType: BgReplacementType = BgReplacementType.BLUR_BOKEH,
    val selectedBgItem: VirtualBackgroundItem = VirtualBackgroundLibrary.presets.first(),
    val blurIntensity: Float = 0.70f, // 0.0 to 1.0
    val edgeFeathering: Float = 0.50f, // 0.0 to 1.0 (Edge smoothness)
    val isRimLightingEnabled: Boolean = true,
    val rimLightColorHex: Long = 0xFF06B6D4, // Cyan rim light default
    val rimLightIntensity: Float = 0.40f,
    val subjectShadowIntensity: Float = 0.35f,
    val aiSegmentationConfidence: Float = 0.98f,
    val customAiPrompt: String = "",
    val isAiGeneratingBg: Boolean = false
)
