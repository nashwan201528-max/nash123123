package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val mediaType: String, // "PHOTO", "VIDEO", "MONTAGE"
    val thumbnailUri: String? = null,
    val lastModifiedTimestamp: Long = System.currentTimeMillis(),
    val durationMs: Long = 0L,
    val aspectRatio: String = "16:9",
    val activeFilterId: String = "cine_teal_orange",
    val filterIntensity: Float = 1.0f,
    val brightness: Float = 0f,
    val contrast: Float = 1f,
    val saturation: Float = 1f,
    val denoiseLevel: Float = 0f,
    val upscaleFactor: Float = 1f,
    val isBgRemoved: Boolean = false,
    val bgReplacementType: String = "TRANSPARENT",
    val textOverlaysJson: String = "[]",
    val videoClipsJson: String = "[]",
    val subtitlesJson: String = "[]",
    val selectedAudioTrackId: String? = null,
    val notes: String = ""
)
