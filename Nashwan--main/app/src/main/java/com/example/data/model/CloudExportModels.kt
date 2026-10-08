package com.example.data.model

data class CloudExportResult(
    val cloudId: String,
    val projectTitle: String,
    val cloudDownloadUrl: String,
    val shareableWebLink: String,
    val fileSizeBytes: Long,
    val durationMs: Long,
    val uploadedTimestamp: Long = System.currentTimeMillis(),
    val isSuccess: Boolean = true,
    val storageBucketPath: String = "gs://montage-pro-cloud.appspot.com/exports/"
)

data class CloudProjectItem(
    val id: String,
    val title: String,
    val durationMs: Long,
    val resolutionLabel: String,
    val cloudDownloadUrl: String,
    val shareableWebLink: String,
    val uploadedTimestamp: Long,
    val fileSizeMb: Float,
    val deviceOrigin: String = "Android Device",
    val isSyncedLocally: Boolean = true
)

data class CloudSyncStatus(
    val isUploading: Boolean = false,
    val uploadProgress: Float = 0f,
    val currentStepLabel: String = "",
    val lastExportResult: CloudExportResult? = null,
    val errorMessage: String? = null
)
