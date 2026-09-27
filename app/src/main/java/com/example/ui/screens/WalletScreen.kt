package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import android.widget.Toast
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity
import com.example.data.local.entity.WalletTransactionEntity
import com.example.data.local.entity.WalletTxType
import com.example.ui.components.formatDate
import com.example.ui.components.formatVnd
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@Composable
fun WalletScreen(
    currentUser: UserEntity?,
    userWallet: WalletEntity?,
    walletTransactions: List<WalletTransactionEntity>,
    onRequestTopUp: (Long) -> Unit
) {
    var showTopUpDialog by remember { mutableStateOf(false) }
    var selectedAmount by remember { mutableLongStateOf(50000L) }
    var showSuccessConfirmation by remember { mutableStateOf(false) }

    val presetAmounts = listOf(10000L, 20000L, 50000L, 100000L)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wallet_screen"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Balance Banner Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(AgriGreenPrimary, Color(0xFF2E7D32), AgriGreenSecondary)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "VÍ TÀI KHOẢN NHÀ CUNG CẤP",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Phí 3.000 ₫/giao dịch",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Text(
                            text = "Số dư hiện tại",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Text(
                            text = formatVnd(userWallet?.balance ?: 0L),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nạp tiền Button CTA
                        Button(
                            onClick = { showTopUpDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = AgriAmberAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "NẠP TÀI KHOẢN NGAY",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 2. Policy Info Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = AgriGreenLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quy định phí nền tảng: Mỗi giao dịch hoàn tất thành công chỉ thu cố định 3.000 ₫ từ bên cung cấp. Tài khoản phải duy trì số dư tối thiểu 3.000 ₫ để hoàn tất hợp đồng.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // 3. Transactions Audit Log Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = AgriGreenPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Lịch sử biến động số dư",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "${walletTransactions.size} biến động",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // 4. Audit List
        if (walletTransactions.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🪙", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Chưa có lịch sử giao dịch ví", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Các khoản nạp tiền và trừ phí sàn 3.000 ₫ sẽ ghi nhận tại đây.", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        } else {
            items(walletTransactions, key = { it.id }) { wtx ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (wtx.amount > 0) AgriGreenLight else Color(0xFFFFEBEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (wtx.amount > 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = if (wtx.amount > 0) AgriGreenPrimary else Color(0xFFD32F2F),
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = wtx.description,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${formatDate(wtx.timestamp)} • Trước: ${formatVnd(wtx.balanceBefore)} • Sau: ${formatVnd(wtx.balanceAfter)}",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = (if (wtx.amount > 0) "+" else "") + formatVnd(wtx.amount),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (wtx.amount > 0) AgriGreenPrimary else Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }
    }

    // Top-Up Dialog (Sections X)
    if (showTopUpDialog) {
        val context = LocalContext.current
        val clipboardManager = LocalClipboardManager.current
        val transferContent = "NAPTIEN ${currentUser?.id ?: "USER12345"}"
        val targetAccountNumber = "0368666219"
        val targetAccountHolder = "NGUYEN VAN THANH"
        val targetBankName = "Viettel Money"

        AlertDialog(
            onDismissRequest = { showTopUpDialog = false },
            title = {
                Text(
                    text = "NẠP TIỀN VÀO TÀI KHOẢN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = AgriGreenPrimary
                )
            },
            text = {
                Column {
                    Text("1. Chọn số tiền nạp:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetAmounts.take(2).forEach { amount ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedAmount == amount) AgriGreenPrimary else AgriGreenLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedAmount = amount }
                            ) {
                                Text(
                                    text = formatVnd(amount),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedAmount == amount) Color.White else AgriGreenPrimary,
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetAmounts.drop(2).forEach { amount ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (selectedAmount == amount) AgriGreenPrimary else AgriGreenLight,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedAmount = amount }
                            ) {
                                Text(
                                    text = formatVnd(amount),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedAmount == amount) Color.White else AgriGreenPrimary,
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text("2. Chuyển khoản đến tài khoản sau:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Kênh nạp / Ví:", fontSize = 11.sp, color = Color.Gray)
                                    Text(targetBankName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AgriGreenPrimary)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Số tài khoản / SĐT:", fontSize = 11.sp, color = Color.Gray)
                                    Text(targetAccountNumber, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = AgriGreenPrimary)
                                }
                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(targetAccountNumber))
                                        Toast.makeText(context, "Đã sao chép: $targetAccountNumber", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Sao chép", modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Sao chép", fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Chủ tài khoản:", fontSize = 11.sp, color = Color.Gray)
                            Text(targetAccountHolder, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Số tiền:", fontSize = 11.sp, color = Color.Gray)
                            Text(formatVnd(selectedAmount), fontSize = 13.sp, fontWeight = FontWeight.Bold, color = AgriAmberAccent)

                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Nội dung CK:", fontSize = 11.sp, color = Color.Gray)
                                    Text(
                                        text = transferContent,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFD84315)
                                    )
                                }
                                OutlinedButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(transferContent))
                                        Toast.makeText(context, "Đã sao chép nội dung: $transferContent", Toast.LENGTH_SHORT).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Sao chép", modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("Sao chép", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Sau khi nạp tiền về Viettel Money, nhấn nút xác nhận bên dưới. Ban Quản Trị sẽ đối soát và duyệt cộng tiền vào ví trong vòng 1-3 phút.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRequestTopUp(selectedAmount)
                        showTopUpDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AgriGreenPrimary)
                ) {
                    Text("TÔI ĐÃ CHUYỂN KHOẢN")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTopUpDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
