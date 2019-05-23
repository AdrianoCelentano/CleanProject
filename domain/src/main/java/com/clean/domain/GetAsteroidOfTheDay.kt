package com.clean.domain

import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(private val nasaRepository: NasaRepository) {

    fun execute(): Observable<Asteroid> {
        return nasaRepository.getAsteroidOfTheDay()
    }
}