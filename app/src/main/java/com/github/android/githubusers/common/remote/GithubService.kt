package com.github.android.githubusers.common.remote

import com.github.android.githubusers.feature.model.GithubResponse
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {
    @GET("search/users")
    fun getUsers(@Query("q", encoded = true) search: String): Single<GithubResponse>
}