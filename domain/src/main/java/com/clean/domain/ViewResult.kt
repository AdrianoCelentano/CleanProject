package com.clean.domain

sealed class ViewResult {
    data class NewAsteroid(val asteroid: Asteroid) : ViewResult()
    object Effect : ViewResult()
}