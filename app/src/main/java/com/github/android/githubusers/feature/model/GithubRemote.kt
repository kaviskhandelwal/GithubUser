package com.github.android.githubusers.feature.model

import com.github.android.githubusers.common.remote.GithubService
import io.reactivex.Single

class GithubRemote(private val service: GithubService) : GithubDataContract.Remote {
    override fun getUser(search: String): Single<GithubResponse> = service.getUsers(search)
}