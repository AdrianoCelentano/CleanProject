package com.clean.domain.asteroid.model

sealed class AsteroidViewEvent {
    object Init : AsteroidViewEvent()
    object Store : AsteroidViewEvent()
    object Refresh : AsteroidViewEvent()
}