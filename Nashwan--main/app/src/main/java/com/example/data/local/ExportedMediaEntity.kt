package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exported_media")
data class ExportedMediaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val mediaType: String, // "PHOTO", "VIDEO", "MONTAGE"
    val filePath: String,
    val mimeType: String,
    val resolutionLabel: String, // "1080p FHD", "4K UHD", "HD"
    val fileSizeBytes: Long,
    val durationSec: Int = 0,
    val appliedFilterName: String = "أصلي",
    val isAiEnhanced: Boolean = false,
    val isBgRemoved: Boolean = false,
    val createdAtTimestamp: Long = System.currentTimeMillis()
)
