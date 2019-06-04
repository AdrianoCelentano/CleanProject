package com.clean.domain.asteroid.model

sealed class AsteroidViewEvent {
    object Init : AsteroidViewEvent()
    data class Store(val asteroid: Asteroid) : AsteroidViewEvent()
    object Refresh : AsteroidViewEvent()
}