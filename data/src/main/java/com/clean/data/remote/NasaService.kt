package com.clean.data.remote

import com.clean.data.model.Asteroid
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET

interface NasaService {
    @GET("apod?api_key=wSNEtcl1BcDve7JggX1VQgScHRoVSmQNcLvM1rI4")
    fun getAsteroidOfTheDay(): Single<Response<Asteroid>>

}