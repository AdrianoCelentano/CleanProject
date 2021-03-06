package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable

interface AsteroidUseCase<in T : AsteroidViewEvent> {

    fun execute(event: T): Observable<AsteroidViewResult>

    fun isForEvent(event: AsteroidViewEvent): Boolean

}
