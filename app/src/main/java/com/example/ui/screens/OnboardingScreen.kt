package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

data class OnboardingStep(
    val icon: ImageVector,
    val badge: String,
    val title: String,
    val description: String
)

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    val steps = listOf(
        OnboardingStep(
            icon = Icons.Default.Flight,
            badge = "🌾 NỀN TẢNG NÔNG NGHIỆP SỐ",
            title = "SÀN DỊCH VỤ NÔNG NGHIỆP",
            description = "“Kết nối nhu cầu – Tìm dịch vụ – Giao dịch thuận tiện”\n\nNền tảng tiên phong kết nối trực tiếp Nông dân, Hợp tác xã và Nhà cung cấp cơ giới hóa, máy bay không người lái tại Việt Nam."
        ),
        OnboardingStep(
            icon = Icons.Default.Agriculture,
            badge = "🔎 TÌM KIẾM DỊCH VỤ",
            title = "Tìm dịch vụ nông nghiệp dễ dàng",
            description = "Dễ dàng tìm kiếm Drone phun thuốc & gieo sạ, máy cắt lúa, máy cày, máy bừa, bơm nước và đơn vị thu mua nông sản uy tín gần bạn nhất với bản đồ radar."
        ),
        OnboardingStep(
            icon = Icons.Default.PostAdd,
            badge = "📝 ĐĂNG BÀI NHANH CHÓNG",
            title = "Đăng nhu cầu & Nhận nhà cung cấp",
            description = "Nông dân chỉ cần vài thao tác để đăng diện tích ruộng. Các đội máy và phi công drone chuyên nghiệp sẽ liên hệ báo giá cạnh tranh ngay lập tức."
        ),
        OnboardingStep(
            icon = Icons.Default.AssignmentTurnedIn,
            badge = "🛡️ MINH BẠCH & AN TOÀN",
            title = "Giao dịch minh bạch – Quản lý thuận tiện",
            description = "Hợp đồng rõ ràng, xác nhận hoàn thành công việc trực tiếp trên sàn. Phí sàn cố định chỉ 3.000 ₫/giao dịch. Bảo vệ tối đa quyền lợi cho nhà nông."
        )
    )

    var currentStepIndex by remember { mutableIntStateOf(0) }
    val step = steps[currentStepIndex]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE8F5E9), Color.White)
                )
            )
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Skip Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (currentStepIndex < steps.size - 1) {
                    TextButton(onClick = onComplete) {
                        Text("Bỏ qua", color = AgriGreenPrimary, fontWeight = FontWeight.Bold)
                    }
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                }
            }

            // Animated Main Content Card
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "onboarding_step"
            ) { targetStep ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Big Icon Box
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(AgriGreenPrimary, AgriGreenSecondary)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = targetStep.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(54.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = AgriGreenLight
                    ) {
                        Text(
                            text = targetStep.badge,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AgriGreenPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = targetStep.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 30.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = targetStep.description,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            // Bottom Navigation & Indicator Row
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Step Indicator Dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    steps.indices.forEach { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentStepIndex) 24.dp else 8.dp, 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (index == currentStepIndex) AgriGreenPrimary else Color.LightGray
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Next or Complete Button
                Button(
                    onClick = {
                        if (currentStepIndex < steps.size - 1) {
                            currentStepIndex++
                        } else {
                            onComplete()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStepIndex == steps.size - 1) AgriAmberAccent else AgriGreenPrimary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Text(
                        text = if (currentStepIndex == steps.size - 1) "BẮT ĐẦU SỬ DỤNG" else "TIẾP TỤC",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = if (currentStepIndex == steps.size - 1) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        }
    }
}
