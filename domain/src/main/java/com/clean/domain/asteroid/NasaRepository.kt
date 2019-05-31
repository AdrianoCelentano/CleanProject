package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Observable

interface NasaRepository {

    fun getAsteroidOfTheDay(): Observable<Asteroid>
}

class RemoteError(message: String) : Exception(message)