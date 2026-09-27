package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AgriDao
import com.example.data.local.entity.ChatMessageEntity
import com.example.data.local.entity.DisputeEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.ReviewEntity
import com.example.data.local.entity.ServiceCategoryEntity
import com.example.data.local.entity.ServicePostEntity
import com.example.data.local.entity.TopUpRequestEntity
import com.example.data.local.entity.TransactionEntity
import com.example.data.local.entity.UserEntity
import com.example.data.local.entity.WalletEntity
import com.example.data.local.entity.WalletTransactionEntity

@Database(
    entities = [
        UserEntity::class,
        ServiceCategoryEntity::class,
        ServicePostEntity::class,
        TransactionEntity::class,
        WalletEntity::class,
        WalletTransactionEntity::class,
        TopUpRequestEntity::class,
        ReviewEntity::class,
        ChatMessageEntity::class,
        NotificationEntity::class,
        DisputeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun agriDao(): AgriDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agri_marketplace_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
