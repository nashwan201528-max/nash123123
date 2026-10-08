package com.example.data.model

import android.graphics.Bitmap
import android.net.Uri

enum class MediaType {
    VIDEO,
    PHOTO,
    MONTAGE
}

enum class AspectRatioType(val labelAr: String, val ratio: Float, val iconDescription: String) {
    FREE("حر", 0f, "حر"),
    ORIGINAL("الأصلي", 1f, "أصلي"),
    SQUARE_1_1("1:1 مربع", 1.0f, "Instagram Post"),
    STORY_9_16("9:16 ستوري", 9f / 16f, "TikTok / Reels / Shorts"),
    PORTRAIT_4_5("4:5 طولي", 4f / 5f, "Instagram Portrait"),
    CINEMA_16_9("16:9 سينمائي", 16f / 9f, "YouTube / TV"),
    ULTRAWIDE_21_9("21:9 شاشة عريضة", 21f / 9f, "UltraWide Cinema")
}

data class VideoClip(
    val id: String,
    val title: String,
    val durationMs: Long,
    val startTrimMs: Long = 0L,
    val endTrimMs: Long = durationMs,
    val speed: Float = 1.0f,
    val volume: Float = 1.0f,
    val filterName: String = "أصلي",
    val uriString: String? = null,
    val colorHex: Long = 0xFF3B82F6,
    val transitionToNext: VideoTransition? = null,
    val speedCurve: SpeedCurvePreset = SpeedCurvePreset.CONSTANT,
    val keepAudioPitch: Boolean = true
) {
    val trimmedDurationMs: Long
        get() = ((endTrimMs - startTrimMs).coerceAtLeast(0) / speed.coerceAtLeast(0.05f)).toLong()
}

data class TextOverlay(
    val id: String,
    val text: String,
    val xPosRatio: Float = 0.5f,
    val yPosRatio: Float = 0.5f,
    val fontSizeSp: Float = 22f,
    val textColorHex: Long = 0xFFFFFFFF,
    val bgColorHex: Long = 0x88000000,
    val isBold: Boolean = true,
    val animationType: String = "Fade"
)

data class StickerOverlay(
    val id: String,
    val emoji: String,
    val xPosRatio: Float = 0.5f,
    val yPosRatio: Float = 0.3f,
    val sizeDp: Float = 48f
)

data class AudioTrack(
    val id: String,
    val titleAr: String,
    val categoryAr: String,
    val durationSec: Int,
    val isMuted: Boolean = false,
    val volume: Float = 1.0f,
    val bpm: Int = 120,
    val beatDropSec: Float = 6.0f
)
