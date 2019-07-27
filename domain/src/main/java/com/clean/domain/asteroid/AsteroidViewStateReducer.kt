package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.AsteroidViewResult
import com.clean.domain.asteroid.model.AsteroidViewState
import com.clean.domain.asteroid.model.ViewData
import javax.inject.Inject

class AsteroidViewStateReducer @Inject constructor() {

    fun reduce(oldviewstate: AsteroidViewState, result: AsteroidViewResult.AsteroidPartialState): AsteroidViewState {

        return when (result) {
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