package com.clean.data.cache

import androidx.room.*
import com.clean.data.cache.DBConstants.ASTEROID_TABLE_NAME
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM ${ASTEROID_TABLE_NAME}")
    fun getAll(): Observable<List<AsteroidEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroidEntity: AsteroidEntity): Completable

    @Delete
    fun delete(asteroidEntity: AsteroidEntity): Completable
}