package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable

class NasaRepositoryFake : NasaRepository {

    override fun saveAsteroid(asteroid: Asteroid): Completable {
        return Completable.complete()
    }

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return Observable.just(Asteroid(title = "title", imageUrl = "imageUrl"))
    }
}