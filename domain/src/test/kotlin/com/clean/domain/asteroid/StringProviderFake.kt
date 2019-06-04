package com.clean.domain.asteroid

class StringProviderFake() : StringProvider {

    override val serverError: String
        get() = "serverError"
    override val generalError: String
        get() = "generalError"
    override val storeAsteroidSuccess: String
        get() = "storeAsteroidSuccess"
    override val messageInABottle: String
        get() = "messageInABottle"
}