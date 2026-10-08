package com.example.ui.screens.templates

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AspectRatioType
import com.example.data.model.ComprehensiveVideoTemplate
import com.example.data.model.FilterRepository
import com.example.data.model.TemplateSceneItem
import com.example.data.model.VideoClip
import com.example.data.model.VideoTemplateRepository
import com.example.data.model.VideoTransition
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioCyanLight
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioViolet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoTemplatesScreen(
    onApplyTemplateToVideoEditor: (ComprehensiveVideoTemplate) -> Unit,
    onApplyTemplateToAiMontage: (ComprehensiveVideoTemplate) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryId by remember { mutableStateOf("all") }
    var searchQuery by remember { mutableStateOf("") }
    var inspectingTemplate by remember { mutableStateOf<ComprehensiveVideoTemplate?>(null) }

    val allTemplates = VideoTemplateRepository.templates
    val filteredTemplates = allTemplates.filter { template ->
        val matchesCategory = selectedCategoryId == "all" || template.categoryId == selectedCategoryId
        val matchesSearch = searchQuery.isBlank() ||
                template.titleAr.contains(searchQuery, ignoreCase = true) ||
                template.descriptionAr.contains(searchQuery, ignoreCase = true) ||
                template.tags.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCategory && matchesSearch
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark)
            .statusBarsPadding()
    ) {
        // 1. Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(38.dp)
                        .background(StudioSurface, CircleShape)
                        .border(1.dp, StudioSurfaceBorder, CircleShape)
                        .testTag("templates_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = StudioCyan
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "قوالب الفيديو الجاهزة",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Surface(
                            color = StudioMagenta.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "VIP & AI",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioMagenta,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "اختر قالبك المفضل وطبقه بضغطة زر واحدة",
                        fontSize = 11.sp,
                        color = StudioTextMuted
                    )
                }
            }
        }

        // 2. Search Box
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("ابحث عن قوالب ريلز، سينمائي، سفر، إعلانات...", fontSize = 12.sp, color = StudioTextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "بحث", tint = StudioCyan, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Close, contentDescription = "مسح", tint = StudioTextMuted, modifier = Modifier.size(18.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = StudioSurface,
                unfocusedContainerColor = StudioSurface,
                focusedBorderColor = StudioCyan,
                unfocusedBorderColor = StudioSurfaceBorder,
                focusedTextColor = StudioTextPrimary,
                unfocusedTextColor = StudioTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .testTag("templates_search_input")
        )

        // 3. Category Horizontal Row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(VideoTemplateRepository.categories) { category ->
                val isSelected = category.id == selectedCategoryId
                Surface(
                    color = if (isSelected) StudioCyan.copy(alpha = 0.2f) else StudioSurface,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isSelected) StudioCyan else StudioSurfaceBorder
                    ),
                    modifier = Modifier
                        .clickable { selectedCategoryId = category.id }
                        .testTag("category_${category.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = category.iconEmoji, fontSize = 14.sp)
                        Text(
                            text = category.titleAr,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextPrimary
                        )
                    }
                }
            }
        }

        // 4. Templates Grid List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "القوالب المتاحة (${filteredTemplates.size})",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                }
            }

            items(filteredTemplates) { template ->
                TemplateCardView(
                    template = template,
                    onInspectClick = { inspectingTemplate = template },
                    onApplyDirectly = { onApplyTemplateToVideoEditor(template) }
                )
            }
        }
    }

    // Modal Sheet for Full Template Inspection and Preview
    inspectingTemplate?.let { template ->
        TemplateInspectorSheet(
            template = template,
            onDismiss = { inspectingTemplate = null },
            onApplyToVideoEditor = {
                onApplyTemplateToVideoEditor(template)
                inspectingTemplate = null
            },
            onApplyToAiMontage = {
                onApplyTemplateToAiMontage(template)
                inspectingTemplate = null
            }
        )
    }
}

