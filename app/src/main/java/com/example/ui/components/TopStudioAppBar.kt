package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet

@Composable
fun TopStudioAppBar(
    title: String,
    subtitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    onExportClick: (() -> Unit)? = null,
    onCloudExportClick: (() -> Unit)? = null,
    onSaveDraftClick: (() -> Unit)? = null,
    onAiMagicClick: (() -> Unit)? = null,
    exportButtonText: String = "تصدير وتحميل",
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        color = StudioSurface.copy(alpha = 0.95f),
        tonalElevation = 6.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false)
            ) {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("app_bar_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = StudioTextPrimary
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "PRO",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                androidx.compose.foundation.layout.Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary,
                        maxLines = 1
                    )
                    if (subtitle != null) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = StudioCyan,
                            maxLines = 1
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (onSaveDraftClick != null) {
                    IconButton(
                        onClick = onSaveDraftClick,
                        modifier = Modifier
                            .background(StudioSurfaceVariant, RoundedCornerShape(10.dp))
                            .testTag("save_draft_quick_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "حفظ المسودة",
                            tint = StudioCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (onAiMagicClick != null) {
                    IconButton(
                        onClick = onAiMagicClick,
                        modifier = Modifier
                            .background(
                                brush = Brush.linearGradient(listOf(StudioViolet, StudioMagenta)),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .testTag("ai_magic_quick_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "ذكاء اصطناعي",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (onCloudExportClick != null) {
                    IconButton(
                        onClick = onCloudExportClick,
                        modifier = Modifier
                            .background(StudioSurfaceVariant, RoundedCornerShape(10.dp))
                            .testTag("cloud_export_quick_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudUpload,
                            contentDescription = "سحابة Firebase",
                            tint = StudioCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                if (onExportClick != null) {
                    Button(
                        onClick = onExportClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudioCyan,
                            contentColor = StudioDark
                        ),
                        modifier = Modifier.testTag("app_bar_export_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "تحميل وتصدير",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(
                            text = exportButtonText,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
