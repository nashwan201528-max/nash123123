package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.ExportedMediaEntity
import com.example.data.local.ProjectEntity
import com.example.data.repository.MediaExportRepository
import com.example.data.repository.ProjectRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val projectRepo = ProjectRepository(db.projectDao())
    private val exportRepo = MediaExportRepository(db.exportDao())

    val projects: StateFlow<List<ProjectEntity>> = projectRepo.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exports: StateFlow<List<ExportedMediaEntity>> = exportRepo.allExports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            projectRepo.deleteProject(project)
        }
    }

    fun deleteExport(export: ExportedMediaEntity) {
        viewModelScope.launch {
            exportRepo.deleteExport(export)
        }
    }
}
