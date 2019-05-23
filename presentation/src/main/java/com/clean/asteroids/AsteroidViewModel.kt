package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.asteroids.model.Asteroid
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    intentToResultProcessor: IntentToResultProcessor,
    viewStateReducer: AsteroidViewStateReducer
) : ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    val intentSubject: PublishSubject<ViewIntent> = PublishSubject.create()

    private val viewStateMutableLive by lazy { MutableLiveData<AsteroidViewState>() }

    val viewStateLive
        get() = viewStateMutableLive

    init {
        intentSubject
            .startWith(ViewIntent.Init)
            .doOnNext { Log.d("qwer", "intent: $it") }
            .flatMap(intentToResultProcessor::process)
            .doOnNext { Log.d("qwer", "result: $it") }
            .compose(viewStateReducer.reduce())
            .doOnNext { Log.d("qwer", "viewstate: $it") }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onNext = { viewStateMutableLive.postValue(it) },
                onError = { Log.e("qwer", "error", it) }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun processIntent(intent: ViewIntent) {
        intentSubject.onNext(intent)
    }
}

sealed class ViewIntent {
    object Init : ViewIntent()
    object Store : ViewIntent()
    object Refresh : ViewIntent()
}

sealed class Result {
    data class NewAsteroid(val asteroid: Asteroid) : Result()
    object NoChange : Result()
}