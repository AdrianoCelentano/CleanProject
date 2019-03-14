package com.clean.data.remote

import com.clean.data.remote.model.Asteroid
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class NasaRemote @Inject constructor(private val nasaService: NasaService) {

    fun getAsteroidOfTheDay(): Single<Asteroid> {
        return nasaService.getAsteroidOfTheDay()
            .map { response ->
                if (isResponseSuccessful(response)) {
                    return@map response.body()!!
                } else {
                    val message = buildErrorMessage(response)
                    throw RemoteError(message)
                }
            }
    }

    private fun buildErrorMessage(response: Response<Asteroid>) =
        "there was a network error, code: ${response.code()}, message: ${response.message()}"

    private fun isResponseSuccessful(response: Response<Asteroid>) =
        response.isSuccessful && response.body() != null
}

class RemoteError(message: String) : Exception(message)
