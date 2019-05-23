package com.clean.domain

import io.reactivex.Observable
import io.reactivex.Single

interface NasaRepository {

    fun getAsteroidOfTheDay(): Observable<Asteroid>
}