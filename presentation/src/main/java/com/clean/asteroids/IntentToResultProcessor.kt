package com.clean.asteroids

import android.util.Log
import com.clean.asteroids.mapper.AsteroidMapper
import com.clean.asteroids.model.Asteroid
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.Observable
import io.reactivex.rxkotlin.cast
import javax.inject.Inject

class IntentToResultProcessor @Inject constructor(private val getAsteroidOfTheDay: GetAsteroidOfTheDay,
                                                  private val asteroidMapper: AsteroidMapper) {

    fun process(viewIntent: ViewIntent): Observable<Result> {
        return when (viewIntent) {
            ViewIntent.Init -> getAsteroidUseCase()
            ViewIntent.Store -> Observable.just(Result.NoChange)
            ViewIntent.Refresh -> getAsteroidUseCase()
        }
    }

    private fun getAsteroidUseCase(): Observable<Result> {
        return getAsteroidOfTheDay.execute()
            .map { asteroidMapper.map(it) }
            .map { Result.NewAsteroid(it) }
    }
}