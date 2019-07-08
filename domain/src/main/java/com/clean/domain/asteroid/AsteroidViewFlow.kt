package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import io.reactivex.Observable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import sun.nio.ch.sctp.ResultContainer
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class AsteroidViewFlow @Inject constructor(
    private val eventHandler: AsteroidViewEventHandler,
    private val viewStateReducer: AsteroidViewStateReducer
) {

    suspend fun start(
        eventChannel: ReceiveChannel<AsteroidViewEvent>
    ): ReceiveChannel<AsteroidViewState> {

        val viewStateChannel = Channel<AsteroidViewState>()

        coroutineScope {
            for (event in eventChannel) {

//                val resultChannel = handleEvent(event)

//                for (result in resultChannel) {
//                    println("flow result: $result")

                    viewStateChannel.send(AsteroidViewState.init())
//                }
            }
        }

        return viewStateChannel

//        val effectEmitter = effectEmitter(sharedResultEmitter)
//        val viewStateEmitter = viewStateEmitter(sharedResultEmitter, viewStateReducer)
//        return effectEmitter to viewStateEmitter
    }

    private suspend fun handleEvent(
        event: AsteroidViewEvent
    ): ReceiveChannel<AsteroidViewResult> {

        println("flow intent: $event")
        val channel = eventHandler.handleEvent(event)
        return channel
    }

    private fun effectEmitter(
        share: Observable<AsteroidViewResult>
    ): Observable<AsteroidViewResult.AsteroidViewEffect> {
        return share.ofType(AsteroidViewResult.AsteroidViewEffect::class.java)
    }

    private fun viewStateEmitter(
        share: Observable<AsteroidViewResult>,
        viewStateReducer: AsteroidViewStateReducer
    ): Observable<AsteroidViewState> {
        return share.ofType(AsteroidViewResult.AsteroidPartialState::class.java)
            .compose(viewStateReducer.reduce())
            .doOnNext { println("flow viewstate: $it") }
            .distinctUntilChanged()
    }

}