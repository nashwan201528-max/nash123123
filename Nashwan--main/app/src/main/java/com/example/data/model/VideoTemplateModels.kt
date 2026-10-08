package com.example.data.model

data class VideoTemplateCategory(
    val id: String,
    val titleAr: String,
    val iconEmoji: String,
    val descriptionAr: String
)

data class TemplateSceneItem(
    val titleAr: String,
    val durationMs: Long,
    val transitionToNext: VideoTransitionType? = null,
    val filterId: String? = null,
    val textOverlay: String? = null,
    val colorHex: Long = 0xFF3B82F6
)

data class ComprehensiveVideoTemplate(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val categoryId: String,
    val iconEmoji: String,
    val defaultAspectRatio: AspectRatioType,
    val totalDurationSec: Int,
    val musicTrackTitleAr: String,
    val musicTrackBpm: Int,
    val recommendedFilterId: String,
    val descriptionAr: String,
    val isTrending: Boolean = false,
    val scenes: List<TemplateSceneItem>,
    val tags: List<String>
)

object VideoTemplateRepository {

    val categories: List<VideoTemplateCategory> = listOf(
        VideoTemplateCategory(
            id = "all",
            titleAr = "جميع القوالب",
            iconEmoji = "✨",
            descriptionAr = "تصفح كافة قوالب الفيديو الجاهزة"
        ),
        VideoTemplateCategory(
            id = "trending_reels",
            titleAr = "تريند وتيك توك (Reels)",
            iconEmoji = "🔥",
            descriptionAr = "قوالب سريعة متزامنة مع الإيقاع لمنصات TikTok و Instagram Reels"
        ),
        VideoTemplateCategory(
            id = "cinematic_film",
            titleAr = "سينمائي وأفلام",
            iconEmoji = "🎬",
            descriptionAr = "تدرجات ألوان هوليوود وانتقالات حركية ناعمة تحاكي الأفلام السينمائية"
        ),
        VideoTemplateCategory(
            id = "travel_vlog",
            titleAr = "فلوج وسفر ومغامرة",
            iconEmoji = "✈️",
            descriptionAr = "توثيق الرحلات والمناظر الطبيعية بألوان دافئة ونصوص وصفية أنيقة"
        ),
        VideoTemplateCategory(
            id = "promo_business",
            titleAr = "إعلانات وتجارة إلكترونية",
            iconEmoji = "🛍️",
            descriptionAr = "عرض المنتجات والخدمات بدقة HDR مع دعوة جذابة للشراء (CTA)"
        ),
        VideoTemplateCategory(
            id = "gaming_action",
            titleAr = "ألعاب وجيمنج وسرعة",
            iconEmoji = "🎮",
            descriptionAr = "لقطات ألعاب حماسية مع ومضات فلاش وجليتش مستقبلي وتكبير سريع"
        ),
        VideoTemplateCategory(
            id = "podcast_quotes",
            titleAr = "بودكاست واقتباسات",
            iconEmoji = "🎙️",
            descriptionAr = "قوالب تركيز على المتحدث مع نصوص عربية بارزة وخلفيات مريحة للعين"
        ),
        VideoTemplateCategory(
            id = "events_memories",
            titleAr = "مناسبات وأعراس وذكريات",
            iconEmoji = "💍",
            descriptionAr = "أجواء رومانسية دافئة مع تلاشي ناعم وتأثيرات شريط الأفلام الكلاسيكية"
        )
    )

