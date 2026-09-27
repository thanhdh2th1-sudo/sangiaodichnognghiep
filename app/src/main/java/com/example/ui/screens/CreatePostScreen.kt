package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.PostType
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.UserEntity
import com.example.ui.theme.AgriAmberAccent
import com.example.ui.theme.AgriAmberLight
import com.example.ui.theme.AgriGreenLight
import com.example.ui.theme.AgriGreenPrimary
import com.example.ui.theme.AgriGreenSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    currentUser: UserEntity?,
    categories: List<ServiceCategoryEntity>,
    onPostCreated: (
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
        machineryCount: Int,
        dailyCapacity: String,
        executionDate: String
    ) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Nhu cầu (Demand), 1: Cung cấp (Offer)

    // Form fields
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull()) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var areaOrQuantityStr by remember { mutableStateOf("20") }
    var unit by remember { mutableStateOf("ha") }
    var province by remember { mutableStateOf("An Giang") }
    var district by remember { mutableStateOf("Tri Tôn") }
    var fullAddress by remember { mutableStateOf("Ấp Vĩnh Gia, Xã Vĩnh Gia") }
    var priceStr by remember { mutableStateOf("150000") }
    var priceMethod by remember { mutableStateOf("Theo ha") }
    var requirements by remember { mutableStateOf("Có kinh nghiệm phun lúa, thiết bị drone bay chuẩn.") }
    var machineryCountStr by remember { mutableStateOf("2") }
    var dailyCapacity by remember { mutableStateOf("50 ha/ngày") }
    var executionDate by remember { mutableStateOf("05/10/2026") }

    var validationError by remember { mutableStateOf<String?>(null) }

    val postType = if (selectedTabIndex == 0) PostType.DEMAND else PostType.OFFER

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("create_post_screen"),
        contentPadding = PaddingValues(16.dp, 12.dp, 16.dp, 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tab Selector: Bên cần dịch vụ vs Bên cung cấp
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = AgriGreenPrimary
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            if (title.isEmpty()) title = "Cần drone phun thuốc cho 20 ha lúa"
                        },
                        text = {
                            Text(
                                "🌾 TÔI CẦN DỊCH VỤ\n(Đăng nhu cầu)",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                            if (title.isEmpty() || title.contains("Cần")) title = "Nhận phun thuốc lúa bằng Drone T40"
                        },
                        text = {
                            Text(
                                "🚜 TÔI CUNG CẤP\n(Đăng dịch vụ)",
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    )
                }
            }
        }

        // Info Banner
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (selectedTabIndex == 0) AgriAmberLight else AgriGreenLight,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = if (selectedTabIndex == 0) AgriAmberAccent else AgriGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedTabIndex == 0)
                            "Đăng nhu cầu của bạn để các chủ máy móc & drone gửi báo giá cạnh tranh nhất!"
                        else
                            "Đăng dịch vụ để tiếp cận hàng ngàn nông dân và hợp tác xã trong khu vực.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Category Selector
        item {
            Text("Loại dịch vụ nông nghiệp *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            ExposedDropdownMenuBox(
                expanded = categoryDropdownExpanded,
                onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCategory?.name ?: "Chọn danh mục dịch vụ",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AgriGreenPrimary
                    )
                )
                ExposedDropdownMenu(
                    expanded = categoryDropdownExpanded,
                    onDismissRequest = { categoryDropdownExpanded = false }
                ) {
                    categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.name) },
                            onClick = {
                                selectedCategory = cat
                                categoryDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Title
        item {
            Text("Tiêu đề bài đăng *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = {
                    Text(
                        if (selectedTabIndex == 0) "Ví dụ: Cần drone phun thuốc cho 25 ha lúa"
                        else "Ví dụ: Nhận phun thuốc lúa bằng Drone T40"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Description
        item {
            Text("Mô tả chi tiết *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = {
                    Text("Ghi rõ tình trạng ruộng, loại giống lúa, yêu cầu kỹ thuật...")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 4
            )
        }

        // Area/Quantity & Unit
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        if (selectedTabIndex == 0) "Diện tích / Khối lượng *" else "Năng lực / Quy mô *",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = areaOrQuantityStr,
                        onValueChange = { areaOrQuantityStr = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.width(110.dp)) {
                    Text("Đơn vị", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Location: Province & District
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Tỉnh / Thành phố *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = province,
                        onValueChange = { province = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Huyện / Xã *", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Full Address
        item {
            Text("Địa chỉ cụ thể / Tên cánh đồng", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = fullAddress,
                onValueChange = { fullAddress = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Expected Price & Price Method
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1.3f)) {
                    Text(
                        if (selectedTabIndex == 0) "Giá dự kiến (VNĐ) *" else "Giá cước dịch vụ (VNĐ) *",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("150000") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text("Phương thức", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = priceMethod,
                        onValueChange = { priceMethod = it },
                        placeholder = { Text("Theo ha") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Provider specific fields or Demand specific fields
        if (selectedTabIndex == 1) {
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Số lượng thiết bị/máy", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = machineryCountStr,
                            onValueChange = { machineryCountStr = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1.5f)) {
                        Text("Công suất ngày", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = dailyCapacity,
                            onValueChange = { dailyCapacity = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        } else {
            item {
                Text("Thời gian cần thực hiện", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = executionDate,
                    onValueChange = { executionDate = it },
                    placeholder = { Text("Ví dụ: 05/10/2026") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            item {
                Text("Yêu cầu đối với nhà cung cấp", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = requirements,
                    onValueChange = { requirements = it },
                    placeholder = { Text("Yêu cầu về kinh nghiệm, máy móc, thời gian hoàn thành...") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }

        // Validation Error Display
        if (validationError != null) {
            item {
                Text(
                    text = validationError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Submit Button
        item {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        validationError = "Vui lòng nhập tiêu đề bài đăng"
                        return@Button
                    }
                    val cat = selectedCategory ?: categories.firstOrNull()
                    if (cat == null) {
                        validationError = "Vui lòng chọn danh mục dịch vụ"
                        return@Button
                    }
                    val qty = areaOrQuantityStr.toDoubleOrNull() ?: 1.0
                    val price = priceStr.toLongOrNull() ?: 0L
                    val machCount = machineryCountStr.toIntOrNull() ?: 1

                    validationError = null
                    onPostCreated(
                        postType,
                        cat.code,
                        cat.name,
                        title.trim(),
                        description.trim(),
                        qty,
                        unit.trim(),
                        province.trim(),
                        district.trim(),
                        fullAddress.trim(),
                        price,
                        priceMethod.trim(),
                        requirements.trim(),
                        machCount,
                        dailyCapacity.trim(),
                        executionDate.trim()
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedTabIndex == 0) AgriAmberAccent else AgriGreenPrimary
                )
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedTabIndex == 0) "ĐĂNG NHU CẦU" else "ĐĂNG DỊCH VỤ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
