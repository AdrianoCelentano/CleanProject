package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<Asteroid> {

    override fun handlesEvent(asteroidViewEvent: AsteroidViewEvent): Boolean {
        return asteroidViewEvent is AsteroidViewEvent.Store
    }

    override fun execute(params: Asteroid): Observable<AsteroidViewResult> {
        return Observable.concat(saveAsteroid(params), emitUserMessageEffect())
    }

    private fun saveAsteroid(asteroid: Asteroid): Observable<AsteroidViewResult>? =
        nasaRepository.saveAsteroid(asteroid).toObservable()

    private fun emitUserMessageEffect(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess))
    }
}