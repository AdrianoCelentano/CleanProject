package com.clean.domain.asteroid.usecase

import com.clean.domain.asteroid.NasaRepository
import com.clean.domain.asteroid.StringProvider
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SaveAsteroid @Inject constructor(
    private val nasaRepository: NasaRepository,
    private val stringProvider: StringProvider
) : AsteroidUseCase<AsteroidViewEvent.Store> {

    override fun isForEvent(event: AsteroidViewEvent): Boolean {
        return event is AsteroidViewEvent.Store
    }

    override suspend fun execute(
        event: AsteroidViewEvent.Store
    ): Flow<AsteroidViewResult> {
        return flow {
            try {
                saveAsteroid(event)
                emit(userMessageEffect())
            } catch (excpetion: Exception) {
                println(excpetion.message)
                emit(AsteroidViewResult.AsteroidPartialState.Error(stringProvider.generalError))
            }
        }

    }

    private suspend fun saveAsteroid(event: AsteroidViewEvent.Store) {
        return nasaRepository.saveAsteroid(event.asteroid)
    }

    private fun userMessageEffect(): AsteroidViewResult {
        return AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess)
    }
}