    val templates: List<ComprehensiveVideoTemplate> = listOf(
        ComprehensiveVideoTemplate(
            id = "tpl_fast_beat_reels",
            titleAr = "إيقاع حماسي وسرعة خاطفة (Viral Beat Drop)",
            titleEn = "Viral Beat Drop Reels",
            categoryId = "trending_reels",
            iconEmoji = "⚡",
            defaultAspectRatio = AspectRatioType.STORY_9_16,
            totalDurationSec = 12,
            musicTrackTitleAr = "إيقاع حماسي للتيك توك والريلز (Upbeat Viral)",
            musicTrackBpm = 135,
            recommendedFilterId = "cine_teal_orange",
            descriptionAr = "قالب سريع للغاية يتزامن مع ضربات الموسيقى، يتضمن تقريب حركي سريع وانتقالات وميض تجذب انتباه المشاهد في أول ثانية.",
            isTrending = true,
            tags = listOf("تيك توك", "ريلز", "تريند", "حماسي", "سريع"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: الهوك الافتتاحي (Hook)", 2000L, VideoTransitionType.SMOOTH_ZOOM_IN, "cine_teal_orange", "لا تفوت هذا السر! 🔥", 0xFF06B6D4),
                TemplateSceneItem("مشهد 2: تسارع نبضي (Speed Ramp)", 2500L, VideoTransitionType.AI_FLASH_BOOST, "futur_cyberpunk_neon", "النتيجة الصادمة ⚡", 0xFF8B5CF6),
                TemplateSceneItem("مشهد 3: استعراض التفاصيل (Details)", 3500L, VideoTransitionType.WHIP_PAN_RIGHT, "cine_teal_orange", "شاهد الجودة 💎", 0xFFEC4899),
                TemplateSceneItem("مشهد 4: الخاتمة والدعوة (CTA)", 4000L, VideoTransitionType.CROSS_DISSOLVE, "nat_golden_hour", "تابعنا للمزيد ✨", 0xFFF59E0B)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_cinematic_hollywood",
            titleAr = "فيلم سينمائي هوليوودي (Hollywood Film Look)",
            titleEn = "Hollywood Cinematic Story",
            categoryId = "cinematic_film",
            iconEmoji = "🎬",
            defaultAspectRatio = AspectRatioType.CINEMA_16_9,
            totalDurationSec = 24,
            musicTrackTitleAr = "لحن سينمائي ملحمي (Epic Cinematic)",
            musicTrackBpm = 95,
            recommendedFilterId = "cine_teal_orange",
            descriptionAr = "تدرجات ألوان تيل وأورانج احترافية مع تلاشي ناعم وإضاءة عريضة تمنح مقاطعك هيبة الأفلام الحائزة على جوائز.",
            isTrending = true,
            tags = listOf("سينمائي", "أفلام", "تيل_أورانج", "قصة", "4K"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: لقطة بانورامية عريضة (Wide Shot)", 6000L, VideoTransitionType.CROSS_DISSOLVE, "cine_teal_orange", "بداية الرحلة", 0xFF0284C7),
                TemplateSceneItem("مشهد 2: حركة مقربة درامية (Close-up)", 5000L, VideoTransitionType.SMOOTH_ZOOM_OUT, "cine_bleach_bypass", "اللحظة الحاسمة", 0xFF475569),
                TemplateSceneItem("مشهد 3: المشهد الملحمي (Climax)", 7000L, VideoTransitionType.WHIP_PAN_LEFT, "cine_epic_gold", "قمة الإنجاز", 0xFFD97706),
                TemplateSceneItem("مشهد 4: تلاشي الختام (Outro)", 6000L, VideoTransitionType.DIP_TO_BLACK, "cine_noir_drama", "النهاية", 0xFF0F172A)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_travel_golden_sunset",
            titleAr = "رحلة المغامرات والغروب (Golden Travel Vlog)",
            titleEn = "Golden Travel Adventure",
            categoryId = "travel_vlog",
            iconEmoji = "🌅",
            defaultAspectRatio = AspectRatioType.STORY_9_16,
            totalDurationSec = 18,
            musicTrackTitleAr = "إيقاع حركة وسفر وتشويق (Travel Beats)",
            musicTrackBpm = 110,
            recommendedFilterId = "nat_golden_hour",
            descriptionAr = "ألوان شمسية دافئة وانتقالات مسح سلسة تجعل رحلاتك وتجاربك السياحية تبدو كحلم رائع.",
            isTrending = true,
            tags = listOf("سفر", "غروب", "طبيعة", "فلوج", "سياحة"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: استكشاف الوجهة 🌴", 4500L, VideoTransitionType.SLIDE_LEFT, "nat_golden_hour", "وجهة الأحلام 📍", 0xFFF59E0B),
                TemplateSceneItem("مشهد 2: لحظات الطبيعة والماء 🌊", 4500L, VideoTransitionType.SMOOTH_ZOOM_IN, "nat_ocean_breeze", "جمال لا يوصف 💙", 0xFF06B6D4),
                TemplateSceneItem("مشهد 3: الغروب الساحر 🌇", 5000L, VideoTransitionType.CROSS_DISSOLVE, "nat_golden_hour", "الساعة الذهبية ✨", 0xFFEA580C),
                TemplateSceneItem("مشهد 4: ذكريات لا تُنسى 📷", 4000L, VideoTransitionType.DIP_TO_WHITE, "vint_polaroid_70s", "احفظ اللحظة 💫", 0xFFEC4899)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_ecom_product_showcase",
            titleAr = "إعلان تجاري فخم للمنتجات (Luxury Product Promo)",
            titleEn = "E-Commerce Showcase",
            categoryId = "promo_business",
            iconEmoji = "🛍️",
            defaultAspectRatio = AspectRatioType.SQUARE_1_1,
            totalDurationSec = 15,
            musicTrackTitleAr = "موسيقى هادئة ودافئة للقصص (Lofi Chill Vibes)",
            musicTrackBpm = 100,
            recommendedFilterId = "nat_crystal_clear",
            descriptionAr = "إبراز تفاصيل المنتج وخاماته بدقة HDR كريستالية، مع بطاقات نصوص عصرية للأسعار والمميزات.",
            isTrending = false,
            tags = listOf("تسويق", "منتجات", "متجر", "عروض", "خصم"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: الكشف عن المنتج (Reveal)", 3500L, VideoTransitionType.SMOOTH_ZOOM_IN, "nat_crystal_clear", "الأفضل في فئته 👑", 0xFF10B981),
                TemplateSceneItem("مشهد 2: استعراض الخامات والمزايا (Features)", 4500L, VideoTransitionType.SPLIT_DOOR, "nat_crystal_clear", "جودة تدوم طويلاً 💎", 0xFF06B6D4),
                TemplateSceneItem("مشهد 3: العرض والخصم الخاص (Special Offer)", 4000L, VideoTransitionType.AI_FLASH_BOOST, "cine_epic_gold", "خصم 30% لفترة محدودة 🏷️", 0xFFF59E0B),
                TemplateSceneItem("مشهد 4: رابط الشراء والتوصيل (Order Now)", 3000L, VideoTransitionType.SLIDE_UP, "nat_crystal_clear", "اطلب الآن عبر الرابط 📦", 0xFF6366F1)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_cyber_gaming_highlights",
            titleAr = "مونتاج ألعاب وجيمنج نيون (Cyberpunk Gaming Highlights)",
            titleEn = "Cyberpunk Gaming Clutches",
            categoryId = "gaming_action",
            iconEmoji = "🎮",
            defaultAspectRatio = AspectRatioType.CINEMA_16_9,
            totalDurationSec = 16,
            musicTrackTitleAr = "موسيقى سايبربانك إلكترونية (Synthwave 80s)",
            musicTrackBpm = 140,
            recommendedFilterId = "futur_cyberpunk_neon",
            descriptionAr = "مؤثرات جليتش رقمية، أضواء نيون فوشيا وأزرق كهربائي، وتقريب سريع مخصص للقطات الكلتش والانتصارات.",
            isTrending = true,
            tags = listOf("جيمنج", "ألعاب", "سايبربانك", "نيون", "كلتش"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: تحضير اللقطة (The Setup)", 3000L, VideoTransitionType.AI_GLITCH_PULSE, "futur_cyberpunk_neon", "READY TO CLUTCH 🎯", 0xFF8B5CF6),
                TemplateSceneItem("مشهد 2: ذروة الاشتباك (The Action Kill)", 4000L, VideoTransitionType.AI_FLASH_BOOST, "futur_cyberpunk_neon", "INSANE PLAY! 💥", 0xFFEC4899),
                TemplateSceneItem("مشهد 3: الإعادة البطيئة (Slow-Mo Victory)", 5000L, VideoTransitionType.SPIN_3D_ROTATE, "futur_matrix_green", "VICTORY ROYALE 🏆", 0xFF10B981),
                TemplateSceneItem("مشهد 4: شاشة المتابعة (Follow for More)", 4000L, VideoTransitionType.SLIDE_LEFT, "futur_synthwave_sunset", "GG WP 🔥", 0xFF3B82F6)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_podcast_insightful_quote",
            titleAr = "كليب بودكاست وحكمة ملهمة (Podcast Highlights)",
            titleEn = "Podcast Insight Reel",
            categoryId = "podcast_quotes",
            iconEmoji = "🎙️",
            defaultAspectRatio = AspectRatioType.STORY_9_16,
            totalDurationSec = 20,
            musicTrackTitleAr = "خلفية حوارية للبودكاست (Podcast Ambience)",
            musicTrackBpm = 80,
            recommendedFilterId = "cine_moody_cold",
            descriptionAr = "تنسيق مخصص لمقاطع البودكاست مع مساحة بارزة للكابشن والترجمة التلقائية وإبراز صوت المتحدث بنقاء فائق.",
            isTrending = false,
            tags = listOf("بودكاست", "اقتباس", "حكمة", "مقابلة", "ريلز"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: السؤال الجوهري 🎙️", 5000L, VideoTransitionType.CROSS_DISSOLVE, "cine_moody_cold", "سؤال يغير تفكيرك.. 💭", 0xFF334155),
                TemplateSceneItem("مشهد 2: الجواب العميق 💡", 6000L, VideoTransitionType.SMOOTH_ZOOM_IN, "cine_noir_drama", "النجاح يبدأ من هنا", 0xFF475569),
                TemplateSceneItem("مشهد 3: استخلاص الفائدة 🎯", 5000L, VideoTransitionType.CROSS_DISSOLVE, "cine_moody_cold", "ركز على ما يهمك فقط", 0xFF64748B),
                TemplateSceneItem("مشهد 4: استمع للحلقة كاملة 🎧", 4000L, VideoTransitionType.DIP_TO_BLACK, "cine_noir_drama", "الحلقة متوفرة الآن", 0xFF1E293B)
            )
        ),
        ComprehensiveVideoTemplate(
            id = "tpl_vintage_wedding_memories",
            titleAr = "ذكريات عتيقة وأعراس كلاسيك (Vintage Nostalgia 1920)",
            titleEn = "Vintage Love Memories",
            categoryId = "events_memories",
            iconEmoji = "💍",
            defaultAspectRatio = AspectRatioType.STORY_9_16,
            totalDurationSec = 22,
            musicTrackTitleAr = "بيانو وثائقي ملهم (Inspirational Piano)",
            musicTrackBpm = 75,
            recommendedFilterId = "vint_sepia_1920",
            descriptionAr = "تأثيرات السيبيا الكلاسيكية وتسريب أضواء الأفلام مع تلاشي رومانسي ناعم يناسب الأعراس والذكريات العائلية.",
            isTrending = true,
            tags = listOf("أعراس", "سيبيا", "ذكريات", "رومانسي", "عتيق"),
            scenes = listOf(
                TemplateSceneItem("مشهد 1: النظرة الأولى 🤍", 5500L, VideoTransitionType.FILM_BURN_LEAK, "vint_sepia_1920", "أجمل اللحظات 💍", 0xFF78350F),
                TemplateSceneItem("مشهد 2: الابتسامة والفرح ✨", 5500L, VideoTransitionType.CROSS_DISSOLVE, "vint_kodak_film", "فرحة العمر 🕊️", 0xFF92400E),
                TemplateSceneItem("مشهد 3: اللحظة الختامية 💖", 6000L, VideoTransitionType.FILM_BURN_LEAK, "vint_faded_nostalgia", "معاً للأبد 🥂", 0xFFB45309),
                TemplateSceneItem("مشهد 4: إلى الأبد 🕯️", 5000L, VideoTransitionType.DIP_TO_WHITE, "vint_sepia_1920", "The Beginning ❤️", 0xFFD97706)
            )
        )
    )

    fun getTemplateById(id: String): ComprehensiveVideoTemplate? {
        return templates.find { it.id == id }
    }
}
