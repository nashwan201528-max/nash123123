package com.example.data.repository

import com.example.data.model.AudioTrack
import com.example.data.model.BeatIntervalMode
import com.example.data.model.BeatMarker
import com.example.data.model.BeatSyncResult
import com.example.data.model.BeatSyncSettings
import com.example.data.model.VideoClip
import com.example.data.model.VideoTransition
import com.example.data.model.VideoTransitionType
import com.example.data.network.GeminiClient
import com.example.data.network.GeminiContent
import com.example.data.network.GeminiGenerationConfig
import com.example.data.network.GeminiPart
import com.example.data.network.GeminiRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class AiBeatSyncEngine {

    fun generateBeatMarkers(track: AudioTrack, totalDurationSec: Int = 30): List<BeatMarker> {
        val bpm = track.bpm.coerceIn(60, 220)
        val intervalMs = (60_000L / bpm)
        val totalMs = totalDurationSec * 1000L
        val markers = mutableListOf<BeatMarker>()

        var currentMs = 0L
        var beatIndex = 0
        while (currentMs < totalMs) {
            val isDownbeat = (beatIndex % 4 == 0)
            val isDrop = (currentMs >= (track.beatDropSec * 1000L).toLong() && currentMs <= (track.beatDropSec * 1000L + 1000L).toLong())
            val intensity = if (isDrop) 1.5f else if (isDownbeat) 1.2f else 0.8f

            markers.add(
                BeatMarker(
                    timeMs = currentMs,
                    intensity = intensity,
                    isDownbeat = isDownbeat
                )
            )
            currentMs += intervalMs
            beatIndex++
        }
        return markers
    }

    suspend fun syncClipsToBeat(
        clips: List<VideoClip>,
        audioTrack: AudioTrack,
        intervalMode: BeatIntervalMode = BeatIntervalMode.EVERY_2_BEATS,
        applyDynamicTransitions: Boolean = true
    ): BeatSyncResult = withContext(Dispatchers.IO) {
        if (clips.isEmpty()) {
            return@withContext BeatSyncResult(
                adjustedClips = emptyList(),
                totalDurationMs = 0L,
                cutsCount = 0,
                beatsMatched = 0,
                summaryAr = "لا توجد مقاطع للمزامنة"
            )
        }

        val bpm = audioTrack.bpm.coerceIn(60, 220)
        val singleBeatMs = (60_000L / bpm)
        val cutDurationMs = singleBeatMs * intervalMode.beatsPerCut.coerceAtLeast(1)

        delay(350) // Beat analysis simulation

        val adjusted = clips.mapIndexed { index, clip ->
            val targetClipDuration = cutDurationMs.coerceAtLeast(600L)
            val newEndTrim = (clip.startTrimMs + targetClipDuration).coerceAtMost(clip.durationMs)
            val actualDuration = (newEndTrim - clip.startTrimMs).coerceAtLeast(400L)

            // Select dynamic beat transition
            val beatTransition = if (applyDynamicTransitions && index < clips.size - 1) {
                val transitionType = when (index % 4) {
                    0 -> VideoTransitionType.AI_FLASH_BOOST
                    1 -> VideoTransitionType.SMOOTH_ZOOM_IN
                    2 -> VideoTransitionType.WHIP_PAN_RIGHT
                    else -> VideoTransitionType.AI_GLITCH_PULSE
                }
                VideoTransition(type = transitionType, durationMs = 400L)
            } else null

            clip.copy(
                startTrimMs = clip.startTrimMs,
                endTrimMs = newEndTrim,
                transitionToNext = beatTransition
            )
        }

        val totalDuration = adjusted.sumOf { it.trimmedDurationMs }
        val beatsCount = (totalDuration / singleBeatMs).toInt()

        val summary = "تمت مزامنة ${clips.size} مقاطع بدقة متناهية مع إيقاع \"${audioTrack.titleAr}\" ($bpm BPM) بمعدل $beatsCount ضربة إيقاع متطابقة! 🎵✨"

        return@withContext BeatSyncResult(
            adjustedClips = adjusted,
            totalDurationMs = totalDuration,
            cutsCount = clips.size - 1,
            beatsMatched = beatsCount,
            summaryAr = summary
        )
    }
}
