package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import com.clean.domain.asteroid.usecase.SaveAsteroid
import io.reactivex.Observable
import javax.inject.Inject

class AsteroidViewEventHandler @Inject constructor(
    private val getAsteroidOfTheDay: GetAsteroidOfTheDay,
    private val saveAsteroid: SaveAsteroid
) {

    fun process(viewEvent: AsteroidViewEvent): Observable<AsteroidViewResult> {
        return when (viewEvent) {
            AsteroidViewEvent.Init -> getAsteroidOfTheDay.execute()
            AsteroidViewEvent.Refresh -> getAsteroidOfTheDay.execute()
            is AsteroidViewEvent.Store -> saveAsteroid.execute(viewEvent.asteroid)
        }
    }
}