package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AsteroidViewFlow @Inject constructor(
    private val eventHandler: AsteroidViewEventHandler,
    private val viewStateReducer: AsteroidViewStateReducer
) {

    private var viewState: AsteroidViewState = AsteroidViewState.initState()

    private val resultChannel: Channel<AsteroidViewResult> = Channel()

    private val effectChannel: Channel<AsteroidViewResult.AsteroidViewEffect> = Channel()
    val receiveEffectChannel get() = effectChannel as ReceiveChannel<AsteroidViewResult.AsteroidViewEffect>

    private val viewStateChannel: Channel<AsteroidViewState> = Channel()
    val receiveViewStateChannel get() = viewStateChannel as ReceiveChannel<AsteroidViewState>

    fun start(
        scope: CoroutineScope,
        eventChannel: ReceiveChannel<AsteroidViewEvent>
    ) {
        observeEvents(scope, eventChannel)
        observeResults(scope)
    }

    private fun observeEvents(scope: CoroutineScope, eventChannel: ReceiveChannel<AsteroidViewEvent>) {
        scope.launch() {
            for (event in eventChannel) {
                println("mvi event: $event")
                eventHandler.handleEvent(event, resultChannel)
            }
        }
    }

    private fun observeResults(scope: CoroutineScope) {
        scope.launch() {
            for (result in resultChannel) {
                println("mvi result: $result")
                handleResult(result)
            }
        }
    }

    private suspend fun handleResult(result: AsteroidViewResult) {
        when (result) {
            is AsteroidViewResult.AsteroidViewEffect -> {
                println("mvi effect: $result")
                effectChannel.send(result)
            }
            is AsteroidViewResult.AsteroidPartialState -> {
                viewState = viewStateReducer.reduce(viewState, result)
                println("mvi viewstate: $viewState")
                viewStateChannel.send(viewState)
            }
        }
    }

}