package com.clean.data.cache

import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class NasaCache @Inject constructor(
    private val nasaDataBase: NasaDataBase
) {

    suspend fun saveAsteroid(asteroidEntity: AsteroidEntity) {
        return nasaDataBase.asteroidDao().insert(asteroidEntity)
    }

    suspend fun getAsteroids(): List<AsteroidEntity> {
        return nasaDataBase.asteroidDao().getAll()
    }
}