package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable

interface NasaRepository {

    fun getAsteroidOfTheDay(): Observable<Asteroid>

    fun saveAsteroid(asteroid: Asteroid): Completable
}

class RemoteError(message: String) : Exception(message)