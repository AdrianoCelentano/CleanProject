package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) {
    fun execute(asteroid: Asteroid): Observable<AsteroidViewResult> {
        return Observable.concat(saveAsteroid(asteroid), emitUserMessageEffect())
    }

    private fun saveAsteroid(asteroid: Asteroid): Observable<AsteroidViewResult>? =
        nasaRepository.saveAsteroid(asteroid).toObservable()

    private fun emitUserMessageEffect(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess))
    }
}