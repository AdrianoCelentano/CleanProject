package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.asteroid.model.ViewData
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class AsteroidViewStateReducer @Inject constructor() {

    fun reduce(): ObservableTransformer<AsteroidViewResult.AsteroidPartialState, AsteroidViewState> {
        return ObservableTransformer { partialStateObservable ->
            partialStateObservable.scan<AsteroidViewState>(AsteroidViewState.init())
            { oldviewstate: AsteroidViewState, result: AsteroidViewResult.AsteroidPartialState ->
                when (result) {
                    is AsteroidViewResult.AsteroidPartialState.NewAsteroid -> {
                        oldviewstate.copy(data = ViewData(result.asteroid), loading = false, errorMessage = null)
                    }
                    is AsteroidViewResult.AsteroidPartialState.Error -> {
                        oldviewstate.copy(errorMessage = result.message, loading = false, data = null)
                    }
                    AsteroidViewResult.AsteroidPartialState.Loading -> {
                        oldviewstate.copy(loading = true, data = null, errorMessage = null)
                    }
                }
            }
        }
    }
}