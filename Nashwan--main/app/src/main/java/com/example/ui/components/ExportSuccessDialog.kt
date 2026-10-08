package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ExportedMediaEntity
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary

@Composable
fun ExportSuccessDialog(
    exportItem: ExportedMediaEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            brush = Brush.radialGradient(listOf(StudioEmerald, Color(0xFF047857))),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DownloadDone,
                        contentDescription = "تم التحميل بنجاح",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "تم التصدير والتحميل بنجاح! 🎉",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = StudioSurfaceVariant),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DetailRow(label = "اسم الملف:", value = exportItem.title)
                    DetailRow(label = "نوع المحتوى:", value = if (exportItem.mediaType == "PHOTO") "صورة معدلة عالية الجودة" else "فيديو مونتاج سينمائي")
                    DetailRow(label = "الدقة:", value = exportItem.resolutionLabel)
                    DetailRow(label = "الفلتر المطبق:", value = exportItem.appliedFilterName)
                    if (exportItem.isAiEnhanced) {
                        DetailRow(label = "الذكاء الاصطناعي:", value = "مفعل ومحسن بالكامل ✨")
                    }
                    if (exportItem.isBgRemoved) {
                        DetailRow(label = "الخلفية:", value = "معزولة بدقة ومعدلة ✂️")
                    }
                    DetailRow(label = "المسار:", value = "معرض الصور والتحميلات (Pictures/Movies)")
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        shareExportedMedia(context, exportItem.filePath, exportItem.mimeType)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("share_exported_btn")
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = StudioDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("مشاركة", color = StudioDark, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceVariant),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("تم", color = StudioTextPrimary)
                }
            }
        },
        containerColor = StudioSurface,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = StudioTextMuted)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = StudioTextPrimary)
    }
}

private fun shareExportedMedia(context: Context, pathOrUri: String, mimeType: String) {
    try {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_TEXT, "تم التعديل والمونتاج عبر تطبيق مونتاج برو ✨🎬")
            if (pathOrUri.startsWith("content://") || pathOrUri.startsWith("file://")) {
                putExtra(Intent.EXTRA_STREAM, Uri.parse(pathOrUri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        }
        context.startActivity(Intent.createChooser(shareIntent, "مشاركة الوسائط عبر"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
