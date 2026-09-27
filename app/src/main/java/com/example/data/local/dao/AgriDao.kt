package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DisputeEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PostStatus
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TopUpRequestEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity
import com.example.data.local.entity.WalletTransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AgriDao {

    // --- Users ---
    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: String): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserByIdDirect(id: String): UserEntity?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :userId")
    suspend fun updateUserVerification(userId: String, isVerified: Boolean)

    // --- Service Categories ---
    @Query("SELECT * FROM service_categories WHERE isActive = 1 ORDER BY sortOrder ASC")
    fun getAllCategories(): Flow<List<ServiceCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ServiceCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<ServiceCategoryEntity>)

    // --- Service Posts ---
    @Query("SELECT * FROM service_posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<ServicePostEntity>>

    @Query("SELECT * FROM service_posts WHERE postType = :postType ORDER BY createdAt DESC")
    fun getPostsByType(postType: PostType): Flow<List<ServicePostEntity>>

    @Query("SELECT * FROM service_posts WHERE categoryCode = :code ORDER BY createdAt DESC")
    fun getPostsByCategory(code: String): Flow<List<ServicePostEntity>>

    @Query("SELECT * FROM service_posts WHERE authorId = :authorId ORDER BY createdAt DESC")
    fun getPostsByAuthor(authorId: String): Flow<List<ServicePostEntity>>

    @Query("SELECT * FROM service_posts WHERE id = :id")
    fun getPostById(id: String): Flow<ServicePostEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: ServicePostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<ServicePostEntity>)

    @Query("DELETE FROM service_posts WHERE id = :id")
    suspend fun deletePost(id: String)

    @Query("UPDATE service_posts SET status = :status WHERE id = :id")
    suspend fun updatePostStatus(id: String, status: PostStatus)

    // --- Transactions ---
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE customerId = :userId OR providerId = :userId ORDER BY createdAt DESC")
    fun getTransactionsForUser(userId: String): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionById(id: String): Flow<TransactionEntity?>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionByIdDirect(id: String): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(tx: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(txs: List<TransactionEntity>)

    @Update
    suspend fun updateTransaction(tx: TransactionEntity)

    // --- Wallet & Platform Fee ---
    @Query("SELECT * FROM wallets WHERE userId = :userId")
    fun getWallet(userId: String): Flow<WalletEntity?>

    @Query("SELECT * FROM wallets WHERE userId = :userId")
    suspend fun getWalletDirect(userId: String): WalletEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateWallet(wallet: WalletEntity)

    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getWalletTransactions(userId: String): Flow<List<WalletTransactionEntity>>

    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllWalletTransactions(): Flow<List<WalletTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletTransaction(tx: WalletTransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWalletTransactions(txs: List<WalletTransactionEntity>)

    // --- Top-Up Requests ---
    @Query("SELECT * FROM topup_requests ORDER BY createdAt DESC")
    fun getAllTopUpRequests(): Flow<List<TopUpRequestEntity>>

    @Query("SELECT * FROM topup_requests WHERE userId = :userId ORDER BY createdAt DESC")
    fun getTopUpRequestsForUser(userId: String): Flow<List<TopUpRequestEntity>>

    @Query("SELECT * FROM topup_requests WHERE id = :id")
    suspend fun getTopUpRequestById(id: String): TopUpRequestEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopUpRequest(req: TopUpRequestEntity)

    @Update
    suspend fun updateTopUpRequest(req: TopUpRequestEntity)

    // --- Reviews ---
    @Query("SELECT * FROM reviews WHERE targetUserId = :targetUserId ORDER BY createdAt DESC")
    fun getReviewsForUser(targetUserId: String): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews ORDER BY createdAt DESC")
    fun getAllReviews(): Flow<List<ReviewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReviews(reviews: List<ReviewEntity>)

    // --- Chat Messages ---
    @Query("SELECT * FROM messages WHERE conversationId = :convId ORDER BY timestamp ASC")
    fun getMessages(convId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(msgs: List<ChatMessageEntity>)

    // --- Notifications ---
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY createdAt DESC")
    fun getNotifications(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notif: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationRead(id: String)

    // --- Disputes ---
    @Query("SELECT * FROM disputes ORDER BY createdAt DESC")
    fun getAllDisputes(): Flow<List<DisputeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDispute(dispute: DisputeEntity)

    @Update
    suspend fun updateDispute(dispute: DisputeEntity)
}
