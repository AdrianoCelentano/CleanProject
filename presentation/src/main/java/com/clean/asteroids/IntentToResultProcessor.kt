package com.clean.asteroids

import android.util.Log
import com.clean.asteroids.mapper.AsteroidMapper
import com.clean.asteroids.model.Asteroid
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.Observable

class IntentToResultProcessor(private val getAsteroidOfTheDay: GetAsteroidOfTheDay) {

    fun process(viewIntent: ViewIntent): Observable<Asteroid> {
        Log.d("qwer", "intent: ${viewIntent.javaClass.simpleName}")
        return when (viewIntent) {
            ViewIntent.Init -> getAsteroidUseCase()
            ViewIntent.Store -> Observable.just(Asteroid())
            ViewIntent.Refresh -> getAsteroidUseCase()
        }
    }

    private fun getAsteroidUseCase() =
        getAsteroidOfTheDay.execute().map { AsteroidMapper().map(it) }
}