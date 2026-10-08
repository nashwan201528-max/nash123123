package com.example.data.model

enum class TransitionCategory(val titleAr: String, val iconEmoji: String) {
    CINEMATIC_ZOOM("سينمائي وحركي", "🚀"),
    DISSOLVE_FADE("تلاشي واندماج", "✨"),
    DYNAMIC_SLIDE("سحب وإزاحة", "📱"),
    AI_CYBER_WARP("ذكاء اصطناعي وسايبر", "⚡")
}

enum class VideoTransitionType(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val category: TransitionCategory,
    val iconEmoji: String,
    val descriptionAr: String,
    val defaultDurationMs: Long = 800L
) {
    // 1. Cinematic & Zoom
    SMOOTH_ZOOM_IN(
        id = "zoom_in",
        nameAr = "تقريب سينمائي ناعم",
        nameEn = "Smooth Zoom In",
        category = TransitionCategory.CINEMATIC_ZOOM,
        iconEmoji = "🔍",
        descriptionAr = "تقريب بصري ديناميكي بالذكاء الاصطناعي مع تسارع ناعم."
    ),
    SMOOTH_ZOOM_OUT(
        id = "zoom_out",
        nameAr = "تبعيد سينمائي ناعم",
        nameEn = "Smooth Zoom Out",
        category = TransitionCategory.CINEMATIC_ZOOM,
        iconEmoji = "🔭",
        descriptionAr = "تراجع سلس للكادر يكشف المشهد التالي بأناقة."
    ),
    WHIP_PAN_RIGHT(
        id = "whip_right",
        nameAr = "مسح حركي سريع لليمين",
        nameEn = "Whip Pan Right",
        category = TransitionCategory.CINEMATIC_ZOOM,
        iconEmoji = "⏩",
        descriptionAr = "حركة كاميرا سينمائية سريعة مع ضبابية حركة Motion Blur."
    ),
    WHIP_PAN_LEFT(
        id = "whip_left",
        nameAr = "مسح حركي سريع لليسار",
        nameEn = "Whip Pan Left",
        category = TransitionCategory.CINEMATIC_ZOOM,
        iconEmoji = "⏪",
        descriptionAr = "انتقال حركة فائق السرعة نحو اليسار."
    ),
    SPIN_3D_ROTATE(
        id = "spin_3d",
        nameAr = "دوران 3D سينمائي",
        nameEn = "3D Spin & Rotate",
        category = TransitionCategory.CINEMATIC_ZOOM,
        iconEmoji = "🔄",
        descriptionAr = "دوران محوري ثلاثي الأبعاد بين الإطارات."
    ),

    // 2. Dissolve & Fade
    CROSS_DISSOLVE(
        id = "cross_dissolve",
        nameAr = "تلاشي متقاطع ناعم",
        nameEn = "Cross Dissolve",
        category = TransitionCategory.DISSOLVE_FADE,
        iconEmoji = "🌫️",
        descriptionAr = "اندماج وتلاشي كلاسيكي متدرج عالي النعومة."
    ),
    DIP_TO_BLACK(
        id = "dip_black",
        nameAr = "تلاشي إلى السواد",
        nameEn = "Dip to Black",
        category = TransitionCategory.DISSOLVE_FADE,
        iconEmoji = "🌑",
        descriptionAr = "انتقال درامي خافت عبر السواد مناسب لنهاية المشاهد."
    ),
    DIP_TO_WHITE(
        id = "dip_white",
        nameAr = "تلاشي ضوئي للبياض",
        nameEn = "Dip to White",
        category = TransitionCategory.DISSOLVE_FADE,
        iconEmoji = "⚪",
        descriptionAr = "إضاءة ساطعة تعبر بسلاسة للمشهد الموالي."
    ),
    FILM_BURN_LEAK(
        id = "film_burn",
        nameAr = "تسريب ضوء كلاسيكي",
        nameEn = "Film Burn & Leak",
        category = TransitionCategory.DISSOLVE_FADE,
        iconEmoji = "🎞️",
        descriptionAr = "توهج وتوهج شريط أفلام سينمائية عتيقة 35mm."
    ),

    // 3. Dynamic Slide & Push
    SLIDE_LEFT(
        id = "slide_left",
        nameAr = "سحب حركي لليسار",
        nameEn = "Slide Left Push",
        category = TransitionCategory.DYNAMIC_SLIDE,
        iconEmoji = "⬅️",
        descriptionAr = "إزاحة أفقية سلسة تدفع المقطع الحالي لليسار."
    ),
    SLIDE_RIGHT(
        id = "slide_right",
        nameAr = "سحب حركي لليمين",
        nameEn = "Slide Right Push",
        category = TransitionCategory.DYNAMIC_SLIDE,
        iconEmoji = "➡️",
        descriptionAr = "إزاحة أفقية سلسة تدفع المقطع الحالي لليمين."
    ),
    SLIDE_UP(
        id = "slide_up",
        nameAr = "سحب عمودي للأعلى",
        nameEn = "Slide Up Push",
        category = TransitionCategory.DYNAMIC_SLIDE,
        iconEmoji = "⬆️",
        descriptionAr = "حركة دفع عمودية ديناميكية ملائمة لمقاطع Reels و Shorts."
    ),
    SPLIT_DOOR(
        id = "split_door",
        nameAr = "انفتاح الباب المزدوج",
        nameEn = "Split Door Wipe",
        category = TransitionCategory.DYNAMIC_SLIDE,
        iconEmoji = "🚪",
        descriptionAr = "انفتاح المشهد من المنتصف إلى الجانبين."
    ),

    // 4. AI Glitch & Cyber Warp
    AI_WARP_MORPH(
        id = "ai_warp",
        nameAr = "تحول تشكيلي ذكي (AI Morph)",
        nameEn = "AI Morph Warp",
        category = TransitionCategory.AI_CYBER_WARP,
        iconEmoji = "🌀",
        descriptionAr = "تحول سائل يطابق الأشكال والألوان بين المشهدين بالذكاء الاصطناعي."
    ),
    AI_GLITCH_PULSE(
        id = "ai_glitch",
        nameAr = "نبضات سايبر جليتش (Glitch)",
        nameEn = "Cyber Glitch Pulse",
        category = TransitionCategory.AI_CYBER_WARP,
        iconEmoji = "⚡",
        descriptionAr = "تشويش رقمي ونبضات سايبربانك سريعة وجذابة."
    ),
    AI_FLASH_BOOST(
        id = "ai_flash",
        nameAr = "وميض فلاش حركي (Impact Flash)",
        nameEn = "Impact Flash",
        category = TransitionCategory.AI_CYBER_WARP,
        iconEmoji = "💥",
        descriptionAr = "وميض أبيض سريع يتزامن مع إيقاع الحركة."
    ),
    CIRCLE_IRIS_WIPE(
        id = "iris_circle",
        nameAr = "قناع الدائرة السينمائي",
        nameEn = "Iris Circle Wipe",
        category = TransitionCategory.AI_CYBER_WARP,
        iconEmoji = "🎯",
        descriptionAr = "انغلاق وتوسع دائري كلاسيكي يركز على نقطة الاهتمام."
    )
}

data class VideoTransition(
    val type: VideoTransitionType,
    val durationMs: Long = 800L
)
