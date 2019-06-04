package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.RemoteError
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import javax.inject.Inject

class GetAsteroidOfTheDay @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Load> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Load
    }

    override fun execute(event: AsteroidViewEvent.Load): Observable<AsteroidViewResult> {
        return Observable.concat(emitLoading(), emitAsteroid())
            .onErrorReturn { throwable ->
                when (throwable) {
                    is RemoteError -> AsteroidViewResult.AsteroidPartialState.Error(stringProvider.serverError)
                    else -> AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError)
                }
            }
    }

    private fun emitLoading(): Observable<AsteroidViewResult> {
        return Observable.just(AsteroidViewResult.AsteroidPartialState.Loading)
    }

    private fun emitAsteroid(): Observable<AsteroidViewResult> {
        return nasaRepository.getAsteroidOfTheDay()
            .map { AsteroidViewResult.AsteroidPartialState.NewAsteroid(it) }
    }
}