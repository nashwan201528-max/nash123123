package com.example.ui.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FilterPreset
import com.example.data.model.FilterRepository
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterThumbnailRow(
    selectedFilterId: String,
    onFilterSelected: (FilterPreset) -> Unit,
    filterIntensity: Float = 1.0f,
    onIntensityChange: ((Float) -> Unit)? = null,
    onSmartMatchClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("الكل") }
    var viewingFilterDetail by remember { mutableStateOf<FilterPreset?>(null) }
    var showFullCatalogSheet by remember { mutableStateOf(false) }

    val filteredList = remember(selectedCategory) {
        if (selectedCategory == "الكل") {
            FilterRepository.presets
        } else {
            FilterRepository.presets.filter { it.categoryAr == selectedCategory }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(StudioDarkElevated)
            .padding(vertical = 8.dp)
    ) {
        // Header with Smart Match and Full Catalog actions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "كتالوج التأثيرات والفلاتر (20 فلتر)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = StudioTextPrimary
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                if (onSmartMatchClick != null) {
                    Surface(
                        color = StudioMagenta.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioMagenta),
                        modifier = Modifier
                            .clickable { onSmartMatchClick() }
                            .testTag("smart_match_filter_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "فلتر ذكي",
                                tint = StudioMagenta,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "تطبيق ذكي للمشهد",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = StudioMagenta
                            )
                        }
                    }
                }

                Surface(
                    color = StudioCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioCyan.copy(alpha = 0.5f)),
                    modifier = Modifier.clickable { showFullCatalogSheet = true }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = "دليل الفئات",
                            tint = StudioCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "دليل الفئات",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = StudioCyan
                        )
                    }
                }
            }
        }

        // Category Chips
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(FilterRepository.categories) { category ->
                val isCatSelected = category == selectedCategory
                FilterChip(
                    selected = isCatSelected,
                    onClick = { selectedCategory = category },
                    label = {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = StudioCyan.copy(alpha = 0.25f),
                        selectedLabelColor = StudioCyan,
                        containerColor = StudioSurfaceVariant,
                        labelColor = StudioTextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isCatSelected) StudioCyan else StudioSurfaceBorder,
                        enabled = true,
                        selected = isCatSelected
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Filters Horizontal Cards
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredList, key = { it.id }) { preset ->
                val isSelected = preset.id == selectedFilterId

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(86.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) StudioSurfaceVariant else StudioSurface)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) StudioCyan else StudioSurfaceBorder,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onFilterSelected(preset) }
                        .padding(6.dp)
                        .testTag("filter_item_${preset.id}")
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                brush = Brush.linearGradient(
                                    when (preset.categoryAr) {
                                        "سينمائي" -> listOf(Color(0xFF0F172A), Color(0xFF0284C7))
                                        "عتيق" -> listOf(Color(0xFF78350F), Color(0xFFD97706))
                                        "مستقبلي" -> listOf(Color(0xFF4C1D95), Color(0xFFEC4899))
                                        else -> listOf(Color(0xFF065F46), Color(0xFF10B981))
                                    }
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = preset.iconEmoji,
                            fontSize = 26.sp
                        )

                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(2.dp)
                                    .size(18.dp)
                                    .background(StudioCyan, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "محدد",
                                    tint = StudioDark,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewingFilterDetail = preset },
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "تفاصيل الفلتر",
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = preset.nameAr,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) StudioCyan else StudioTextPrimary,
                        maxLines = 1,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = preset.categoryAr,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        color = StudioTextMuted
                    )
                }
            }
        }

        // Intensity Slider when a filter is active
        if (onIntensityChange != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🎛️ شدة تأثير الفلتر (Intensity):",
                        fontSize = 11.sp,
                        color = StudioTextPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${(filterIntensity * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = StudioCyan
                    )
                }
                Slider(
                    value = filterIntensity,
                    onValueChange = onIntensityChange,
                    valueRange = 0.0f..1.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = StudioCyan,
                        activeTrackColor = StudioCyan,
                        inactiveTrackColor = StudioSurfaceVariant
                    ),
                    modifier = Modifier
                        .height(28.dp)
                        .testTag("filter_intensity_slider")
                )
            }
        }
    }

    // Full Categories & Effects Catalog BottomSheet
    if (showFullCatalogSheet) {
        val catalogSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showFullCatalogSheet = false },
            sheetState = catalogSheetState,
            containerColor = StudioDarkElevated,
            contentColor = StudioTextPrimary,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "كتالوج التأثيرات والفلاتر (20 تأثيراً)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "فئات عتيقة، سينمائية، مستقبلية وطبيعية مع تحكم في الشدة",
                            fontSize = 12.sp,
                            color = StudioCyan
                        )
                    }

                    IconButton(onClick = { showFullCatalogSheet = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = StudioTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val grouped = FilterRepository.presets.groupBy { it.categoryAr }
                    grouped.forEach { (categoryName, filterList) ->
                        item {
                            Surface(
                                color = StudioSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "فئة: $categoryName (${filterList.size} تأثيرات)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (categoryName) {
                                        "سينمائي" -> StudioCyan
                                        "عتيق" -> Color(0xFFF59E0B)
                                        "مستقبلي" -> StudioMagenta
                                        else -> Color(0xFF10B981)
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }

                        items(filterList) { preset ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onFilterSelected(preset)
                                        showFullCatalogSheet = false
                                    },
                                colors = CardDefaults.cardColors(containerColor = StudioSurface),
                                shape = RoundedCornerShape(12.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (preset.id == selectedFilterId) StudioCyan else StudioSurfaceBorder
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(text = preset.iconEmoji, fontSize = 28.sp)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = preset.nameAr,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (preset.id == selectedFilterId) StudioCyan else StudioTextPrimary
                                            )
                                            if (preset.id == selectedFilterId) {
                                                Text(
                                                    text = "✓ مطبق حالياً",
                                                    fontSize = 10.sp,
                                                    color = StudioCyan,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Text(
                                            text = preset.descriptionAr,
                                            fontSize = 11.sp,
                                            color = StudioTextMuted,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Single Filter Detail Dialog
    viewingFilterDetail?.let { detailPreset ->
        AlertDialog(
            onDismissRequest = { viewingFilterDetail = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = detailPreset.iconEmoji, fontSize = 24.sp)
                    Column {
                        Text(
                            text = detailPreset.nameAr,
                            fontWeight = FontWeight.Bold,
                            color = StudioTextPrimary
                        )
                        Text(
                            text = "فئة: ${detailPreset.categoryAr}",
                            fontSize = 12.sp,
                            color = StudioCyan
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "طريقة تأثير الفلتر على الصورة أو الفيديو:",
                        fontWeight = FontWeight.SemiBold,
                        color = StudioTextPrimary
                    )
                    Text(
                        text = detailPreset.descriptionAr,
                        style = MaterialTheme.typography.bodyMedium,
                        color = StudioTextMuted,
                        lineHeight = 22.sp
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onFilterSelected(detailPreset)
                        viewingFilterDetail = null
                    }
                ) {
                    Text("تطبيق هذا الفلتر", color = StudioCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewingFilterDetail = null }) {
                    Text("إغلاق", color = StudioTextMuted)
                }
            },
            containerColor = StudioSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
