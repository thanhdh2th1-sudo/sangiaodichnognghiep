package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.ui.components.AgriBottomNav
import com.example.ui.components.AgriTopBar
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.CreatePostScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PostDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.TransactionsScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriMarketplaceTheme
import com.example.ui.viewmodel.AgriViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgriMarketplaceTheme {
                AgriMarketplaceApp()
            }
        }
    }
}

@Composable
fun AgriMarketplaceApp(
    viewModel: AgriViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigation states
    var currentScreen by remember { mutableStateOf("home") }
    var selectedPost by remember { mutableStateOf<ServicePostEntity?>(null) }
    var chatPartnerId by remember { mutableStateOf("") }
    var chatPartnerName by remember { mutableStateOf("") }
    var chatTxId by remember { mutableStateOf("") }

    var showRoleSwitchDialog by remember { mutableStateOf(false) }

    // ViewModel States
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val allUsers by viewModel.allUsers.collectAsStateWithLifecycle()
    val categories by viewModel.allCategories.collectAsStateWithLifecycle()
    val posts by viewModel.allPosts.collectAsStateWithLifecycle()
    val transactions by viewModel.userTransactions.collectAsStateWithLifecycle()
    val allTransactions by viewModel.allTransactions.collectAsStateWithLifecycle()
    val userWallet by viewModel.userWallet.collectAsStateWithLifecycle()
    val walletTransactions by viewModel.walletTransactions.collectAsStateWithLifecycle()
    val topUpRequests by viewModel.allTopUpRequests.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    // Active conversation messages
    val activeConversationId = remember(currentUser?.id, chatPartnerId) {
        if (currentUser != null && chatPartnerId.isNotEmpty()) {
            val (a, b) = listOf(currentUser!!.id, chatPartnerId).sorted()
            "CONV_${a}_${b}"
        } else ""
    }
    val chatMessages by viewModel.getChatMessages(activeConversationId)
        .collectAsStateWithLifecycle(initialValue = emptyList<com.example.data.local.entity.ChatMessageEntity>())

    // UI Feedback events
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Android BackHandler for sub-screens
    BackHandler(enabled = currentScreen != "home") {
        when (currentScreen) {
            "post_detail", "chat", "admin", "wallet", "onboarding" -> currentScreen = "home"
            else -> currentScreen = "home"
        }
    }

    // Onboarding Fullscreen View
    if (currentScreen == "onboarding") {
        OnboardingScreen(
            onComplete = { currentScreen = "home" }
        )
        return
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (currentScreen in listOf("home", "search", "create_post", "transactions", "profile", "wallet")) {
                AgriTopBar(
                    currentUser = currentUser,
                    unreadNotifCount = notifications.count { !it.isRead },
                    onRoleSwitchClick = { showRoleSwitchDialog = true },
                    onNotificationClick = {
                        scope.launch {
                            val count = notifications.size
                            snackbarHostState.showSnackbar("Bạn có $count thông báo mới từ hệ thống.")
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentScreen in listOf("home", "search", "create_post", "transactions", "profile")) {
                AgriBottomNav(
                    currentScreen = currentScreen,
                    onNavigate = { screen ->
                        currentScreen = screen
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                "home" -> {
                    HomeScreen(
                        currentUser = currentUser,
                        userWallet = userWallet,
                        categories = categories,
                        posts = posts,
                        onSelectPost = { post ->
                            selectedPost = post
                            currentScreen = "post_detail"
                        },
                        onCategoryClick = { cat ->
                            currentScreen = "search"
                        },
                        onNavigateSearch = { currentScreen = "search" },
                        onNavigateCreatePost = { currentScreen = "create_post" },
                        onNavigateWallet = { currentScreen = "wallet" },
                        onNavigateTransactions = { currentScreen = "transactions" }
                    )
                }

                "search" -> {
                    SearchScreen(
                        categories = categories,
                        posts = posts,
                        onSelectPost = { post ->
                            selectedPost = post
                            currentScreen = "post_detail"
                        }
                    )
                }

                "create_post" -> {
                    CreatePostScreen(
                        currentUser = currentUser,
                        categories = categories,
                        onPostCreated = { postType, catCode, catName, title, desc, qty, unit, prov, dist, addr, price, method, reqs, machCount, cap, date ->
                            viewModel.createPost(
                                postType = postType,
                                categoryCode = catCode,
                                categoryName = catName,
                                title = title,
                                description = desc,
                                areaOrQuantity = qty,
                                unit = unit,
                                province = prov,
                                district = dist,
                                fullAddress = addr,
                                expectedPrice = price,
                                priceMethod = method,
                                requirements = reqs,
                                machineryCount = machCount,
                                dailyCapacity = cap,
                                executionDate = date
                            )
                            currentScreen = "home"
                        }
                    )
                }

                "transactions" -> {
                    TransactionsScreen(
                        currentUser = currentUser,
                        userWallet = userWallet,
                        transactions = transactions,
                        onCompleteTransaction = { txId ->
                            viewModel.completeTransaction(txId)
                        },
                        onUpdateStatus = { txId, newStatus ->
                            viewModel.updateTransactionStatus(txId, newStatus)
                        },
                        onOpenChat = { txId, otherId, otherName ->
                            chatTxId = txId
                            chatPartnerId = otherId
                            chatPartnerName = otherName
                            currentScreen = "chat"
                        },
                        onSubmitReview = { txId, targetId, targetName, rating, comment, serviceName ->
                            viewModel.submitReview(txId, targetId, targetName, rating, comment, serviceName)
                        },
                        onNavigateWallet = { currentScreen = "wallet" }
                    )
                }

                "wallet" -> {
                    WalletScreen(
                        currentUser = currentUser,
                        userWallet = userWallet,
                        walletTransactions = walletTransactions,
                        onRequestTopUp = { amount ->
                            viewModel.requestTopUp(amount)
                        }
                    )
                }

                "profile" -> {
                    ProfileScreen(
                        currentUser = currentUser,
                        allUsers = allUsers,
                        userWallet = userWallet,
                        onSwitchUser = { newUserId ->
                            viewModel.switchActiveUser(newUserId)
                        },
                        onNavigateWallet = { currentScreen = "wallet" },
                        onNavigateTransactions = { currentScreen = "transactions" },
                        onNavigateAdmin = { currentScreen = "admin" },
                        onNavigateOnboarding = { currentScreen = "onboarding" }
                    )
                }

                "post_detail" -> {
                    selectedPost?.let { post ->
                        PostDetailScreen(
                            post = post,
                            currentUser = currentUser,
                            onBack = { currentScreen = "home" },
                            onOpenChat = { postId, authorId, authorName ->
                                chatTxId = ""
                                chatPartnerId = authorId
                                chatPartnerName = authorName
                                currentScreen = "chat"
                            },
                            onCreateTransaction = { pId, sName, cCode, cId, cName, cPhone, prId, prName, prPhone, loc, wDate, vol, unit, uPrice, terms ->
                                viewModel.createTransaction(
                                    postId = pId,
                                    serviceName = sName,
                                    categoryCode = cCode,
                                    customerId = cId,
                                    customerName = cName,
                                    customerPhone = cPhone,
                                    providerId = prId,
                                    providerName = prName,
                                    providerPhone = prPhone,
                                    location = loc,
                                    workDate = wDate,
                                    volume = vol,
                                    unit = unit,
                                    unitPrice = uPrice,
                                    terms = terms
                                )
                                currentScreen = "transactions"
                            }
                        )
                    }
                }

                "chat" -> {
                    ChatScreen(
                        currentUser = currentUser,
                        partnerName = chatPartnerName,
                        partnerId = chatPartnerId,
                        transactionId = chatTxId,
                        messages = chatMessages,
                        onSendMessage = { text ->
                            viewModel.sendChatMessage(activeConversationId, chatPartnerId, text, chatTxId)
                        },
                        onBack = { currentScreen = "home" }
                    )
                }

                "admin" -> {
                    AdminDashboardScreen(
                        users = allUsers,
                        posts = posts,
                        transactions = allTransactions,
                        topUpRequests = topUpRequests,
                        categories = categories,
                        onApproveTopUp = { reqId ->
                            viewModel.adminApproveTopUp(reqId)
                        },
                        onRejectTopUp = { reqId, reason ->
                            viewModel.adminRejectTopUp(reqId, reason)
                        },
                        onAddCategory = { name, code, desc, unit ->
                            viewModel.adminAddCategory(name, code, desc, unit)
                        },
                        onVerifyUser = { uId, isVerified ->
                            viewModel.adminVerifyUser(uId, isVerified)
                        },
                        onBack = { currentScreen = "profile" }
                    )
                }
            }
        }
    }

    // Role Switch Dialog
    if (showRoleSwitchDialog) {
        AlertDialog(
            onDismissRequest = { showRoleSwitchDialog = false },
            title = { Text("Chuyển vai trò thử nghiệm", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Chọn tài khoản để trải nghiệm các góc nhìn nghiệp vụ:", fontSize = 12.sp, color = Color.Gray)

                    allUsers.forEach { user ->
                        val isSelected = user.id == currentUser?.id
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) AgriGreenLight else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.switchActiveUser(user.id)
                                    showRoleSwitchDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (user.role) {
                                        UserRole.CUSTOMER -> "🌾"
                                        UserRole.PROVIDER -> "🚜"
                                        UserRole.ADMIN -> "🛡️"
                                        else -> "👤"
                                    },
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(user.fullName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(
                                        text = when (user.role) {
                                            UserRole.CUSTOMER -> "Nông dân (Thuê dịch vụ)"
                                            UserRole.PROVIDER -> "Nhà cung cấp (Chủ máy & drone, ví tiền)"
                                            UserRole.ADMIN -> "Quản trị viên (Duyệt nạp, thu phí)"
                                            else -> "Người dùng"
                                        },
                                        fontSize = 11.sp,
                                        color = AgriGreenPrimary
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = AgriGreenPrimary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRoleSwitchDialog = false }) {
                    Text("Đóng")
                }
            }
        )
    }
}
