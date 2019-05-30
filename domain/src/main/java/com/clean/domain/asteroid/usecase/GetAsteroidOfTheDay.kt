package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Observable
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(private val nasaRepository: NasaRepository) {

    fun execute(): Observable<Asteroid> {
        return nasaRepository.getAsteroidOfTheDay()
    }
}