package com.example.ui.screens.projects

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExportedMediaEntity
import com.example.data.local.ProjectEntity
import com.example.ui.components.StudioTab
import com.example.ui.components.TopStudioAppBar
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet
import com.example.ui.viewmodel.ProjectsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProjectsHistoryScreen(
    viewModel: ProjectsViewModel,
    onNavigateTab: (StudioTab) -> Unit,
    onOpenProjectDraft: (ProjectEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    val exports by viewModel.exports.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopStudioAppBar(
                title = "المشاريع والمسودات المحفوظة",
                subtitle = "${projects.size} مسودات (Room DB) • ${exports.size} ملفات مصدرة"
            )
        },
        containerColor = StudioDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(StudioDark)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = StudioSurfaceVariant,
                contentColor = StudioCyan
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "مسودات الفيديو والصور (${projects.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "الملفات المحملة والمصدرة (${exports.size})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                )
            }

            if (selectedTab == 0) {
                if (projects.isEmpty()) {
                    EmptyHistoryPlaceholder(
                        title = "لا توجد مسودات مشاريع محفوظة بعد",
                        subtitle = "عند تعديل أي فيديو أو صورة، يتم حفظ مسودة العمل محلياً في قاعدة بيانات Room لتعود وتكمل المونتاج في أي وقت.",
                        onCtaClick = { onNavigateTab(StudioTab.VIDEO) },
                        ctaText = "إنشاء مشروع فيديو جديد 🎬"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(projects, key = { it.id }) { project ->
                            ProjectItemCard(
                                project = project,
                                onOpen = {
                                    onOpenProjectDraft(project)
                                },
                                onDelete = { viewModel.deleteProject(project) }
                            )
                        }
                    }
                }
            } else {
                if (exports.isEmpty()) {
                    EmptyHistoryPlaceholder(
                        title = "لم تقم بتصدير أي وسائط بعد",
                        subtitle = "عند الضغط على \"تصدير وتحميل\" في محرر الصور أو الفيديو، ستظهر ملفاتك المحملة هنا.",
                        onCtaClick = { onNavigateTab(StudioTab.PHOTO) },
                        ctaText = "فتح محرر الصور 🖼️"
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(exports, key = { it.id }) { export ->
                            ExportItemCard(
                                export = export,
                                onShare = { shareMedia(context, export.filePath, export.mimeType) },
                                onDelete = { viewModel.deleteExport(export) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectItemCard(
    project: ProjectEntity,
    onOpen: () -> Unit,
    onDelete: () -> Unit
) {
    val dateStr = remember(project.lastModifiedTimestamp) {
        SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault()).format(Date(project.lastModifiedTimestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onOpen() }
            .testTag("project_draft_card_${project.id}"),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(
                            if (project.mediaType == "VIDEO") StudioCyan.copy(alpha = 0.2f) else StudioMagenta.copy(alpha = 0.2f),
                            RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (project.mediaType == "PHOTO") Icons.Default.Image else Icons.Default.Movie,
                        contentDescription = "نوع المشروع",
                        tint = if (project.mediaType == "VIDEO") StudioCyan else StudioMagenta,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        text = project.title,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary,
                        fontSize = 14.sp,
                        maxLines = 1
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = StudioSurfaceVariant,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = if (project.mediaType == "PHOTO") "صورة" else "فيديو (${project.durationMs / 1000}ث)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioCyan,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "آخر تعديل: $dateStr",
                            fontSize = 10.sp,
                            color = StudioTextMuted
                        )
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = StudioCyan,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable { onOpen() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "متابعة المونتاج", tint = StudioDark, modifier = Modifier.size(14.dp))
                        Text("متابعة", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioDark)
                    }
                }

                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف المسودة", tint = StudioRose, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
private fun ExportItemCard(
    export: ExportedMediaEntity,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = StudioSurface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
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
                        .background(StudioEmerald.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = "ملف محمل",
                        tint = StudioEmerald
                    )
                }

                Column {
                    Text(
                        text = export.title,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${export.resolutionLabel} • ${export.appliedFilterName}",
                        fontSize = 11.sp,
                        color = StudioTextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onShare) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = StudioCyan)
                }
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "حذف", tint = StudioRose)
                }
            }
        }
    }
}

@Composable
private fun EmptyHistoryPlaceholder(
    title: String,
    subtitle: String,
    onCtaClick: () -> Unit,
    ctaText: String = "بدء مشروع جديد"
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(StudioSurfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Folder,
                contentDescription = "فارغ",
                tint = StudioCyan,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = StudioTextMuted,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            color = StudioCyan,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.clickable { onCtaClick() }
        ) {
            Text(
                text = ctaText,
                fontWeight = FontWeight.Bold,
                color = StudioDark,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

private fun shareMedia(context: Context, pathOrUri: String, mimeType: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_TEXT, "تم التصدير والتحميل عبر تطبيق مونتاج برو ✨🎬")
            if (pathOrUri.startsWith("content://") || pathOrUri.startsWith("file://")) {
                putExtra(Intent.EXTRA_STREAM, Uri.parse(pathOrUri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        context.startActivity(Intent.createChooser(shareIntent, "مشاركة الملف عبر"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
