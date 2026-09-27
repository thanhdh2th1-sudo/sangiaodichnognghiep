package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats standard Vietnamese currency: 150.000 ₫, 3.000.000 ₫
 */
fun formatVnd(amount: Long): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply {
        groupingSeparator = '.'
    }
    val formatter = DecimalFormat("#,###", symbols)
    return "${formatter.format(amount)} ₫"
}

fun formatDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("vi", "VN"))
    return sdf.format(Date(millis))
}

fun formatShortDate(millis: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN"))
    return sdf.format(Date(millis))
}

fun getCategoryIcon(code: String): ImageVector {
    return when {
        code.contains("DRONE") -> Icons.Default.Flight
        code.contains("MAY_GAT") || code.contains("CAY") || code.contains("BUA") || code.contains("TRAT") -> Icons.Default.Agriculture
        code.contains("BOM") || code.contains("NUOC") -> Icons.Default.Opacity
        code.contains("CO") -> Icons.Default.Grass
        code.contains("THU_MUA") -> Icons.Default.Yard
        code.contains("VAN_CHUYEN") -> Icons.Default.LocalShipping
        code.contains("SAY") -> Icons.Default.Science
        else -> Icons.Default.Category
    }
}
