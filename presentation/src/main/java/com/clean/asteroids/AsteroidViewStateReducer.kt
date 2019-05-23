package com.clean.asteroids

import android.util.Log
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class AsteroidViewStateReducer @Inject constructor() {

    fun reduce(): ObservableTransformer<Result, AsteroidViewState> {
        return ObservableTransformer {
            it.scan(AsteroidViewState(null, false, null))
            { oldviewstate: AsteroidViewState, result: Result ->
                when (result) {
                    is Result.NoChange -> {
                        oldviewstate
                    }
                    is Result.NewAsteroid -> {
                        AsteroidViewState(result.asteroid, false, null)
                    }
                }
            }
        }
    }
}