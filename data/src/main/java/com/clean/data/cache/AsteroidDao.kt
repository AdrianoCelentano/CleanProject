package com.clean.data.cache

import androidx.room.*
import com.clean.data.cache.DBConstants.ASTEROID_TABLE_NAME
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM ${ASTEROID_TABLE_NAME}")
    suspend fun getAll(): List<AsteroidEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroidEntity: AsteroidEntity)

    @Delete
    suspend fun delete(asteroidEntity: AsteroidEntity)
}