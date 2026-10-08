package com.example.data.model

data class FilterPreset(
    val id: String,
    val nameAr: String,
    val categoryAr: String,
    val descriptionAr: String,
    val iconEmoji: String,
    val matrix: FloatArray,
    val sharpnessFactor: Float = 0f,
    val vignetteStrength: Float = 0f
)

object FilterRepository {
    // 20 unique professional filters across Cinematic, Vintage, Futuristic, and Natural
    val presets: List<FilterPreset> = listOf(
        // === سينمائي (Cinematic) ===
        FilterPreset(
            id = "cine_teal_orange",
            nameAr = "هوليوود (تيل & أورانج)",
            categoryAr = "سينمائي",
            descriptionAr = "المظهر السينمائي الأشهر عالمياً: يعزز درجات البشرة الدافئة مع تحويل الظلال إلى أزرق فيروزي غني لخلق تباين بصري هائل.",
            iconEmoji = "🎬",
            matrix = floatArrayOf(
                1.25f, 0.05f, 0.0f, 0f, 15f,
                0.0f, 1.05f, 0.1f, 0f, 5f,
                0.0f, 0.1f, 1.35f, 0f, 25f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            vignetteStrength = 0.25f
        ),
        FilterPreset(
            id = "cine_noir_drama",
            nameAr = "نوار درامي (أبيض وأسود)",
            categoryAr = "سينمائي",
            descriptionAr = "مستوحى من أفلام الجريمة الكلاسيكية، يمنح تباينًا حادًا وظلالًا داكنة مع إبراز عميق لملامح الوجه وتفاصيل المشهد.",
            iconEmoji = "📽️",
            matrix = floatArrayOf(
                0.35f, 0.55f, 0.10f, 0f, -15f,
                0.35f, 0.55f, 0.10f, 0f, -15f,
                0.35f, 0.55f, 0.10f, 0f, -15f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            sharpnessFactor = 0.2f
        ),
        FilterPreset(
            id = "cine_bleach_bypass",
            nameAr = "تبييض كيميائي (Bleach Bypass)",
            categoryAr = "سينمائي",
            descriptionAr = "طابع أفلام الحرب والأكشن الشهيرة، يقلل تشبع الألوان ويزيد التباين والحدة ليمنح المقطع مظهرًا قاسيًا وواقعيًا.",
            iconEmoji = "⚡",
            matrix = floatArrayOf(
                1.2f, 0.1f, 0.1f, 0f, -10f,
                0.1f, 1.2f, 0.1f, 0f, -10f,
                0.1f, 0.1f, 1.2f, 0f, -10f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            sharpnessFactor = 0.35f
        ),
        FilterPreset(
            id = "cine_moody_cold",
            nameAr = "غموض أزرق بارد",
            categoryAr = "سينمائي",
            descriptionAr = "يضفي طابعاً شتوياً غامضاً ومثيراً عبر زيادة الدرجات الزرقاء والفحمية في الإضاءة والمشاهد الليلية.",
            iconEmoji = "❄️",
            matrix = floatArrayOf(
                0.9f, 0.0f, 0.1f, 0f, -5f,
                0.0f, 0.95f, 0.1f, 0f, 0f,
                0.1f, 0.15f, 1.35f, 0f, 20f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            vignetteStrength = 0.3f
        ),
        FilterPreset(
            id = "cine_epic_gold",
            nameAr = "ملحمي ذهبي",
            categoryAr = "سينمائي",
            descriptionAr = "تدرج لوني أسطوري ملهم للأفلام التاريخية والرحلات، يغمر الكادر بتوهج برونزي دافئ ولمعان ناعم.",
            iconEmoji = "👑",
            matrix = floatArrayOf(
                1.3f, 0.15f, 0.0f, 0f, 20f,
                0.1f, 1.15f, 0.0f, 0f, 10f,
                0.0f, 0.05f, 0.7f, 0f, -15f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),

        // === عتيق (Vintage / Retro) ===
        FilterPreset(
            id = "vint_sepia_1920",
            nameAr = "سيبيا كلاسيك 1920",
            categoryAr = "عتيق",
            descriptionAr = "يحاكي الصور الفوتوغرافية الأولى بدرجات البني الداكن والحبيبات الأثرية الدافئة.",
            iconEmoji = "📜",
            matrix = floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            vignetteStrength = 0.35f
        ),
        FilterPreset(
            id = "vint_polaroid_70s",
            nameAr = "بولارويد السبعينات",
            categoryAr = "عتيق",
            descriptionAr = "ألوان كاميرا البولارويد الفورية مع بهتان خفيف في اللون الأسود ودفء ناعم في درجات البيج.",
            iconEmoji = "📷",
            matrix = floatArrayOf(
                1.15f, 0.1f, 0.0f, 0f, 25f,
                0.05f, 1.05f, 0.05f, 0f, 15f,
                0.0f, 0.1f, 0.85f, 0f, 20f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "vint_vhs_tape",
            nameAr = "شريط فيديو VHS",
            categoryAr = "عتيق",
            descriptionAr = "تأثير شرائط الفيديو المنزلية من الثمانينات مع انزياح خفيف في الألوان وتشبع بصري جذاب.",
            iconEmoji = "📼",
            matrix = floatArrayOf(
                1.1f, 0.0f, 0.2f, 0f, 10f,
                0.0f, 1.15f, 0.0f, 0f, -5f,
                0.2f, 0.0f, 1.25f, 0f, 15f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "vint_kodak_film",
            nameAr = "فيلم كوداك رول",
            categoryAr = "عتيق",
            descriptionAr = "محاكاة خامات أفلام كوداك الشهيرة (Kodachrome)، ألوان نقية تبرز درجات الأحمر والأصفر برونق فني.",
            iconEmoji = "🎞️",
            matrix = floatArrayOf(
                1.22f, 0.05f, 0.0f, 0f, 10f,
                0.05f, 1.1f, 0.0f, 0f, 5f,
                0.0f, 0.05f, 0.9f, 0f, -5f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "vint_faded_nostalgia",
            nameAr = "نوستالجيا باهتة",
            categoryAr = "عتيق",
            descriptionAr = "يقلل التباين بدرجة مدروسة ليعطي مظهر الذكريات القديمة المحفوظة في ألبومات الصور العائلية.",
            iconEmoji = "🕯️",
            matrix = floatArrayOf(
                0.85f, 0.1f, 0.1f, 0f, 30f,
                0.1f, 0.85f, 0.1f, 0f, 25f,
                0.1f, 0.1f, 0.85f, 0f, 35f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),

        // === مستقبلي (Futuristic / Cyberpunk / Sci-Fi) ===
        FilterPreset(
            id = "futur_cyberpunk_neon",
            nameAr = "سايبربانك نيون",
            categoryAr = "مستقبلي",
            descriptionAr = "شوارع المستقبل المضيئة، يعزز أضواء النيون الفوشيا والأزرق الكهربائي مع تعميق السواد الليلي.",
            iconEmoji = "🌆",
            matrix = floatArrayOf(
                1.35f, 0.0f, 0.35f, 0f, 25f,
                0.0f, 0.85f, 0.1f, 0f, -15f,
                0.35f, 0.1f, 1.45f, 0f, 35f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            sharpnessFactor = 0.3f
        ),
        FilterPreset(
            id = "futur_matrix_green",
            nameAr = "ماتريكس سايبر",
            categoryAr = "مستقبلي",
            descriptionAr = "مستوحى من الشاشات الرقمية الخضراء وعالم الأكواد البرمجية الافتراضي.",
            iconEmoji = "🟢",
            matrix = floatArrayOf(
                0.5f, 0.3f, 0.0f, 0f, -20f,
                0.2f, 1.4f, 0.2f, 0f, 20f,
                0.0f, 0.3f, 0.5f, 0f, -20f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "futur_hologram_cyan",
            nameAr = "هولوغرام مستقبلي",
            categoryAr = "مستقبلي",
            descriptionAr = "إضاءة سماوية زرقاء ساطعة تحاكي الإسقاطات الهولوغرافية التفاعلية.",
            iconEmoji = "🌐",
            matrix = floatArrayOf(
                0.6f, 0.2f, 0.4f, 0f, -10f,
                0.1f, 1.25f, 0.3f, 0f, 15f,
                0.3f, 0.3f, 1.5f, 0f, 40f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "futur_synthwave_sunset",
            nameAr = "سينث ويف 80s",
            categoryAr = "مستقبلي",
            descriptionAr = "جمالية موسيقى السينث ويف الإلكترونية مع مزيج البنفسجي والأرجواني اللامع.",
            iconEmoji = "🎛️",
            matrix = floatArrayOf(
                1.3f, 0.0f, 0.4f, 0f, 20f,
                0.0f, 0.9f, 0.2f, 0f, -10f,
                0.4f, 0.1f, 1.3f, 0f, 30f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "futur_deep_space",
            nameAr = "الفضاء السحيق",
            categoryAr = "مستقبلي",
            descriptionAr = "يعزل درجات الضوء الأبيض والنجوم في خلفية سوداء نقية مع توهج كوني خفيف.",
            iconEmoji = "🚀",
            matrix = floatArrayOf(
                1.1f, 0.0f, 0.2f, 0f, -10f,
                0.0f, 1.05f, 0.1f, 0f, -10f,
                0.2f, 0.1f, 1.4f, 0f, 15f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            vignetteStrength = 0.4f
        ),

        // === طبيعي (Natural / Landscapes / Glow) ===
        FilterPreset(
            id = "nat_golden_hour",
            nameAr = "الساعة الذهبية",
            categoryAr = "طبيعي",
            descriptionAr = "يحاكي أشعة الشمس الدافئة قبل الغروب مباشرة، يضفي إشراقة ساحرة ونعومة للبشرة والمناظر.",
            iconEmoji = "🌅",
            matrix = floatArrayOf(
                1.25f, 0.1f, 0.0f, 0f, 25f,
                0.1f, 1.15f, 0.0f, 0f, 15f,
                0.0f, 0.0f, 0.75f, 0f, -20f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "nat_emerald_valley",
            nameAr = "الواحة الزمردية",
            categoryAr = "طبيعي",
            descriptionAr = "مخصص للمناظر الطبيعية والحدائق، يعزز نقاء درجات الأخضر والأزرق دون تشويه باقي الألوان.",
            iconEmoji = "🌿",
            matrix = floatArrayOf(
                0.95f, 0.05f, 0.0f, 0f, -5f,
                0.05f, 1.3f, 0.05f, 0f, 20f,
                0.0f, 0.1f, 1.05f, 0f, 5f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "nat_ocean_breeze",
            nameAr = "نسيم المحيط",
            categoryAr = "طبيعي",
            descriptionAr = "يبرز زرقة مياه البحر والسماء بوضوح كريستالي منعش مع إضاءة بيضاء مشرقة.",
            iconEmoji = "🌊",
            matrix = floatArrayOf(
                0.9f, 0.1f, 0.0f, 0f, 0f,
                0.05f, 1.1f, 0.15f, 0f, 10f,
                0.0f, 0.15f, 1.4f, 0f, 25f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "nat_autumn_warmth",
            nameAr = "دفء الخريف",
            categoryAr = "طبيعي",
            descriptionAr = "يبرز الألوان الخريفية مثل البرتقالي والأحمر القرمزي والبني المتناسق في أوراق الشجر والمباني.",
            iconEmoji = "🍂",
            matrix = floatArrayOf(
                1.3f, 0.1f, 0.0f, 0f, 20f,
                0.05f, 1.05f, 0.0f, 0f, 5f,
                0.0f, 0.0f, 0.7f, 0f, -25f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            )
        ),
        FilterPreset(
            id = "nat_crystal_clear",
            nameAr = "نقاء كريستالي (HDR)",
            categoryAr = "طبيعي",
            descriptionAr = "يعزز الإشراق والوضوح الطبيعي لكل تفصيل في الصورة بدون أي تلوين مصطنع، يبرز الحيوية الواقعية.",
            iconEmoji = "💎",
            matrix = floatArrayOf(
                1.12f, -0.04f, -0.04f, 0f, 8f,
                -0.04f, 1.12f, -0.04f, 0f, 8f,
                -0.04f, -0.04f, 1.12f, 0f, 8f,
                0.0f, 0.0f, 0.0f, 1f, 0f
            ),
            sharpnessFactor = 0.25f
        )
    )

    fun getFilterById(id: String): FilterPreset {
        return presets.find { it.id == id } ?: presets.first()
    }

    val categories = listOf("الكل", "سينمائي", "عتيق", "مستقبلي", "طبيعي")
}
