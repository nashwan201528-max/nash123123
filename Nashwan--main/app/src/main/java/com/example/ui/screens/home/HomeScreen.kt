package com.example.ui.screens.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ProjectEntity
import com.example.ui.components.StudioTab
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
import com.example.utils.MontageTemplate
import com.example.utils.SampleMediaProvider

import com.example.data.model.ComprehensiveVideoTemplate
import com.example.data.model.VideoTemplateRepository
import androidx.compose.material.icons.filled.VideoLibrary

@Composable
fun HomeScreen(
    onNavigateTab: (StudioTab) -> Unit,
    onOpenPhotoEditorWithUri: (Uri) -> Unit,
    onOpenTemplate: (MontageTemplate) -> Unit,
    onOpenComprehensiveTemplate: (ComprehensiveVideoTemplate) -> Unit = {},
    onOpenProjectDraft: (ProjectEntity) -> Unit,
    recentProjects: List<ProjectEntity>,
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onOpenPhotoEditorWithUri(uri)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(StudioDark)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Studio Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
        ) {
            Image(
                painter = painterResource(id = R.drawable.hero_banner),
                contentDescription = "استوديو المونتاج",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Gradient overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, StudioDark.copy(alpha = 0.9f), StudioDark)
                        )
                    )
            )

            // Top Right Settings Button
            IconButton(
                onClick = { onNavigateTab(StudioTab.SETTINGS) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .background(StudioDark.copy(alpha = 0.7f), CircleShape)
                    .border(1.dp, StudioSurfaceBorder, CircleShape)
                    .testTag("home_settings_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "الإعدادات",
                    tint = StudioCyan
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(listOf(StudioCyan, StudioMagenta)),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "استوديو الذكاء الاصطناعي 2026",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Text(
                    text = "مونتاج وتعديل الفيديو والصور",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = StudioTextPrimary
                )

                Text(
                    text = "قص احترافي، فلاتر سينمائية، عزل الخلفيات، وتصدير بجودة فائقة",
                    style = MaterialTheme.typography.bodySmall,
                    color = StudioCyan
                )
            }
        }

        // Quick Primary Actions (Grid 2x2)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "ابدأ مشروعاً جديداً 🎬",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CreationCard(
                    title = "محرر ومونتاج الفيديو",
                    subtitle = "قص المقاطع، إضافة موسيقى وفلاتر",
                    icon = Icons.Default.Movie,
                    gradient = listOf(StudioCyan, Color(0xFF0284C7)),
                    onClick = { onNavigateTab(StudioTab.VIDEO) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_video_card"
                )

                CreationCard(
                    title = "محرر الصور المتقدم",
                    subtitle = "20 فلتر، قص ونصوص وعزل الخلفية",
                    icon = Icons.Default.Image,
                    gradient = listOf(StudioViolet, StudioMagenta),
                    onClick = { onNavigateTab(StudioTab.PHOTO) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_photo_card"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CreationCard(
                    title = "المونتاج الذكي (AI)",
                    subtitle = "توليد قصة وسيناريو كامل بضغطة زر",
                    icon = Icons.Default.AutoAwesome,
                    gradient = listOf(StudioMagenta, StudioAmber),
                    onClick = { onNavigateTab(StudioTab.AI_MONTAGE) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_ai_montage_card"
                )

                CreationCard(
                    title = "مكتبة قوالب الفيديو",
                    subtitle = "قوالب تيك توك، سينمائي، سفر وإعلانات",
                    icon = Icons.Default.VideoLibrary,
                    gradient = listOf(StudioEmerald, StudioCyan),
                    onClick = { onNavigateTab(StudioTab.TEMPLATES) },
                    modifier = Modifier.weight(1f),
                    testTag = "home_templates_card"
                )
            }
        }

        // Dedicated Video Templates Showcase Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(18.dp))
                .clickable { onNavigateTab(StudioTab.TEMPLATES) }
                .testTag("home_templates_hub_banner"),
            colors = CardDefaults.cardColors(containerColor = StudioSurface),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.4f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "شاشة قوالب الفيديو الجاهزة 🔥",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            Surface(
                                color = StudioAmber.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "جديد",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioAmber,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "تصفح واختر من بين قوالب احترافية جاهزة بضغطة زر",
                            fontSize = 11.sp,
                            color = StudioTextMuted
                        )
                    }
                }

                Surface(
                    color = StudioCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "استعراض 🌟",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Popular Templates Carousel
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "قوالب فيديو جاهزة للاختيار السريع 🎬",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
                Text(
                    text = "عرض الكل (${VideoTemplateRepository.templates.size})",
                    style = MaterialTheme.typography.labelSmall,
                    color = StudioCyan,
                    modifier = Modifier.clickable { onNavigateTab(StudioTab.TEMPLATES) }
                )
            }

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(VideoTemplateRepository.templates) { compTemplate ->
                    Card(
                        modifier = Modifier
                            .width(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable {
                                onOpenComprehensiveTemplate(compTemplate)
                            }
                            .testTag("home_tpl_${compTemplate.id}"),
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = compTemplate.iconEmoji, fontSize = 28.sp)
                                Box(
                                    modifier = Modifier
                                        .background(StudioSurfaceVariant, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${compTemplate.defaultAspectRatio.labelAr} | ${compTemplate.totalDurationSec}ث",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StudioCyan
                                        )
                                    }
                                }

                                Text(
                                    text = compTemplate.titleAr,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = StudioTextPrimary,
                                    maxLines = 1
                                )

                                Text(
                                    text = compTemplate.descriptionAr,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = StudioTextMuted,
                                    maxLines = 2,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

        // AI Super Tools Showcase
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "أدوات الذكاء الاصطناعي المدمجة ⚡",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AiFeatureRow(
                        icon = Icons.Default.ContentCut,
                        title = "إزالة واستبدال الخلفية تلقائياً",
                        description = "عزل دقيق للعناصر مع خيارات شفافة PNG وخلفيات استوديو متناسقة"
                    )
                    AiFeatureRow(
                        icon = Icons.Default.HighQuality,
                        title = "رفع الدقة الفائقة وشحذ التفاصيل (Super Resolution)",
                        description = "معالجة التغبيش وإبراز الملامح بدقة HD و 4K"
                    )
                    AiFeatureRow(
                        icon = Icons.Default.Tune,
                        title = "تصفية وإزالة الضوضاء الرقمية (AI Denoise)",
                        description = "تنعيم مثالي للمشاهد الليلية مع الحفاظ على حواف المشهد"
                    )
                    AiFeatureRow(
                        icon = Icons.Default.AutoAwesome,
                        title = "كتالوج 20 فلتراً سينمائياً وعصرياً",
                        description = "تدرجات سينمائية، عتيقة، مستقبلية وطبيعية بلمسة واحدة"
                    )
                }
            }
        }

        // Recent Projects (if any)
        if (recentProjects.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "المشاريع المحفوظة مؤخراً 📁",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = "عرض الكل",
                        style = MaterialTheme.typography.labelSmall,
                        color = StudioCyan,
                        modifier = Modifier.clickable { onNavigateTab(StudioTab.PROJECTS) }
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(recentProjects.take(5)) { project ->
                        RecentProjectCard(
                            project = project,
                            onClick = {
                                onOpenProjectDraft(project)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreationCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag(testTag),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(brush = Brush.linearGradient(gradient), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = StudioTextMuted,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun TemplateItemCard(
    template: MontageTemplate,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("template_${template.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = template.icon, fontSize = 28.sp)
                Box(
                    modifier = Modifier
                        .background(StudioSurfaceVariant, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${template.aspectRatio} | ${template.durationSec}ث",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )
                }
            }

            Text(
                text = template.titleAr,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Text(
                text = template.descriptionAr,
                style = MaterialTheme.typography.bodySmall,
                fontSize = 11.sp,
                color = StudioTextMuted,
                maxLines = 2,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun AiFeatureRow(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(StudioSurfaceVariant, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = StudioCyan,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = StudioTextMuted
            )
        }
    }
}

@Composable
private fun StudioEngineRow(
    title: String,
    description: String,
    badgeText: String,
    badgeColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
                Surface(
                    color = badgeColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = description,
                fontSize = 11.sp,
                color = StudioTextMuted,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun RecentProjectCard(
    project: ProjectEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(StudioSurfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (project.mediaType == "PHOTO") Icons.Default.Image else Icons.Default.Movie,
                    contentDescription = "مشروع",
                    tint = StudioCyan
                )
            }
            Text(
                text = project.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary,
                maxLines = 1
            )
            Text(
                text = project.aspectRatio,
                fontSize = 10.sp,
                color = StudioTextMuted
            )
        }
    }
}
