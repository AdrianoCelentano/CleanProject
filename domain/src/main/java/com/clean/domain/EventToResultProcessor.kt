package com.clean.domain

import com.clean.domain.GetAsteroidOfTheDay
import com.clean.domain.ViewEvent
import com.clean.domain.ViewResult
import io.reactivex.Observable
import javax.inject.Inject

class EventToResultProcessor @Inject constructor(
    private val getAsteroidOfTheDay: GetAsteroidOfTheDay
) {

    fun process(viewEvent: ViewEvent): Observable<ViewResult> {
        return when (viewEvent) {
            ViewEvent.Init -> getAsteroidUseCase()
            ViewEvent.Store -> Observable.just(ViewResult.Effect)
            ViewEvent.Refresh -> getAsteroidUseCase()
        }
    }

    private fun getAsteroidUseCase(): Observable<ViewResult> {
        return getAsteroidOfTheDay.execute()
            .map { ViewResult.NewAsteroid(it) }
    }
}