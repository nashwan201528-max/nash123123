package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExportDao {
    @Query("SELECT * FROM exported_media ORDER BY createdAtTimestamp DESC")
    fun getAllExportsFlow(): Flow<List<ExportedMediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExport(export: ExportedMediaEntity): Long

    @Delete
    suspend fun deleteExport(export: ExportedMediaEntity)

    @Query("DELETE FROM exported_media WHERE id = :id")
    suspend fun deleteExportById(id: Long)
}
