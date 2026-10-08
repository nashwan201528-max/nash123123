package com.example.ui.screens.montage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiMontageScene
import com.example.ui.components.ExportSuccessDialog
import com.example.ui.components.TopStudioAppBar
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet
import com.example.ui.viewmodel.AiMontageViewModel
import com.example.utils.SampleMediaProvider

@Composable
fun AiMontageStudioScreen(
    viewModel: AiMontageViewModel,
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.statusMessage) {
        state.statusMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearStatusMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopStudioAppBar(
                title = "صانع المونتاج بالذكاء الاصطناعي",
                subtitle = "توليد مشاهد وسيناريو ومونتاج متكامل",
                onBackClick = onBackClick,
                onAiMagicClick = { viewModel.generateMontagePlan() },
                onExportClick = { viewModel.exportMontage() },
                exportButtonText = if (state.isExporting) "جاري التحميل..." else "تصدير المونتاج"
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = StudioDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(StudioDark)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Prompt Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    brush = Brush.radialGradient(listOf(StudioMagenta, StudioViolet)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "ذكاء اصطناعي",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "صف فكرة المونتاج التي تريد إنتاجها:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            Text(
                                text = "سيقوم الذكاء الاصطناعي بتقسيم المشاهد واقتراح الانتقالات والموسيقى",
                                fontSize = 11.sp,
                                color = StudioCyan
                            )
                        }
                    }

                    OutlinedTextField(
                        value = state.promptInput,
                        onValueChange = { viewModel.setPrompt(it) },
                        placeholder = { Text("مثال: مونتاج ريلز ترويجي لمطعم عصري مع لقطات سريعة وإضاءة دافئة...", color = StudioTextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ai_montage_prompt_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioCyan,
                            unfocusedBorderColor = StudioSurfaceBorder,
                            focusedTextColor = StudioTextPrimary,
                            unfocusedTextColor = StudioTextPrimary,
                            focusedContainerColor = StudioDarkElevated,
                            unfocusedContainerColor = StudioDarkElevated
                        ),
                        maxLines = 3
                    )

                    // Quick prompt suggestion pills
                    Text(
                        text = "أفكار مقترحة سريعة:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudioTextMuted
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val suggestions = listOf(
                            "سياحة واستكشاف طبيعة 🌄",
                            "ريلز تريند تيك توك ⚡",
                            "إعلان منتج تجاري 🛍️",
                            "قصة وثائقية ملهمة 🎙️",
                            "سايبر نيون وحماس 🔥"
                        )
                        items(suggestions) { promptText ->
                            Surface(
                                color = StudioSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                                modifier = Modifier.clickable {
                                    viewModel.setPrompt(promptText)
                                    viewModel.generateMontagePlan(promptText)
                                }
                            ) {
                                Text(
                                    text = promptText,
                                    fontSize = 11.sp,
                                    color = StudioCyan,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Platform Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val platforms = listOf(
                            "TikTok & Reels (9:16)",
                            "YouTube & TV (16:9)",
                            "Instagram (1:1)"
                        )
                        platforms.forEach { plat ->
                            val isSelected = state.selectedPlatform == plat
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setPlatform(plat) },
                                label = { Text(plat, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = StudioCyan.copy(alpha = 0.2f),
                                    selectedLabelColor = StudioCyan
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Generate Button
                    Button(
                        onClick = { viewModel.generateMontagePlan() },
                        enabled = !state.isGenerating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("generate_ai_montage_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isGenerating) {
                            CircularProgressIndicator(color = StudioDark, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("جاري بناء خطة المونتاج الذكي...", color = StudioDark, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "توليد", tint = StudioDark)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("توليد المشاهد والسيناريو بالذكاء الاصطناعي", color = StudioDark, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Generated Storyboard Breakdown
            state.generatedPlan?.let { plan ->
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎬 سيناريو ومخطط المشاهد المقترح:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )

                        Surface(
                            color = StudioSurfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "المدة: ${plan.estimatedTotalDurationSec}ثانية",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Music & Mood Banner
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = "الموسيقى", tint = StudioMagenta)
                            Column {
                                Text(text = "طابع الموسيقى المقترح:", fontSize = 11.sp, color = StudioTextMuted)
                                Text(text = plan.musicVibeAr, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            }
                        }
                    }

                    // Scenes Cards List
                    plan.scenes.forEach { scene ->
                        SceneCard(scene = scene)
                    }

                    // Full Export & Download CTA
                    Button(
                        onClick = { viewModel.exportMontage() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("export_montage_cta_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudioEmerald
                        ),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "تصدير وتحميل", tint = Color.White)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = "تصدير وتحميل المونتاج بجودة فائقة (1080p FHD)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Export Success Dialog
    state.lastExportedItem?.let { item ->
        ExportSuccessDialog(
            exportItem = item,
            onDismiss = { viewModel.dismissExportDialog() }
        )
    }
}

@Composable
private fun SceneCard(scene: AiMontageScene) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .background(StudioCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${scene.sceneNumber}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioDark
                        )
                    }

                    Text(
                        text = scene.sceneTitleAr,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                }

                Text(
                    text = "${scene.suggestedDurationSec} ثوانٍ",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = StudioCyan
                )
            }

            Text(
                text = "الوصف البصري: ${scene.visualDescriptionAr}",
                fontSize = 12.sp,
                color = StudioTextPrimary,
                lineHeight = 18.sp
            )

            Surface(
                color = StudioSurfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🎙️ النص والصوت: \"${scene.voiceoverOrTextAr}\"",
                    fontSize = 11.sp,
                    color = StudioAmber,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "🔄 انتقال: ${scene.transitionEffectAr}",
                    fontSize = 10.sp,
                    color = StudioTextMuted
                )
                Text(
                    text = "🔊 صوت: ${scene.soundEffectAr}",
                    fontSize = 10.sp,
                    color = StudioTextMuted
                )
            }
        }
    }
}
