package com.tastytrade.kurshin.data.persisted.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlists")
data class WatchListEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String
)