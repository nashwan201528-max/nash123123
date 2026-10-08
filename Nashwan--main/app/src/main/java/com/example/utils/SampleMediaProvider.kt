package com.example.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.R
import com.example.data.model.AudioTrack
import com.example.data.model.VideoClip

object SampleMediaProvider {

    fun loadSampleBitmap(context: Context, resId: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resId)
    }

    val sampleImageResources = listOf(
        Pair("غروب الجبال الذهبي", R.drawable.sample_photo_sunset),
        Pair("شارع السايبربانك الليلي", R.drawable.sample_photo_neon),
        Pair("استوديو الإنتاج السينمائي", R.drawable.hero_banner)
    )

    fun getSampleVideoClips(): List<VideoClip> {
        return listOf(
            VideoClip(
                id = "clip_1",
                title = "المشهد 1: لقطة سينمائية افتتاحية",
                durationMs = 8500L,
                startTrimMs = 500L,
                endTrimMs = 7500L,
                speed = 1.0f,
                filterName = "هوليوود (تيل & أورانج)",
                colorHex = 0xFF06B6D4
            ),
            VideoClip(
                id = "clip_2",
                title = "المشهد 2: حركة سريعة وأضواء نيون",
                durationMs = 6000L,
                startTrimMs = 0L,
                endTrimMs = 5200L,
                speed = 1.5f,
                filterName = "سايبربانك نيون",
                colorHex = 0xFF8B5CF6
            ),
            VideoClip(
                id = "clip_3",
                title = "المشهد 3: غروب هادئ وخاتمة",
                durationMs = 9000L,
                startTrimMs = 1000L,
                endTrimMs = 8000L,
                speed = 0.75f,
                filterName = "الساعة الذهبية",
                colorHex = 0xFFF59E0B
            )
        )
    }

    val sampleAudioTracks = listOf(
        AudioTrack("track_1", "لحن سينمائي ملحمي (Epic Cinematic)", "سينمائي 🎬", 120),
        AudioTrack("track_2", "إيقاع حماسي للتيك توك والريلز (Upbeat Viral)", "ترند وحماسي ⚡", 45),
        AudioTrack("track_3", "موسيقى هادئة ودافئة للقصص (Lofi Chill Vibes)", "هادئ ولطيف ☕", 90),
        AudioTrack("track_4", "تقاسيم عود وشرقي أصيل (Oriental Oud)", "شرقي وتراثي 🪕", 110),
        AudioTrack("track_5", "موسيقى سايبربانك إلكترونية (Synthwave 80s)", "إلكتروني وسايبر 🚀", 75),
        AudioTrack("track_6", "بيانو وثائقي ملهم (Inspirational Piano)", "وثائقي وإلهام 🎹", 130),
        AudioTrack("track_7", "إيقاع حركة وسفر وتشويق (Travel Beats)", "سفر ومغامرة ✈️", 60),
        AudioTrack("track_8", "خلفية حوارية للبودكاست (Podcast Ambience)", "بودكاست 🎙️", 150)
    )

    val sampleStickers = listOf(
        "🔥", "✨", "🎬", "⚡", "🌟", "🎥", "💫", "🚀", "👑", "❤️",
        "🎯", "💯", "🎉", "🎞️", "🎵", "🏆", "🕶️", "🔮", "💥", "🤩"
    )

    val sampleTemplates = listOf(
        MontageTemplate(
            id = "template_reels_fast",
            titleAr = "ريلز وإكسبلور سريع (Fast Cuts)",
            aspectRatio = "9:16",
            durationSec = 15,
            icon = "⚡",
            recommendedFilter = "cine_teal_orange",
            descriptionAr = "انتقالات بصرية سريعة وتكبير نبضي متزامن مع الإيقاع الصوتي، مثالي لـ TikTok و Instagram Reels."
        ),
        MontageTemplate(
            id = "template_travel_vlog",
            titleAr = "مدونة السفر والمغامرات (Travel Vlog)",
            aspectRatio = "16:9",
            durationSec = 30,
            icon = "✈️",
            recommendedFilter = "nat_golden_hour",
            descriptionAr = "لقطات بانورامية ناعمة، نصوص وصفية أنيقة للمواقع السياحية، وتدرجات ألوان ذهبية مشمسة."
        ),
        MontageTemplate(
            id = "template_promo_product",
            titleAr = "إعلان تجاري وترويجي (Product Launch)",
            aspectRatio = "1:1",
            durationSec = 20,
            icon = "🛍️",
            recommendedFilter = "nat_crystal_clear",
            descriptionAr = "إبراز تفاصيل المنتج بدقة فائقة HDR، نصوص مميزة للأسعار والمزايا، وخاتمة للدعوة للشراء."
        ),
        MontageTemplate(
            id = "template_cyberpunk_story",
            titleAr = "مستقبل نيون ومؤثرات خيال (Cyber Story)",
            aspectRatio = "9:16",
            durationSec = 18,
            icon = "🌆",
            recommendedFilter = "futur_cyberpunk_neon",
            descriptionAr = "ألوان مستقبلية متوهجة، وميض نيون، وإيقاعات سنث ويف تصنع تجربة غامرة ومميزة."
        )
    )
}

data class MontageTemplate(
    val id: String,
    val titleAr: String,
    val aspectRatio: String,
    val durationSec: Int,
    val icon: String,
    val recommendedFilter: String,
    val descriptionAr: String
)
