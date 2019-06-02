package com.clean.data

import android.app.Application
import com.clean.domain.asteroid.StringProvider
import javax.inject.Inject

class StringProviderImpl @Inject constructor(private val application: Application) : StringProvider {

    override val serverError: String
        get() = application.getString(R.string.server_errror)
    override val generalError: String
        get() = application.getString(R.string.general_errror)
    override val storeAsteroidSuccess: String
        get() = application.getString(R.string.store_asteroid_success)
    override val messageInABottle: String
        get() = application.getString(R.string.message_in_a_bottle)

}