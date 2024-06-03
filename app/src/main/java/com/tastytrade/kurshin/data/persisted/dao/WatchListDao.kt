package com.tastytrade.kurshin.data.persisted.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tastytrade.kurshin.data.persisted.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {
    @Query("SELECT * FROM watchlists")
    fun getAllWatchLists(): Flow<List<WatchListEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWatchList(watchList: WatchListEntity)

    @Update
    suspend fun updateWatchList(watchList: WatchListEntity)

    @Delete
    suspend fun deleteWatchList(watchList: WatchListEntity)
}