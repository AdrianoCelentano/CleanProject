package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class AsteroidViewFlow @Inject constructor(
    private val eventHandler: AsteroidViewEventHandler,
    private val viewStateReducer: AsteroidViewStateReducer
) {

    private var viewState: AsteroidViewState = AsteroidViewState.initState()

    private val effectChannel: Channel<AsteroidViewResult.AsteroidViewEffect> = Channel()
    val receiveEffectChannel get() = effectChannel as ReceiveChannel<AsteroidViewResult.AsteroidViewEffect>

    private val viewStateChannel: Channel<AsteroidViewState> = Channel()
    val receiveViewStateChannel get() = viewStateChannel as ReceiveChannel<AsteroidViewState>

    suspend fun start(
        eventChannel: ReceiveChannel<AsteroidViewEvent>
    ) {
        observeEvents(eventChannel)
    }

    private suspend fun observeEvents(eventChannel: ReceiveChannel<AsteroidViewEvent>) {
        channelFlow() {
            for (event in eventChannel) {
                send(event)
            }
        }.flatMapMerge {
            println("mvi event: $it")
            return@flatMapMerge eventHandler.handleEvent(it)
        }.collect {
            println("mvi result: $it")
            handleResult(it)
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