package com.example.data.repository

import com.example.data.local.ExportDao
import com.example.data.local.ExportedMediaEntity
import kotlinx.coroutines.flow.Flow

class MediaExportRepository(private val exportDao: ExportDao) {

    val allExports: Flow<List<ExportedMediaEntity>> = exportDao.getAllExportsFlow()

    suspend fun recordExport(export: ExportedMediaEntity): Long {
        return exportDao.insertExport(export)
    }

    suspend fun deleteExport(export: ExportedMediaEntity) {
        exportDao.deleteExport(export)
    }

    suspend fun deleteExportById(id: Long) {
        exportDao.deleteExportById(id)
    }
}
