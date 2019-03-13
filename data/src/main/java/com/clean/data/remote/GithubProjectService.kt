package com.clean.data.remote

import com.clean.data.remote.model.ProjectsResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubProjectService {

    @GET("search/repositories")
    fun searchRepositories(@Query("q") query: String,
                           @Query("sort") sortBy: String,
                           @Query("order") order: String)
            : Observable<Response<ProjectsResponseModel>>

}