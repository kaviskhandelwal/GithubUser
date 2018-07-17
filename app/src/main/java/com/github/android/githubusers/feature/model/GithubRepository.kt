package com.github.android.githubusers.feature.model

import com.github.android.githubusers.core.extensions.*
import com.github.android.githubusers.core.networking.Response
import com.github.android.githubusers.core.networking.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

class GithubRepository(private val remote: GithubDataContract.Remote, private val scheduler: Scheduler,
        private val compositeDisposable: CompositeDisposable) : GithubDataContract.Repository {
    override val getUserResponse: PublishSubject<Response<GithubResponse>> = PublishSubject.create()

    override fun getUser(search: String): PublishSubject<Response<GithubResponse>> {
        getUserResponse.loading(true)
        remote.getUser(search)
                .performOnBackOutOnMain(scheduler)
                .subscribe({
                    getUserResponse.success(it)
                },{
                    getUserResponse.failed(it)
                }).addTo(compositeDisposable)
        return getUserResponse
    }
}