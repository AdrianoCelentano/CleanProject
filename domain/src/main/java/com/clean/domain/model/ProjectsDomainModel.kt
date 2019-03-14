package com.clean.domain.model

data class ProjectModel(
    val id: String,
    val name: String,
    val fullName: String,
    val starCount: Int,
    val dateCreated: String,
    val owner: OwnerModel
)

data class OwnerModel(
    val ownerName: String,
    val ownerAvatar: String
)
