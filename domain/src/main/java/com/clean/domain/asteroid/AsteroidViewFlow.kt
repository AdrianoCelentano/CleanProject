package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import io.reactivex.Observable
import javax.inject.Inject

class AsteroidViewFlow @Inject constructor(
    private val eventHandler: AsteroidViewEventHandler,
    private val viewStateReducer: AsteroidViewStateReducer
) {

    fun start(
        eventEmitter: Observable<AsteroidViewEvent>
    ): Pair<Observable<AsteroidViewResult.AsteroidViewEffect>, Observable<AsteroidViewState>> {
        val sharedResultEmitter = eventEmitter
            .handleEvent(eventHandler)
            .share()
        val effectEmitter = effectEmitter(sharedResultEmitter)
        val viewStateEmitter = viewStateEmitter(sharedResultEmitter, viewStateReducer)
        return effectEmitter to viewStateEmitter
    }

    private fun Observable<AsteroidViewEvent>.handleEvent(
        eventToResultProcessor: AsteroidViewEventHandler
    ): Observable<AsteroidViewResult> {
        return doOnNext { println("flow intent: $it") }
            .flatMap(eventToResultProcessor::handleEvent)
            .doOnNext { println("flow result: $it") }
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