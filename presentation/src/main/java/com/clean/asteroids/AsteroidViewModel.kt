package com.clean.asteroids

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidViewFlow
import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    asteroidViewFlow: AsteroidViewFlow
) : ViewModel(), CoroutineScope by MainScope() {

    val viewStateLive
        get() = viewStateMutableLive

    val asteroidOfTheDay: Asteroid? get() = viewStateLive.value?.data?.asteroid

    val effectChannel: ReceiveChannel<AsteroidViewResult.AsteroidViewEffect>

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val eventChannel = Channel<AsteroidViewEvent>()

    private val eventReceiver get() = eventChannel as ReceiveChannel<AsteroidViewEvent>

    init {
        startFlow(asteroidViewFlow)
        effectChannel = asteroidViewFlow.receiveEffectChannel
        observeViewState(asteroidViewFlow.receiveViewStateChannel)
        processEvent(AsteroidViewEvent.Load)
    }

    private fun startFlow(asteroidViewFlow: AsteroidViewFlow) {
        launch {
            asteroidViewFlow.start(eventReceiver)
        }
    }

    fun processEvent(viewEvent: AsteroidViewEvent) {
        launch {
            eventChannel.send(viewEvent)
        }
    }

    private fun observeViewState(viewStateChannel: ReceiveChannel<AsteroidViewState>) {
        launch {
            for (viewState: AsteroidViewState in viewStateChannel) {
                viewStateLive.postValue(viewState)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}