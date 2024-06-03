package com.tastytrade.kurshin.data.persisted.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes",
    foreignKeys = [ForeignKey(
        entity = WatchListEntity::class,
        parentColumns = ["id"],
        childColumns = ["watchlistId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val watchlistId: Int,
    val lastPrice: Double = 0.0,
    val askPrice: Double = 0.0,
    val bidPrice: Double = 0.0
)