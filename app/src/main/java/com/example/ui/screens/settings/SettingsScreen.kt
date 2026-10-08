package com.example.ui.screens.settings

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppStudioSettings
import com.example.data.model.SubtitleLanguage
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
import com.example.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settings.collectAsState()
    val cacheSizeMb by viewModel.cacheSizeMb.collectAsState()
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SettingsTopBar(onBackClick = onBackClick)
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = StudioDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Profile & Pro Studio Header Banner
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "MontagePro Studio AI",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioTextPrimary
                            )
                            Surface(
                                color = StudioCyan.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "ULTRA",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    color = StudioCyan,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "محرك المونتاج الذكي • إصدار 2.5 • ميزة المعالجة السحابية نشطة",
                            fontSize = 11.sp,
                            color = StudioTextSecondary
                        )
                    }
                }
            }

            // === 1. EXPORT & RENDERING SETTINGS ===
            SettingsSectionHeader(
                title = "محرك وجودة التصدير (Export & Rendering Engine)",
                icon = Icons.Default.HighQuality,
                iconTint = StudioCyan
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Default Resolution Options
                    Text(
                        text = "الدقة الافتراضية لتصدير الفيديو:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    val resOptions = listOf("720p HD", "1080p FHD 60fps", "4K Ultra UHD")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        resOptions.forEach { res ->
                            val isSelected = settings.defaultExportResolution == res
                            Surface(
                                color = if (isSelected) StudioCyan.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioCyan else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateExportResolution(res) }
                                    .testTag("setting_res_$res")
                            ) {
                                Text(
                                    text = res,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) StudioCyan else StudioTextPrimary,
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    // Auto-Save To Gallery Toggle
                    SettingsSwitchRow(
                        title = "الحفظ التلقائي في معرض صور الجهاز (Gallery)",
                        subtitle = "حفظ الفيديو المصدّر مباشرة في مجلد الكاميرا والمعرض",
                        isChecked = settings.autoSaveToGallery,
                        onCheckedChange = { viewModel.toggleAutoSaveToGallery() },
                        icon = Icons.Default.Save
                    )

                    // Hardware GPU Acceleration Toggle
                    SettingsSwitchRow(
                        title = "تسريع العتاد ومعالج الرسوميات (GPU Acceleration)",
                        subtitle = "مضاعفة سرعة الرندر والرسم الحركي بالذكاء الاصطناعي",
                        isChecked = settings.hardwareAccelerationEnabled,
                        onCheckedChange = { viewModel.toggleHardwareAcceleration() },
                        icon = Icons.Default.Videocam
                    )
                }
            }

            // === 2. AI ENGINE & SUBTITLES ===
            SettingsSectionHeader(
                title = "محرك الذكاء الاصطناعي والترجمة (AI Speech & Script)",
                icon = Icons.Default.AutoAwesome,
                iconTint = StudioViolet
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "لغة الترجمة والتعرف الصوتي الافتراضية (Default Language):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )

                    // Subtitle Languages Carousel
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(SubtitleLanguage.values()) { lang ->
                            val isSelected = settings.defaultSubtitleLanguage == lang
                            Surface(
                                color = if (isSelected) StudioViolet.copy(alpha = 0.25f) else StudioSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) StudioViolet else StudioSurfaceBorder
                                ),
                                modifier = Modifier
                                    .clickable { viewModel.updateDefaultSubtitleLanguage(lang) }
                                    .testTag("setting_lang_${lang.code}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(lang.flagEmoji, fontSize = 12.sp)
                                    Text(
                                        text = lang.nameAr,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) StudioViolet else StudioTextPrimary
                                    )
                                }
                            }
                        }
                    }

                    // Auto AI Voice Denoise
                    SettingsSwitchRow(
                        title = "تنقية الصوت التلقائية وعزل الضوضاء (AI Denoise)",
                        subtitle = "تصفية صوت الميكروفون والتعليق الصوتي من ضوضاء الغرفة",
                        isChecked = settings.autoAiVoiceDenoise,
                        onCheckedChange = { viewModel.toggleAutoAiDenoise() },
                        icon = Icons.Default.GraphicEq
                    )
                }
            }

            // === 3. CLOUD STORAGE & MULTI-DEVICE SYNC ===
            SettingsSectionHeader(
                title = "المزامنة السحابية و Firebase (Cloud Storage & Sync)",
                icon = Icons.Default.CloudSync,
                iconTint = StudioEmerald
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SettingsSwitchRow(
                        title = "المزامنة السحابية التلقائية للمشاريع (Cross-Device Sync)",
                        subtitle = "حفظ المسودات في Firebase ومزامنتها فوراً مع أجهزتك الأخرى",
                        isChecked = settings.autoCloudSyncEnabled,
                        onCheckedChange = { viewModel.toggleAutoCloudSync() },
                        icon = Icons.Default.Devices
                    )

                    SettingsSwitchRow(
                        title = "الرفع والمزامنة عبر Wi-Fi فقط",
                        subtitle = "توفير باقة بيانات الهاتف وعدم رفع المشاريع الضخمة إلا عبر الوايفاي",
                        isChecked = settings.uploadOnWifiOnly,
                        onCheckedChange = { viewModel.toggleUploadOnWifiOnly() },
                        icon = Icons.Default.Wifi
                    )
                }
            }

            // === 4. STORAGE & CACHE CLEANER ===
            SettingsSectionHeader(
                title = "إدارة التخزين والذاكرة المؤقتة (Storage & Cache)",
                icon = Icons.Default.Storage,
                iconTint = StudioAmber
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("الذاكرة المؤقتة للمونتاج والرندر:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                            Text("تخزين مؤقت لمعاينات الفيديو والفلاتر", fontSize = 9.sp, color = StudioTextMuted)
                        }

                        Surface(
                            color = StudioAmber.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${String.format("%.1f", cacheSizeMb)} MB",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioAmber,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.clearRenderCache() },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioAmber),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("clear_cache_btn"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, tint = StudioAmber, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("تفريغ الكاش 🧹", fontSize = 11.sp, color = StudioAmber, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.resetAllSettingsToDefault() },
                            colors = ButtonDefaults.buttonColors(containerColor = StudioDarkElevated),
                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioRose),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("reset_defaults_btn"),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = StudioRose, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("استعادة الافتراضي ↺", fontSize = 11.sp, color = StudioRose, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // === 5. ABOUT & VERSION ===
            Card(
                colors = CardDefaults.cardColors(containerColor = StudioDarkElevated),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioSurfaceBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MontagePro Studio AI • الإصدار 2.5",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = "تطبيق المونتاج وتعديل الفيديو والذكاء الاصطناعي الشامل لأجهزة أندرويد",
                        fontSize = 10.sp,
                        color = StudioTextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "آمن • محلي وخالي من الإعلانات • متوافق مع كافة المعايير",
                        fontSize = 9.sp,
                        color = StudioTextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsTopBar(
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("settings_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = StudioTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            brush = Brush.linearGradient(listOf(StudioCyan, StudioViolet)),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column {
                    Text(
                        text = "إعدادات التطبيق والاستوديو",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = "تخصيص الجودة • الذكاء الاصطناعي • المزامنة السحابية",
                        style = MaterialTheme.typography.bodySmall,
                        color = StudioCyan
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionHeader(
    title: String,
    icon: ImageVector,
    iconTint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(16.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = StudioTextPrimary
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = StudioCyan, modifier = Modifier.size(18.dp))
            Column {
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = StudioTextPrimary)
                Text(subtitle, fontSize = 9.sp, color = StudioTextMuted)
            }
        }

        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = StudioCyan
            )
        )
    }
}
