package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.ui.components.PostCard
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@Composable
fun SearchScreen(
    categories: List<ServiceCategoryEntity>,
    posts: List<ServicePostEntity>,
    onSelectPost: (ServicePostEntity) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryCode by remember { mutableStateOf<String?>(null) }
    var selectedProvince by remember { mutableStateOf<String?>(null) }
    var maxRadiusKm by remember { mutableFloatStateOf(30f) }
    var isMapViewMode by remember { mutableStateOf(false) }

    val provinces = listOf("Tất cả", "An Giang", "Đồng Tháp", "Cần Thơ", "Kiên Giang", "Long An", "Tiền Giang")

    val searchResults = remember(posts, searchQuery, selectedCategoryCode, selectedProvince, maxRadiusKm) {
        posts.filter { post ->
            val matchQuery = searchQuery.isBlank() ||
                    post.title.contains(searchQuery, ignoreCase = true) ||
                    post.description.contains(searchQuery, ignoreCase = true) ||
                    post.categoryName.contains(searchQuery, ignoreCase = true) ||
                    post.authorName.contains(searchQuery, ignoreCase = true)

            val matchCat = selectedCategoryCode == null || post.categoryCode == selectedCategoryCode
            val matchProv = selectedProvince == null || selectedProvince == "Tất cả" || post.province.contains(selectedProvince!!, ignoreCase = true)
            val matchDist = post.distanceKm <= maxRadiusKm

            matchQuery && matchCat && matchProv && matchDist
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("search_screen")
    ) {
        // Search Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Tìm theo tên dịch vụ, máy móc, vị trí...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Tìm kiếm", tint = AgriGreenPrimary)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Xóa")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                // View Mode Toggle (Danh sách vs Bản đồ radar gần tôi)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FilterChip(
                            selected = selectedCategoryCode == null,
                            onClick = { selectedCategoryCode = null },
                            label = { Text("Tất cả", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AgriGreenPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                        categories.forEach { cat ->
                            FilterChip(
                                selected = selectedCategoryCode == cat.code,
                                onClick = { selectedCategoryCode = if (selectedCategoryCode == cat.code) null else cat.code },
                                label = { Text(cat.name, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AgriGreenPrimary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Map View Switcher Button
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isMapViewMode) AgriGreenPrimary else AgriGreenLight,
                        modifier = Modifier.clickable { isMapViewMode = !isMapViewMode }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isMapViewMode) Icons.Default.ViewList else Icons.Default.Map,
                                contentDescription = "Đổi chế độ xem",
                                tint = if (isMapViewMode) Color.White else AgriGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isMapViewMode) "Danh sách" else "Bản đồ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isMapViewMode) Color.White else AgriGreenPrimary
                            )
                        }
                    }
                }

                // Province Filter chips
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    provinces.forEach { prov ->
                        val isSelected = (selectedProvince == null && prov == "Tất cả") || selectedProvince == prov
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) AgriBlueLight else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.clickable {
                                selectedProvince = if (prov == "Tất cả") null else prov
                            }
                        ) {
                            Text(
                                text = "📍 $prov",
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) AgriGreenSecondary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Radius Slider
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NearMe, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Bán kính tìm kiếm: ${maxRadiusKm.toInt()} km",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Slider(
                        value = maxRadiusKm,
                        onValueChange = { maxRadiusKm = it },
                        valueRange = 5f..50f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = AgriGreenPrimary,
                            activeTrackColor = AgriGreenPrimary
                        ),
                        modifier = Modifier
                            .width(140.dp)
                            .height(24.dp)
                    )
                }
            }
        }

        // Search Results or Interactive Map View
        if (isMapViewMode) {
            // Interactive Agriculture Service Map & Radar Mode
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Simulated Satellite / Agri Grid Map Canvas
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Radar circle indicator
                            Box(
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(CircleShape)
                                    .background(AgriGreenPrimary.copy(alpha = 0.08f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(130.dp)
                                        .clip(CircleShape)
                                        .background(AgriGreenPrimary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // User location center
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(AgriGreenPrimary),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Bản đồ dịch vụ nông nghiệp",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = AgriGreenPrimary
                            )
                            Text(
                                text = "Đang quét trong bán kính ${maxRadiusKm.toInt()} km quanh bạn. Tìm thấy ${searchResults.size} dịch vụ khả dụng.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 24.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal scroll of nearby providers on map
                Text(
                    text = "Dịch vụ gần bạn nhất (${searchResults.size})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(searchResults) { post ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectPost(post) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(post.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
                                    Text("Cách bạn ~${post.distanceKm} km • ${post.district}", fontSize = 11.sp, color = AgriGreenSecondary)
                                    Text(formatVnd(post.expectedPrice), fontWeight = FontWeight.Bold, color = AgriGreenPrimary, fontSize = 13.sp)
                                }
                                Button(
                                    onClick = { onSelectPost(post) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Text("Xem", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Standard List View
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "Tìm thấy ${searchResults.size} kết quả phù hợp",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (searchResults.isEmpty()) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🔍", fontSize = 36.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text("Không có dịch vụ nào trong khu vực này", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Hãy tăng bán kính quét hoặc bỏ bớt tiêu chí lọc.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                } else {
                    items(searchResults, key = { it.id }) { post ->
                        PostCard(post = post, onClick = { onSelectPost(post) })
                    }
                }
            }
        }
    }
}
