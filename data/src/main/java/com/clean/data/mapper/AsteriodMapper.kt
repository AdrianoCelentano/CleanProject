package com.clean.data.mapper

import com.clean.data.remote.model.Asteroid
import javax.inject.Inject

class AsteriodMapper @Inject constructor() : EntityMapper<Asteroid, com.clean.domain.Asteroid> {

    override fun mapToEntity(data: Asteroid): com.clean.domain.Asteroid {
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