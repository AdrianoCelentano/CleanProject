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
            AsteroidViewEvent.Init -> getAsteroidOfTheDay.execute()
            AsteroidViewEvent.Store -> Observable.just(AsteroidViewResult.AsteroidViewEffect.UserMessage("test"))
            AsteroidViewEvent.Refresh -> getAsteroidOfTheDay.execute()
        }
    }
}