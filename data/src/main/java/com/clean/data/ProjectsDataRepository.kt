package com.clean.data

import com.clean.data.mapper.EntityMapper
import com.clean.data.remote.GithubProjectRemote
import com.clean.data.remote.model.ProjectsResponseModel
import com.clean.domain.ProjectsRepository
import com.clean.domain.model.ProjectModel
import io.reactivex.Observable
import javax.inject.Inject

class ProjectsDataRepository @Inject constructor(
    private val githubProjectRemote: GithubProjectRemote,
    private val mapper: EntityMapper<ProjectsResponseModel, List<ProjectModel>>
) : ProjectsRepository {

    override fun getProjects(query: String, sortBy: String, order: String): Observable<List<ProjectModel>> {
        return githubProjectRemote.getRepositories(query, sortBy, order)
            .map { mapper.mapToEntity(it) }
    }

}