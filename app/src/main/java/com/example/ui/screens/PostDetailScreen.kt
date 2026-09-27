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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.PrecisionManufacturing
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.ui.components.formatShortDate
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: ServicePostEntity,
    currentUser: UserEntity?,
    onBack: () -> Unit,
    onOpenChat: (postId: String, otherUserId: String, otherUserName: String) -> Unit,
    onCreateTransaction: (
        postId: String,
        serviceName: String,
        categoryCode: String,
        customerId: String,
        customerName: String,
        customerPhone: String,
        providerId: String,
        providerName: String,
        providerPhone: String,
        location: String,
        workDate: String,
        volume: Double,
        unit: String,
        unitPrice: Long,
        terms: String
    ) -> Unit
) {
    var showBookingDialog by remember { mutableStateOf(false) }

    // Booking parameters
    var bookingVolume by remember { mutableDoubleStateOf(post.areaOrQuantity) }
    var bookingUnitPrice by remember { mutableLongStateOf(post.expectedPrice) }
    var bookingDate by remember { mutableStateOf(post.executionDate.ifEmpty { "05/10/2026" }) }
    var bookingTerms by remember {
        mutableStateOf("Thực hiện đúng kỹ thuật, kiểm tra đạt 100% trước khi bàn giao. Phí dịch vụ sàn 3.000 ₫ do bên cung cấp chịu khi hoàn tất.")
    }

    val isAuthor = currentUser?.id == post.authorId

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết bài đăng", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Bottom Action Bar with CTA
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { onOpenChat(post.id, post.authorId, post.authorName) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Nhắn tin", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                    }

                    Button(
                        onClick = { showBookingDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (post.postType == PostType.DEMAND) "GỬI BÁO GIÁ" else "ĐẶT DỊCH VỤ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("post_detail_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Type & Date
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (post.postType == PostType.DEMAND) AgriAmberLight else AgriGreenLight
                    ) {
                        Text(
                            text = if (post.postType == PostType.DEMAND) "🌾 CẦN DỊCH VỤ (NÔNG DÂN)" else "🚜 CUNG CẤP DỊCH VỤ (CHỦ MÁY / DRONE)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (post.postType == PostType.DEMAND) Color(0xFFB78103) else AgriGreenPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }

                    Text(
                        text = "Đăng ngày: ${formatShortDate(post.createdAt)}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Title
            item {
                Text(
                    text = post.title,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 26.sp
                )
            }

            // Price & Quantity Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = AgriGreenLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Đơn giá", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = formatVnd(post.expectedPrice),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AgriGreenPrimary
                            )
                            Text(post.priceMethod, fontSize = 11.sp, color = AgriGreenSecondary)
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Quy mô / Diện tích", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                text = "${post.areaOrQuantity} ${post.unit}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text("Tổng ước tính: ${formatVnd((post.areaOrQuantity * post.expectedPrice).toLong())}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Author Profile Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Thông tin người đăng", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(AgriGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(post.authorName.take(1), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(post.authorName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.Verified, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                                }
                                Text("Số điện thoại: ${post.authorPhone}", fontSize = 12.sp, color = AgriGreenSecondary, fontWeight = FontWeight.Medium)
                                Text("Đánh giá: 4.9 ⭐ (86 giao dịch thành công)", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            // Location & Date Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("Địa điểm thực hiện", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(post.fullAddress, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Schedule, contentDescription = null, tint = AgriAmberAccent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text("Thời gian thực hiện", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(post.executionDate.ifEmpty { "Thỏa thuận theo lịch bà con" }, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Detailed Description & Requirements
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Nội dung chi tiết", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(post.description, fontSize = 13.sp, lineHeight = 20.sp, color = MaterialTheme.colorScheme.onSurface)

                        if (post.requirements.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Yêu cầu công việc:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(post.requirements, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        if (post.dailyCapacity.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Công suất máy: ${post.dailyCapacity} (${post.machineryCount} thiết bị)", fontSize = 12.sp, color = AgriGreenPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Fee Guarantee Badge
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF1F8E9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Giao dịch an toàn & Minh bạch", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AgriGreenPrimary)
                            Text("Phí nền tảng chỉ 3.000 ₫/hợp đồng thu sau khi hoàn tất. Nông dân và chủ máy xác nhận trực tiếp.", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }

    // Direct Booking / Contract Agreement Dialog (Creates Transaction)
    if (showBookingDialog) {
        val totalAmount = (bookingVolume * bookingUnitPrice).toLong()

        // Determine Customer & Provider
        val isPostDemand = post.postType == PostType.DEMAND
        val customerId = if (isPostDemand) post.authorId else (currentUser?.id ?: "USR_TEMP_CUST")
        val customerName = if (isPostDemand) post.authorName else (currentUser?.fullName ?: "Nông dân")
        val customerPhone = if (isPostDemand) post.authorPhone else (currentUser?.phone ?: "0912345678")

        val providerId = if (isPostDemand) (currentUser?.id ?: "USR_TEMP_PROV") else post.authorId
        val providerName = if (isPostDemand) (currentUser?.fullName ?: "Nhà cung cấp") else post.authorName
        val providerPhone = if (isPostDemand) (currentUser?.phone ?: "0987654321") else post.authorPhone

        AlertDialog(
            onDismissRequest = { showBookingDialog = false },
            title = {
                Text(
                    text = "TẠO HỢP ĐỒNG DỊCH VỤ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AgriGreenPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Dịch vụ: ${post.title}", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Khối lượng: ${bookingVolume} ${post.unit}", fontSize = 12.sp)
                    Text("Đơn giá: ${formatVnd(bookingUnitPrice)} / ${post.unit}", fontSize = 12.sp)

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = AgriGreenLight,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("TỔNG GIÁ TRỊ HỢP ĐỒNG:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(formatVnd(totalAmount), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenPrimary)
                            Text("Phí nền tảng cố định: 3.000 ₫ (Chủ máy chịu)", fontSize = 11.sp, color = AgriAmberAccent)
                        }
                    }

                    OutlinedTextField(
                        value = bookingDate,
                        onValueChange = { bookingDate = it },
                        label = { Text("Ngày thực hiện công việc") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = bookingTerms,
                        onValueChange = { bookingTerms = it },
                        label = { Text("Điều khoản & yêu cầu cam kết") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCreateTransaction(
                            post.id,
                            post.title,
                            post.categoryCode,
                            customerId,
                            customerName,
                            customerPhone,
                            providerId,
                            providerName,
                            providerPhone,
                            post.fullAddress,
                            bookingDate,
                            bookingVolume,
                            post.unit,
                            bookingUnitPrice,
                            bookingTerms
                        )
                        showBookingDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("XÁC NHẬN TẠO HỢP ĐỒNG")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBookingDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
