package com.example.ui.components.video

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CloudProjectItem
import com.example.data.model.CloudSyncStatus
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CloudExportBottomSheet(
    projectTitle: String,
    durationMs: Long,
    syncStatus: CloudSyncStatus,
    cloudProjects: List<CloudProjectItem>,
    onDismiss: () -> Unit,
    onStartCloudExport: (resolution: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current
    var selectedResolution by remember { mutableStateOf("1080p FHD") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = StudioDarkElevated,
        contentColor = StudioTextPrimary,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "تصدير سحابي",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "التصدير السحابي عبر Firebase (Cloud Storage)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "توفير الفيديو عبر جميع أجهزتك • مشاركة الروابط • نسخ احتياطي آمن",
                            fontSize = 10.sp,
                            color = StudioCyan
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                }
            }

            // Cross-Device Sync Badge
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
                            .size(34.dp)
                            .background(StudioEmerald.copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Devices, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "متاح تلقائياً عبر كل أجهزتك (Multi-Device Sync)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "يتم حفظ الفيديو في Firebase Storage ومزامنته مع هاتفك وجهازك اللوحي والكمبيوتر فوراً.",
                            fontSize = 9.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
            }

            // Upload Card Deck
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDark),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (syncStatus.lastExportResult != null) StudioEmerald else StudioSurfaceBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(projectTitle, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            Text("المدة: ${(durationMs / 1000f).toInt()} ثانية • الجودة: $selectedResolution", fontSize = 10.sp, color = StudioTextMuted)
                        }

                        // Resolution Picker
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("1080p FHD", "4K UHD").forEach { res ->
                                val isSelected = selectedResolution == res
                                Surface(
                                    color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) StudioCyan else StudioSurfaceBorder),
                                    modifier = Modifier.clickable { selectedResolution = res }
                                ) {
                                    Text(
                                        text = res,
                                        fontSize = 9.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StudioCyan else StudioTextPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Upload Progress or Action Button
                    if (syncStatus.isUploading) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = syncStatus.currentStepLabel,
                                    fontSize = 11.sp,
                                    color = StudioCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${(syncStatus.uploadProgress * 100).toInt()}%",
                                    fontSize = 11.sp,
                                    color = StudioCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            LinearProgressIndicator(
                                progress = { syncStatus.uploadProgress },
                                color = StudioCyan,
                                trackColor = StudioSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                        }
                    } else if (syncStatus.lastExportResult != null) {
                        // Success Result Box
                        val res = syncStatus.lastExportResult
                        Surface(
                            color = StudioEmerald.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioEmerald)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(imageVector = Icons.Default.CloudDone, contentDescription = null, tint = StudioEmerald, modifier = Modifier.size(18.dp))
                                    Text("تم التصدير السحابي بنجاح إلى Firebase Storage! ☁️✨", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioEmerald)
                                }

                                Text(
                                    text = "رابط المشاركة السحابي: ${res.shareableWebLink}",
                                    fontSize = 10.sp,
                                    color = StudioTextSecondary,
                                    maxLines = 1
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Cloud Video Link", res.shareableWebLink)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "تم نسخ رابط الفيديو السحابي بنجاح! 📋", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioEmerald),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = StudioDark, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("نسخ الرابط 📋", fontSize = 10.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                                    }

                                    Button(
                                        onClick = { onStartCloudExport(selectedResolution) },
                                        colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.weight(1f),
                                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Sync, contentDescription = null, tint = StudioTextPrimary, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("إعادة الرفع ↺", fontSize = 10.sp, color = StudioTextPrimary, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { onStartCloudExport(selectedResolution) },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("start_firebase_cloud_export_btn")
                        ) {
                            Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, tint = StudioDark, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "تصدير الفيديو ورفعه إلى مساحة Firebase السحابية ☁️",
                                color = StudioDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Synced User Cloud Projects List
            if (cloudProjects.isNotEmpty()) {
                Text(
                    text = "المشاريع السحابية المتزامنة على كافة أجهزتك (${cloudProjects.size}):",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )

                cloudProjects.forEach { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = StudioSurface),
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(StudioCyan.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(imageVector = Icons.Default.CloudQueue, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                                }
                                Column {
                                    Text(item.title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                                    Text(
                                        text = "${item.resolutionLabel} • ${item.fileSizeMb}MB • ${item.deviceOrigin}",
                                        fontSize = 9.sp,
                                        color = StudioTextMuted
                                    )
                                }
                            }

                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Cloud Video Link", item.shareableWebLink)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "تم نسخ رابط المشروع السحابي! 📋", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Link, contentDescription = "نسخ الرابط", tint = StudioCyan, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
