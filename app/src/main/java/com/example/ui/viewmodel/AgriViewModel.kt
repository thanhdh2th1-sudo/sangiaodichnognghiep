package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.ChatMessageType
import com.example.data.local.entity.DisputeEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TopUpRequestEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionStatus
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.data.local.entity.WalletEntity
import com.example.data.local.entity.WalletTransactionEntity
import com.example.data.repository.AgriRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class AgriViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AgriRepository
    init {
        val database = AppDatabase.getDatabase(application)
        repository = AgriRepository(database.agriDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    // Active User State (Default: Nông dân Nguyễn Văn An, can easily switch to Provider or Admin)
    private val _activeUserId = MutableStateFlow("USR_ND01")
    val activeUserId: StateFlow<String> = _activeUserId.asStateFlow()

    val allUsers: StateFlow<List<UserEntity>> = repository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentUser: StateFlow<UserEntity?> = combine(allUsers, _activeUserId) { users, activeId ->
        users.find { it.id == activeId } ?: users.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Posts & Categories
    val allCategories: StateFlow<List<ServiceCategoryEntity>> = repository.allCategories
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPosts: StateFlow<List<ServicePostEntity>> = repository.allPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Transactions
    val allTransactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userTransactions: StateFlow<List<TransactionEntity>> = _activeUserId.flatMapLatest { userId ->
        repository.getTransactionsForUser(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Wallet & Top-up
    val userWallet: StateFlow<WalletEntity?> = _activeUserId.flatMapLatest { userId ->
        repository.getWallet(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val walletTransactions: StateFlow<List<WalletTransactionEntity>> = _activeUserId.flatMapLatest { userId ->
        repository.getWalletTransactions(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTopUpRequests: StateFlow<List<TopUpRequestEntity>> = repository.allTopUpRequests
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allDisputes: StateFlow<List<DisputeEntity>> = repository.allDisputes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationEntity>> = _activeUserId.flatMapLatest { userId ->
        repository.getNotifications(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Search and Filter states
    val searchQuery = MutableStateFlow("")
    val selectedCategoryFilter = MutableStateFlow<String?>(null)
    val selectedProvinceFilter = MutableStateFlow<String?>(null)
    val selectedPostTypeFilter = MutableStateFlow<PostType?>(null)
    val maxRadiusKmFilter = MutableStateFlow(50f)

    // SnackBar / Alert Events
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent: SharedFlow<String> = _uiEvent.asSharedFlow()

    fun getNotifications(userId: String) = repository.getNotifications(userId)

    fun getChatMessages(conversationId: String): Flow<List<ChatMessageEntity>> =
        repository.getChatMessages(conversationId)

    fun switchActiveUser(userId: String) {
        _activeUserId.value = userId
        viewModelScope.launch {
            _uiEvent.emit("Đã chuyển sang tài khoản: $userId")
        }
    }

    /**
     * Completes a transaction and automatically applies the 3.000 VNĐ fee logic.
     */
    fun completeTransaction(transactionId: String) {
        viewModelScope.launch {
            val result = repository.completeTransaction(transactionId)
            result.onSuccess { msg ->
                _uiEvent.emit(msg)
            }.onFailure { err ->
                _uiEvent.emit("LỖI: ${err.message}")
            }
        }
    }

    fun updateTransactionStatus(transactionId: String, newStatus: TransactionStatus) {
        viewModelScope.launch {
            repository.updateTransactionStatus(transactionId, newStatus)
            _uiEvent.emit("Đã cập nhật trạng thái hợp đồng: ${newStatus.label}")
        }
    }

    fun requestTopUp(amount: Long) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val req = repository.createTopUpRequest(user.id, amount)
            _uiEvent.emit("Đã tạo yêu cầu nạp ${amount} ₫ (Mã: ${req.id}). Vui lòng chờ BQT duyệt!")
        }
    }

    fun adminApproveTopUp(requestId: String) {
        viewModelScope.launch {
            val admin = currentUser.value?.fullName ?: "Ban Quản Trị"
            val result = repository.approveTopUpRequest(requestId, admin)
            result.onSuccess { msg ->
                _uiEvent.emit(msg)
            }.onFailure { err ->
                _uiEvent.emit("Không thể duyệt: ${err.message}")
            }
        }
    }

    fun adminRejectTopUp(requestId: String, reason: String) {
        viewModelScope.launch {
            val admin = currentUser.value?.fullName ?: "Ban Quản Trị"
            repository.rejectTopUpRequest(requestId, reason, admin)
            _uiEvent.emit("Đã từ chối yêu cầu nạp tiền $requestId.")
        }
    }

    fun createPost(
        postType: PostType,
        categoryCode: String,
        categoryName: String,
        title: String,
        description: String,
        areaOrQuantity: Double,
        unit: String,
        province: String,
        district: String,
        fullAddress: String,
        expectedPrice: Long,
        priceMethod: String,
        requirements: String,
        machineryCount: Int = 1,
        dailyCapacity: String = "",
        executionDate: String = ""
    ) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val newPost = ServicePostEntity(
                id = "POST_" + (1000 + (Math.random() * 8999).toInt()),
                authorId = user.id,
                authorName = user.fullName,
                authorPhone = user.phone,
                authorRole = user.role,
                authorAvatar = user.avatarUrl,
                postType = postType,
                categoryCode = categoryCode,
                categoryName = categoryName,
                title = title,
                description = description,
                areaOrQuantity = areaOrQuantity,
                unit = unit,
                province = province,
                district = district,
                fullAddress = fullAddress,
                expectedPrice = expectedPrice,
                priceMethod = priceMethod,
                requirements = requirements,
                machineryCount = machineryCount,
                dailyCapacity = dailyCapacity,
                executionDate = executionDate,
                createdAt = System.currentTimeMillis()
            )
            repository.createServicePost(newPost)
            _uiEvent.emit("Đăng bài thành công!")
        }
    }

    fun createTransaction(
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
    ) {
        viewModelScope.launch {
            val txId = "HD-2026-" + (1000 + (Math.random() * 8999).toInt())
            val totalAmount = (volume * unitPrice).toLong()
            val newTx = TransactionEntity(
                id = txId,
                customerId = customerId,
                customerName = customerName,
                customerPhone = customerPhone,
                providerId = providerId,
                providerName = providerName,
                providerPhone = providerPhone,
                postId = postId,
                serviceName = serviceName,
                categoryCode = categoryCode,
                location = location,
                workDate = workDate,
                volume = volume,
                unit = unit,
                unitPrice = unitPrice,
                totalAmount = totalAmount,
                terms = terms,
                status = TransactionStatus.PENDING_CONFIRMATION,
                platformFee = 3000L,
                feeDeducted = false
            )
            repository.createTransaction(newTx)
            _uiEvent.emit("Tạo giao dịch thành công (Mã: $txId). Chờ xác nhận!")
        }
    }

    fun sendChatMessage(convId: String, receiverId: String, messageText: String, txId: String = "") {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val msg = ChatMessageEntity(
                id = "MSG-" + UUID.randomUUID().toString().take(8),
                conversationId = convId,
                transactionId = txId,
                senderId = user.id,
                senderName = user.fullName,
                senderRole = user.role,
                receiverId = receiverId,
                message = messageText,
                messageType = ChatMessageType.TEXT,
                timestamp = System.currentTimeMillis()
            )
            repository.sendChatMessage(msg)
        }
    }

    fun submitReview(transactionId: String, targetUserId: String, targetUserName: String, rating: Int, comment: String, serviceName: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val review = ReviewEntity(
                id = "REV-" + UUID.randomUUID().toString().take(8),
                transactionId = transactionId,
                reviewerId = user.id,
                reviewerName = user.fullName,
                reviewerRole = user.role,
                targetUserId = targetUserId,
                targetUserName = targetUserName,
                rating = rating,
                comment = comment,
                serviceName = serviceName
            )
            repository.addReview(review)
            _uiEvent.emit("Cảm ơn bạn đã gửi đánh giá $rating sao!")
        }
    }

    fun submitDispute(transactionId: String, reason: String, description: String) {
        viewModelScope.launch {
            val user = currentUser.value ?: return@launch
            val dispute = DisputeEntity(
                id = "DISP-" + UUID.randomUUID().toString().take(8),
                transactionId = transactionId,
                reporterId = user.id,
                reporterName = user.fullName,
                reason = reason,
                description = description
            )
            repository.createDispute(dispute)
            _uiEvent.emit("Đã gửi khiếu nại tới Ban Quản Trị!")
        }
    }

    fun adminAddCategory(name: String, code: String, desc: String, defaultUnit: String) {
        viewModelScope.launch {
            val newCat = ServiceCategoryEntity(
                code = if (code.startsWith("CAT_")) code else "CAT_$code",
                name = name,
                iconKey = "custom",
                description = desc,
                defaultUnit = defaultUnit,
                isActive = true,
                sortOrder = 99
            )
            repository.addCategory(newCat)
            _uiEvent.emit("Đã thêm danh mục mới: $name")
        }
    }

    fun adminVerifyUser(userId: String, isVerified: Boolean) {
        viewModelScope.launch {
            repository.verifyUser(userId, isVerified)
            _uiEvent.emit("Đã cập nhật trạng thái xác minh!")
        }
    }
}
