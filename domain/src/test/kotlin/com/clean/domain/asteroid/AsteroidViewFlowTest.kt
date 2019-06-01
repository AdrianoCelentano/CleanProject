package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.*
import com.clean.domain.asteroid.usecase.GetAsteroidOfTheDay
import io.reactivex.Observable
import org.junit.Test

class AsteroidViewFlowTest {

    private val asteroidViewFlow: AsteroidViewFlow = asteroidViewFlow()

    @Test
    fun `when event is store then return effect user message`() {
        val (effects, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Store))

        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(1)
            .assertValue(AsteroidViewResult.AsteroidViewEffect.UserMessage("test"))
    }

    @Test
    fun `when event is init then return effect user message and partial states init, loading, new asteroid`() {
        val (effects, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Init))

        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(1)
            .assertValue(AsteroidViewResult.AsteroidViewEffect.UserMessage("message in a bottle"))

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(3)
        viewStateSubscriber.assertValueAt(0, AsteroidViewState.init())
        viewStateSubscriber.assertValueAt(1, AsteroidViewState.init().copy(loading = true))
        viewStateSubscriber.assertValueAt(
            2,
            AsteroidViewState.init().copy(data = ViewData(Asteroid(title = "title", url = "imageUrl")))
        )
    }

    @Test
    fun `when event is refresh then return effect user message and partial states init, loading, new asteroid`() {
        val (effects, viewstate) =
            asteroidViewFlow.start(Observable.just(AsteroidViewEvent.Refresh))

        val effectsSubscriber = effects.test()
        effectsSubscriber.assertValueCount(1)
            .assertValue(AsteroidViewResult.AsteroidViewEffect.UserMessage("message in a bottle"))

        val viewStateSubscriber = viewstate.test()
        viewStateSubscriber.assertValueCount(3)
        viewStateSubscriber.assertValueAt(0, AsteroidViewState.init())
        viewStateSubscriber.assertValueAt(1, AsteroidViewState.init().copy(loading = true))
        viewStateSubscriber.assertValueAt(
            2,
            AsteroidViewState.init().copy(data = ViewData(Asteroid(title = "title", url = "imageUrl")))
        )
    }

    private fun asteroidViewFlow(): AsteroidViewFlow {
        return AsteroidViewFlow(
            AsteroidViewEventHandler(
                GetAsteroidOfTheDay(
                    NasaRepositoryFake()
                )
            ),
            AsteroidViewStateReducer()
        )
    }
}