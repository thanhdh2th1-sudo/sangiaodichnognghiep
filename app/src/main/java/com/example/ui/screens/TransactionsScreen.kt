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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionStatus
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.data.local.entity.WalletEntity
import com.example.ui.components.StatusBadgeView
import com.example.ui.components.formatShortDate
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@Composable
fun TransactionsScreen(
    currentUser: UserEntity?,
    userWallet: WalletEntity?,
    transactions: List<TransactionEntity>,
    onCompleteTransaction: (String) -> Unit,
    onUpdateStatus: (String, TransactionStatus) -> Unit,
    onOpenChat: (transactionId: String, otherUserId: String, otherUserName: String) -> Unit,
    onSubmitReview: (transactionId: String, targetUserId: String, targetUserName: String, rating: Int, comment: String, serviceName: String) -> Unit,
    onNavigateWallet: () -> Unit
) {
    var selectedStatusFilter by remember { mutableStateOf<TransactionStatus?>(null) }
    var txToReview by remember { mutableStateOf<TransactionEntity?>(null) }
    var reviewRating by remember { mutableIntStateOf(5) }
    var reviewComment by remember { mutableStateOf("Dịch vụ làm việc rất nhanh chóng, đúng hẹn, uy tín!") }

    var lowBalanceTxWarning by remember { mutableStateOf<TransactionEntity?>(null) }

    val filteredTransactions = remember(transactions, selectedStatusFilter) {
        if (selectedStatusFilter == null) transactions
        else transactions.filter { it.status == selectedStatusFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("transactions_screen")
    ) {
        // Status Filter Chips
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                Text(
                    text = "Quản lý hợp đồng & Giao dịch",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedStatusFilter == null,
                        onClick = { selectedStatusFilter = null },
                        label = { Text("Tất cả (${transactions.size})", fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AgriGreenPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                    listOf(
                        TransactionStatus.IN_PROGRESS,
                        TransactionStatus.COMPLETED,
                        TransactionStatus.PENDING_CONFIRMATION,
                        TransactionStatus.DISPUTED
                    ).forEach { status ->
                        val count = transactions.count { it.status == status }
                        FilterChip(
                            selected = selectedStatusFilter == status,
                            onClick = { selectedStatusFilter = if (selectedStatusFilter == status) null else status },
                            label = { Text("${status.label} ($count)", fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AgriGreenPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // Transactions List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (filteredTransactions.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("📋", fontSize = 42.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Chưa có hợp đồng giao dịch nào", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Các giao dịch kết nối giữa bạn và đối tác sẽ hiển thị minh bạch tại đây.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(filteredTransactions, key = { it.id }) { tx ->
                    TransactionItemCard(
                        tx = tx,
                        currentUser = currentUser,
                        userWallet = userWallet,
                        onCompleteClick = {
                            // Check provider balance rule before triggering complete
                            val isProvider = currentUser?.id == tx.providerId
                            if (isProvider && (userWallet?.balance ?: 0L) < tx.platformFee) {
                                lowBalanceTxWarning = tx
                            } else {
                                onCompleteTransaction(tx.id)
                            }
                        },
                        onConfirmClick = { onUpdateStatus(tx.id, TransactionStatus.IN_PROGRESS) },
                        onRequestCompleteClick = { onUpdateStatus(tx.id, TransactionStatus.AWAITING_COMPLETION_CONFIRM) },
                        onChatClick = {
                            val otherId = if (currentUser?.id == tx.customerId) tx.providerId else tx.customerId
                            val otherName = if (currentUser?.id == tx.customerId) tx.providerName else tx.customerName
                            onOpenChat(tx.id, otherId, otherName)
                        },
                        onReviewClick = {
                            txToReview = tx
                        }
                    )
                }
            }
        }
    }

    // Low Balance Alert Dialog (Mandated in Section IX)
    if (lowBalanceTxWarning != null) {
        val missing = 3000L - (userWallet?.balance ?: 0L)
        AlertDialog(
            onDismissRequest = { lowBalanceTxWarning = null },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("Số dư ví không đủ 3.000 ₫", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Theo quy định sàn, mỗi giao dịch hoàn tất nhà cung cấp chịu phí cố định 3.000 ₫.",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Số dư ví hiện tại của bạn: ${formatVnd(userWallet?.balance ?: 0L)}\nCần nạp thêm tối thiểu: ${formatVnd(missing)}.",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Vui lòng nạp thêm tiền vào ví để hoàn tất giao dịch này.", fontSize = 12.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        lowBalanceTxWarning = null
                        onNavigateWallet()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Nạp tiền ngay")
                }
            },
            dismissButton = {
                TextButton(onClick = { lowBalanceTxWarning = null }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Review Dialog (Section XIII)
    if (txToReview != null) {
        val tx = txToReview!!
        val isCustomer = currentUser?.id == tx.customerId
        val targetId = if (isCustomer) tx.providerId else tx.customerId
        val targetName = if (isCustomer) tx.providerName else tx.customerName

        AlertDialog(
            onDismissRequest = { txToReview = null },
            title = { Text("Đánh giá dịch vụ", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Đối tác: $targetName", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text("Dịch vụ: ${tx.serviceName}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Số sao đánh giá:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..5).forEach { star ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$star sao",
                                tint = if (star <= reviewRating) AgriAmberAccent else Color.LightGray,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { reviewRating = star }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Nhận xét chi tiết") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmitReview(
                            tx.id,
                            targetId,
                            targetName,
                            reviewRating,
                            reviewComment,
                            tx.serviceName
                        )
                        txToReview = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("Gửi đánh giá")
                }
            },
            dismissButton = {
                TextButton(onClick = { txToReview = null }) {
                    Text("Bỏ qua")
                }
            }
        )
    }
}

@Composable
fun TransactionItemCard(
    tx: TransactionEntity,
    currentUser: UserEntity?,
    userWallet: WalletEntity?,
    onCompleteClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onRequestCompleteClick: () -> Unit,
    onChatClick: () -> Unit,
    onReviewClick: () -> Unit
) {
    val isCustomer = currentUser?.id == tx.customerId
    val isProvider = currentUser?.id == tx.providerId

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Code + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AgriGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Assignment, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(16.dp))
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tx.id,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                StatusBadgeView(status = tx.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Name
            Text(
                text = tx.serviceName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Parties info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AgriGreenSecondary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Bên thuê: ${tx.customerName} (${tx.customerPhone})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Bên cung cấp: ${tx.providerName} (${tx.providerPhone})",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Location & Date
            Row(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tx.location, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(tx.workDate, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Volume & Total contract value & Platform fee badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AgriGreenLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Khối lượng: ${tx.volume} ${tx.unit} × ${formatVnd(tx.unitPrice)}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Tổng tiền: ${formatVnd(tx.totalAmount)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AgriGreenPrimary
                        )
                    }

                    // Platform fee indicator
                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = "Phí sàn: 3.000 ₫",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tx.feeDeducted) AgriGreenPrimary else Color(0xFFD84315),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = if (tx.feeDeducted) "Đã thu phí" else "Chưa thu phí",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons based on status & role
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Chat button
                OutlinedButton(
                    onClick = onChatClick,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Nhắn tin", fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Status advancement buttons
                when (tx.status) {
                    TransactionStatus.PENDING_CONFIRMATION -> {
                        Button(
                            onClick = onConfirmClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Xác nhận giao dịch", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    TransactionStatus.IN_PROGRESS -> {
                        Button(
                            onClick = onRequestCompleteClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriAmberAccent),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Báo đã xong việc", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    TransactionStatus.AWAITING_COMPLETION_CONFIRM -> {
                        Button(
                            onClick = onCompleteClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("XÁC NHẬN HOÀN THÀNH (Phí 3k)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    TransactionStatus.COMPLETED -> {
                        Button(
                            onClick = onReviewClick,
                            colors = ButtonDefaults.buttonColors(containerColor = AgriAmberAccent),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Đánh giá sao", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
