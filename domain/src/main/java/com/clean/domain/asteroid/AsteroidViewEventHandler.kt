package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import io.reactivex.Observable
import javax.inject.Inject

class AsteroidViewEventHandler @Inject constructor(
    private val getAsteroidOfTheDay: GetAsteroidOfTheDay
) {

    fun process(viewEvent: AsteroidViewEvent): Observable<AsteroidViewResult> {
        return when (viewEvent) {
            AsteroidViewEvent.Init -> getAsteroidUseCase()
            AsteroidViewEvent.Store -> Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage("test"))
            AsteroidViewEvent.Refresh -> getAsteroidUseCase()
        }
    }

    private fun getAsteroidUseCase(): Observable<AsteroidViewResult> {
        return Observable.concat(emitLoading(), emitAsteroid(), emitEffect())
            .onErrorReturn { throwable ->
                when (throwable) {
                    is Exception -> AsteroidViewResult.AsteroidPartialState.Error("Error loading Asteroid")
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
        return getAsteroidOfTheDay.execute()
            .map { AsteroidViewResult.AsteroidPartialState.NewAsteroid(it) }
    }
}