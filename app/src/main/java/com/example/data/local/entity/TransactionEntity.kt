package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TransactionStatus(val label: String) {
    PENDING_CONFIRMATION("Chờ xác nhận"),
    CONFIRMED("Đã xác nhận"),
    IN_PROGRESS("Đang thực hiện"),
    AWAITING_COMPLETION_CONFIRM("Chờ xác nhận hoàn thành"),
    COMPLETED("Đã hoàn tất"),
    CANCELLED("Hủy giao dịch"),
    DISPUTED("Có tranh chấp")
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String, // e.g., "HD-2026-0081"
    val customerId: String,
    val customerName: String,
    val customerPhone: String,
    val providerId: String,
    val providerName: String,
    val providerPhone: String,
    val postId: String,
    val serviceName: String,
    val categoryCode: String,
    val location: String,
    val workDate: String,
    val volume: Double,          // Diện tích hoặc sản lượng (e.g. 20 ha)
    val unit: String,            // ha, sào, công, tấn
    val unitPrice: Long,         // Đơn giá VNĐ (e.g. 150.000)
    val totalAmount: Long,       // Tổng tiền = volume * unitPrice (e.g. 3.000.000)
    val terms: String,           // Điều khoản công việc
    val status: TransactionStatus,
    val platformFee: Long = 3000L, // Phí nền tảng cố định 3.000đ do bên cung cấp chịu
    val feeDeducted: Boolean = false, // Idempotency check: chỉ trừ đúng 1 lần
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
