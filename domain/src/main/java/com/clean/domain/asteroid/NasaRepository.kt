package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable

interface NasaRepository {

    fun getAsteroidOfTheDay(): Observable<Asteroid>

    fun getSavedAsteroid(): Observable<List<Asteroid>>

    fun saveAsteroid(asteroid: Asteroid): Completable
}

class DataError(message: String) : Exception(message)