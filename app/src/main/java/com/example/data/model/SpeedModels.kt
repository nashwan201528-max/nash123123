package com.example.data.model

enum class SpeedCurvePreset(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val iconEmoji: String,
    val descriptionAr: String,
    val curvePoints: List<Float> // Normalized speed multipliers at [0%, 25%, 50%, 75%, 100%] of the clip
) {
    CONSTANT(
        id = "constant",
        titleAr = "سرعة قياسية منتظمة",
        titleEn = "Standard Speed",
        iconEmoji = "⏱️",
        descriptionAr = "سرعة خطية ثابتة بدون تسارع مفاجئ طوال مدة المقطع.",
        curvePoints = listOf(1.0f, 1.0f, 1.0f, 1.0f, 1.0f)
    ),
    HERO_BULLET_TIME(
        id = "bullet_time",
        titleAr = "لحظة الأبطال (Bullet Time)",
        titleEn = "Hero Slow-Fast-Slow",
        iconEmoji = "🦸‍♂️",
        descriptionAr = "بداية بطيئة سينمائية، يعقبها تسارع حركي خارق ثم عودة للحركة البطيئة.",
        curvePoints = listOf(0.3f, 0.5f, 2.5f, 0.4f, 0.3f)
    ),
    MONTAGE_JUMP(
        id = "jump_ramp",
        titleAr = "تسارع قفزي (Fast In, Slow Out)",
        titleEn = "Montage Jump Cut",
        iconEmoji = "⚡",
        descriptionAr = "انطلاقة فائقة السرعة تتباطأ برقة عند أهم لحظة في المقطع.",
        curvePoints = listOf(3.5f, 2.0f, 1.0f, 0.5f, 0.4f)
    ),
    FLASH_DROP_BEAT(
        id = "flash_drop",
        titleAr = "نبضة الإيقاع (Beat Flash Drop)",
        titleEn = "Impact Drop",
        iconEmoji = "💥",
        descriptionAr = "قفزة سرعة سريعة ومفاجئة في المنتصف تتطابق مع ضربات البيس الموسيقية.",
        curvePoints = listOf(1.0f, 1.2f, 4.0f, 1.2f, 1.0f)
    ),
    SLOW_MO_DRAMA(
        id = "slow_drama",
        titleAr = "حركة بطيئة درامية (Cinematic Slow-Mo)",
        titleEn = "Ultra Slow-Mo",
        iconEmoji = "🎬",
        descriptionAr = "إبطاء سينمائي سلس فائق النعومة يبرز أدق التفاصيل التعبيرية.",
        curvePoints = listOf(0.25f, 0.25f, 0.25f, 0.25f, 0.25f)
    )
}
