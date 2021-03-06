package com.clean.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.clean.data.cache.DBConstants.ASTEROID_TABLE_NAME

@Entity(tableName = ASTEROID_TABLE_NAME)
data class AsteroidEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String? = null,
    val url: String? = null
)