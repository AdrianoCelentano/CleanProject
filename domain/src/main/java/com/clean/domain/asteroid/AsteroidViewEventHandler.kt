package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.usecase.AsteroidUseCase
import kotlinx.coroutines.channels.SendChannel
import javax.inject.Inject

class AsteroidViewEventHandler @Inject constructor(
    private val useCases: Set<@JvmSuppressWildcards AsteroidUseCase<AsteroidViewEvent>>
) {

    suspend fun handleEvent(
        viewEvent: AsteroidViewEvent,
        resultChannel: SendChannel<AsteroidViewResult>
    ) {
        useCases
            .find { it.isForEvent(viewEvent) }
            .let { requireNotNull(it) { "No UseCase supports the ViewEvent: $viewEvent" } }
            .execute(viewEvent, resultChannel)
    }
}