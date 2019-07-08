package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.domain.asteroid.AsteroidViewFlow
import com.clean.domain.asteroid.model.Asteroid
import com.clean.domain.asteroid.model.AsteroidViewEvent
import com.clean.domain.asteroid.model.AsteroidViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    asteroidViewFlow: AsteroidViewFlow
) : ViewModel() {

    val viewStateLive
        get() = viewStateMutableLive

//    val viewEffectEmitter: Observable<AsteroidViewResult.AsteroidViewEffect>

    val asteroidOfTheDay: Asteroid? get() = viewStateLive.value?.data?.asteroid

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    private val eventChannel = Channel<AsteroidViewEvent>()

    private val viewStateChannel = Channel<AsteroidViewState>()

    private val eventReceiver get() = eventChannel as ReceiveChannel<AsteroidViewEvent>

    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    init {
        viewModelScope.launch {
            val viewStateChannel = asteroidViewFlow.start(eventReceiver)
            observeViewState(viewStateChannel)
//            for (event in eventReceiver) {
//                viewStateChannel.send(AsteroidViewState(loading = true))
//                kotlinx.coroutines.delay(500)
//                viewStateChannel.send(AsteroidViewState(loading = false))
//            }
        }

        viewModelScope.launch {
            observeViewState(viewStateChannel)
        }

//        this.viewEffectEmitter = effectEmitter
    }

    fun processEvent(viewEvent: AsteroidViewEvent) {
        viewModelScope.launch {
            Log.d("qwer", "event: $viewEvent")
            eventChannel.send(viewEvent)
        }
    }

    private suspend fun observeViewState(viewStateChannel: ReceiveChannel<AsteroidViewState>) {
        for (viewState: AsteroidViewState in viewStateChannel) {
            Log.d("qwer", "state: $viewState")
            viewStateLive.postValue(viewState)
        }
//        for (viewState: AsteroidViewState in this.viewStateChannel) {
//            viewStateLive.postValue(viewState)
//        }
//                onError = { error -> Log.e("qwer", "error viewstate", error) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}