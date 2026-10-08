package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StickerOverlay
import com.example.data.model.TextOverlay
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet
import com.example.utils.SampleMediaProvider
import java.util.UUID

@Composable
fun TextStickerEditorDialog(
    onDismiss: () -> Unit,
    onAddTextOverlay: (TextOverlay) -> Unit,
    onAddSticker: (StickerOverlay) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    var inputText by remember { mutableStateOf("") }
    var isBold by remember { mutableStateOf(true) }
    var selectedColorIndex by remember { mutableStateOf(0) }

    val colorPalette = listOf(
        0xFFFFFFFF to "أبيض",
        0xFF06B6D4 to "سماوي",
        0xFFF59E0B to "ذهبي",
        0xFFEC4899 to "وردي",
        0xFF8B5CF6 to "بنفسجي",
        0xFF10B981 to "زمردي",
        0xFFEF4444 to "أحمر",
        0xFF000000 to "أسود"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = StudioSurfaceVariant,
                contentColor = StudioCyan
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("كتابة نص عربي", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("ملصقات وفيسات", fontWeight = FontWeight.Bold) }
                )
            }
        },
        text = {
            if (selectedTab == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("اكتب النص الذي تريد إضافته...", color = StudioTextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("text_overlay_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = StudioCyan,
                            unfocusedBorderColor = StudioSurfaceBorder,
                            focusedTextColor = StudioTextPrimary,
                            unfocusedTextColor = StudioTextPrimary,
                            focusedContainerColor = StudioSurface,
                            unfocusedContainerColor = StudioSurface
                        ),
                        singleLine = false,
                        maxLines = 3
                    )

                    Text(
                        text = "لون الخط:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = StudioTextPrimary
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(colorPalette.indices.toList()) { idx ->
                            val colorHex = colorPalette[idx].first
                            val isSelected = selectedColorIndex == idx
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(colorHex))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) StudioCyan else StudioSurfaceBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColorIndex = idx }
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isBold = !isBold },
                            modifier = Modifier
                                .background(
                                    if (isBold) StudioCyan.copy(alpha = 0.2f) else StudioSurfaceVariant,
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatBold,
                                contentDescription = "عريض",
                                tint = if (isBold) StudioCyan else StudioTextMuted
                            )
                        }
                        Text(
                            text = if (isBold) "خط عريض (Bold)" else "خط عادي",
                            fontSize = 12.sp,
                            color = StudioTextPrimary
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "اختر ملصقاً لإضافته فوراً:",
                        fontSize = 13.sp,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        SampleMediaProvider.sampleStickers.chunked(5).take(3).forEach { rowStickers ->
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                rowStickers.forEach { emoji ->
                                    Box(
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(StudioSurfaceVariant)
                                            .clickable {
                                                onAddSticker(
                                                    StickerOverlay(
                                                        id = UUID.randomUUID().toString(),
                                                        emoji = emoji,
                                                        xPosRatio = 0.5f,
                                                        yPosRatio = 0.4f
                                                    )
                                                )
                                                onDismiss()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = emoji, fontSize = 24.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (selectedTab == 0) {
                Button(
                    onClick = {
                        if (inputText.isNotBlank()) {
                            onAddTextOverlay(
                                TextOverlay(
                                    id = UUID.randomUUID().toString(),
                                    text = inputText.trim(),
                                    textColorHex = colorPalette[selectedColorIndex].first,
                                    isBold = isBold
                                )
                            )
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    enabled = inputText.isNotBlank(),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("confirm_add_text_btn")
                ) {
                    Text("إضافة النص", color = StudioDark, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = StudioTextMuted)
            }
        },
        containerColor = StudioSurface,
        shape = RoundedCornerShape(20.dp)
    )
}
