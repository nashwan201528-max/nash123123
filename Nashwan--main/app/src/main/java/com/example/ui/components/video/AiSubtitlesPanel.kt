package com.example.ui.components.video

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SubtitleLanguage
import com.example.data.model.SubtitleLine
import com.example.data.model.SubtitleStyleSettings
import com.example.ui.theme.StudioAmber
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioDarkElevated
import com.example.ui.theme.StudioEmerald
import com.example.ui.theme.StudioRose
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioSurfaceVariant
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioTextSecondary
import com.example.ui.theme.StudioViolet

@Composable
fun AiSubtitlesPanel(
    subtitles: List<SubtitleLine>,
    styleSettings: SubtitleStyleSettings,
    selectedLanguage: SubtitleLanguage,
    isGenerating: Boolean,
    onOpenStudioSheet: () -> Unit,
    onQuickGenerate: () -> Unit,
    onQuickChangeColor: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Header & Open Full Subtitle Studio Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Default.Subtitles, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(16.dp))
                Text(
                    text = "ترجمة النصوص والكابشن بالذكاء الاصطناعي:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = StudioTextPrimary
                )
            }

            Button(
                onClick = onOpenStudioSheet,
                colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.testTag("open_subtitles_studio_btn")
            ) {
                Icon(imageVector = Icons.Default.TextFields, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("تخصيص كامل 🎨", fontSize = 10.sp, color = StudioCyan, fontWeight = FontWeight.Bold)
            }
        }

        // 2. Quick One-Tap Generate Bar
        Card(
            colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
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
                    Text(selectedLanguage.flagEmoji, fontSize = 20.sp)
                    Column {
                        Text(
                            text = if (subtitles.isNotEmpty()) "${subtitles.size} أسطر ترجمة متزامنة ✓" else "لم يتم توليد ترجمة بعد",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (subtitles.isNotEmpty()) StudioEmerald else StudioTextPrimary
                        )
                        Text(
                            text = "اللغة: ${selectedLanguage.nameAr} • نمط: ${styleSettings.fontFamily.labelAr.take(15)}...",
                            fontSize = 9.sp,
                            color = StudioTextMuted,
                            maxLines = 1
                        )
                    }
                }

                Button(
                    onClick = onQuickGenerate,
                    enabled = !isGenerating,
                    colors = ButtonDefaults.buttonColors(containerColor = StudioCyan),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("quick_generate_subtitles_btn")
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(color = StudioDark, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = StudioDark, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (subtitles.isNotEmpty()) "إعادة توليد ⚡" else "توليد تلقائي ⚡", fontSize = 10.sp, color = StudioDark, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 3. Quick Color Presets Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("ألوان سريعة للخط:", fontSize = 10.sp, color = StudioTextSecondary, fontWeight = FontWeight.Bold)

            val quickColors = listOf(
                Pair("أبيض", 0xFFFFFFFF),
                Pair("أصفر نيون", 0xFFFACC15),
                Pair("سماوي", 0xFF22D3EE),
                Pair("وردي", 0xFFF43F5E),
                Pair("أخضر", 0xFF4ADE80),
                Pair("برتقالي", 0xFFFB923C)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                quickColors.forEach { (name, colorHex) ->
                    val isSelected = styleSettings.textColorHex == colorHex
                    Surface(
                        color = Color(colorHex),
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(
                            if (isSelected) 2.dp else 0.5.dp,
                            if (isSelected) Color.White else StudioSurfaceBorder
                        ),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onQuickChangeColor(colorHex) }
                    ) {}
                }
            }
        }
    }
}
