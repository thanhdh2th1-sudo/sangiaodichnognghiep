package com.example.data.repository

import com.example.data.local.dao.AgriDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.ChatMessageType
import com.example.data.local.entity.DisputeEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PostStatus
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TopUpRequestEntity
import com.example.data.local.entity.TopUpStatus
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.TransactionStatus
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.UserRole
import com.example.data.local.entity.WalletEntity
import com.example.data.local.entity.WalletTransactionEntity
import com.example.data.local.entity.WalletTxType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AgriRepository(private val dao: AgriDao) {

    val allPosts: Flow<List<ServicePostEntity>> = dao.getAllPosts()
    val allCategories: Flow<List<ServiceCategoryEntity>> = dao.getAllCategories()
    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsers()
    val allTopUpRequests: Flow<List<TopUpRequestEntity>> = dao.getAllTopUpRequests()
    val allDisputes: Flow<List<DisputeEntity>> = dao.getAllDisputes()

    fun getWallet(userId: String): Flow<WalletEntity?> = dao.getWallet(userId)
    fun getWalletTransactions(userId: String): Flow<List<WalletTransactionEntity>> = dao.getWalletTransactions(userId)
    fun getTransactionsForUser(userId: String): Flow<List<TransactionEntity>> = dao.getTransactionsForUser(userId)
    fun getPostsByAuthor(authorId: String): Flow<List<ServicePostEntity>> = dao.getPostsByAuthor(authorId)
    fun getReviewsForUser(userId: String): Flow<List<ReviewEntity>> = dao.getReviewsForUser(userId)
    fun getChatMessages(conversationId: String): Flow<List<ChatMessageEntity>> = dao.getMessages(conversationId)
    fun getNotifications(userId: String): Flow<List<NotificationEntity>> = dao.getNotifications(userId)

    suspend fun getPostById(id: String): Flow<ServicePostEntity?> = dao.getPostById(id)
    suspend fun getTransactionById(id: String): Flow<TransactionEntity?> = dao.getTransactionById(id)

    /**
     * Seeds realistic initial data for demonstration if DB is fresh.
     */
    suspend fun seedInitialDataIfEmpty() = withContext(Dispatchers.IO) {
        val categories = dao.getAllCategories().firstOrNull()
        if (categories.isNullOrEmpty()) {
            populateSeedData()
        }
    }

    private suspend fun populateSeedData() {
        val now = System.currentTimeMillis()

        // 1. Initial Users
        val userCustomer1 = UserEntity(
            id = "USR_ND01",
            fullName = "Nguyễn Văn An",
            phone = "0918 234 567",
            email = "vanan.ag@gmail.com",
            role = UserRole.CUSTOMER,
            province = "An Giang",
            district = "Tri Tôn",
            address = "Ấp Vĩnh Lộc, Xã Vĩnh Gia",
            businessName = "Ruộng lúa 25 ha Hè Thu",
            isVerified = true,
            ratingAverage = 4.9f,
            completedTransactionsCount = 14
        )

        val userCustomer2 = UserEntity(
            id = "USR_HTX02",
            fullName = "Hợp Tác Xã Thắng Lợi",
            phone = "0977 889 900",
            email = "htxthangloi.dt@gmail.com",
            role = UserRole.CUSTOMER,
            province = "Đồng Tháp",
            district = "Tháp Mười",
            address = "Khu sản xuất lúa chất lượng cao 150 ha",
            businessName = "HTX Nông Nghiệp Công Nghệ Cao",
            isVerified = true,
            ratingAverage = 5.0f,
            completedTransactionsCount = 38
        )

        val userProvider1 = UserEntity(
            id = "USR_DRONE01",
            fullName = "Đội Bay Drone Miền Tây (Trần Quốc Hưng)",
            phone = "0909 334 455",
            email = "hungdrone.mientay@gmail.com",
            role = UserRole.PROVIDER,
            province = "An Giang",
            district = "Thoại Sơn",
            address = "Thị trấn Núi Sập",
            businessName = "Công ty TNHH Dịch Vụ Bay Nông Nghiệp AGRI-SKY",
            machineryDetails = "03 Thiết bị Drone DJI Agras T40 & T50 công suất lớn",
            dailyCapacity = "80 ha/ngày (Phun thuốc & Rải phân, Sạ lúa)",
            serviceArea = "An Giang, Cần Thơ, Kiên Giang, Đồng Tháp",
            experienceYears = 4,
            isVerified = true,
            ratingAverage = 4.95f,
            completedTransactionsCount = 86
        )

        val userProvider2 = UserEntity(
            id = "USR_MAYCAT02",
            fullName = "Đội Máy Gặt Đập Liên Hợp Ba Tri (Lê Hoàng Nam)",
            phone = "0988 556 677",
            email = "hoangnam.maygat@gmail.com",
            role = UserRole.PROVIDER,
            province = "Đồng Tháp",
            district = "Tam Nông",
            address = "Xã Phú Cường",
            businessName = "Dịch Vụ Cơ Giới Hóa Ba Tri",
            machineryDetails = "04 Máy gặt đập Kubota DC-70G Plus & Yanmar AW82V",
            dailyCapacity = "35 ha/ngày (Cắt lúa ôm bờ bao sạch, hạn chế thất thoát)",
            serviceArea = "Đồng Tháp, Long An, An Giang",
            experienceYears = 6,
            isVerified = true,
            ratingAverage = 4.88f,
            completedTransactionsCount = 112
        )

        val userProvider3 = UserEntity(
            id = "USR_THUMUA03",
            fullName = "Doanh Nghiệp Thu Mua Nông Sản Vàng (Trần Thị Bích)",
            phone = "0933 667 889",
            email = "bich.nongsanvang@gmail.com",
            role = UserRole.PROVIDER,
            province = "An Giang",
            district = "Long Xuyên",
            address = "Cụm Công Nghiệp Bình Đức",
            businessName = "Vựa Nông Sản Vàng Miền Tây",
            machineryDetails = "05 Xe tải 15 tấn & Trạm cân điện tử lưu động",
            dailyCapacity = "Thu mua 200 tấn lúa/ngày",
            serviceArea = "Toàn bộ khu vực Đồng Bằng Sông Cửu Long",
            experienceYears = 8,
            isVerified = true,
            ratingAverage = 4.9f,
            completedTransactionsCount = 160
        )

        val userAdmin = UserEntity(
            id = "USR_ADMIN01",
            fullName = "Ban Quản Trị Hệ Thống",
            phone = "1900 6868",
            email = "admin@sandichvunongnghiep.vn",
            role = UserRole.ADMIN,
            province = "Hà Nội / Cần Thơ",
            district = "Ninh Kiều",
            address = "Trung Tâm Đổi Mới Nông Nghiệp Số",
            isVerified = true
        )

        dao.insertUsers(listOf(userCustomer1, userCustomer2, userProvider1, userProvider2, userProvider3, userAdmin))

        // 2. Initial Categories (14 categories from Section XXIV)
        val categories = listOf(
            ServiceCategoryEntity("CAT_DRONE_THUOC", "Drone phun thuốc", "drone", "Phun thuốc bảo vệ thực vật chính xác, siêu mịn bằng Drone", "ha", true, 1),
            ServiceCategoryEntity("CAT_DRONE_PHAN", "Drone phun phân", "fertilizer", "Rải phân bón lá, phân hạt bằng máy bay không người lái", "ha", true, 2),
            ServiceCategoryEntity("CAT_DRONE_SA", "Drone gieo sạ", "seeding", "Gieo sạ lúa chuẩn hàng, đều mộng, tiết kiệm giống", "ha", true, 3),
            ServiceCategoryEntity("CAT_MAY_GAT", "Máy cắt thu hoạch", "harvester", "Gặt đập liên hợp gom lúa bao sạch tại ruộng", "ha", true, 4),
            ServiceCategoryEntity("CAT_CAY_DAT", "Cày đất", "plow", "Lật đất, phơi ải, làm tơi xốp đất trước gieo cấy", "ha", true, 5),
            ServiceCategoryEntity("CAT_BUA_DAT", "Bừa đất", "harrow", "Bừa nhuyễn đất bùn, chuẩn bị mặt ruộng", "ha", true, 6),
            ServiceCategoryEntity("CAT_TRAT_DAT", "Trạt đất / San bằng", "leveler", "San phẳng mặt ruộng bằng tia laser công nghệ cao", "ha", true, 7),
            ServiceCategoryEntity("CAT_BOM_NUOC", "Bơm tưới tiêu", "pump", "Bơm tiêu úng, bơm nước vào ruộng mùa khô hạn", "giờ", true, 8),
            ServiceCategoryEntity("CAT_LAM_CO", "Làm cỏ & tỉa dặm", "weed", "Làm cỏ ruộng lúa, tỉa dặm thủ công và cơ giới", "công", true, 9),
            ServiceCategoryEntity("CAT_THU_MUA_LUA", "Thu mua lúa tươi", "rice", "Thu mua lúa tươi tại ruộng giá cao, cân uy tín", "tấn", true, 10),
            ServiceCategoryEntity("CAT_THU_MUA_NS", "Thu mua nông sản", "vegetables", "Bao tiêu và thu mua rau màu, cây ăn trái các loại", "tấn", true, 11),
            ServiceCategoryEntity("CAT_VAN_CHUYEN", "Vận chuyển nông sản", "truck", "Xe tải, ghe thuyền chở lúa gạo và nông sản", "chuyến", true, 12),
            ServiceCategoryEntity("CAT_SAY_NS", "Sấy nông sản", "dryer", "Hệ thống lò sấy vỉ ngang, tháp sấy lúa bắp hiện đại", "tấn", true, 13),
            ServiceCategoryEntity("CAT_KHAC", "Dịch vụ khác", "more", "Cắt gốc rạ, cuộn rơm, kiểm định chất lượng", "gói", true, 14)
        )
        dao.insertCategories(categories)

        // 3. Initial Posts (Both DEMAND and OFFER)
        val posts = listOf(
            ServicePostEntity(
                id = "POST_001",
                authorId = userCustomer1.id,
                authorName = userCustomer1.fullName,
                authorPhone = userCustomer1.phone,
                authorRole = userCustomer1.role,
                postType = PostType.DEMAND,
                categoryCode = "CAT_DRONE_THUOC",
                categoryName = "Drone phun thuốc",
                title = "Cần drone phun thuốc rầy và đạo ôn cho 25 ha lúa",
                description = "Ruộng lúa 25 ha tại Tri Tôn cần đội drone xịt thuốc phòng trừ rầy nâu và đạo ôn cổ bông. Yêu cầu thợ bay chuẩn, không sót góc, có thể hoàn thành trong 1 ngày.",
                areaOrQuantity = 25.0,
                unit = "ha",
                province = "An Giang",
                district = "Tri Tôn",
                fullAddress = "Cánh đồng mẫu lớn Vĩnh Gia, Huyện Tri Tôn, An Giang",
                expectedPrice = 150000L,
                priceMethod = "150.000 ₫/ha",
                requirements = "Có kinh nghiệm phun lúa trổ đều, máy T40 trở lên, thuốc có sẵn tại bờ.",
                executionDate = "05/10/2026",
                deadlineDate = "03/10/2026",
                status = PostStatus.OPEN,
                distanceKm = 4.2,
                viewCount = 135,
                responseCount = 4
            ),
            ServicePostEntity(
                id = "POST_002",
                authorId = userProvider1.id,
                authorName = userProvider1.fullName,
                authorPhone = userProvider1.phone,
                authorRole = userProvider1.role,
                postType = PostType.OFFER,
                categoryCode = "CAT_DRONE_THUOC",
                categoryName = "Drone phun thuốc",
                title = "Nhận phun thuốc & bón phân bằng Drone DJI Agras T40",
                description = "Đội bay 3 máy T40 sẵn sàng phục vụ bà con. Giọt sương siêu mịn, bám dính tốt, tiết kiệm 30% thuốc sâu, không giẫm nát lúa. Bao bay đều tất cả bờ góc.",
                areaOrQuantity = 50.0,
                unit = "ha/ngày",
                province = "An Giang",
                district = "Thoại Sơn",
                fullAddress = "Khu vực Thoại Sơn, Tri Tôn, Châu Thành (An Giang)",
                expectedPrice = 150000L,
                priceMethod = "150.000 ₫/ha",
                machineryCount = 3,
                dailyCapacity = "80 ha/ngày",
                requirements = "Bà con chuẩn bị sẵn nguồn nước sạch và thuốc theo nhu cầu.",
                executionDate = "Phục vụ 24/7",
                status = PostStatus.OPEN,
                distanceKm = 6.8,
                viewCount = 320,
                responseCount = 18
            ),
            ServicePostEntity(
                id = "POST_003",
                authorId = userCustomer2.id,
                authorName = userCustomer2.fullName,
                authorPhone = userCustomer2.phone,
                authorRole = userCustomer2.role,
                postType = PostType.DEMAND,
                categoryCode = "CAT_MAY_GAT",
                categoryName = "Máy cắt thu hoạch",
                title = "Cần thuê đội máy gặt đập liên hợp cắt 35 ha lúa OM 18",
                description = "Lúa chín rộ ngày 08/10, chân ruộng khô cứng dễ đi. Yêu cầu máy cắt sạch gốc, không làm đổ rạp, hạn chế rụng hạt. Bao ăn nghỉ cho anh em tài xế.",
                areaOrQuantity = 35.0,
                unit = "ha",
                province = "Đồng Tháp",
                district = "Tháp Mười",
                fullAddress = "Vùng lúa Đốc Binh Kiều, Tháp Mười, Đồng Tháp",
                expectedPrice = 280000L,
                priceMethod = "280.000 ₫/công (2.800.000 ₫/ha)",
                requirements = "Đội từ 2 đến 3 máy Kubota DC70 trở lên để cắt trong 2 ngày.",
                executionDate = "08/10/2026",
                deadlineDate = "06/10/2026",
                status = PostStatus.OPEN,
                distanceKm = 12.5,
                viewCount = 210,
                responseCount = 7
            ),
            ServicePostEntity(
                id = "POST_004",
                authorId = userProvider2.id,
                authorName = userProvider2.fullName,
                authorPhone = userProvider2.phone,
                authorRole = userProvider2.role,
                postType = PostType.OFFER,
                categoryCode = "CAT_MAY_GAT",
                categoryName = "Máy cắt thu hoạch",
                title = "Dịch vụ máy gặt Kubota DC-70G cắt bao sạch, nhanh chóng",
                description = "Có 4 dàn máy cắt hiện đại, tài xế kinh nghiệm trên 5 năm. Chuyên trị lúa sập, lúa ngã, đầm lầy. Đảm bảo lúa sạch hạt, bao đóng đẹp ngay mé bờ.",
                areaOrQuantity = 30.0,
                unit = "ha/ngày",
                province = "Đồng Tháp",
                district = "Tam Nông",
                fullAddress = "Nhận cắt tại Đồng Tháp, An Giang, Long An",
                expectedPrice = 270000L,
                priceMethod = "270.000 ₫/công",
                machineryCount = 4,
                dailyCapacity = "35 ha/ngày",
                executionDate = "Theo lịch đặt trước",
                status = PostStatus.OPEN,
                distanceKm = 15.0,
                viewCount = 412,
                responseCount = 22
            ),
            ServicePostEntity(
                id = "POST_005",
                authorId = userProvider3.id,
                authorName = userProvider3.fullName,
                authorPhone = userProvider3.phone,
                authorRole = userProvider3.role,
                postType = PostType.OFFER,
                categoryCode = "CAT_THU_MUA_LUA",
                categoryName = "Thu mua lúa tươi",
                title = "Bao tiêu và thu mua lúa tươi vụ Thu Đông giá cao tại bờ bao",
                description = "Thu mua các giống lúa: Đài Thơm 8, OM 18, OM 5451, Lúa Nhật, ST25. Tiền mặt ngay tại ruộng hoặc chuyển khoản liền tay. Trạm cân chuẩn 100%.",
                areaOrQuantity = 500.0,
                unit = "tấn",
                province = "An Giang",
                district = "Long Xuyên",
                fullAddress = "Các huyện thuộc An Giang và Kiên Giang",
                expectedPrice = 8200L,
                priceMethod = "8.200 ₫/kg (Theo giá thị trường từng ngày)",
                dailyCapacity = "200 tấn/ngày",
                executionDate = "Thu mua xuyên vụ",
                status = PostStatus.OPEN,
                distanceKm = 8.0,
                viewCount = 580,
                responseCount = 35
            )
        )
        dao.insertPosts(posts)

        // 4. Initial Wallets & Top-up transactions (Mandatory 3.000đ fee logic)
        val walletProvider1 = WalletEntity(
            userId = userProvider1.id,
            balance = 50000L, // 50.000đ initial balance
            updatedAt = now
        )
        val walletProvider2 = WalletEntity(
            userId = userProvider2.id,
            balance = 120000L, // 120.000đ
            updatedAt = now
        )
        dao.insertOrUpdateWallet(walletProvider1)
        dao.insertOrUpdateWallet(walletProvider2)

        // Wallet transaction history for Provider 1:
        // +50.000đ top up
        val wtx1 = WalletTransactionEntity(
            id = "WTX-2026-101",
            userId = userProvider1.id,
            amount = 53000L,
            balanceBefore = 0L,
            balanceAfter = 53000L,
            type = WalletTxType.TOP_UP,
            description = "Nạp tiền vào tài khoản (Mã: NAP-1001)",
            referenceId = "NAP-1001",
            status = "SUCCESS",
            timestamp = now - 86400000L * 3
        )
        // -3.000đ platform fee for previous completed deal
        val wtx2 = WalletTransactionEntity(
            id = "WTX-2026-102",
            userId = userProvider1.id,
            amount = -3000L,
            balanceBefore = 53000L,
            balanceAfter = 50000L,
            type = WalletTxType.PLATFORM_FEE,
            description = "Phí giao dịch dịch vụ Phun thuốc Drone (HD-2026-0075)",
            referenceId = "HD-2026-0075",
            status = "SUCCESS",
            timestamp = now - 86400000L * 1
        )
        dao.insertWalletTransactions(listOf(wtx1, wtx2))

        // 5. Initial Transactions
        // Completed deal (fee already deducted)
        val txCompleted = TransactionEntity(
            id = "HD-2026-0075",
            customerId = userCustomer1.id,
            customerName = userCustomer1.fullName,
            customerPhone = userCustomer1.phone,
            providerId = userProvider1.id,
            providerName = userProvider1.fullName,
            providerPhone = userProvider1.phone,
            postId = "POST_001",
            serviceName = "Phun thuốc rầy lúa bằng Drone",
            categoryCode = "CAT_DRONE_THUOC",
            location = "Xã Vĩnh Gia, Tri Tôn, An Giang",
            workDate = "22/09/2026",
            volume = 10.0,
            unit = "ha",
            unitPrice = 150000L,
            totalAmount = 1500000L, // 10 x 150.000 = 1.500.000đ
            terms = "Đã phun xong 10 ha, kiểm tra đạt 100%, lúa xanh tốt không dập gãy.",
            status = TransactionStatus.COMPLETED,
            platformFee = 3000L,
            feeDeducted = true,
            completedAt = now - 86400000L * 1,
            createdAt = now - 86400000L * 5
        )

        // Active deal: Example in section XXV:
        // Customer A: Nguyễn Văn An (20 ha)
        // Provider B: Đội Drone Miền Tây (Trần Quốc Hưng)
        // 20 x 150.000 = 3.000.000 VNĐ
        val txActive = TransactionEntity(
            id = "HD-2026-0081",
            customerId = userCustomer1.id,
            customerName = userCustomer1.fullName,
            customerPhone = userCustomer1.phone,
            providerId = userProvider1.id,
            providerName = userProvider1.fullName,
            providerPhone = userProvider1.phone,
            postId = "POST_001",
            serviceName = "Phun thuốc bằng drone cho 20 ha lúa",
            categoryCode = "CAT_DRONE_THUOC",
            location = "Tri Tôn, An Giang",
            workDate = "05/10/2026",
            volume = 20.0,
            unit = "ha",
            unitPrice = 150000L,
            totalAmount = 3000000L, // 20 x 150.000 = 3.000.000đ
            terms = "Phun đều 20 ha bằng 2 drone T40 trong buổi sáng. Nước và thuốc sẵn sàng tại ruộng. Sau hoàn tất thanh toán đủ 3.000.000đ.",
            status = TransactionStatus.IN_PROGRESS,
            platformFee = 3000L,
            feeDeducted = false,
            createdAt = now - 3600000L * 4
        )

        dao.insertTransactions(listOf(txCompleted, txActive))

        // 6. Sample Pending Top-Up Request (For Admin review demo)
        val pendingTopUp = TopUpRequestEntity(
            id = "NAP-1002",
            userId = userProvider1.id,
            userName = userProvider1.fullName,
            userPhone = userProvider1.phone,
            amount = 50000L,
            bankName = "Viettel Money",
            accountNumber = "0368666219",
            accountHolder = "NGUYEN VAN THANH",
            transferContent = "NAPTIEN USR_DRONE01",
            status = TopUpStatus.PENDING,
            createdAt = now - 1800000L
        )
        dao.insertTopUpRequest(pendingTopUp)

        // 7. Sample Reviews
        val review = ReviewEntity(
            id = "REV-001",
            transactionId = "HD-2026-0075",
            reviewerId = userCustomer1.id,
            reviewerName = userCustomer1.fullName,
            reviewerRole = UserRole.CUSTOMER,
            targetUserId = userProvider1.id,
            targetUserName = userProvider1.fullName,
            rating = 5,
            comment = "Đội bay rất chuyên nghiệp, đến đúng giờ 5h sáng, bay sát ngọn lúa không bị trôi thuốc. Vụ sau sẽ tiếp tục ủng hộ!",
            serviceName = "Drone phun thuốc",
            createdAt = now - 86400000L
        )
        dao.insertReview(review)

        // 8. Sample Chat Messages
        val convId = "CONV_${userCustomer1.id}_${userProvider1.id}"
        val msgs = listOf(
            ChatMessageEntity(
                id = "MSG-01",
                conversationId = convId,
                transactionId = "HD-2026-0081",
                senderId = userCustomer1.id,
                senderName = userCustomer1.fullName,
                senderRole = UserRole.CUSTOMER,
                receiverId = userProvider1.id,
                message = "Chào anh Hưng, tôi ở Tri Tôn cần phun thuốc sâu cuốn lá cho 20 ha lúa OM18 ngày 05/10.",
                timestamp = now - 3600000L * 5
            ),
            ChatMessageEntity(
                id = "MSG-02",
                conversationId = convId,
                transactionId = "HD-2026-0081",
                senderId = userProvider1.id,
                senderName = userProvider1.fullName,
                senderRole = UserRole.PROVIDER,
                receiverId = userCustomer1.id,
                message = "Dạ chào chú An, bên con sẵn sàng điều 2 máy T40. Đơn giá chuẩn sàn 150.000đ/ha bao bay kỹ, tổng 3.000.000đ chú nhé.",
                timestamp = now - 3600000L * 4
            ),
            ChatMessageEntity(
                id = "MSG-03",
                conversationId = convId,
                transactionId = "HD-2026-0081",
                senderId = userCustomer1.id,
                senderName = userCustomer1.fullName,
                senderRole = UserRole.CUSTOMER,
                receiverId = userProvider1.id,
                message = "Nhất trí! Chú vừa tạo hợp đồng trên sàn, cháu kiểm tra rồi nhận nhé.",
                timestamp = now - 3600000L * 3
            )
        )
        dao.insertMessages(msgs)

        // 9. Notifications
        val notif1 = NotificationEntity(
            id = "NOTIF-01",
            userId = userProvider1.id,
            title = "Hợp đồng mới được xác nhận",
            content = "Hợp đồng HD-2026-0081 cho dịch vụ Drone phun thuốc (20 ha) đã chuyển sang trạng thái Đang thực hiện.",
            type = "TRANSACTION",
            relatedId = "HD-2026-0081",
            isRead = false,
            createdAt = now - 3600000L
        )
        dao.insertNotification(notif1)
    }

    // --- TRANSACTION COMPLETION WITH STRICT 3.000 VNĐ FEE RULE ---

    /**
     * Finalizes the transaction when work is complete.
     * Enforces:
     * - Status becomes COMPLETED
     * - Minimum 3.000đ balance check on Provider wallet
     * - Exact 3.000đ platform fee deducted from Provider
     * - Idempotent (strictly deducted once)
     * - Balance log generated with before/after snapshots
     */
    suspend fun completeTransaction(transactionId: String): Result<String> = withContext(Dispatchers.IO) {
        val tx = dao.getTransactionByIdDirect(transactionId)
            ?: return@withContext Result.failure(Exception("Không tìm thấy hợp đồng $transactionId"))

        if (tx.status == TransactionStatus.COMPLETED) {
            return@withContext Result.success("Giao dịch đã được hoàn tất trước đó.")
        }

        val providerId = tx.providerId
        val wallet = dao.getWalletDirect(providerId) ?: WalletEntity(userId = providerId, balance = 0L)

        // Minimum balance verification
        if (wallet.balance < tx.platformFee) {
            val missing = tx.platformFee - wallet.balance
            return@withContext Result.failure(
                Exception(
                    "Số dư tài khoản nhà cung cấp (${wallet.balance} ₫) không đủ để thanh toán phí sàn 3.000 ₫. " +
                            "Cần nạp thêm tối thiểu $missing ₫ để hoàn tất giao dịch!"
                )
            )
        }

        // Idempotency check: Fee deduction
        val currentBalance = wallet.balance
        val fee = tx.platformFee
        val newBalance = currentBalance - fee

        val now = System.currentTimeMillis()

        if (!tx.feeDeducted) {
            // Update wallet
            dao.insertOrUpdateWallet(wallet.copy(balance = newBalance, updatedAt = now))

            // Create fee transaction log
            val feeTx = WalletTransactionEntity(
                id = "WTX-FEE-" + UUID.randomUUID().toString().take(8).uppercase(),
                userId = providerId,
                amount = -fee,
                balanceBefore = currentBalance,
                balanceAfter = newBalance,
                type = WalletTxType.PLATFORM_FEE,
                description = "Phí giao dịch dịch vụ ${tx.serviceName} ($transactionId)",
                referenceId = transactionId,
                status = "SUCCESS",
                timestamp = now
            )
            dao.insertWalletTransaction(feeTx)
        }

        // Update transaction status
        dao.updateTransaction(
            tx.copy(
                status = TransactionStatus.COMPLETED,
                feeDeducted = true,
                completedAt = now,
                updatedAt = now
            )
        )

        // Send notifications
        dao.insertNotification(
            NotificationEntity(
                id = "NOTIF-" + UUID.randomUUID().toString().take(8),
                userId = tx.customerId,
                title = "Giao dịch đã hoàn tất",
                content = "Giao dịch ${tx.serviceName} ($transactionId) đã hoàn tất thành công. Hãy để lại đánh giá!",
                type = "TRANSACTION",
                relatedId = transactionId
            )
        )
        dao.insertNotification(
            NotificationEntity(
                id = "NOTIF-" + UUID.randomUUID().toString().take(8),
                userId = tx.providerId,
                title = "Giao dịch hoàn tất - Phí sàn 3.000đ",
                content = "Giao dịch $transactionId hoàn tất. Hệ thống đã trừ phí dịch vụ 3.000đ. Số dư hiện tại: $newBalance ₫.",
                type = "WALLET",
                relatedId = transactionId
            )
        )

        Result.success("Hoàn tất giao dịch thành công! Đã trừ 3.000 ₫ phí dịch vụ.")
    }

    /**
     * Updates transaction status (e.g. Confirm, Start, Dispute).
     */
    suspend fun updateTransactionStatus(transactionId: String, newStatus: TransactionStatus): Result<Unit> = withContext(Dispatchers.IO) {
        val tx = dao.getTransactionByIdDirect(transactionId)
            ?: return@withContext Result.failure(Exception("Không tìm thấy hợp đồng"))

        dao.updateTransaction(tx.copy(status = newStatus, updatedAt = System.currentTimeMillis()))
        Result.success(Unit)
    }

    /**
     * Creates top-up request (10.000đ, 20.000đ, 50.000đ, 100.000đ).
     */
    suspend fun createTopUpRequest(userId: String, amount: Long): TopUpRequestEntity = withContext(Dispatchers.IO) {
        val user = dao.getUserByIdDirect(userId)
        val userName = user?.fullName ?: "Nhà cung cấp"
        val userPhone = user?.phone ?: ""
        val reqCode = "NAP-" + (10000 + (Math.random() * 89999).toInt())

        val request = TopUpRequestEntity(
            id = reqCode,
            userId = userId,
            userName = userName,
            userPhone = userPhone,
            amount = amount,
            transferContent = "NAPTIEN $userId",
            status = TopUpStatus.PENDING,
            createdAt = System.currentTimeMillis()
        )
        dao.insertTopUpRequest(request)

        dao.insertNotification(
            NotificationEntity(
                id = "NOTIF-" + UUID.randomUUID().toString().take(8),
                userId = userId,
                title = "Yêu cầu nạp tiền đang chờ duyệt",
                content = "Yêu cầu nạp ${amount} ₫ ($reqCode) đã được ghi nhận và đang chờ BQT xác nhận.",
                type = "WALLET",
                relatedId = reqCode
            )
        )

        request
    }

    /**
     * Admin approves top up request and credits provider's wallet.
     */
    suspend fun approveTopUpRequest(requestId: String, adminName: String): Result<String> = withContext(Dispatchers.IO) {
        val req = dao.getTopUpRequestById(requestId)
            ?: return@withContext Result.failure(Exception("Không tìm thấy yêu cầu nạp tiền $requestId"))

        if (req.status == TopUpStatus.APPROVED) {
            return@withContext Result.failure(Exception("Yêu cầu này đã được duyệt trước đó!"))
        }

        val now = System.currentTimeMillis()
        val wallet = dao.getWalletDirect(req.userId) ?: WalletEntity(userId = req.userId, balance = 0L)
        val balanceBefore = wallet.balance
        val balanceAfter = balanceBefore + req.amount

        // Credit wallet
        dao.insertOrUpdateWallet(wallet.copy(balance = balanceAfter, updatedAt = now))

        // Record wallet transaction
        val wtx = WalletTransactionEntity(
            id = "WTX-TOP-" + UUID.randomUUID().toString().take(8).uppercase(),
            userId = req.userId,
            amount = req.amount,
            balanceBefore = balanceBefore,
            balanceAfter = balanceAfter,
            type = WalletTxType.TOP_UP,
            description = "Nạp tiền vào tài khoản thành công (Mã: $requestId)",
            referenceId = requestId,
            status = "SUCCESS",
            timestamp = now
        )
        dao.insertWalletTransaction(wtx)

        // Update request status
        dao.updateTopUpRequest(
            req.copy(
                status = TopUpStatus.APPROVED,
                approvedAt = now,
                approvedBy = adminName
            )
        )

        // Send notification to provider
        dao.insertNotification(
            NotificationEntity(
                id = "NOTIF-" + UUID.randomUUID().toString().take(8),
                userId = req.userId,
                title = "Nạp tiền thành công +${req.amount} ₫",
                content = "Yêu cầu nạp $requestId đã được duyệt bởi $adminName. Số dư mới: $balanceAfter ₫.",
                type = "WALLET",
                relatedId = requestId
            )
        )

        Result.success("Đã duyệt nạp tiền thành công! Cộng ${req.amount} ₫ vào tài khoản ${req.userName}.")
    }

    /**
     * Admin rejects top-up request.
     */
    suspend fun rejectTopUpRequest(requestId: String, reason: String, adminName: String): Result<Unit> = withContext(Dispatchers.IO) {
        val req = dao.getTopUpRequestById(requestId)
            ?: return@withContext Result.failure(Exception("Không tìm thấy yêu cầu nạp tiền"))

        dao.updateTopUpRequest(
            req.copy(
                status = TopUpStatus.REJECTED,
                approvedAt = System.currentTimeMillis(),
                approvedBy = "$adminName ($reason)"
            )
        )
        Result.success(Unit)
    }

    suspend fun createServicePost(post: ServicePostEntity) = withContext(Dispatchers.IO) {
        dao.insertPost(post)
    }

    suspend fun createTransaction(tx: TransactionEntity) = withContext(Dispatchers.IO) {
        dao.insertTransaction(tx)
        // Notify provider
        dao.insertNotification(
            NotificationEntity(
                id = "NOTIF-" + UUID.randomUUID().toString().take(8),
                userId = tx.providerId,
                title = "Yêu cầu hợp đồng mới",
                content = "${tx.customerName} đã tạo yêu cầu hợp đồng cho dịch vụ ${tx.serviceName}.",
                type = "TRANSACTION",
                relatedId = tx.id
            )
        )
    }

    suspend fun addReview(review: ReviewEntity) = withContext(Dispatchers.IO) {
        dao.insertReview(review)
    }

    suspend fun sendChatMessage(msg: ChatMessageEntity) = withContext(Dispatchers.IO) {
        dao.insertMessage(msg)
    }

    suspend fun addCategory(category: ServiceCategoryEntity) = withContext(Dispatchers.IO) {
        dao.insertCategory(category)
    }

    suspend fun verifyUser(userId: String, isVerified: Boolean) = withContext(Dispatchers.IO) {
        dao.updateUserVerification(userId, isVerified)
    }

    suspend fun createDispute(dispute: DisputeEntity) = withContext(Dispatchers.IO) {
        dao.insertDispute(dispute)
        // mark transaction disputed
        val tx = dao.getTransactionByIdDirect(dispute.transactionId)
        if (tx != null) {
            dao.updateTransaction(tx.copy(status = TransactionStatus.DISPUTED))
        }
    }
}
