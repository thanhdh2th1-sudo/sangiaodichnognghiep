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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TransactionStatus
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgriTopBar(
    currentUser: UserEntity?,
    unreadNotifCount: Int = 0,
    onRoleSwitchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(AgriGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Flight,
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SÀN NÔNG NGHIỆP",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = AgriGreenPrimary,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = when (currentUser?.role) {
                            UserRole.CUSTOMER -> "🌾 Nông dân / Người thuê"
                            UserRole.PROVIDER -> "🚜 Nhà cung cấp dịch vụ"
                            UserRole.ADMIN -> "🛡️ Quản trị viên hệ thống"
                            else -> "Nền tảng kết nối trực tiếp"
                        },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        actions = {
            // Quick role switcher button to test seamlessly
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = AgriGreenLight,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clickable { onRoleSwitchClick() }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Đổi vai trò",
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Đổi vai trò",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AgriGreenPrimary
                    )
                }
            }

            // Notification Bell with badge
            IconButton(onClick = onNotificationClick) {
                BadgedBox(
                    badge = {
                        if (unreadNotifCount > 0) {
                            Badge(containerColor = MaterialTheme.colorScheme.error) {
                                Text("$unreadNotifCount", color = Color.White)
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Thông báo",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}

@Composable
fun AgriBottomNav(
    currentScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple("home", "Trang chủ", Icons.Default.Home),
            Triple("search", "Tìm kiếm", Icons.Default.Search),
            Triple("create_post", "Đăng bài", Icons.Default.AddCircle),
            Triple("transactions", "Giao dịch", Icons.Default.Assignment),
            Triple("profile", "Cá nhân", Icons.Default.Person)
        )

        items.forEach { (route, label, icon) ->
            val isSelected = currentScreen == route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(route) },
                icon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AgriGreenPrimary,
                    selectedTextColor = AgriGreenPrimary,
                    indicatorColor = AgriGreenLight,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun StatusBadgeView(status: TransactionStatus) {
    val (bgColor, textColor, label) = when (status) {
        TransactionStatus.PENDING_CONFIRMATION -> Triple(AgriAmberLight, Color(0xFFB78103), "Chờ xác nhận")
        TransactionStatus.CONFIRMED -> Triple(AgriBlueLight, Color(0xFF0277BD), "Đã xác nhận")
        TransactionStatus.IN_PROGRESS -> Triple(Color(0xFFE8EAF6), Color(0xFF283593), "Đang thực hiện")
        TransactionStatus.AWAITING_COMPLETION_CONFIRM -> Triple(AgriAmberLight, Color(0xFFE65100), "Chờ xác nhận hoàn tất")
        TransactionStatus.COMPLETED -> Triple(AgriGreenLight, Color(0xFF2E7D32), "Đã hoàn tất (Phí 3k)")
        TransactionStatus.CANCELLED -> Triple(Color(0xFFFFEBEE), Color(0xFFC62828), "Đã hủy")
        TransactionStatus.DISPUTED -> Triple(Color(0xFFFFEBEE), Color(0xFFD50000), "Có tranh chấp")
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun PostCard(
    post: ServicePostEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Post Type Badge + Category Icon + Date
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Post Type Badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (post.postType == PostType.DEMAND) AgriAmberLight else AgriGreenLight
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (post.postType == PostType.DEMAND) "🌾 CẦN DỊCH VỤ" else "🚜 CUNG CẤP DỊCH VỤ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (post.postType == PostType.DEMAND) Color(0xFFB78103) else AgriGreenPrimary
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(13.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = post.executionDate.ifEmpty { formatShortDate(post.createdAt) },
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Post Title
            Text(
                text = post.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Short Description
            Text(
                text = post.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Key info chips: Location & Area/Quantity & Price
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = AgriGreenSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${post.district}, ${post.province}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Volume / Area Tag
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "${post.areaOrQuantity} ${post.unit}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom row: Author + Price & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Author row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(AgriGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = post.authorName.take(1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.authorName,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.widthIn(max = 120.dp)
                    )
                }

                // Price display
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = if (post.expectedPrice > 0) formatVnd(post.expectedPrice) else "Thỏa thuận",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AgriGreenPrimary
                    )
                    if (post.priceMethod.isNotEmpty()) {
                        Text(
                            text = post.priceMethod,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
