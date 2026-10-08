package com.example.data.local

import com.example.data.model.StickerOverlay
import com.example.data.model.SubtitleLine
import com.example.data.model.TextOverlay
import com.example.data.model.VideoClip
import org.json.JSONArray
import org.json.JSONObject

object ProjectJsonConverters {

    fun videoClipsToJson(clips: List<VideoClip>): String {
        val array = JSONArray()
        for (clip in clips) {
            val obj = JSONObject().apply {
                put("id", clip.id)
                put("title", clip.title)
                put("durationMs", clip.durationMs)
                put("startTrimMs", clip.startTrimMs)
                put("endTrimMs", clip.endTrimMs)
                put("speed", clip.speed.toDouble())
                put("volume", clip.volume.toDouble())
                put("filterName", clip.filterName)
                put("uriString", clip.uriString ?: "")
                put("colorHex", clip.colorHex)
                clip.transitionToNext?.let { trans ->
                    put("transitionTypeId", trans.type.id)
                    put("transitionDurationMs", trans.durationMs)
                }
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToVideoClips(json: String?): List<VideoClip> {
        if (json.isNullOrBlank() || json == "[]") return emptyList()
        val list = mutableListOf<VideoClip>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val transitionTypeId = obj.optString("transitionTypeId").takeIf { it.isNotBlank() }
                val transitionDuration = obj.optLong("transitionDurationMs", 800L)
                val transition = transitionTypeId?.let { tid ->
                    com.example.data.model.VideoTransitionType.values().find { it.id == tid }?.let { transType ->
                        com.example.data.model.VideoTransition(transType, transitionDuration)
                    }
                }

                list.add(
                    VideoClip(
                        id = obj.optString("id", "clip_$i"),
                        title = obj.optString("title", "المقطع $i"),
                        durationMs = obj.optLong("durationMs", 5000L),
                        startTrimMs = obj.optLong("startTrimMs", 0L),
                        endTrimMs = obj.optLong("endTrimMs", 5000L),
                        speed = obj.optDouble("speed", 1.0).toFloat(),
                        volume = obj.optDouble("volume", 1.0).toFloat(),
                        filterName = obj.optString("filterName", "أصلي"),
                        uriString = obj.optString("uriString").takeIf { it.isNotBlank() },
                        colorHex = obj.optLong("colorHex", 0xFF3B82F6),
                        transitionToNext = transition
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun subtitlesToJson(subtitles: List<SubtitleLine>): String {
        val array = JSONArray()
        for (sub in subtitles) {
            val obj = JSONObject().apply {
                put("startTimeSec", sub.startTimeSec.toDouble())
                put("endTimeSec", sub.endTimeSec.toDouble())
                put("textAr", sub.textAr)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToSubtitles(json: String?): List<SubtitleLine> {
        if (json.isNullOrBlank() || json == "[]") return emptyList()
        val list = mutableListOf<SubtitleLine>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    SubtitleLine(
                        startTimeSec = obj.optDouble("startTimeSec", 0.0).toFloat(),
                        endTimeSec = obj.optDouble("endTimeSec", 3.0).toFloat(),
                        textAr = obj.optString("textAr", "")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun textOverlaysToJson(overlays: List<TextOverlay>): String {
        val array = JSONArray()
        for (text in overlays) {
            val obj = JSONObject().apply {
                put("id", text.id)
                put("text", text.text)
                put("xPosRatio", text.xPosRatio.toDouble())
                put("yPosRatio", text.yPosRatio.toDouble())
                put("fontSizeSp", text.fontSizeSp.toDouble())
                put("textColorHex", text.textColorHex)
                put("bgColorHex", text.bgColorHex)
                put("isBold", text.isBold)
            }
            array.put(obj)
        }
        return array.toString()
    }

    fun jsonToTextOverlays(json: String?): List<TextOverlay> {
        if (json.isNullOrBlank() || json == "[]") return emptyList()
        val list = mutableListOf<TextOverlay>()
        try {
            val array = JSONArray(json)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(
                    TextOverlay(
                        id = obj.optString("id", "text_$i"),
                        text = obj.optString("text", ""),
                        xPosRatio = obj.optDouble("xPosRatio", 0.5).toFloat(),
                        yPosRatio = obj.optDouble("yPosRatio", 0.5).toFloat(),
                        fontSizeSp = obj.optDouble("fontSizeSp", 22.0).toFloat(),
                        textColorHex = obj.optLong("textColorHex", 0xFFFFFFFF),
                        bgColorHex = obj.optLong("bgColorHex", 0x88000000),
                        isBold = obj.optBoolean("isBold", true)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
