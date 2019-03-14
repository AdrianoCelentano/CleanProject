package com.clean.domain

import io.reactivex.Single

interface NasaRepository {

    fun getAsteroidOfTheDay(): Single<Asteroid>
}