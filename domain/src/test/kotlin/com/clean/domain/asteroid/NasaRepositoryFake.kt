package com.clean.domain.asteroid

import com.clean.domain.asteroid.model.Asteroid
import io.reactivex.Completable
import io.reactivex.Observable

class NasaRepositoryFake : NasaRepository {

    val cache: MutableList<Asteroid> = mutableListOf()

    override fun saveAsteroid(asteroid: Asteroid): Completable {
        cache.add(asteroid)
        return Completable.complete()
    }

    override fun getAsteroidOfTheDay(): Observable<Asteroid> {
        return Observable.just(Asteroid(title = "title", imageUrl = "imageUrl"))
    }

    override fun getSavedAsteroid(): Observable<List<Asteroid>> {
        return Observable.just(cache)
    }
}