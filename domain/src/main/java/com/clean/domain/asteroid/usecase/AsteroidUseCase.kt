package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import io.reactivex.Observable
import kotlinx.coroutines.channels.ReceiveChannel

interface AsteroidUseCase<in T : AsteroidViewEvent> {

    suspend fun execute(event: T): ReceiveChannel<AsteroidViewResult>

    fun isForEvent(event: AsteroidViewEvent): Boolean

}
