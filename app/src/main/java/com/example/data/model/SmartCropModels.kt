package com.example.data.model

enum class TrackingSubjectMode(
    val id: String,
    val titleAr: String,
    val iconEmoji: String,
    val descriptionAr: String
) {
    FACE_PERSON(
        id = "face_person",
        titleAr = "تتبع الوجوه والأشخاص (Face Tracking)",
        iconEmoji = "👤",
        descriptionAr = "يتعرف الذكاء الاصطناعي على وجه المتحدث ويحافظ على بقائه في منتصف الكادر تماماً."
    ),
    ACTION_MOTION(
        id = "action_motion",
        titleAr = "تتبع الحركة السريعة (Action & Motion)",
        iconEmoji = "⚡",
        descriptionAr = "تتبع فائق الديناميكية للعناصر سريعة الحركة كالرياضيين والسيارات."
    ),
    FOCAL_SALIENCY(
        id = "focal_saliency",
        titleAr = "نقطة التركيز البصرية (Focal Saliency)",
        iconEmoji = "🎯",
        descriptionAr = "تحديد مركز التباين البصري والعمق وتوسيط المشهد عليه."
    ),
    GROUP_FRAME(
        id = "group_frame",
        titleAr = "تأطير المجموعات (Multi-Subject Framing)",
        iconEmoji = "👥",
        descriptionAr = "توسيع زاوية التأطير ليشمل جميع المتحدثين في اللقطة دون قص أي شخص."
    )
}

data class SmartCropSettings(
    val targetRatio: AspectRatioType = AspectRatioType.STORY_9_16,
    val trackingMode: TrackingSubjectMode = TrackingSubjectMode.FACE_PERSON,
    val isAiAutoTrackingEnabled: Boolean = true,
    val trackingSmoothness: Float = 0.85f, // 0.0 to 1.0 (Higher = smoother camera pan)
    val focalOffsetX: Float = 0.5f, // 0.0 (left) to 1.0 (right), 0.5 = centered
    val focalOffsetY: Float = 0.5f, // 0.0 (top) to 1.0 (bottom)
    val detectedSubjectConfidence: Float = 0.96f, // AI detection certainty
    val autoZoomScale: Float = 1.0f,
    val showTrackingOverlay: Boolean = true
)
