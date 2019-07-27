package com.clean.domain.asteroid.model

data class AsteroidViewState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val data: ViewData? = null
) {
    companion object {
        fun initState(): AsteroidViewState {
            return AsteroidViewState(false, null, null)
        }
    }
}

data class ViewData(val asteroid: Asteroid)