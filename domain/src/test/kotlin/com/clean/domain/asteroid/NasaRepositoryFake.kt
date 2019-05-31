package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Observable

class NasaRepositoryFake : NasaRepository {

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return Observable.just(Asteroid(title = "title", url = "imageUrl"))
    }
}