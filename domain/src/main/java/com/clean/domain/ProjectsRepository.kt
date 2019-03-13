package com.clean.domain

import com.clean.domain.model.ProjectModel
import io.reactivex.Observable

interface ProjectsRepository {

    fun getProjects(query: String, sortBy: String, order: String): Observable<List<ProjectModel>>
}