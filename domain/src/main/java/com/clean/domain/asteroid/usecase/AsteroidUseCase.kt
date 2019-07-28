package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import kotlinx.coroutines.channels.SendChannel

interface AsteroidUseCase<in T : AsteroidViewEvent> {

    suspend fun execute(
        event: T,
        resultChannel: SendChannel<AsteroidViewResult>
    )

    fun isForEvent(event: AsteroidViewEvent): Boolean

}
