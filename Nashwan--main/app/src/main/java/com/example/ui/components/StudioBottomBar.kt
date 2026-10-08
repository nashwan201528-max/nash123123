package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StudioCyan
import com.example.ui.theme.StudioDark
import com.example.ui.theme.StudioMagenta
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceBorder
import com.example.ui.theme.StudioTextMuted
import com.example.ui.theme.StudioTextPrimary
import com.example.ui.theme.StudioViolet

import androidx.compose.material.icons.filled.VideoLibrary

enum class StudioTab(val titleAr: String, val icon: ImageVector, val tag: String) {
    HOME("الرئيسية", Icons.Default.Home, "tab_home"),
    TEMPLATES("القوالب", Icons.Default.VideoLibrary, "tab_templates"),
    VIDEO("محرر الفيديو", Icons.Default.Movie, "tab_video"),
    AI_MONTAGE("المونتاج الذكي", Icons.Default.AutoAwesome, "tab_ai_montage"),
    PHOTO("محرر الصور", Icons.Default.Image, "tab_photo"),
    PROJECTS("المشاريع", Icons.Default.Folder, "tab_projects"),
    SETTINGS("الإعدادات", Icons.Default.Settings, "tab_settings")
}

@Composable
fun StudioBottomBar(
    currentTab: StudioTab,
    onTabSelected: (StudioTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = StudioSurface.copy(alpha = 0.96f),
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudioTab.values().forEach { tab ->
                val isSelected = tab == currentTab
                val isSpecialAi = tab == StudioTab.AI_MONTAGE

                if (isSpecialAi) {
                    // Elevated glowing center action
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                            .testTag(tab.tag)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        listOf(StudioCyan, StudioMagenta)
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.titleAr,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Text(
                            text = tab.titleAr,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) StudioCyan else StudioTextPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onTabSelected(tab) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag(tab.tag)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isSelected) StudioCyan.copy(alpha = 0.15f) else Color.Transparent
                                )
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tab.icon,
                                contentDescription = tab.titleAr,
                                tint = if (isSelected) StudioCyan else StudioTextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Text(
                            text = tab.titleAr,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) StudioCyan else StudioTextMuted
                        )
                    }
                }
            }
        }
    }
}
