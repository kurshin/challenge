package com.tastytrade.kurshin.data.persisted

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tastytrade.kurshin.data.persisted.dao.WatchListDao
import com.tastytrade.kurshin.data.persisted.entity.QuoteEntity
import com.tastytrade.kurshin.data.persisted.entity.WatchListEntity

@Database(entities = [WatchListEntity::class, QuoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun watchListDao(): WatchListDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tastytrade_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}