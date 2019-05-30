package com.clean.domain

sealed class ViewEvent {
    object Init : ViewEvent()
    object Store : ViewEvent()
    object Refresh : ViewEvent()
}