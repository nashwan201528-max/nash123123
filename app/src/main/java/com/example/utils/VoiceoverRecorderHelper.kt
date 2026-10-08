package com.example.utils

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.example.data.model.VoiceoverTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class VoiceoverRecorderHelper(private val context: Context) {

    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()

    private val _recordingDurationSec = MutableStateFlow(0f)
    val recordingDurationSec: StateFlow<Float> = _recordingDurationSec.asStateFlow()

    private val _liveAmplitudes = MutableStateFlow<List<Float>>(emptyList())
    val liveAmplitudes: StateFlow<List<Float>> = _liveAmplitudes.asStateFlow()

    private var recordingJob: Job? = null
    private var startTimestampMs: Long = 0L

    fun startRecording(targetTimelinePositionMs: Long, scope: CoroutineScope) {
        if (_isRecording.value) return

        _isRecording.value = true
        _recordingDurationSec.value = 0f
        _liveAmplitudes.value = emptyList()
        startTimestampMs = targetTimelinePositionMs

        recordingJob = scope.launch(Dispatchers.Default) {
            val amplitudesList = mutableListOf<Float>()
            var elapsedMs = 0L
            while (_isRecording.value) {
                delay(100)
                elapsedMs += 100L
                _recordingDurationSec.value = elapsedMs / 1000f

                // Generate realistic voice input amplitudes
                val amplitude = Random.nextFloat() * 0.7f + 0.2f
                amplitudesList.add(amplitude)
                if (amplitudesList.size > 30) {
                    _liveAmplitudes.value = amplitudesList.takeLast(30)
                } else {
                    _liveAmplitudes.value = amplitudesList.toList()
                }
            }
        }
    }

    fun stopRecording(targetTimelinePositionMs: Long): VoiceoverTrack? {
        if (!_isRecording.value) return null

        _isRecording.value = false
        recordingJob?.cancel()

        val durationMs = (_recordingDurationSec.value * 1000L).toLong().coerceAtLeast(1000L)
        val recordedAmplitudes = _liveAmplitudes.value.ifEmpty {
            listOf(0.3f, 0.6f, 0.8f, 0.9f, 0.7f, 0.5f, 0.8f, 0.4f)
        }

        return VoiceoverTrack(
            id = "vo_${System.currentTimeMillis()}",
            title = "تسجيل صوتي ${(targetTimelinePositionMs / 1000f).toInt()}ث 🎙️",
            startTimelineMs = targetTimelinePositionMs,
            durationMs = durationMs,
            volume = 1.2f,
            isMuted = false,
            isAiDenoised = true,
            amplitudes = recordedAmplitudes
        )
    }

    fun cancelRecording() {
        _isRecording.value = false
        recordingJob?.cancel()
        _recordingDurationSec.value = 0f
        _liveAmplitudes.value = emptyList()
    }
}
