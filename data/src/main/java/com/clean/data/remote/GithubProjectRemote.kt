package com.clean.data.remote

import com.clean.data.remote.model.ProjectsResponseModel
import io.reactivex.Observable
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class GithubProjectRemote @Inject constructor(private val githubRepositoryService: GithubProjectService) {

    fun getRepositories(query: String, sortBy: String, order: String): Observable<ProjectsResponseModel> {
        return githubRepositoryService.searchRepositories(query, sortBy, order)
            .map { response ->
                if (isResponseSuccessful(response)) {
                    return@map response.body()!!
                } else {
                    val message = buildErrorMessage(response)
                    throw RemoteError(message)
                }
            }
    }

    private fun buildErrorMessage(response: Response<ProjectsResponseModel>) =
        "there was a network error, code: ${response.code()}, message: ${response.message()}"

    private fun isResponseSuccessful(response: Response<ProjectsResponseModel>) =
        response.isSuccessful && response.body() != null
}

class RemoteError(message: String) : Exception(message)
