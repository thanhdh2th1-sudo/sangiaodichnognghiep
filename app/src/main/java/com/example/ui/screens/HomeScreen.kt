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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.data.local.entity.WalletEntity
import com.example.ui.components.PostCard
import com.example.ui.components.formatVnd
import com.example.ui.components.getCategoryIcon
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@Composable
fun HomeScreen(
    currentUser: UserEntity?,
    userWallet: WalletEntity?,
    categories: List<ServiceCategoryEntity>,
    posts: List<ServicePostEntity>,
    onSelectPost: (ServicePostEntity) -> Unit,
    onCategoryClick: (ServiceCategoryEntity) -> Unit,
    onNavigateSearch: () -> Unit,
    onNavigateCreatePost: () -> Unit,
    onNavigateWallet: () -> Unit,
    onNavigateTransactions: () -> Unit
) {
    var selectedFilterTab by remember { mutableStateOf<PostType?>(null) }
    var localSearchText by remember { mutableStateOf("") }

    val filteredPosts = remember(posts, selectedFilterTab, localSearchText) {
        posts.filter { post ->
            val matchType = selectedFilterTab == null || post.postType == selectedFilterTab
            val matchText = localSearchText.isBlank() ||
                    post.title.contains(localSearchText, ignoreCase = true) ||
                    post.categoryName.contains(localSearchText, ignoreCase = true) ||
                    post.province.contains(localSearchText, ignoreCase = true)
            matchType && matchText
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // 1. Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(AgriGreenPrimary, Color(0xFF2E7D32), AgriGreenSecondary)
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Nông nghiệp 4.0",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        // Direct post button shortcut
                        Button(
                            onClick = onNavigateCreatePost,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriAmberAccent),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Đăng tin", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "SÀN DỊCH VỤ NÔNG NGHIỆP",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Kết nối trực tiếp Nông dân ↔ Chủ máy gặt, máy cày & Drone xịt thuốc. Giao dịch an toàn, thu phí chỉ 3.000 ₫/hợp đồng.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // 2. Role Highlight Card (For Providers: show wallet balance & top up quick link)
        if (currentUser?.role == UserRole.PROVIDER || currentUser?.role == UserRole.BOTH) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AgriGreenLight)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(AgriGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = "Ví tài khoản",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Số dư ví cung cấp:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatVnd(userWallet?.balance ?: 0L),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AgriGreenPrimary
                                )
                            }
                        }

                        Button(
                            onClick = onNavigateWallet,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Nạp tiền", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. Search Bar
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = localSearchText,
                    onValueChange = { localSearchText = it },
                    placeholder = {
                        Text(
                            "Bạn cần tìm dịch vụ gì? (Drone, máy gặt, cày...)",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm",
                            tint = AgriGreenPrimary
                        )
                    },
                    trailingIcon = {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = AgriGreenLight,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { onNavigateSearch() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = "Bộ lọc nâng cao",
                                tint = AgriGreenPrimary,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedBorderColor = AgriGreenPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input")
                )
            }
        }

        // 4. Categories Section Header & Grid
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Danh mục dịch vụ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${categories.size} dịch vụ",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriGreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable Category Icons
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(categories) { category ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .width(74.dp)
                                .clickable { onCategoryClick(category) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(AgriGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = getCategoryIcon(category.code),
                                    contentDescription = category.name,
                                    tint = AgriGreenPrimary,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = category.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // 5. Post Filter Tabs: [Tất cả] [Cần dịch vụ] [Cung cấp dịch vụ]
        item {
            Column(modifier = Modifier.padding(top = 18.dp, bottom = 8.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Bài đăng mới nhất",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "${filteredPosts.size} bài đăng",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedFilterTab == null,
                        onClick = { selectedFilterTab = null },
                        label = { Text("Tất cả bài đăng", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedFilterTab == PostType.DEMAND,
                        onClick = { selectedFilterTab = PostType.DEMAND },
                        label = { Text("🌾 Nông dân cần dịch vụ", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriAmberAccent,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedFilterTab == PostType.OFFER,
                        onClick = { selectedFilterTab = PostType.OFFER },
                        label = { Text("🚜 Nhà cung cấp nhận làm", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenSecondary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // 6. Posts List
        if (filteredPosts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🌾", fontSize = 40.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Chưa có bài đăng phù hợp",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hãy thử thay đổi từ khóa hoặc đăng nhu cầu của bạn ngay!",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onNavigateCreatePost,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                        ) {
                            Text("Đăng bài ngay")
                        }
                    }
                }
            }
        } else {
            items(filteredPosts, key = { it.id }) { post ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    PostCard(
                        post = post,
                        onClick = { onSelectPost(post) }
                    )
                }
            }
        }
    }
}
