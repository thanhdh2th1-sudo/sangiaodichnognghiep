package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.data.local.entity.WalletEntity
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@Composable
fun ProfileScreen(
    currentUser: UserEntity?,
    allUsers: List<UserEntity>,
    userWallet: WalletEntity?,
    onSwitchUser: (String) -> Unit,
    onNavigateWallet: () -> Unit,
    onNavigateTransactions: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onNavigateOnboarding: () -> Unit
) {
    var showSwitchAccountDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // User Profile Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(AgriGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentUser?.fullName?.take(1) ?: "U",
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = currentUser?.fullName ?: "Người dùng",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                if (currentUser?.isVerified == true) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Đã xác minh",
                                        tint = AgriGreenPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "SĐT: ${currentUser?.phone}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "Khu vực: ${currentUser?.district}, ${currentUser?.province}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role Badge & Switch Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (currentUser?.role) {
                                UserRole.CUSTOMER -> AgriAmberLight
                                UserRole.PROVIDER -> AgriGreenLight
                                UserRole.ADMIN -> AgriBlueLight
                                else -> AgriGreenLight
                            }
                        ) {
                            Text(
                                text = when (currentUser?.role) {
                                    UserRole.CUSTOMER -> "🌾 Bên cần dịch vụ (Nông dân / HTX)"
                                    UserRole.PROVIDER -> "🚜 Bên cung cấp dịch vụ (Chủ máy / Drone)"
                                    UserRole.ADMIN -> "🛡️ Quản trị viên hệ thống"
                                    else -> "Tài khoản đa năng"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (currentUser?.role) {
                                    UserRole.CUSTOMER -> Color(0xFFB78103)
                                    UserRole.PROVIDER -> AgriGreenPrimary
                                    UserRole.ADMIN -> AgriGreenSecondary
                                    else -> AgriGreenPrimary
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Button(
                            onClick = { showSwitchAccountDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Đổi vai trò", fontSize = 11.sp)
                        }
                    }

                    // Provider stats details
                    if (currentUser?.role == UserRole.PROVIDER || currentUser?.role == UserRole.BOTH) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Năng lực & Thiết bị:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(currentUser.machineryDetails, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Công suất: ${currentUser.dailyCapacity}", fontSize = 11.sp, color = AgriGreenPrimary, fontWeight = FontWeight.SemiBold)
                                Text("Phạm vi: ${currentUser.serviceArea}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        // Wallet Quick Action (For Providers)
        if (currentUser?.role == UserRole.PROVIDER || currentUser?.role == UserRole.BOTH) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AgriGreenLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateWallet() }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Ví tài khoản", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(formatVnd(userWallet?.balance ?: 0L), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenPrimary)
                            }
                        }
                        Button(
                            onClick = onNavigateWallet,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Nạp tiền")
                        }
                    }
                }
            }
        }

        // Navigation Menu Items
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Default.AccountBalanceWallet,
                        title = "Ví tài khoản & Nạp tiền",
                        subtitle = "Quản lý số dư, phí 3.000 ₫ & nạp tiền qua ngân hàng",
                        onClick = onNavigateWallet
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.Assignment,
                        title = "Hợp đồng & Giao dịch của tôi",
                        subtitle = "Theo dõi tiến độ, xác nhận hoàn tất & đánh giá",
                        onClick = onNavigateTransactions
                    )
                    ProfileMenuItem(
                        icon = Icons.Default.AdminPanelSettings,
                        title = "Bảng điều khiển Quản Trị Viên (Admin)",
                        subtitle = "Duyệt nạp tiền, quản trị người dùng, danh mục & thống kê phí",
                        onClick = onNavigateAdmin
                    )
                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Filled.Help,
                        title = "Giới thiệu sàn & Quy chế giao dịch",
                        subtitle = "Xem lại 4 bước giới thiệu & hướng dẫn sử dụng",
                        onClick = onNavigateOnboarding
                    )
                }
            }
        }
    }

    // Role switcher dialog (demonstrates the 3 user groups)
    if (showSwitchAccountDialog) {
        AlertDialog(
            onDismissRequest = { showSwitchAccountDialog = false },
            title = { Text("Chọn vai trò trải nghiệm", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        "Ứng dụng hỗ trợ trải nghiệm đầy đủ 3 nhóm tài khoản trên cùng thiết bị:",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    allUsers.forEach { user ->
                        val isCurrent = user.id == currentUser?.id
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) AgriGreenLight else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(user.id)
                                    showSwitchAccountDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (user.role) {
                                        UserRole.CUSTOMER -> "🌾"
                                        UserRole.PROVIDER -> "🚜"
                                        UserRole.ADMIN -> "🛡️"
                                        else -> "👤"
                                    },
                                    fontSize = 20.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        when (user.role) {
                                            UserRole.CUSTOMER -> "Bên cần dịch vụ (Nông dân / HTX)"
                                            UserRole.PROVIDER -> "Bên cung cấp (Chủ drone, máy gặt)"
                                            UserRole.ADMIN -> "Quản trị viên toàn hệ thống"
                                            else -> "Người dùng"
                                        },
                                        fontSize = 11.sp,
                                        color = AgriGreenPrimary
                                    )
                                }
                                if (isCurrent) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSwitchAccountDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(18.dp))
    }
}
