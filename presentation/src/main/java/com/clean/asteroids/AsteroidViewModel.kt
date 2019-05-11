package com.clean.asteroids

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clean.asteroids.mapper.AsteroidMapper
import com.clean.asteroids.model.Asteroid
import com.clean.domain.GetAsteroidOfTheDay
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AsteroidViewModel @Inject constructor(
    getAsteroidOfTheDay: GetAsteroidOfTheDay
) : ViewModel() {

    val disposables: CompositeDisposable = CompositeDisposable()

    private val asteroidMutableLive by lazy { MutableLiveData<Asteroid>() }

    val asteroidLive
        get() = asteroidMutableLive

    init {
        getAsteroidOfTheDay.execute()
            .map { AsteroidMapper().map(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = {
                    asteroidMutableLive.value = it
                },
                onError = {
                    Log.e("qwer", "error", it)
                }
            ).addTo(disposables)
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}