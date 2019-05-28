package com.clean.asteroids

import com.clean.asteroids.mapper.AsteroidMapper
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.Observable
import javax.inject.Inject

class EventToResultProcessor @Inject constructor(
    private val getAsteroidOfTheDay: GetAsteroidOfTheDay,
    private val asteroidMapper: AsteroidMapper
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
            .map { asteroidMapper.map(it) }
            .map { Result.NewAsteroid(it) }
    }
}