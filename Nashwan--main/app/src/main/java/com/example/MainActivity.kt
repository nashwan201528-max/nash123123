package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.local.ProjectEntity
import com.example.ui.components.StudioBottomBar
import com.example.ui.components.StudioTab
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.montage.AiMontageStudioScreen
import com.example.ui.screens.photo.PhotoEditorScreen
import com.example.ui.screens.projects.ProjectsHistoryScreen
import com.example.ui.screens.settings.SettingsScreen
import com.example.ui.screens.video.VideoEditorScreen
import com.example.ui.theme.MontageProTheme
import com.example.ui.theme.StudioDark
import com.example.ui.viewmodel.AiMontageViewModel
import com.example.ui.viewmodel.PhotoEditorViewModel
import com.example.ui.viewmodel.ProjectsViewModel
import com.example.ui.viewmodel.SettingsViewModel
import com.example.ui.viewmodel.VideoEditorViewModel

class MainActivity : ComponentActivity() {

    private val photoViewModel: PhotoEditorViewModel by viewModels()
    private val videoViewModel: VideoEditorViewModel by viewModels()
    private val aiMontageViewModel: AiMontageViewModel by viewModels()
    private val projectsViewModel: ProjectsViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MontageProTheme {
                MainAppContent(
                    photoViewModel = photoViewModel,
                    videoViewModel = videoViewModel,
                    aiMontageViewModel = aiMontageViewModel,
                    projectsViewModel = projectsViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@Composable
fun MainAppContent(
    photoViewModel: PhotoEditorViewModel,
    videoViewModel: VideoEditorViewModel,
    aiMontageViewModel: AiMontageViewModel,
    projectsViewModel: ProjectsViewModel,
    settingsViewModel: SettingsViewModel
) {
    var currentTab by remember { mutableStateOf(StudioTab.HOME) }
    val projects by projectsViewModel.projects.collectAsState()

    fun handleOpenDraft(project: ProjectEntity) {
        if (project.mediaType == "PHOTO") {
            currentTab = StudioTab.PHOTO
        } else {
            videoViewModel.loadDraft(project)
            currentTab = StudioTab.VIDEO
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = StudioDark,
        bottomBar = {
            StudioBottomBar(
                currentTab = currentTab,
                onTabSelected = { selected -> currentTab = selected }
            )
        }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding()),
            label = "tab_transition"
        ) { targetTab ->
            when (targetTab) {
                StudioTab.HOME -> {
                    HomeScreen(
                        onNavigateTab = { tab -> currentTab = tab },
                        onOpenPhotoEditorWithUri = { uri ->
                            photoViewModel.loadFromUri(uri)
                            currentTab = StudioTab.PHOTO
                        },
                        onOpenTemplate = { template ->
                            aiMontageViewModel.applyTemplate(template)
                            currentTab = StudioTab.AI_MONTAGE
                        },
                        onOpenComprehensiveTemplate = { compTemplate ->
                            videoViewModel.applyComprehensiveTemplate(compTemplate)
                            currentTab = StudioTab.VIDEO
                        },
                        onOpenProjectDraft = { project ->
                            handleOpenDraft(project)
                        },
                        recentProjects = projects
                    )
                }

                StudioTab.TEMPLATES -> {
                    com.example.ui.screens.templates.VideoTemplatesScreen(
                        onApplyTemplateToVideoEditor = { template ->
                            videoViewModel.applyComprehensiveTemplate(template)
                            currentTab = StudioTab.VIDEO
                        },
                        onApplyTemplateToAiMontage = { template ->
                            val montageTpl = com.example.utils.MontageTemplate(
                                id = template.id,
                                titleAr = template.titleAr,
                                aspectRatio = template.defaultAspectRatio.labelAr,
                                durationSec = template.totalDurationSec,
                                icon = template.iconEmoji,
                                recommendedFilter = template.recommendedFilterId,
                                descriptionAr = template.descriptionAr
                            )
                            aiMontageViewModel.applyTemplate(montageTpl)
                            currentTab = StudioTab.AI_MONTAGE
                        },
                        onBackClick = { currentTab = StudioTab.HOME }
                    )
                }

                StudioTab.PHOTO -> {
                    PhotoEditorScreen(
                        viewModel = photoViewModel,
                        onBackClick = { currentTab = StudioTab.HOME }
                    )
                }

                StudioTab.VIDEO -> {
                    VideoEditorScreen(
                        viewModel = videoViewModel,
                        onBackClick = { currentTab = StudioTab.HOME }
                    )
                }

                StudioTab.AI_MONTAGE -> {
                    AiMontageStudioScreen(
                        viewModel = aiMontageViewModel,
                        onBackClick = { currentTab = StudioTab.HOME }
                    )
                }

                StudioTab.PROJECTS -> {
                    ProjectsHistoryScreen(
                        viewModel = projectsViewModel,
                        onNavigateTab = { tab -> currentTab = tab },
                        onOpenProjectDraft = { project ->
                            handleOpenDraft(project)
                        }
                    )
                }

                StudioTab.SETTINGS -> {
                    SettingsScreen(
                        viewModel = settingsViewModel,
                        onBackClick = { currentTab = StudioTab.HOME }
                    )
                }
            }
        }
    }
}
