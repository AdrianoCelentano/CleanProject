package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.*
import com.clean.domain.asteroid.usecase.AsteroidUseCase
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import com.clean.domain.asteroid.usecase.SaveAsteroid
import io.reactivex.Observable
import org.assertj.core.api.Assertions
import org.junit.Test

class AsteroidViewFlowTest {

    private val repository: NasaRepository = NasaRepositoryFake()

    private val stringProvider: StringProvider = StringProviderFake()

    private val asteroidViewFlow: AsteroidViewFlow = asteroidViewFlow()

    @Test
    fun `when event is store then return effect user message`() {
        val asteroid = Asteroid("title", "url")
        val (effects, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Store(asteroid)))

        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(1)
            .assertValue(AsteroidViewResult.AsteroidViewEffect.UserMessage(stringProvider.storeAsteroidSuccess))

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(1)
        Assertions.assertThat(viewStateSubscriber.completions()).isEqualTo(1)
    }

    @Test
    fun `when event is load then return effect user message and partial states init, loading, new asteroid`() {
        val (effects, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Load))

        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(0)

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(3)
        viewStateSubscriber.assertValueAt(0, AsteroidViewState.init())
        viewStateSubscriber.assertValueAt(1, AsteroidViewState.init().copy(loading = true))
        viewStateSubscriber.assertValueAt(
            2,
            AsteroidViewState.init().copy(data = ViewData(Asteroid(title = "title", imageUrl = "imageUrl")))
        )
    }

    private fun asteroidViewFlow(): AsteroidViewFlow {
        return AsteroidViewFlow(
            AsteroidViewEventHandler(
                setOf(
                    GetAsteroidOfTheDay(
                        nasaRepository = repository,
                        stringProvider = stringProvider
                    ) as AsteroidUseCase<AsteroidViewEvent>,
                    SaveAsteroid(
                        nasaRepository = repository,
                        stringProvider = stringProvider
                    ) as AsteroidUseCase<AsteroidViewEvent>
                )
            ),
            AsteroidViewStateReducer()
        )
    }
}