package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable

interface NasaRepository {

    suspend fun getAsteroidOfTheDay(): Asteroid

    suspend fun getSavedAsteroid(): List<Asteroid>

    suspend fun saveAsteroid(asteroid: Asteroid)
}

class DataError(message: String) : Exception(message)