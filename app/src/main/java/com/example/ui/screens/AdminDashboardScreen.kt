package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TopUpRequestEntity
import com.example.data.local.entity.TopUpStatus
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionStatus
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.ui.components.formatDate
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    users: List<UserEntity>,
    posts: List<ServicePostEntity>,
    transactions: List<TransactionEntity>,
    topUpRequests: List<TopUpRequestEntity>,
    categories: List<ServiceCategoryEntity>,
    onApproveTopUp: (String) -> Unit,
    onRejectTopUp: (String, String) -> Unit,
    onAddCategory: (name: String, code: String, desc: String, unit: String) -> Unit,
    onVerifyUser: (String, Boolean) -> Unit,
    onBack: () -> Unit
) {
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var newCatName by remember { mutableStateOf("") }
    var newCatCode by remember { mutableStateOf("") }
    var newCatDesc by remember { mutableStateOf("") }
    var newCatUnit by remember { mutableStateOf("ha") }

    // Analytics calculations
    val totalUsers = users.size
    val totalProviders = users.count { it.role == UserRole.PROVIDER || it.role == UserRole.BOTH }
    val totalCustomers = users.count { it.role == UserRole.CUSTOMER }
    val completedTxs = transactions.count { it.status == TransactionStatus.COMPLETED }
    val inProgressTxs = transactions.count { it.status == TransactionStatus.IN_PROGRESS }
    val totalFeeCollected = completedTxs * 3000L
    val pendingTopUps = topUpRequests.filter { it.status == TopUpStatus.PENDING }
    val totalPendingTopUpAmount = pendingTopUps.sumOf { it.amount }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bảng điều khiển Quản trị (Admin)", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddCategoryDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm danh mục", tint = AgriGreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        modifier = Modifier.testTag("admin_dashboard_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 40.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Analytics Grid
            item {
                Text("Thống kê toàn sàn", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminStatCard(
                        title = "Tổng người dùng",
                        value = "$totalUsers tài khoản",
                        sub = "$totalCustomers Nông dân • $totalProviders Chủ máy",
                        icon = Icons.Default.People,
                        color = AgriGreenSecondary,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Phí sàn thu được",
                        value = formatVnd(totalFeeCollected),
                        sub = "Từ $completedTxs giao dịch hoàn tất",
                        icon = Icons.Default.MonetizationOn,
                        color = AgriGreenPrimary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AdminStatCard(
                        title = "Hợp đồng hoạt động",
                        value = "$inProgressTxs đang chạy",
                        sub = "Tổng: ${transactions.size} hợp đồng",
                        icon = Icons.Default.Receipt,
                        color = AgriAmberAccent,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Nạp tiền chờ duyệt",
                        value = "${pendingTopUps.size} yêu cầu",
                        sub = "Tổng: ${formatVnd(totalPendingTopUpAmount)}",
                        icon = Icons.Default.MonetizationOn,
                        color = Color(0xFFD84315),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Pending Top-Up Approvals
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Duyệt nạp tiền tài khoản", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Surface(shape = RoundedCornerShape(8.dp), color = AgriAmberLight) {
                        Text(
                            "${pendingTopUps.size} đang chờ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB78103),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            if (pendingTopUps.isEmpty()) {
                item {
                    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Không có yêu cầu nạp tiền nào đang chờ duyệt.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(pendingTopUps, key = { it.id }) { req ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(req.id, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(formatVnd(req.amount), fontWeight = FontWeight.ExtraBold, color = AgriGreenPrimary, fontSize = 15.sp)
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("Nhà cung cấp: ${req.userName} (${req.userPhone})", fontSize = 12.sp)
                            Text("Kênh nhận: ${req.bankName} - ${req.accountNumber} (${req.accountHolder})", fontSize = 11.sp, color = AgriGreenPrimary, fontWeight = FontWeight.Medium)
                            Text("Nội dung CK: ${req.transferContent}", fontSize = 11.sp, color = AgriGreenSecondary, fontWeight = FontWeight.SemiBold)
                            Text("Thời gian: ${formatDate(req.createdAt)}", fontSize = 10.sp, color = Color.Gray)

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { onRejectTopUp(req.id, "Sai nội dung CK hoặc chưa nhận được tiền") },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Từ chối", fontSize = 11.sp)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = { onApproveTopUp(req.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.height(34.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Duyệt nạp (+${formatVnd(req.amount)})", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // User Verification Management
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text("Quản lý xác minh nhà cung cấp", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            items(users.filter { it.role == UserRole.PROVIDER || it.role == UserRole.BOTH }) { user ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (user.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                                }
                            }
                            Text("${user.businessName} • ${user.phone}", fontSize = 11.sp, color = Color.Gray)
                        }

                        Button(
                            onClick = { onVerifyUser(user.id, !user.isVerified) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user.isVerified) Color.LightGray else AgriGreenPrimary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = if (user.isVerified) "Hủy xác minh" else "Xác minh ngay",
                                fontSize = 11.sp,
                                color = if (user.isVerified) Color.Black else Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Category Dialog (Section XXIV - Admin dynamic category extension)
    if (showAddCategoryDialog) {
        AlertDialog(
            onDismissRequest = { showAddCategoryDialog = false },
            title = { Text("Thêm danh mục dịch vụ mới", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newCatName,
                        onValueChange = {
                            newCatName = it
                            if (newCatCode.isEmpty()) newCatCode = "CAT_" + it.uppercase().replace(" ", "_")
                        },
                        label = { Text("Tên dịch vụ (VD: Cuộn rơm, Tỉa cành)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCatCode,
                        onValueChange = { newCatCode = it },
                        label = { Text("Mã dịch vụ (VD: CAT_CUON_ROM)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCatDesc,
                        onValueChange = { newCatDesc = it },
                        label = { Text("Mô tả dịch vụ") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newCatUnit,
                        onValueChange = { newCatUnit = it },
                        label = { Text("Đơn vị tính chuẩn (ha, tấn, cuộn...)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newCatName.isNotBlank()) {
                            onAddCategory(newCatName.trim(), newCatCode.trim(), newCatDesc.trim(), newCatUnit.trim())
                            showAddCategoryDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Lưu danh mục")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCategoryDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    sub: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
            Spacer(modifier = Modifier.height(2.dp))
            Text(sub, fontSize = 9.sp, color = Color.Gray, maxLines = 1)
        }
    }
}
