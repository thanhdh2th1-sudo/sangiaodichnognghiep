package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PostType {
    DEMAND, // Bên cần dịch vụ (Nông dân cần thuê)
    OFFER   // Bên cung cấp dịch vụ (Chủ máy nhận làm)
}

enum class PostStatus {
    OPEN,        // Đang tìm kiếm / nhận báo giá
    IN_PROGRESS, // Đang thỏa thuận / thực hiện
    COMPLETED,   // Đã hoàn thành
    CLOSED       // Đã đóng
}

@Entity(tableName = "service_posts")
data class ServicePostEntity(
    @PrimaryKey val id: String,
    val authorId: String,
    val authorName: String,
    val authorPhone: String,
    val authorRole: UserRole,
    val authorAvatar: String = "",
    val postType: PostType,
    val categoryCode: String,
    val categoryName: String,
    val title: String,
    val description: String,
    val areaOrQuantity: Double, // e.g. 25.0 (ha hoặc tấn, giờ)
    val unit: String,           // ha, sào, công, tấn, giờ, máy
    val province: String,       // An Giang, Đồng Tháp, Cần Thơ, etc.
    val district: String,       // Tri Tôn, Thoại Sơn, etc.
    val fullAddress: String,
    val expectedPrice: Long,    // Giá dự kiến hoặc giá dịch vụ (VNĐ/đơn vị)
    val priceMethod: String,    // "Theo ha", "Theo công", "Trọn gói", "Thỏa thuận"
    val requirements: String = "",
    val machineryCount: Int = 1,
    val dailyCapacity: String = "",
    val executionDate: String = "", // e.g. "05/10/2026"
    val deadlineDate: String = "",
    val status: PostStatus = PostStatus.OPEN,
    val distanceKm: Double = 5.2, // simulated distance for map/search
    val viewCount: Int = 0,
    val responseCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)
