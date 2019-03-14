package com.clean.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProjectsResponseModel(val items: List<ProjectData>)

data class ProjectData(
    val id: String, val name: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("stargazers_count") val starCount: Int,
    @SerializedName("created_at") val dateCreated: String,
    val owner: OwnerData
)

data class OwnerData(
    @SerializedName("login") val ownerName: String,
    @SerializedName("avatar_url") val ownerAvatar: String
)
