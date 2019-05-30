package com.clean.asteroids

import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.Observable
import javax.inject.Inject

class EventToResultProcessor @Inject constructor(
    private val getAsteroidOfTheDay: GetAsteroidOfTheDay
) {

    fun process(viewEvent: ViewEvent): Observable<Result> {
        return when (viewEvent) {
            ViewEvent.Init -> getAsteroidUseCase()
            ViewEvent.Store -> Observable.just(Result.Effect)
            ViewEvent.Refresh -> getAsteroidUseCase()
        }
    }

    private fun getAsteroidUseCase(): Observable<Result> {
        return getAsteroidOfTheDay.execute()
            .map { Result.NewAsteroid(it) }
    }
}