@Composable
private fun TemplateCardView(
    template: ComprehensiveVideoTemplate,
    onInspectClick: () -> Unit,
    onApplyDirectly: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onInspectClick() }
            .testTag("template_card_${template.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Emoji & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(
                                brush = Brush.radialGradient(listOf(StudioViolet, StudioCyan)),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = template.iconEmoji, fontSize = 24.sp)
                    }

                    Column {
                        Text(
                            text = template.titleAr,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = template.titleEn,
                            fontSize = 10.sp,
                            color = StudioTextMuted
                        )
                    }
                }

                if (template.isTrending) {
                    Surface(
                        color = StudioRose.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("🔥", fontSize = 10.sp)
                            Text("تريند", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = StudioRose)
                        }
                    }
                }
            }

            Text(
                text = template.descriptionAr,
                fontSize = 11.sp,
                color = StudioTextSecondary,
                lineHeight = 16.sp
            )

            // Dynamic Scene Sequence Preview
            AnimatedTemplateTimelinePreview(template = template)

            // Metadata Badges Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MetaBadge(icon = Icons.Default.Timer, text = "${template.totalDurationSec} ثانية", color = StudioAmber)
                MetaBadge(icon = Icons.Default.Movie, text = template.defaultAspectRatio.labelAr, color = StudioCyan)
                MetaBadge(icon = Icons.Default.MusicNote, text = "${template.musicTrackBpm} BPM", color = StudioMagenta)
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onInspectClick,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioCyan)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("معاينة القالب", fontSize = 11.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onApplyDirectly,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("apply_template_btn_${template.id}")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = StudioDark)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("تطبيق القالب 🎬", fontSize = 11.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MetaBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    color: Color
) {
    Surface(
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(12.dp))
            Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun AnimatedTemplateTimelinePreview(template: ComprehensiveVideoTemplate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(StudioDark)
            .padding(2.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        template.scenes.forEachIndexed { index, scene ->
            Box(
                modifier = Modifier
                    .weight((scene.durationMs / 1000f).coerceAtLeast(1f))
                    .fillMaxSize()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(scene.colorHex).copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${index + 1}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplateInspectorSheet(
    template: ComprehensiveVideoTemplate,
    onDismiss: () -> Unit,
    onApplyToVideoEditor: () -> Unit,
    onApplyToAiMontage: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StudioDarkElevated,
        contentColor = StudioTextPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = template.iconEmoji, fontSize = 26.sp)
                    Column {
                        Text(
                            text = template.titleAr,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "${template.defaultAspectRatio.labelAr} • ${template.totalDurationSec} ثانية",
                            fontSize = 11.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Interactive Simulated Player Box
            TemplateLiveInteractivePlayer(template = template)

            // Scenes Breakdown
            Text(
                text = "تسلسل وتكوين مشاهد القالب (${template.scenes.size} مشاهد):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                template.scenes.forEachIndexed { index, scene ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(scene.colorHex), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = scene.titleAr,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary
                                )
                                scene.textOverlay?.let {
                                    Text(
                                        text = "نص: $it",
                                        fontSize = 10.sp,
                                        color = StudioAmber
                                    )
                                }
                            }

                            scene.transitionToNext?.let { trans ->
                                Surface(
                                    color = StudioViolet.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${trans.iconEmoji} ${trans.nameAr}",
                                        fontSize = 9.sp,
                                        color = StudioViolet,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${scene.durationMs / 1000f}ث",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan
                            )
                        }
                    }
                }
            }

            // Audio track info
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(StudioMagenta.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MusicNote, contentDescription = null, tint = StudioMagenta)
                    }
                    Column {
                        Text(
                            text = "الموسيقى والإيقاع المعتمد للقالب:",
                            fontSize = 10.sp,
                            color = StudioTextMuted
                        )
                        Text(
                            text = template.musicTrackTitleAr,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                    }
                }
            }

            // Final Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onApplyToVideoEditor,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("modal_apply_video_editor_btn")
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = StudioDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تطبيق القالب وفتح محرر الفيديو 🎬",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioDark
                    )
                }

                Button(
                    onClick = onApplyToAiMontage,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioViolet),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("modal_apply_ai_montage_btn")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "المونتاج والتوليد بالذكاء الاصطناعي ⚡",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun TemplateLiveInteractivePlayer(template: ComprehensiveVideoTemplate) {
    val infiniteTransition = rememberInfiniteTransition(label = "template_player")
    val totalScenes = template.scenes.size.coerceAtLeast(1)
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = totalScenes.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = template.totalDurationSec * 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scene_progress"
    )

    val currentSceneIndex = (progress.toInt()).coerceIn(0, totalScenes - 1)
    val currentScene = template.scenes.getOrNull(currentSceneIndex) ?: template.scenes.first()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(currentScene.colorHex).copy(alpha = 0.85f))
            .border(1.dp, StudioSurfaceBorder, RoundedCornerShape(14.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                color = Color.Black.copy(alpha = 0.5f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "المشهد ${currentSceneIndex + 1} من ${template.scenes.size}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Text(
                text = currentScene.titleAr,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            currentScene.textOverlay?.let {
                Surface(
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Live player indicator
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(StudioEmerald, CircleShape)
            )
            Text(
                text = "معاينة حية للقالب",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
