package com.example.data.model

data class ColorGradingState(
    val temperature: Float = 0f,       // -100f (Cold blue) to +100f (Warm orange)
    val tint: Float = 0f,              // -100f (Green) to +100f (Magenta)
    val hueShiftDegrees: Float = 0f,   // 0° to 360° color rotation
    val brightness: Float = 0f,        // -100f to +100f
    val contrast: Float = 1.0f,        // 0.5f to 2.0f
    val saturation: Float = 1.0f,      // 0.0f to 2.5f
    val vibrance: Float = 0f,          // -100f to +100f
    val highlights: Float = 0f,        // -100f to +100f
    val shadows: Float = 0f            // -100f to +100f
) {
    val isModified: Boolean
        get() = temperature != 0f || tint != 0f || hueShiftDegrees != 0f ||
                brightness != 0f || contrast != 1.0f || saturation != 1.0f ||
                vibrance != 0f || highlights != 0f || shadows != 0f
}

data class VisualFxParameters(
    val filmGrainStrength: Float = 0f,       // 0.0f to 1.0f
    val vignetteStrength: Float = 0f,        // 0.0f to 1.0f
    val glowBloomIntensity: Float = 0f,      // 0.0f to 1.0f
    val sharpnessFactor: Float = 0f,         // 0.0f to 3.0f
    val denoiseFactor: Float = 0f,           // 0.0f to 1.0f
    val chromaticAberration: Float = 0f      // 0.0f to 1.0f
) {
    val isFxActive: Boolean
        get() = filmGrainStrength > 0.05f || vignetteStrength > 0.05f ||
                glowBloomIntensity > 0.05f || sharpnessFactor > 0.1f ||
                denoiseFactor > 0.1f || chromaticAberration > 0.05f
}

enum class TransitionEasingCurve(val titleAr: String, val descriptionAr: String, val iconEmoji: String) {
    SMOOTH_EASE_IN_OUT("انسيابي ناعم (Ease In-Out)", "تسارع ناعم في البداية وتباطؤ سلس عند النهاية", "🌊"),
    FAST_SNAPPY("خاطف وسريع (Snappy & Punchy)", "انتقال فوري وحاد مخصص لمقاطع التريند", "⚡"),
    LINEAR_CONSTANT("سرعة ثابتة (Linear Constant)", "حركة منتظمة على طول مدة الانتقال", "📏"),
    BOUNCE_SPRING("ارتداد ديناميكي (Spring Bounce)", "حركة ارتدادية مرحة تمنح حيوية للمشهد", "🏀")
}

data class TransitionCustomization(
    val selectedType: VideoTransitionType = VideoTransitionType.SMOOTH_ZOOM_IN,
    val durationMs: Long = 800L,
    val easingCurve: TransitionEasingCurve = TransitionEasingCurve.SMOOTH_EASE_IN_OUT,
    val isMotionBlurEnabled: Boolean = true,
    val isSoundCueEnabled: Boolean = true
)

data class AiColorPaletteSuggestion(
    val titleAr: String,
    val descriptionAr: String,
    val colorGrading: ColorGradingState,
    val visualFx: VisualFxParameters,
    val recommendedFilterId: String,
    val reasonAr: String
)
