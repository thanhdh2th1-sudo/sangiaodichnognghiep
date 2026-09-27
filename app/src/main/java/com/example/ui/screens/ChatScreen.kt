package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.components.formatDate
import com.example.ui.theme.AgriBlueLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    currentUser: UserEntity?,
    partnerName: String,
    partnerId: String,
    transactionId: String = "",
    messages: List<ChatMessageEntity>,
    onSendMessage: (String) -> Unit,
    onBack: () -> Unit
) {
    var textMessage by remember { mutableStateOf("") }

    val quickReplies = listOf(
        "Bên em sẵn sàng thực hiện đúng hẹn",
        "Giá trọn gói 150.000 ₫/ha",
        "Máy bay T40 công suất 50 ha/ngày",
        "Đã tạo hợp đồng trên sàn, anh kiểm tra nhé"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(AgriGreenPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(partnerName.take(1), fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(partnerName, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(
                                if (transactionId.isNotEmpty()) "Gắn với hợp đồng: $transactionId" else "Trao đổi dịch vụ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    // Quick reply chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        quickReplies.forEach { reply ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = AgriGreenLight,
                                modifier = Modifier.clickable {
                                    textMessage = reply
                                }
                            ) {
                                Text(
                                    text = reply,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = AgriGreenPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    // Input Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = textMessage,
                            onValueChange = { textMessage = it },
                            placeholder = { Text("Nhập tin nhắn trao đổi...", fontSize = 13.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AgriGreenPrimary
                            ),
                            maxLines = 3
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (textMessage.isNotBlank()) {
                                    onSendMessage(textMessage.trim())
                                    textMessage = ""
                                }
                            },
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(AgriGreenPrimary)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Gửi", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        },
        modifier = Modifier.testTag("chat_screen")
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("💬", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Bắt đầu cuộc trao đổi", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                "Thỏa thuận kỹ thuật, địa điểm ruộng và thời gian triển khai với đối tác.",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(messages, key = { it.id }) { msg ->
                    val isMe = msg.senderId == currentUser?.id
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            shape = RoundedCornerShape(
                                topStart = 14.dp,
                                topEnd = 14.dp,
                                bottomStart = if (isMe) 14.dp else 2.dp,
                                bottomEnd = if (isMe) 2.dp else 14.dp
                            ),
                            color = if (isMe) AgriGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                if (!isMe) {
                                    Text(
                                        text = msg.senderName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = AgriGreenPrimary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                                Text(
                                    text = msg.message,
                                    fontSize = 13.sp,
                                    color = if (isMe) Color.White else MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 18.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = formatDate(msg.timestamp),
                                    fontSize = 9.sp,
                                    color = if (isMe) Color.White.copy(alpha = 0.7f) else Color.Gray,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
