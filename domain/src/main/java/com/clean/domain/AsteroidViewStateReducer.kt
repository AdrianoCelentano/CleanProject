package com.clean.domain

import com.clean.domain.AsteroidViewState
import com.clean.domain.ViewResult
import io.reactivex.ObservableTransformer
import javax.inject.Inject

class AsteroidViewStateReducer @Inject constructor() {

    fun reduce(): ObservableTransformer<ViewResult, AsteroidViewState> {
        return ObservableTransformer {
            it.scan(AsteroidViewState(null, false, null))
            { oldviewstate: AsteroidViewState, result: ViewResult ->
                when (result) {
                    is ViewResult.Effect -> {
                        oldviewstate
                    }
                    is ViewResult.NewAsteroid -> {
                        AsteroidViewState(result.asteroid, false, null)
                    }
                }
            }
        }
    }
}