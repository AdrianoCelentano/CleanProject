package com.clean.data.mapper

import com.clean.data.model.Asteroid
import javax.inject.Inject

class AsteroidMapper @Inject constructor() {

    fun map(data: Asteroid): com.clean.domain.Asteroid {
        return com.clean.domain.Asteroid(
            copyright = data.copyright,
            date = data.date,
            explanation = data.explanation,
            hdurl = data.hdurl,
            media_type = data.media_type,
            service_version = data.service_version,
            title = data.title,
            url = data.url
        )
    }
}