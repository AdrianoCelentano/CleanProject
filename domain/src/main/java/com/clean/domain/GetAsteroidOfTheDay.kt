package com.clean.domain

import io.reactivex.Single
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(private val nasaRepository: NasaRepository) {

    fun execute(): Single<Asteroid> {
        return nasaRepository.getAsteroidOfTheDay()
    }
}