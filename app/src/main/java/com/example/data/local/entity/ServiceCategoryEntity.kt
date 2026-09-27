package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_categories")
data class ServiceCategoryEntity(
    @PrimaryKey val code: String,
    val name: String,
    val iconKey: String,
    val description: String,
    val defaultUnit: String = "ha",
    val isActive: Boolean = true,
    val sortOrder: Int = 0
)
