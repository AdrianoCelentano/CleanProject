package com.clean.domain.asteroid.model

sealed class AsteroidViewResult {

    sealed class AsteroidPartialState : AsteroidViewResult() {
        data class NewAsteroid(val asteroid: Asteroid) : AsteroidPartialState()
        data class Error(val message: String) : AsteroidPartialState()
        object Loading : AsteroidPartialState()
    }

    sealed class AsteroidViewEffect : AsteroidViewResult() {
        data class UserMessage(val message: String) : AsteroidViewEffect()
    }

}
