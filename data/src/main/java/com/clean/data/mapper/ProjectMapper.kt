package com.clean.data.mapper

import com.clean.data.remote.model.ProjectData
import com.clean.data.remote.model.ProjectsResponseModel
import com.clean.domain.model.OwnerModel
import com.clean.domain.model.ProjectModel

class ProjectMapper : EntityMapper<ProjectsResponseModel, List<ProjectModel>> {

    override fun mapToEntity(data: ProjectsResponseModel): List<ProjectModel> {
        return data.items.map { mapProject(it) }
    }

    private fun mapProject(it: ProjectData): ProjectModel {
        return ProjectModel(
            id = it.id,
            name = it.name,
            fullName = it.fullName,
            starCount = it.starCount,
            dateCreated = it.dateCreated,
            owner = mapOwner(it)
        )
    }

    private fun mapOwner(it: ProjectData) =
        OwnerModel(
            ownerName = it.owner.ownerName,
            ownerAvatar = it.owner.ownerAvatar
        )
}