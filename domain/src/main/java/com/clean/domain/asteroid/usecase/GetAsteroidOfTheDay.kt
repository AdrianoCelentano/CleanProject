package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.RemoteError
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(private val nasaRepository: NasaRepository) {

    fun execute(): Observable<AsteroidViewResult> {
        return Observable.concat(emitLoading(), emitAsteroid(), emitEffect())
            .onErrorReturn { throwable ->
                when (throwable) {
                    is RemoteError -> AsteroidViewResult.AsteroidPartialState.Error("Error loading Asteroid from Server")
                    else -> AsteroidViewResult.AsteroidPartialState.Error("Error loading Asteroid")
                }
            }
    }

    private fun emitEffect(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage("message in a bottle"))
    }

    private fun emitLoading(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidPartialState.Loading)
    }

    private fun emitAsteroid(): Observable<AsteroidViewResult> {
        return nasaRepository.getAsteroidOfTheDay()
            .map { AsteroidViewResult.AsteroidPartialState.NewAsteroid(it) }
    }
}