package com.clean.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(AsteroidEntity::class), version = 1)
abstract class NasaDataBase : RoomDatabase() {
    abstract fun asteroidDao(): AsteroidDao
}