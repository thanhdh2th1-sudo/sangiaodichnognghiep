package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val reviewerId: String,
    val reviewerName: String,
    val reviewerRole: UserRole,
    val targetUserId: String,
    val targetUserName: String,
    val rating: Int, // 1..5 stars
    val comment: String,
    val serviceName: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

enum class ChatMessageType {
    TEXT,
    LOCATION,
    TRANSACTION_INFO,
    SYSTEM
}

@Entity(tableName = "messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val conversationId: String,
    val transactionId: String = "",
    val senderId: String,
    val senderName: String,
    val senderRole: UserRole,
    val receiverId: String,
    val message: String,
    val messageType: ChatMessageType = ChatMessageType.TEXT,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val content: String,
    val type: String, // "TRANSACTION", "WALLET", "MESSAGE", "REVIEW"
    val relatedId: String = "",
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "disputes")
data class DisputeEntity(
    @PrimaryKey val id: String,
    val transactionId: String,
    val reporterId: String,
    val reporterName: String,
    val reason: String,
    val description: String,
    val status: String = "PENDING", // PENDING, RESOLVED
    val resolutionNote: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
