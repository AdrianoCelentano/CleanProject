package com.clean.domain.asteroid.model

sealed class AsteroidViewEvent {
    data class Store(val asteroid: Asteroid) : AsteroidViewEvent()
    object Load : AsteroidViewEvent()
}