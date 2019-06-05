package com.clean.data.cache

import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class NasaCache @Inject constructor(
    private val nasaDataBase: NasaDataBase
) {

    fun saveAsteroid(asteroidEntity: AsteroidEntity): Completable {
        return nasaDataBase.asteroidDao().insert(asteroidEntity)
    }

    fun getAsteroids(): Observable<List<AsteroidEntity>> {
        return nasaDataBase.asteroidDao().getAll()
    }
}