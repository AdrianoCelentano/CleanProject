package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable

interface AsteroidUseCase<in Params> {

    fun execute(params: Params): Observable<AsteroidViewResult>

    fun handlesEvent(asteroidViewEvent: AsteroidViewEvent): Boolean
}