package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class WalletEntity(
    @PrimaryKey val userId: String,
    val balance: Long = 0L, // Số dư VNĐ
    val updatedAt: Long = System.currentTimeMillis()
)

enum class WalletTxType(val label: String) {
    TOP_UP("Nạp tiền vào ví"),
    PLATFORM_FEE("Phí giao dịch dịch vụ"),
    REFUND("Hoàn tiền")
}

@Entity(tableName = "wallet_transactions")
data class WalletTransactionEntity(
    @PrimaryKey val id: String, // e.g. "WTX-2026-001"
    val userId: String,
    val amount: Long,           // +50000 or -3000
    val balanceBefore: Long,
    val balanceAfter: Long,
    val type: WalletTxType,
    val description: String,
    val referenceId: String = "", // e.g. Transaction ID or TopUp Request ID
    val status: String = "SUCCESS",
    val timestamp: Long = System.currentTimeMillis()
)

enum class TopUpStatus(val label: String) {
    PENDING("Chờ xác nhận"),
    APPROVED("Đã duyệt thành công"),
    REJECTED("Bị từ chối")
}

@Entity(tableName = "topup_requests")
data class TopUpRequestEntity(
    @PrimaryKey val id: String, // e.g. "NAP-83921"
    val userId: String,
    val userName: String,
    val userPhone: String,
    val amount: Long,
    val bankName: String = "Viettel Money",
    val accountNumber: String = "0368666219",
    val accountHolder: String = "NGUYEN VAN THANH",
    val transferContent: String, // e.g. "NAPTIEN USER12345"
    val status: TopUpStatus = TopUpStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null,
    val approvedBy: String = ""
)
