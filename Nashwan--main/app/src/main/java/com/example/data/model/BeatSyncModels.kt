package com.example.data.model

enum class BeatIntervalMode(
    val id: String,
    val titleAr: String,
    val subtitleAr: String,
    val iconEmoji: String,
    val beatsPerCut: Int
) {
    EVERY_BEAT(
        id = "every_beat",
        titleAr = "قطع سريع مع كل ضربة بيس (Every Beat 1/1)",
        subtitleAr = "مونتاج خاطف وفائق الحماس للتريندات والرياضة.",
        iconEmoji = "⚡",
        beatsPerCut = 1
    ),
    EVERY_2_BEATS(
        id = "every_2_beats",
        titleAr = "قطع متناسق كل ضربتين (Balanced 1/2 Beats)",
        subtitleAr = "إيقاع سينمائي متوازن ومثالي للفلوقات والإعلانات.",
        iconEmoji = "🎬",
        beatsPerCut = 2
    ),
    EVERY_4_BEATS(
        id = "every_4_beats",
        titleAr = "جمل موسيقية كاملة (Full Measure 1/4 Beats)",
        subtitleAr = "إبراز جماليات المشاهد الطويلة مع التغيير عند نهاية اللحن.",
        iconEmoji = "🎼",
        beatsPerCut = 4
    ),
    DROP_CLIMAX_SYNC(
        id = "drop_sync",
        titleAr = "مزامنة ضربة الدروب الخارقة (Beat Drop Climax)",
        subtitleAr = "تنسيق أروع لقطة لتظهر في اللحظة التي تنفجر فيها الموسيقى.",
        iconEmoji = "💥",
        beatsPerCut = 4
    )
}

data class BeatMarker(
    val timeMs: Long,
    val intensity: Float = 1.0f,
    val isDownbeat: Boolean = false
)

data class BeatSyncSettings(
    val isBeatSyncActive: Boolean = false,
    val bpm: Int = 120,
    val intervalMode: BeatIntervalMode = BeatIntervalMode.EVERY_2_BEATS,
    val applyTransitionsOnBeats: Boolean = true,
    val detectedBeatsCount: Int = 24,
    val beatMarkers: List<BeatMarker> = emptyList()
)

data class BeatSyncResult(
    val adjustedClips: List<VideoClip>,
    val totalDurationMs: Long,
    val cutsCount: Int,
    val beatsMatched: Int,
    val summaryAr: String
)
