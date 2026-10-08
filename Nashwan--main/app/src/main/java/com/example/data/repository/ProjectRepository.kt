package com.example.data.repository

import com.example.data.local.ProjectDao
import com.example.data.local.ProjectEntity
import kotlinx.coroutines.flow.Flow

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<ProjectEntity>> = projectDao.getAllProjectsFlow()

    suspend fun getProjectById(id: Long): ProjectEntity? {
        return projectDao.getProjectById(id)
    }

    suspend fun saveOrUpdateProject(project: ProjectEntity): Long {
        return if (project.id == 0L) {
            projectDao.insertProject(project)
        } else {
            projectDao.updateProject(project)
            project.id
        }
    }

    suspend fun deleteProject(project: ProjectEntity) {
        projectDao.deleteProject(project)
    }

    suspend fun deleteProjectById(id: Long) {
        projectDao.deleteProjectById(id)
    }
}
