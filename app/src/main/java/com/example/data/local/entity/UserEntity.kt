package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    CUSTOMER,    // Bên sử dụng dịch vụ (Nông dân, HTX, Chủ ruộng)
    PROVIDER,    // Bên cung cấp dịch vụ (Chủ drone, máy gặt, máy cày...)
    ADMIN,       // Quản trị viên hệ thống
    BOTH         // Cả hai vai trò
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val role: UserRole,
    val province: String,
    val district: String,
    val address: String,
    val avatarUrl: String = "",
    val businessName: String = "",
    val machineryDetails: String = "",
    val dailyCapacity: String = "",
    val serviceArea: String = "",
    val experienceYears: Int = 3,
    val isVerified: Boolean = false,
    val ratingAverage: Float = 4.9f,
    val completedTransactionsCount: Int = 12,
    val createdAt: Long = System.currentTimeMillis()
)
