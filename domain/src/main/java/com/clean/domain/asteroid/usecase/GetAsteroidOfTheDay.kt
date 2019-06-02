package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.RemoteError
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) {

    fun execute(): Observable<AsteroidViewResult> {
        return Observable.concat(emitLoading(), emitAsteroid(), emitUserMessageEffect())
            .onErrorReturn { throwable ->
                when (throwable) {
                    is RemoteError -> AsteroidViewResult.AsteroidPartialState.Error(stringProvider.serverError)
                    else -> AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError)
                }
            }
    }

    private fun emitUserMessageEffect(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.messageInABottle))
    }

    private fun emitLoading(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidPartialState.Loading)
    }

    private fun emitAsteroid(): Observable<AsteroidViewResult> {
        return nasaRepository.getAsteroidOfTheDay()
            .map { AsteroidViewResult.AsteroidPartialState.NewAsteroid(it) }
    }
}